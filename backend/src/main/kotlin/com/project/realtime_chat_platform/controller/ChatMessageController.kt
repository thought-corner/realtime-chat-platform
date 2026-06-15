package com.project.realtime_chat_platform.controller

import com.project.realtime_chat_platform.controller.dto.chat.ChatMessageRequest
import com.project.realtime_chat_platform.controller.dto.chat.ChatMessageResponse
import com.project.realtime_chat_platform.service.ChatMessageService
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Controller

/**
 * STOMP 실시간 채팅 메시지 컨트롤러.
 *
 * 클라이언트가 `/publish/{roomId}`로 발행한 메시지를 받아 DB에 저장한 뒤, 같은 방을
 * 구독 중인 클라이언트에게 인메모리 브로커(`/topic/{roomId}`)로 브로드캐스트한다.
 */
@Controller
class ChatMessageController(
    private val messageTemplate: SimpMessageSendingOperations,
    private val chatMessageService: ChatMessageService,
) {
    /**
     * `/publish/{roomId}`로 발행된 메시지를 저장하고 `/topic/{roomId}` 구독자에게 전달한다.
     * 브로드캐스트 페이로드에는 안 읽은 인원 수가 포함된 [ChatMessageResponse]를 사용해,
     * 이력 조회와 동일한 형태로 내려보낸다.
     *
     * @param roomId STOMP 목적지에서 추출한 채팅방 식별자
     * @param request 클라이언트가 보낸 메시지 본문
     */
    @MessageMapping("/{roomId}")
    fun sendMessage(
        @DestinationVariable roomId: Long,
        request: ChatMessageRequest,
    ) {
        val saved = chatMessageService.saveMessage(roomId, request.senderEmail, request.message)
        messageTemplate.convertAndSend("/topic/$roomId", ChatMessageResponse.from(saved))
    }
}

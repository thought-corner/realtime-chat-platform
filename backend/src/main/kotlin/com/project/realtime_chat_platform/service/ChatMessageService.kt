package com.project.realtime_chat_platform.service

import com.project.realtime_chat_platform.common.exception.BusinessException
import com.project.realtime_chat_platform.common.exception.ChatErrorCode
import com.project.realtime_chat_platform.domain.ChatMessage
import com.project.realtime_chat_platform.repository.ChatMessageRepository
import com.project.realtime_chat_platform.repository.ChatRoomRepository
import com.project.realtime_chat_platform.repository.MemberRepository
import com.project.realtime_chat_platform.repository.getByEmailOrThrow
import com.project.realtime_chat_platform.repository.getOrThrow
import com.project.realtime_chat_platform.service.dto.chat.ChatMessageResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 채팅 메시지 책임. 메시지 저장과 이력 조회를 담당한다.
 *
 * 메시지 저장 시 참여자별 읽음 상태 생성은 [ReadStatusService]에, 이력 조회의 참여자 인가는
 * [ChatRoomService]에 위임한다.
 */
@Service
@Transactional
class ChatMessageService(
    private val chatRoomRepository: ChatRoomRepository,
    private val memberRepository: MemberRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val chatRoomService: ChatRoomService,
    private val readStatusService: ReadStatusService,
) {
    /**
     * 메시지를 저장하고, 참여자별 읽음 상태 생성을 [ReadStatusService]에 위임한다.
     * 저장 직후의 안 읽은 인원 수(보낸 사람을 제외한 참여자 수)를 담아 반환한다.
     */
    fun saveMessage(
        roomId: Long,
        senderEmail: String,
        message: String,
    ): ChatMessageResult {
        val chatRoom = chatRoomRepository.getOrThrow(roomId)
        val sender = memberRepository.getByEmailOrThrow(senderEmail)
        val chatMessage =
            chatMessageRepository.save(
                ChatMessage(chatRoom = chatRoom, member = sender, content = message),
            )
        readStatusService.createForNewMessage(chatRoom, chatMessage, sender)
        return ChatMessageResult.from(chatMessage, readStatusService.countUnreadByMessage(chatMessage))
    }

    /**
     * 채팅방의 메시지 이력을 시간순으로 조회한다. 현재 회원이 참여자가 아니면 거부한다.
     * 각 메시지에는 아직 읽지 않은 참여자 수가 포함된다.
     */
    @Transactional(readOnly = true)
    fun getChatHistory(
        email: String,
        roomId: Long,
    ): List<ChatMessageResult> {
        if (!chatRoomService.isRoomParticipant(email, roomId)) {
            throw BusinessException(ChatErrorCode.NOT_ROOM_PARTICIPANT)
        }
        val chatRoom = chatRoomRepository.getOrThrow(roomId)
        return chatMessageRepository.findByChatRoomOrderByCreatedTimeAsc(chatRoom).map { chatMessage ->
            ChatMessageResult.from(chatMessage, readStatusService.countUnreadByMessage(chatMessage))
        }
    }
}

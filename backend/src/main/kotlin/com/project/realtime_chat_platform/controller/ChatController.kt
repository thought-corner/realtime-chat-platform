package com.project.realtime_chat_platform.controller

import com.project.realtime_chat_platform.aop.CurrentMember
import com.project.realtime_chat_platform.controller.dto.chat.ChatMessageResponseList
import com.project.realtime_chat_platform.controller.dto.chat.ChatRoomResponseList
import com.project.realtime_chat_platform.controller.dto.chat.MessageReadResponse
import com.project.realtime_chat_platform.controller.dto.chat.MyChatRoomResponseList
import com.project.realtime_chat_platform.service.ChatMessageService
import com.project.realtime_chat_platform.service.ChatRoomService
import com.project.realtime_chat_platform.service.ReadStatusService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * 채팅방 관리 REST API. 그룹/1:1 방 개설·참여·퇴장, 메시지 이력 조회, 읽음 처리를 제공한다.
 *
 * 인증된 회원의 이메일은 [CurrentMember]로 경계에서 해석해 서비스에 전달한다.
 * 실시간 메시지 송수신은 STOMP를 쓰는 [ChatMessageController]가 담당한다.
 */
@RestController
@RequestMapping("/chat")
class ChatController(
    private val chatRoomService: ChatRoomService,
    private val chatMessageService: ChatMessageService,
    private val readStatusService: ReadStatusService,
    private val messageTemplate: SimpMessageSendingOperations,
) {
    @PostMapping("/room/group/create")
    fun createGroupRoom(
        @CurrentMember email: String,
        @RequestParam roomName: String,
    ): ResponseEntity<Void> {
        chatRoomService.createGroupRoom(email, roomName)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/room/group/list")
    fun getGroupChatRooms(): ResponseEntity<ChatRoomResponseList> =
        ResponseEntity(ChatRoomResponseList.from(chatRoomService.getGroupChatRooms()), HttpStatus.OK)

    @PostMapping("/room/group/{roomId}/join")
    fun joinGroupChatRoom(
        @CurrentMember email: String,
        @PathVariable roomId: Long,
    ): ResponseEntity<Void> {
        chatRoomService.addParticipantToGroupChat(email, roomId)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/history/{roomId}")
    fun getChatHistory(
        @CurrentMember email: String,
        @PathVariable roomId: Long,
    ): ResponseEntity<ChatMessageResponseList> =
        ResponseEntity(ChatMessageResponseList.from(chatMessageService.getChatHistory(email, roomId)), HttpStatus.OK)

    @PostMapping("/room/{roomId}/read")
    fun messageRead(
        @CurrentMember email: String,
        @PathVariable roomId: Long,
    ): ResponseEntity<Void> {
        val updates = readStatusService.markRead(email, roomId)
        // 안 읽은 인원 수가 바뀐 메시지가 있으면 같은 방 구독자에게 실시간 전파한다.
        if (updates.isNotEmpty()) {
            messageTemplate.convertAndSend("/topic/$roomId", MessageReadResponse.from(updates))
        }
        return ResponseEntity.ok().build()
    }

    @GetMapping("/my/rooms")
    fun getMyChatRooms(
        @CurrentMember email: String,
    ): ResponseEntity<MyChatRoomResponseList> =
        ResponseEntity(MyChatRoomResponseList.from(chatRoomService.getMyChatRooms(email)), HttpStatus.OK)

    @DeleteMapping("/room/group/{roomId}/leave")
    fun leaveGroupChatRoom(
        @CurrentMember email: String,
        @PathVariable roomId: Long,
    ): ResponseEntity<Void> {
        chatRoomService.leaveGroupChatRoom(email, roomId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/room/private/create")
    fun getOrCreatePrivateRoom(
        @CurrentMember email: String,
        @RequestParam otherMemberId: Long,
    ): ResponseEntity<Long> = ResponseEntity(chatRoomService.getOrCreatePrivateRoom(email, otherMemberId), HttpStatus.OK)
}

package com.project.realtime_chat_platform.service.dto.chat

import com.project.realtime_chat_platform.domain.ChatMessage
import com.project.realtime_chat_platform.domain.ChatRoom

/**
 * 그룹 채팅방 목록 항목.
 */
data class ChatRoomResult(
    val roomId: Long?,
    val roomName: String,
) {
    companion object {
        fun from(room: ChatRoom) =
            ChatRoomResult(
                roomId = room.id,
                roomName = room.name,
            )
    }
}

/**
 * 내 채팅방 목록 항목. 그룹 여부와 안 읽은 메시지 수를 포함한다.
 */
data class MyChatRoomResult(
    val roomId: Long?,
    val roomName: String,
    val isGroupChat: String,
    val unreadCount: Long,
)

/**
 * 채팅 메시지 한 건(이력 조회용).
 */
data class ChatMessageResult(
    val message: String,
    val senderEmail: String,
) {
    companion object {
        fun from(chatMessage: ChatMessage) =
            ChatMessageResult(
                message = chatMessage.content,
                senderEmail = chatMessage.member.email,
            )
    }
}

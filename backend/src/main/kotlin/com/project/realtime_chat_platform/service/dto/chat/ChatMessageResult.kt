package com.project.realtime_chat_platform.service.dto.chat

import com.project.realtime_chat_platform.domain.ChatMessage

/**
 * 채팅 메시지 한 건. `unreadCount`는 이 메시지를 아직 읽지 않은 참여자 수다.
 * `id`는 클라이언트가 실시간 읽음 갱신 시 어느 메시지인지 식별하는 데 쓰인다.
 */
data class ChatMessageResult(
    val id: Long?,
    val message: String,
    val senderEmail: String,
    val unreadCount: Long,
) {
    companion object {
        fun from(
            chatMessage: ChatMessage,
            unreadCount: Long,
        ) = ChatMessageResult(
            id = chatMessage.id,
            message = chatMessage.content,
            senderEmail = chatMessage.member.email,
            unreadCount = unreadCount,
        )
    }
}

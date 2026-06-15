package com.project.realtime_chat_platform.service.dto.chat

import com.project.realtime_chat_platform.domain.ChatMessage

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

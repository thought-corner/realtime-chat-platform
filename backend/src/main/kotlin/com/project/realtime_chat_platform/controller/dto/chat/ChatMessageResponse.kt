package com.project.realtime_chat_platform.controller.dto.chat

import com.project.realtime_chat_platform.service.dto.chat.ChatMessageResult

data class ChatMessageResponse(
    val message: String,
    val senderEmail: String,
) {
    companion object {
        fun from(result: ChatMessageResult) =
            ChatMessageResponse(
                message = result.message,
                senderEmail = result.senderEmail,
            )
    }
}

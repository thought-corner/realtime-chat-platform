package com.project.realtime_chat_platform.controller.dto.chat

import com.project.realtime_chat_platform.service.dto.chat.ChatMessageResult

data class ChatMessageResponseList(
    val messages: List<ChatMessageResponse>,
) {
    companion object {
        fun from(results: List<ChatMessageResult>) = ChatMessageResponseList(results.map(ChatMessageResponse::from))
    }
}

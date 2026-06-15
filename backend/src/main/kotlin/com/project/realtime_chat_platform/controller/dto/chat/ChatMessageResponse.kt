package com.project.realtime_chat_platform.controller.dto.chat

import com.project.realtime_chat_platform.service.dto.chat.ChatMessageResult

data class ChatMessageResponse(
    val id: Long?,
    val message: String,
    val senderEmail: String,
    val unreadCount: Long,
    // 같은 /topic 채널로 흐르는 READ 이벤트와 구분하기 위한 판별자.
    val type: ChatTopicEventType = ChatTopicEventType.MESSAGE,
) {
    companion object {
        fun from(result: ChatMessageResult) =
            ChatMessageResponse(
                id = result.id,
                message = result.message,
                senderEmail = result.senderEmail,
                unreadCount = result.unreadCount,
            )
    }
}

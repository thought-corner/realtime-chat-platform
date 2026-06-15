package com.project.realtime_chat_platform.controller.dto.chat

import com.project.realtime_chat_platform.service.dto.chat.ChatRoomResult

data class ChatRoomResponseList(
    val rooms: List<ChatRoomResponse>,
) {
    companion object {
        fun from(results: List<ChatRoomResult>) = ChatRoomResponseList(results.map(ChatRoomResponse::from))
    }
}

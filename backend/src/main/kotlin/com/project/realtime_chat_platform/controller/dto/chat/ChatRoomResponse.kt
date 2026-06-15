package com.project.realtime_chat_platform.controller.dto.chat

import com.project.realtime_chat_platform.service.dto.chat.ChatRoomResult

data class ChatRoomResponse(
    val roomId: Long?,
    val roomName: String,
) {
    companion object {
        fun from(result: ChatRoomResult) =
            ChatRoomResponse(
                roomId = result.roomId,
                roomName = result.roomName,
            )
    }
}

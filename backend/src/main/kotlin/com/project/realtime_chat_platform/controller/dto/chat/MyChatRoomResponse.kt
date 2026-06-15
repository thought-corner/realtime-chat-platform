package com.project.realtime_chat_platform.controller.dto.chat

import com.project.realtime_chat_platform.service.dto.chat.MyChatRoomResult

data class MyChatRoomResponse(
    val roomId: Long?,
    val roomName: String,
    val isGroupChat: String,
    val unreadCount: Long,
) {
    companion object {
        fun from(result: MyChatRoomResult) =
            MyChatRoomResponse(
                roomId = result.roomId,
                roomName = result.roomName,
                isGroupChat = result.isGroupChat,
                unreadCount = result.unreadCount,
            )
    }
}

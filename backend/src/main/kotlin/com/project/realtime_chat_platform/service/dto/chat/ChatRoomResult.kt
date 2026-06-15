package com.project.realtime_chat_platform.service.dto.chat

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

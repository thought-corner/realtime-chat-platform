package com.project.realtime_chat_platform.controller.dto.chat

import com.project.realtime_chat_platform.service.dto.chat.MyChatRoomResult

data class MyChatRoomResponseList(
    val rooms: List<MyChatRoomResponse>,
) {
    companion object {
        fun from(results: List<MyChatRoomResult>) = MyChatRoomResponseList(results.map(MyChatRoomResponse::from))
    }
}

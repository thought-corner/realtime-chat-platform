package com.project.realtime_chat_platform.controller.dto.chat

import com.project.realtime_chat_platform.service.dto.chat.ChatMessageResult
import com.project.realtime_chat_platform.service.dto.chat.ChatRoomResult
import com.project.realtime_chat_platform.service.dto.chat.MyChatRoomResult

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

        fun from(results: List<ChatRoomResult>): List<ChatRoomResponse> = results.map { from(it) }
    }
}

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

        fun from(results: List<MyChatRoomResult>): List<MyChatRoomResponse> = results.map { from(it) }
    }
}

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

        fun from(results: List<ChatMessageResult>): List<ChatMessageResponse> = results.map { from(it) }
    }
}

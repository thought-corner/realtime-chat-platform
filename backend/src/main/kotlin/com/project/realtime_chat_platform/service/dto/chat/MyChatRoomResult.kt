package com.project.realtime_chat_platform.service.dto.chat

/**
 * 내 채팅방 목록 항목. 그룹 여부와 안 읽은 메시지 수를 포함한다.
 */
data class MyChatRoomResult(
    val roomId: Long?,
    val roomName: String,
    val isGroupChat: String,
    val unreadCount: Long,
)

package com.project.realtime_chat_platform.service.dto.chat

/**
 * 한 메시지의 갱신된 안 읽은 인원 수. 읽음 처리로 카운트가 바뀐 메시지를 실시간으로 전파할 때 쓴다.
 */
data class MessageUnreadResult(
    val messageId: Long,
    val unreadCount: Long,
)

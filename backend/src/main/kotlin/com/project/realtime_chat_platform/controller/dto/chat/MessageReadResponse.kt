package com.project.realtime_chat_platform.controller.dto.chat

import com.project.realtime_chat_platform.service.dto.chat.MessageUnreadResult

/**
 * `/topic/{roomId}`로 브로드캐스트되는 실시간 읽음 이벤트.
 *
 * 누군가 메시지를 읽어 안 읽은 인원 수가 바뀌었을 때, 변경된 메시지들의 새 카운트를 담아 전파한다.
 * 같은 채널의 채팅 메시지([ChatMessageResponse], `type = "MESSAGE"`)와 `type`으로 구분한다.
 */
data class MessageReadResponse(
    val updates: List<MessageUnreadItem>,
    val type: ChatTopicEventType = ChatTopicEventType.READ,
) {
    companion object {
        fun from(results: List<MessageUnreadResult>) =
            MessageReadResponse(
                updates = results.map { MessageUnreadItem(it.messageId, it.unreadCount) },
            )
    }
}

data class MessageUnreadItem(
    val messageId: Long,
    val unreadCount: Long,
)

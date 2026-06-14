package com.project.realtime_chat_platform.controller.dto.chat

/**
 * STOMP 채팅 메시지 페이로드.
 *
 * 클라이언트가 발행할 때(`/publish/{roomId}`)와 구독자에게 브로드캐스트할 때
 * (`/topic/{roomId}`) 동일하게 사용한다. `roomId`는 발행 시점에는 비어 있고,
 * 서버가 STOMP 목적지에서 추출해 채워 넣는다.
 */
data class ChatMessageRequest(
    var roomId: Long? = null,
    val message: String,
    val senderEmail: String,
)

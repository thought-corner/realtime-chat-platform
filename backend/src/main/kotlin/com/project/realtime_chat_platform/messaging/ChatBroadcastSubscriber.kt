package com.project.realtime_chat_platform.messaging

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Component

/**
 * Redis 채널에서 브로드캐스트를 수신해, 이 인스턴스에 연결된 STOMP 구독자에게 전달한다.
 *
 * [ChatBroadcaster]가 발행한 [ChatBroadcastMessage]를 받아 `/topic/{roomId}`로 relay한다.
 * 페이로드는 [com.fasterxml.jackson.databind.JsonNode] 그대로 보내므로, 원본 객체의 JSON이
 * 그대로 클라이언트에 전달된다.
 */
@Component
class ChatBroadcastSubscriber(
    private val messageTemplate: SimpMessageSendingOperations,
    private val objectMapper: ObjectMapper,
) : MessageListener {
    override fun onMessage(
        message: Message,
        pattern: ByteArray?,
    ) {
        val envelope = objectMapper.readValue(message.body, ChatBroadcastMessage::class.java)
        messageTemplate.convertAndSend("/topic/${envelope.roomId}", envelope.payload)
    }
}

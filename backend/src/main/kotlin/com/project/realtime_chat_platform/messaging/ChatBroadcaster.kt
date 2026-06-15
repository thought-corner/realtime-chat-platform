package com.project.realtime_chat_platform.messaging

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component

/**
 * 채팅 이벤트를 Redis Pub/Sub 채널로 발행한다.
 *
 * 컨트롤러는 STOMP 브로커에 직접 보내지 않고 이 컴포넌트로 발행한다. 그러면 모든 인스턴스의
 * [ChatBroadcastSubscriber]가 수신해 각자의 로컬 `/topic/{roomId}` 구독자에게 전달하므로,
 * 다중 인스턴스에서도 메시지가 모든 사용자에게 도달한다.
 */
@Component
class ChatBroadcaster(
    private val stringRedisTemplate: StringRedisTemplate,
    private val objectMapper: ObjectMapper,
) {
    /**
     * 주어진 방으로 보낼 페이로드를 Redis 채널에 발행한다.
     *
     * @param roomId 대상 채팅방
     * @param payload 구독자에게 전달할 객체(채팅 메시지, 읽음 이벤트 등)
     */
    fun broadcast(
        roomId: Long,
        payload: Any,
    ) {
        val envelope = ChatBroadcastMessage(roomId, objectMapper.valueToTree(payload))
        stringRedisTemplate.convertAndSend(CHANNEL, objectMapper.writeValueAsString(envelope))
    }

    companion object {
        const val CHANNEL = "chat"
    }
}

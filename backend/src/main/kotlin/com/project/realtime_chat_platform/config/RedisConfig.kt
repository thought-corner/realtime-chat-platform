package com.project.realtime_chat_platform.config

import com.project.realtime_chat_platform.messaging.ChatBroadcaster
import com.project.realtime_chat_platform.messaging.ChatBroadcastSubscriber
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer

/**
 * Redis Pub/Sub 설정. 채팅 브로드캐스트 채널을 구독해 [ChatBroadcastSubscriber]로 메시지를 흘려보낸다.
 *
 * 연결 팩토리·`StringRedisTemplate`은 Spring Boot가 `spring.data.redis.*` 설정으로 자동 구성한 것을 재사용한다.
 */
@Configuration
class RedisConfig {
    @Bean
    fun redisMessageListenerContainer(
        connectionFactory: RedisConnectionFactory,
        subscriber: ChatBroadcastSubscriber,
    ): RedisMessageListenerContainer {
        val container = RedisMessageListenerContainer()
        container.setConnectionFactory(connectionFactory)
        container.addMessageListener(subscriber, ChannelTopic(ChatBroadcaster.CHANNEL))
        return container
    }
}

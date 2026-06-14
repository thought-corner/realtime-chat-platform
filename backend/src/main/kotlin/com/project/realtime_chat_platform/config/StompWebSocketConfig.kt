package com.project.realtime_chat_platform.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

/**
 * STOMP over WebSocket 설정.
 *
 * WebSocket 연결 위에 STOMP 메시징 계층을 활성화하여, 채팅방 단위의 구독(subscribe)/발행(publish) 라우팅을 프레임워크가 처리하도록 구성한다.
 */
@Configuration
@EnableWebSocketMessageBroker
class StompWebSocketConfig(
    private val stompAuthChannelInterceptor: StompAuthChannelInterceptor,
) : WebSocketMessageBrokerConfigurer {
    /**
     * 클라이언트가 STOMP 연결을 맺을 핸드셰이크 엔드포인트를 등록한다.
     *
     * `/connect`로 들어오는 WebSocket 핸드셰이크만 허용하며, 프론트엔드 dev 서버(`http://localhost:3000`)의 cross-origin 연결을 허용한다.
     */
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry
            .addEndpoint("/connect")
            .setAllowedOrigins("http://localhost:3000")
    }

    /**
     * 메시지 브로커의 라우팅 규칙을 설정한다.
     *
     * - `/publish`: 클라이언트가 서버로 메시지를 보낼 때 붙이는 prefix로, `/publish/{roomId}` 형태로 발행하면 `@MessageMapping` 핸들러로 라우팅된다.
     * - `/topic`: 인메모리 SimpleBroker가 처리하는 구독 경로로, `/topic/{roomId}`를 구독한 클라이언트에게 메시지를 브로드캐스트한다.
     */
    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.setApplicationDestinationPrefixes("/publish")
        registry.enableSimpleBroker("/topic")
    }

    /**
     * 클라이언트 → 서버 방향 인바운드 채널에 [StompAuthChannelInterceptor]를 등록한다.
     *
     * CONNECT/SUBSCRIBE 등 STOMP 프레임이 컨트롤러로 전달되기 전에 인터셉터를 거쳐
     * JWT 토큰 유효성을 검증하도록 한다.
     */
    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(stompAuthChannelInterceptor)
    }
}

package com.project.realtime_chat_platform.config

import com.project.realtime_chat_platform.security.JwtTokenProvider
import org.slf4j.LoggerFactory
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.stereotype.Component

/**
 * STOMP 메시징 계층의 JWT 인증 인터셉터.
 *
 * WebSocket은 핸드셰이크 이후 서블릿 필터 체인을 거치지 않으므로, HTTP 요청을 검증하는 [com.project.realtime_chat_platform.security.JwtAuthFilter]는 STOMP 프레임에 닿지 못한다.
 * 이 인터셉터가 그 빈틈을 메워, 클라이언트가 보낸 STOMP 프레임을 컨트롤러로 전달하기 전에 가로채 `Authorization` 네이티브 헤더의 JWT를 검증한다.
 *
 * - CONNECT: 연결 시점의 신원을 확인한다.
 * - SUBSCRIBE: 구독 요청자의 토큰을 확인한다. (방 참여자 인가 검증은 추후 추가)
 */
@Component
class StompAuthChannelInterceptor(
    private val jwtTokenProvider: JwtTokenProvider,
) : ChannelInterceptor {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun preSend(
        message: Message<*>,
        channel: MessageChannel,
    ): Message<*> {
        val accessor = StompHeaderAccessor.wrap(message)
        when (accessor.command) {
            StompCommand.CONNECT -> {
                log.info("STOMP CONNECT 토큰 검증")
                verifyToken(accessor)
            }

            StompCommand.SUBSCRIBE -> {
                log.info("STOMP SUBSCRIBE 토큰 검증")
                verifyToken(accessor)
            }

            else -> {
                Unit
            }
        }
        return message
    }

    /**
     * STOMP 프레임의 `Authorization` 네이티브 헤더에서 Bearer 토큰을 꺼내 검증을 위임한다.
     * 토큰 검증 자체는 [JwtTokenProvider]가 담당하며, 토큰이 없거나 유효하지 않으면
     * 예외가 전파되어 해당 STOMP 프레임이 거부된다.
     */
    private fun verifyToken(accessor: StompHeaderAccessor) {
        val bearerToken =
            requireNotNull(accessor.getFirstNativeHeader("Authorization")) {
                "Authorization 헤더가 없습니다."
            }
        require(bearerToken.startsWith("Bearer ")) { "Bearer 형식이 아닙니다." }
        jwtTokenProvider.validateToken(bearerToken.substring(7))
    }
}

package com.project.realtime_chat_platform.config

import com.project.realtime_chat_platform.security.JwtTokenProvider
import com.project.realtime_chat_platform.service.ChatRoomService
import org.slf4j.LoggerFactory
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.stereotype.Component

/**
 * STOMP 메시징 계층의 JWT 인증·인가 인터셉터.
 *
 * WebSocket은 핸드셰이크 이후 서블릿 필터 체인을 거치지 않으므로, HTTP 요청을 검증하는 [com.project.realtime_chat_platform.security.JwtAuthFilter]는 STOMP 프레임에 닿지 못한다.
 * 이 인터셉터가 그 빈틈을 메운다.
 *
 * - CONNECT: 한 번만 토큰을 검증해 신원(이메일)을 STOMP 세션 속성에 저장한다.
 * - SUBSCRIBE: 토큰을 다시 요구하지 않고, CONNECT 때 저장한 신원으로 구독 대상 채팅방(`/topic/{roomId}`)의 참여자인지 인가한다.
 *
 * 핸드셰이크(`/connect`)에서 Principal을 만들지 않는 구성이라 `accessor.user`는 프레임 간 유지되지 않는다.
 * 반면 세션 속성([StompHeaderAccessor.getSessionAttributes])은 WebSocket 세션의 실제 속성 맵이라
 * 모든 프레임에 일관되게 따라붙으므로, 프레임 간 신원 전달에 이를 사용한다.
 */
@Component
class StompAuthChannelInterceptor(
    private val jwtTokenProvider: JwtTokenProvider,
    private val chatRoomService: ChatRoomService,
) : ChannelInterceptor {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun preSend(
        message: Message<*>,
        channel: MessageChannel,
    ): Message<*> {
        val accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java) ?: return message
        when (accessor.command) {
            StompCommand.CONNECT -> {
                log.info("STOMP CONNECT 토큰 검증")
                val email = authenticate(accessor)
                accessor.sessionAttributes?.put(MEMBER_EMAIL, email)
            }

            StompCommand.SUBSCRIBE -> {
                log.info("STOMP SUBSCRIBE 참여자 인가")
                authorizeRoomSubscription(accessor.sessionAttributes?.get(MEMBER_EMAIL) as? String, accessor.destination)
            }

            else -> {
                Unit
            }
        }
        return message
    }

    /**
     * CONNECT 프레임의 `Authorization` 네이티브 헤더에서 Bearer 토큰을 꺼내 검증하고, 회원 이메일(subject)을 반환한다.
     * 토큰 검증은 [JwtTokenProvider]가 담당하며, 토큰이 없거나 유효하지 않으면 예외가 전파되어 연결이 거부된다.
     */
    private fun authenticate(accessor: StompHeaderAccessor): String {
        val bearerToken =
            requireNotNull(accessor.getFirstNativeHeader("Authorization")) {
                "Authorization 헤더가 없습니다."
            }
        require(bearerToken.startsWith("Bearer ")) { "Bearer 형식이 아닙니다." }
        return jwtTokenProvider.validateToken(bearerToken.substring(7)).subject
    }

    /**
     * CONNECT에서 인증된 신원으로 구독 대상 채팅방의 참여자인지 인가한다.
     * 인증되지 않은 세션이거나 참여자가 아니면 구독을 거부한다.
     */
    private fun authorizeRoomSubscription(
        email: String?,
        destination: String?,
    ) {
        val authenticatedEmail = email ?: throw AuthenticationServiceException("인증되지 않은 연결입니다.")
        val roomId = roomIdFrom(destination)
        if (!chatRoomService.isRoomParticipant(authenticatedEmail, roomId)) {
            throw AuthenticationServiceException("해당 채팅방에 대한 권한이 없습니다.")
        }
    }

    /**
     * `/topic/{roomId}` 형태의 구독 목적지에서 채팅방 식별자를 추출한다.
     */
    private fun roomIdFrom(destination: String?): Long {
        val raw = requireNotNull(destination) { "구독 목적지가 없습니다." }
        return raw.substringAfterLast("/").toLongOrNull()
            ?: throw AuthenticationServiceException("잘못된 구독 목적지입니다: $raw")
    }

    companion object {
        private const val MEMBER_EMAIL = "memberEmail"
    }
}

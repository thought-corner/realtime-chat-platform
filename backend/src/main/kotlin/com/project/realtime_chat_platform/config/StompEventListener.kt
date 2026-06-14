package com.project.realtime_chat_platform.config

import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import java.util.concurrent.ConcurrentHashMap

/**
 * STOMP 세션 연결/해제 이벤트 리스너.
 *
 * 세션 생명주기는 Spring/STOMP가 내부적으로 관리하므로 동작에 필수는 아니나, 현재 연결된 세션 수를 실시간으로 추적해 로깅함으로써 연결 상태를 관찰(디버깅/모니터링)할 수 있게 한다.
 */
@Component
class StompEventListener {
    private val log = LoggerFactory.getLogger(javaClass)
    private val sessions: MutableSet<String> = ConcurrentHashMap.newKeySet()

    @EventListener
    fun connectHandle(event: SessionConnectEvent) {
        val accessor = StompHeaderAccessor.wrap(event.message)
        accessor.sessionId?.let { sessions.add(it) }
        log.info("STOMP connect sessionId={}, total={}", accessor.sessionId, sessions.size)
    }

    @EventListener
    fun disconnectHandle(event: SessionDisconnectEvent) {
        val accessor = StompHeaderAccessor.wrap(event.message)
        accessor.sessionId?.let { sessions.remove(it) }
        log.info("STOMP disconnect sessionId={}, total={}", accessor.sessionId, sessions.size)
    }
}

package com.project.realtime_chat_platform.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.Base64
import java.util.Date
import javax.crypto.spec.SecretKeySpec

@Component
class JwtTokenProvider(
    @Value("\${jwt.secretKey}") private val secretKey: String,
    @Value("\${jwt.expiration}") private val expiration: Int,
) {
    private val signingKey: Key =
        SecretKeySpec(Base64.getDecoder().decode(secretKey), SignatureAlgorithm.HS512.jcaName)

    fun createToken(
        email: String,
        role: String,
    ): String {
        val claims: Claims = Jwts.claims().setSubject(email)
        claims["role"] = role
        val now = Date()
        return Jwts
            .builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + expiration * 60_000L))
            .signWith(signingKey)
            .compact()
    }

    /**
     * JWT의 서명·만료를 검증하고 클레임을 반환한다. 토큰을 다루는 단일 책임자로서,
     * HTTP 요청([JwtAuthFilter])과 STOMP 프레임(`StompAuthChannelInterceptor`)
     * 양쪽의 검증 진입점이다. 유효하지 않으면 `JwtException`이 전파된다.
     */
    fun validateToken(token: String): Claims =
        Jwts
            .parserBuilder()
            .setSigningKey(signingKey)
            .build()
            .parseClaimsJws(token)
            .body
}

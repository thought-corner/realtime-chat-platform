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
}

package com.project.realtime_chat_platform.security

import io.jsonwebtoken.Jwts
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    @Value("\${jwt.secretKey}") private val secretKey: String,
) : OncePerRequestFilter() {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val header = request.getHeader("Authorization")
        if (header != null) {
            try {
                authenticate(header)
            } catch (e: Exception) {
                log.warn("invalid jwt token", e)
                response.status = HttpStatus.UNAUTHORIZED.value()
                response.contentType = "application/json"
                response.writer.write("invalid token")
                return
            }
        }
        filterChain.doFilter(request, response)
    }

    private fun authenticate(header: String) {
        require(header.startsWith("Bearer ")) { "Bearer 형식이 아닙니다." }
        val jwtToken = header.substring(7)
        val claims =
            Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwtToken)
                .body
        val authorities: List<GrantedAuthority> =
            listOf(SimpleGrantedAuthority("ROLE_" + claims["role"]))
        val userDetails: UserDetails = User(claims.subject, "", authorities)
        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }
}

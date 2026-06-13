package com.project.realtime_chat_platform.service

import com.project.realtime_chat_platform.common.exception.BusinessException
import com.project.realtime_chat_platform.common.exception.MemberErrorCode
import com.project.realtime_chat_platform.domain.Member
import com.project.realtime_chat_platform.repository.MemberRepository
import com.project.realtime_chat_platform.security.JwtTokenProvider
import com.project.realtime_chat_platform.service.dto.auth.SignInResult
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AuthService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
) {
    fun signUp(
        name: String,
        email: String,
        password: String,
    ): Long {
        if (memberRepository.findByEmail(email) != null) {
            throw BusinessException(MemberErrorCode.DUPLICATE_EMAIL)
        }
        val member = Member(name = name, email = email, password = passwordEncoder.encode(password)!!)
        return memberRepository.save(member).id!!
    }

    fun signIn(
        email: String,
        password: String,
    ): SignInResult {
        val member =
            memberRepository.findByEmail(email)
                ?: throw BusinessException(MemberErrorCode.INVALID_CREDENTIALS)
        if (!passwordEncoder.matches(password, member.password)) {
            throw BusinessException(MemberErrorCode.INVALID_CREDENTIALS)
        }
        val token = jwtTokenProvider.createToken(member.email, member.role.toString())
        return SignInResult.from(member, token)
    }
}

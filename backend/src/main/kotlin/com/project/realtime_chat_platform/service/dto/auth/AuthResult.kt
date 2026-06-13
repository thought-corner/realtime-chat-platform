package com.project.realtime_chat_platform.service.dto.auth

import com.project.realtime_chat_platform.domain.Member

data class SignInResult(
    val id: Long?,
    val token: String,
) {
    companion object {
        fun from(
            member: Member,
            token: String,
        ) = SignInResult(
            id = member.id,
            token = token,
        )
    }
}

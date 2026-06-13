package com.project.realtime_chat_platform.controller.dto.auth

import com.project.realtime_chat_platform.service.dto.auth.SignInResult

data class SignInResponse(
    val id: Long?,
    val token: String,
) {
    companion object {
        fun from(result: SignInResult) =
            SignInResponse(
                id = result.id,
                token = result.token,
            )
    }
}

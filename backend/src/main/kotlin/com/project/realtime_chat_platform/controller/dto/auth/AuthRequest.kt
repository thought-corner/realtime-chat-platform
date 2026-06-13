package com.project.realtime_chat_platform.controller.dto.auth

data class SignUpRequest(
    val name: String,
    val email: String,
    val password: String,
)

data class SignInRequest(
    val email: String,
    val password: String,
)

package com.project.realtime_chat_platform.controller

import com.project.realtime_chat_platform.controller.dto.auth.SignInRequest
import com.project.realtime_chat_platform.controller.dto.auth.SignInResponse
import com.project.realtime_chat_platform.controller.dto.auth.SignUpRequest
import com.project.realtime_chat_platform.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/member")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/sign-up")
    fun signUp(
        @RequestBody request: SignUpRequest,
    ): ResponseEntity<Long> {
        val id = authService.signUp(request.name, request.email, request.password)
        return ResponseEntity(id, HttpStatus.CREATED)
    }

    @PostMapping("/sign-in")
    fun signIn(
        @RequestBody request: SignInRequest,
    ): ResponseEntity<SignInResponse> {
        val result = authService.signIn(request.email, request.password)
        return ResponseEntity(SignInResponse.from(result), HttpStatus.OK)
    }
}

package com.project.realtime_chat_platform.controller

import com.project.realtime_chat_platform.common.exception.BusinessException
import com.project.realtime_chat_platform.common.exception.ErrorResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(e: BusinessException): ResponseEntity<ErrorResponse> {
        val errorCode = e.errorCode
        return ResponseEntity
            .status(errorCode.status)
            .body(ErrorResponse(errorCode.code, errorCode.message))
    }
}

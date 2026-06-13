package com.project.realtime_chat_platform.common.exception

import org.springframework.http.HttpStatus

interface ErrorCode {
    val code: String
    val status: HttpStatus
    val message: String
}

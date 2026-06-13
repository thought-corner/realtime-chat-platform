package com.project.realtime_chat_platform.common.exception

import org.springframework.http.HttpStatus

enum class MemberErrorCode(
    override val status: HttpStatus,
    override val message: String,
) : ErrorCode {
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."),
    ;

    override val code: String get() = name
}

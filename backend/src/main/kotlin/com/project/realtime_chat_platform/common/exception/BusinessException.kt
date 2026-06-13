package com.project.realtime_chat_platform.common.exception

open class BusinessException(
    val errorCode: ErrorCode,
) : RuntimeException(errorCode.message)

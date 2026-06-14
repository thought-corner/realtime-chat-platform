package com.project.realtime_chat_platform.common.exception

import org.springframework.http.HttpStatus

enum class ChatErrorCode(
    override val status: HttpStatus,
    override val message: String,
) : ErrorCode {
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    PARTICIPANT_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅방 참여자를 찾을 수 없습니다."),
    NOT_GROUP_CHAT(HttpStatus.BAD_REQUEST, "그룹 채팅방이 아닙니다."),
    NOT_ROOM_PARTICIPANT(HttpStatus.FORBIDDEN, "본인이 속하지 않은 채팅방입니다."),
    ;

    override val code: String get() = name
}

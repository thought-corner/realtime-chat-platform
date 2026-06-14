package com.project.realtime_chat_platform.repository

import com.project.realtime_chat_platform.common.exception.BusinessException
import com.project.realtime_chat_platform.common.exception.ChatErrorCode
import com.project.realtime_chat_platform.domain.ChatRoom
import com.project.realtime_chat_platform.domain.Member
import org.springframework.data.repository.findByIdOrNull

/**
 * 채팅 도메인에서 반복되는 "조회 후 없으면 예외" 패턴을 모은 확장 함수.
 * 여러 서비스가 동일한 not-found 처리를 중복하지 않도록 한다.
 */

fun ChatRoomRepository.getOrThrow(roomId: Long): ChatRoom = findByIdOrNull(roomId) ?: throw BusinessException(ChatErrorCode.ROOM_NOT_FOUND)

fun MemberRepository.getByEmailOrThrow(email: String): Member =
    findByEmail(email) ?: throw BusinessException(ChatErrorCode.MEMBER_NOT_FOUND)

fun MemberRepository.getByIdOrThrow(id: Long): Member = findByIdOrNull(id) ?: throw BusinessException(ChatErrorCode.MEMBER_NOT_FOUND)

package com.project.realtime_chat_platform.repository

import com.project.realtime_chat_platform.domain.ChatMessage
import com.project.realtime_chat_platform.domain.ChatRoom
import com.project.realtime_chat_platform.domain.Member
import com.project.realtime_chat_platform.domain.ReadStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReadStatusRepository : JpaRepository<ReadStatus, Long> {
    fun findByChatRoomAndMember(
        chatRoom: ChatRoom,
        member: Member,
    ): List<ReadStatus>

    fun countByChatRoomAndMemberAndIsReadFalse(
        chatRoom: ChatRoom,
        member: Member,
    ): Long

    fun countByChatMessageAndIsReadFalse(chatMessage: ChatMessage): Long
}

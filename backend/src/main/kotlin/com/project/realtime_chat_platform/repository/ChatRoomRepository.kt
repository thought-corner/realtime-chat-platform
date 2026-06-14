package com.project.realtime_chat_platform.repository

import com.project.realtime_chat_platform.domain.ChatRoom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatRoomRepository : JpaRepository<ChatRoom, Long> {
    fun findByIsGroupChat(isGroupChat: String): List<ChatRoom>
}

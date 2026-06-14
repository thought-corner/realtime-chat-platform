package com.project.realtime_chat_platform.repository

import com.project.realtime_chat_platform.domain.ChatMessage
import com.project.realtime_chat_platform.domain.ChatRoom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatMessageRepository : JpaRepository<ChatMessage, Long> {
    fun findByChatRoomOrderByCreatedTimeAsc(chatRoom: ChatRoom): List<ChatMessage>
}

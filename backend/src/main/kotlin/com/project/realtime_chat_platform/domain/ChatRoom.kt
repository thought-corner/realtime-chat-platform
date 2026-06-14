package com.project.realtime_chat_platform.domain

import com.project.realtime_chat_platform.common.domain.BaseTimeEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

/**
 * 채팅방. 그룹 채팅(`isGroupChat = "Y"`)과 1:1 채팅(`"N"`)을 함께 표현한다.
 */
@Entity
class ChatRoom(
    @Column(nullable = false)
    var name: String,
    var isGroupChat: String = "N",
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @OneToMany(mappedBy = "chatRoom", cascade = [CascadeType.REMOVE])
    val chatParticipants: MutableList<ChatParticipant> = mutableListOf()

    @OneToMany(mappedBy = "chatRoom", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    val chatMessages: MutableList<ChatMessage> = mutableListOf()
}

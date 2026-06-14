package com.project.realtime_chat_platform.domain

import com.project.realtime_chat_platform.common.domain.BaseTimeEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany

/**
 * 채팅 메시지. 어떤 회원이 어떤 채팅방에 보낸 한 건의 메시지를 나타낸다.
 */
@Entity
class ChatMessage(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    var chatRoom: ChatRoom,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    var member: Member,
    @Column(nullable = false, length = 500)
    var content: String,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @OneToMany(mappedBy = "chatMessage", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    val readStatuses: MutableList<ReadStatus> = mutableListOf()
}

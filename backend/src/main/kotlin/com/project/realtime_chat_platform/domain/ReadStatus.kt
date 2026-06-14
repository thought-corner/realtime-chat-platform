package com.project.realtime_chat_platform.domain

import com.project.realtime_chat_platform.common.domain.BaseTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

/**
 * 메시지 읽음 상태. 메시지 한 건에 대해 참여자별로 읽음 여부를 기록하여,
 * 채팅방별 안 읽은 메시지 수를 계산하는 근거가 된다.
 */
@Entity
class ReadStatus(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    var chatRoom: ChatRoom,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    var member: Member,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_message_id", nullable = false)
    var chatMessage: ChatMessage,
    @Column(nullable = false)
    var isRead: Boolean,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun updateIsRead(isRead: Boolean) {
        this.isRead = isRead
    }
}

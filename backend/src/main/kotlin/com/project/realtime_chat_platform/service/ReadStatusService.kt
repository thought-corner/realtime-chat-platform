package com.project.realtime_chat_platform.service

import com.project.realtime_chat_platform.domain.ChatMessage
import com.project.realtime_chat_platform.domain.ChatRoom
import com.project.realtime_chat_platform.domain.Member
import com.project.realtime_chat_platform.domain.ReadStatus
import com.project.realtime_chat_platform.repository.ChatParticipantRepository
import com.project.realtime_chat_platform.repository.ChatRoomRepository
import com.project.realtime_chat_platform.repository.MemberRepository
import com.project.realtime_chat_platform.repository.ReadStatusRepository
import com.project.realtime_chat_platform.repository.getByEmailOrThrow
import com.project.realtime_chat_platform.repository.getOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 메시지 읽음 추적 책임. 새 메시지에 대한 참여자별 읽음 상태 생성, 읽음 처리, 안 읽은 수 집계를 담당한다.
 */
@Service
@Transactional
class ReadStatusService(
    private val chatRoomRepository: ChatRoomRepository,
    private val memberRepository: MemberRepository,
    private val chatParticipantRepository: ChatParticipantRepository,
    private val readStatusRepository: ReadStatusRepository,
) {
    /**
     * 새로 저장된 메시지에 대해 채팅방 참여자별 읽음 상태를 생성한다. 보낸 사람 본인은 읽음 처리한다.
     */
    fun createForNewMessage(
        chatRoom: ChatRoom,
        chatMessage: ChatMessage,
        sender: Member,
    ) {
        chatParticipantRepository.findByChatRoom(chatRoom).forEach { participant ->
            readStatusRepository.save(
                ReadStatus(
                    chatRoom = chatRoom,
                    member = participant.member,
                    chatMessage = chatMessage,
                    isRead = participant.member.id == sender.id,
                ),
            )
        }
    }

    /**
     * 현재 회원의 해당 채팅방 메시지를 모두 읽음 처리한다.
     */
    fun markRead(
        email: String,
        roomId: Long,
    ) {
        val chatRoom = chatRoomRepository.getOrThrow(roomId)
        val member = memberRepository.getByEmailOrThrow(email)
        readStatusRepository.findByChatRoomAndMember(chatRoom, member).forEach { it.updateIsRead(true) }
    }

    @Transactional(readOnly = true)
    fun countUnread(
        chatRoom: ChatRoom,
        member: Member,
    ): Long = readStatusRepository.countByChatRoomAndMemberAndIsReadFalse(chatRoom, member)
}

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
import com.project.realtime_chat_platform.service.dto.chat.MessageUnreadResult
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
     * 현재 회원의 해당 채팅방 메시지를 모두 읽음 처리하고, 이로 인해 안 읽은 인원 수가 바뀐
     * 메시지들의 갱신된 수를 반환한다. (실시간 읽음 전파에 사용)
     *
     * 실제로 미읽음→읽음으로 바뀐 행만 대상으로 하므로, 이미 다 읽은 상태면 빈 목록을 반환한다.
     */
    fun markRead(
        email: String,
        roomId: Long,
    ): List<MessageUnreadResult> {
        val chatRoom = chatRoomRepository.getOrThrow(roomId)
        val member = memberRepository.getByEmailOrThrow(email)
        val changed = readStatusRepository.findByChatRoomAndMember(chatRoom, member).filter { !it.isRead }
        changed.forEach { it.updateIsRead(true) }
        // 변경된 메시지별로 갱신된 미읽음 수를 계산한다(같은 메시지 중복 제거).
        // 같은 트랜잭션 내 COUNT 쿼리는 flush 후 실행되어 갱신된 is_read 값을 반영한다.
        return changed
            .map { it.chatMessage }
            .distinctBy { it.id }
            .map { message -> MessageUnreadResult(message.id!!, countUnreadByMessage(message)) }
    }

    @Transactional(readOnly = true)
    fun countUnread(
        chatRoom: ChatRoom,
        member: Member,
    ): Long = readStatusRepository.countByChatRoomAndMemberAndIsReadFalse(chatRoom, member)

    /**
     * 특정 메시지를 아직 읽지 않은 참여자 수를 센다. 보낸 사람은 생성 시 읽음 처리되므로
     * 결과는 "이 메시지를 안 읽은 다른 참여자 수"가 된다. (카카오톡의 안 읽은 인원 표시)
     */
    @Transactional(readOnly = true)
    fun countUnreadByMessage(chatMessage: ChatMessage): Long =
        readStatusRepository.countByChatMessageAndIsReadFalse(chatMessage)
}

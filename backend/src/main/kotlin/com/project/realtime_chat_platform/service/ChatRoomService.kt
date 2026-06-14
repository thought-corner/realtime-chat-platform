package com.project.realtime_chat_platform.service

import com.project.realtime_chat_platform.common.exception.BusinessException
import com.project.realtime_chat_platform.common.exception.ChatErrorCode
import com.project.realtime_chat_platform.domain.ChatParticipant
import com.project.realtime_chat_platform.domain.ChatRoom
import com.project.realtime_chat_platform.domain.Member
import com.project.realtime_chat_platform.repository.ChatParticipantRepository
import com.project.realtime_chat_platform.repository.ChatRoomRepository
import com.project.realtime_chat_platform.repository.MemberRepository
import com.project.realtime_chat_platform.repository.getByEmailOrThrow
import com.project.realtime_chat_platform.repository.getByIdOrThrow
import com.project.realtime_chat_platform.repository.getOrThrow
import com.project.realtime_chat_platform.service.dto.chat.ChatRoomResult
import com.project.realtime_chat_platform.service.dto.chat.MyChatRoomResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 채팅방 생명주기와 참여자 관리 책임. 그룹/1:1 방 개설·참여·퇴장, 내 방 목록, 참여자 인가를 담당한다.
 */
@Service
@Transactional
class ChatRoomService(
    private val chatRoomRepository: ChatRoomRepository,
    private val chatParticipantRepository: ChatParticipantRepository,
    private val memberRepository: MemberRepository,
    private val readStatusService: ReadStatusService,
) {
    /**
     * 그룹 채팅방을 개설하고 개설자를 참여자로 등록한다.
     */
    fun createGroupRoom(
        email: String,
        chatRoomName: String,
    ) {
        val member = memberRepository.getByEmailOrThrow(email)
        val chatRoom = chatRoomRepository.save(ChatRoom(name = chatRoomName, isGroupChat = "Y"))
        chatParticipantRepository.save(ChatParticipant(chatRoom = chatRoom, member = member))
    }

    @Transactional(readOnly = true)
    fun getGroupChatRooms(): List<ChatRoomResult> = chatRoomRepository.findByIsGroupChat("Y").map(ChatRoomResult::from)

    /**
     * 현재 회원을 그룹 채팅방에 참여시킨다. 이미 참여 중이면 아무 동작도 하지 않는다.
     */
    fun addParticipantToGroupChat(
        email: String,
        roomId: Long,
    ) {
        val chatRoom = chatRoomRepository.getOrThrow(roomId)
        val member = memberRepository.getByEmailOrThrow(email)
        if (chatRoom.isGroupChat == "N") {
            throw BusinessException(ChatErrorCode.NOT_GROUP_CHAT)
        }
        if (chatParticipantRepository.findByChatRoomAndMember(chatRoom, member) == null) {
            addParticipantToRoom(chatRoom, member)
        }
    }

    fun addParticipantToRoom(
        chatRoom: ChatRoom,
        member: Member,
    ) {
        chatParticipantRepository.save(ChatParticipant(chatRoom = chatRoom, member = member))
    }

    /**
     * 현재 회원이 참여 중인 채팅방 목록을 안 읽은 메시지 수와 함께 조회한다.
     */
    @Transactional(readOnly = true)
    fun getMyChatRooms(email: String): List<MyChatRoomResult> {
        val member = memberRepository.getByEmailOrThrow(email)
        return chatParticipantRepository.findAllByMember(member).map { participant ->
            MyChatRoomResult(
                roomId = participant.chatRoom.id,
                roomName = participant.chatRoom.name,
                isGroupChat = participant.chatRoom.isGroupChat,
                unreadCount = readStatusService.countUnread(participant.chatRoom, member),
            )
        }
    }

    /**
     * 현재 회원이 그룹 채팅방에서 나간다. 마지막 참여자가 나가면 채팅방 자체를 삭제한다.
     */
    fun leaveGroupChatRoom(
        email: String,
        roomId: Long,
    ) {
        val chatRoom = chatRoomRepository.getOrThrow(roomId)
        val member = memberRepository.getByEmailOrThrow(email)
        if (chatRoom.isGroupChat == "N") {
            throw BusinessException(ChatErrorCode.NOT_GROUP_CHAT)
        }
        val participant =
            chatParticipantRepository.findByChatRoomAndMember(chatRoom, member)
                ?: throw BusinessException(ChatErrorCode.PARTICIPANT_NOT_FOUND)
        chatParticipantRepository.delete(participant)
        if (chatParticipantRepository.findByChatRoom(chatRoom).isEmpty()) {
            chatRoomRepository.delete(chatRoom)
        }
    }

    /**
     * 현재 회원과 상대 회원의 1:1 채팅방을 반환한다. 없으면 새로 개설하고 두 사람을 참여자로 등록한다.
     */
    fun getOrCreatePrivateRoom(
        email: String,
        otherMemberId: Long,
    ): Long {
        val member = memberRepository.getByEmailOrThrow(email)
        val otherMember = memberRepository.getByIdOrThrow(otherMemberId)

        chatParticipantRepository.findExistingPrivateRoom(member.id!!, otherMember.id!!)?.let { return it.id!! }

        val newRoom =
            chatRoomRepository.save(
                ChatRoom(name = "${member.name}-${otherMember.name}", isGroupChat = "N"),
            )
        addParticipantToRoom(newRoom, member)
        addParticipantToRoom(newRoom, otherMember)
        return newRoom.id!!
    }

    /**
     * 주어진 이메일의 회원이 해당 채팅방의 참여자인지 검증한다. (STOMP SUBSCRIBE 인가, 이력 조회 인가에 사용)
     */
    @Transactional(readOnly = true)
    fun isRoomParticipant(
        email: String,
        roomId: Long,
    ): Boolean {
        val chatRoom = chatRoomRepository.getOrThrow(roomId)
        val member = memberRepository.getByEmailOrThrow(email)
        return chatParticipantRepository.findByChatRoom(chatRoom).any { it.member.id == member.id }
    }
}

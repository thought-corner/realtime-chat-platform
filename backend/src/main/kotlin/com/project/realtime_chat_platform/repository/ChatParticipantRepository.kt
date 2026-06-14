package com.project.realtime_chat_platform.repository

import com.project.realtime_chat_platform.domain.ChatParticipant
import com.project.realtime_chat_platform.domain.ChatRoom
import com.project.realtime_chat_platform.domain.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ChatParticipantRepository : JpaRepository<ChatParticipant, Long> {
    fun findByChatRoom(chatRoom: ChatRoom): List<ChatParticipant>

    fun findByChatRoomAndMember(
        chatRoom: ChatRoom,
        member: Member,
    ): ChatParticipant?

    fun findAllByMember(member: Member): List<ChatParticipant>

    /**
     * 두 회원이 모두 참여 중인 기존 1:1 채팅방을 찾는다. 같은 채팅방에 두 참여자가
     * 함께 묶여 있고 그룹 채팅이 아닌(`isGroupChat = 'N'`) 방을 self-join으로 조회한다.
     */
    @Query(
        "SELECT cp1.chatRoom FROM ChatParticipant cp1 " +
            "JOIN ChatParticipant cp2 ON cp1.chatRoom.id = cp2.chatRoom.id " +
            "WHERE cp1.member.id = :myId AND cp2.member.id = :otherMemberId " +
            "AND cp1.chatRoom.isGroupChat = 'N'",
    )
    fun findExistingPrivateRoom(
        @Param("myId") myId: Long,
        @Param("otherMemberId") otherMemberId: Long,
    ): ChatRoom?
}

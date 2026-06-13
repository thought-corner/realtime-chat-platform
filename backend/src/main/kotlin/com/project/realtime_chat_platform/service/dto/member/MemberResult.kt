package com.project.realtime_chat_platform.service.dto.member

import com.project.realtime_chat_platform.domain.Member

data class MemberSummaryResult(
    val id: Long?,
    val name: String,
    val email: String,
) {
    companion object {
        fun from(member: Member) =
            MemberSummaryResult(
                id = member.id,
                name = member.name,
                email = member.email,
            )
    }
}

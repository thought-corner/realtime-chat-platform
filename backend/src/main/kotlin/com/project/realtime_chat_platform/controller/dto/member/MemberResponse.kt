package com.project.realtime_chat_platform.controller.dto.member

import com.project.realtime_chat_platform.service.dto.member.MemberSummaryResult

data class MemberResponse(
    val id: Long?,
    val name: String,
    val email: String,
) {
    companion object {
        fun from(result: MemberSummaryResult) =
            MemberResponse(
                id = result.id,
                name = result.name,
                email = result.email,
            )
    }
}

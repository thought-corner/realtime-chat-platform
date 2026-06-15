package com.project.realtime_chat_platform.controller.dto.member

import com.project.realtime_chat_platform.service.dto.member.MemberSummaryResult

data class MemberResponseList(
    val members: List<MemberResponse>,
) {
    companion object {
        fun from(results: List<MemberSummaryResult>) = MemberResponseList(results.map(MemberResponse::from))
    }
}

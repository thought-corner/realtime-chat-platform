package com.project.realtime_chat_platform.service

import com.project.realtime_chat_platform.repository.MemberRepository
import com.project.realtime_chat_platform.service.dto.member.MemberSummaryResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService(
    private val memberRepository: MemberRepository,
) {
    fun findAll(): List<MemberSummaryResult> = memberRepository.findAll().map(MemberSummaryResult::from)
}

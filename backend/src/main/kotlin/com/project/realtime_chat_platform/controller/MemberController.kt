package com.project.realtime_chat_platform.controller

import com.project.realtime_chat_platform.controller.dto.member.MemberResponse
import com.project.realtime_chat_platform.service.MemberService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/member")
class MemberController(
    private val memberService: MemberService,
) {
    @GetMapping("/list")
    fun members(): ResponseEntity<List<MemberResponse>> {
        val responses = MemberResponse.from(memberService.findAll())
        return ResponseEntity(responses, HttpStatus.OK)
    }
}

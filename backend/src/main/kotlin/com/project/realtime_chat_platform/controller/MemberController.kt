package com.project.realtime_chat_platform.controller

import com.project.realtime_chat_platform.controller.dto.member.MemberResponseList
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
    fun members(): ResponseEntity<MemberResponseList> {
        val response = MemberResponseList.from(memberService.findAll())
        return ResponseEntity(response, HttpStatus.OK)
    }
}

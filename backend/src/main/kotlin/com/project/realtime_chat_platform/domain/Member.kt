package com.project.realtime_chat_platform.domain

import com.project.realtime_chat_platform.common.domain.BaseTimeEntity
import com.project.realtime_chat_platform.constants.Role
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Member(
    var name: String,

    @Column(nullable = false, unique = true)
    var email: String,

    var password: String,

    @Enumerated(EnumType.STRING)
    var role: Role = Role.USER,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}

package com.project.realtime_chat_platform.common.domain

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@MappedSuperclass
abstract class BaseTimeEntity {
    @CreationTimestamp
    @Column(updatable = false)
    var createdTime: LocalDateTime? = null
        protected set

    @UpdateTimestamp
    var updatedTime: LocalDateTime? = null
        protected set
}

package io.toasting.domain.message.entity

import io.toasting.domain.model.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Message(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val content: String,
    val senderId: Long,
    val receiverId: Long,
    val isRead: Boolean,
    val postId: Long? = null,
) : BaseEntity()

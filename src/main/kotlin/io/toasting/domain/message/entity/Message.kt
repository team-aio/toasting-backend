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
    val chatRoomId: Long,
    val content: String,
    var isRead: Boolean,
) : BaseEntity() {
    fun read() {
        isRead = true
    }
}

package io.toasting.domain.message.entity

import io.toasting.domain.model.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class ChatRoom(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val postId: Long? = null,
    val recentSenderId: Long? = null,
    val recentMessageContent: String? = null,
    val recentSendAt: LocalDateTime? = null,
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chatRoom")
    var chatMemberList: MutableList<ChatMember> = mutableListOf()
) : BaseEntity() {
    fun isActivated(): Boolean {
        return recentSenderId != null && recentMessageContent != null && recentSendAt != null
    }
}
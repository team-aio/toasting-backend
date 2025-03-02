package io.toasting.domain.message.applicatoin.`in`

import io.toasting.domain.member.entity.Member
import io.toasting.domain.message.entity.Message

class SendMessageInput(
    val receiverId: Long,
    val postId: Long?,
    val content: String,
) {
    fun toEntity(member: Member): Message {
        return Message(
            content = content,
            senderId = member.id!!,
            receiverId = receiverId,
            isRead = false,
            postId = postId,
        )
    }
}
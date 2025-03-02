package io.toasting.creator

import io.toasting.domain.message.entity.Message

class MessageCreator {
    companion object {
        fun defaultMessage(
            content: String,
            senderId: Long,
            receiverId: Long,
            isRead: Boolean
        ) = Message(
            content = content,
            senderId = senderId,
            receiverId = receiverId,
            isRead = isRead
        )
    }
}
package io.toasting.creator

import io.toasting.domain.message.entity.ChatRoom
import io.toasting.domain.message.entity.Message

class MessageCreator {
    companion object {
        fun unreadMessage(
            content: String,
            senderId: Long,
            chatRoom: ChatRoom
        ) = Message(
            content = content,
            chatRoom = chatRoom,
            senderId = senderId,
            isRead = false
        )

        fun readMessage(
            content: String,
            senderId: Long,
            chatRoom: ChatRoom
        ) = Message(
            content = content,
            chatRoom = chatRoom,
            senderId = senderId,
            isRead = true
        )
    }
}
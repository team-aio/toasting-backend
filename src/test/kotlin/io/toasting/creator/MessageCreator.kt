package io.toasting.creator

import io.toasting.domain.message.entity.ChatRoom
import io.toasting.domain.message.entity.Message

class MessageCreator {
    companion object {
        fun unreadMessage(
            content: String,
            chatRoom: ChatRoom
        ) = Message(
            content = content,
            chatRoom = chatRoom,
            isRead = false
        )

        fun readMessage(
            content: String,
            chatRoom: ChatRoom
        ) = Message(
            content = content,
            chatRoom = chatRoom,
            isRead = true
        )
    }
}
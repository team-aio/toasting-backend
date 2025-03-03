package io.toasting.domain.message.applicatoin.`in`

import io.toasting.domain.message.entity.ChatRoom
import io.toasting.domain.message.entity.Message

class SendMessageInput(
    val content: String,
) {
    fun toEntity(senderId: Long, chatRoom: ChatRoom): Message {
        return Message(
            content = content,
            chatRoom = chatRoom,
            senderId = senderId,
            isRead = false,
        )
    }
}
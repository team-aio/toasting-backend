package io.toasting.domain.message.applicatoin.`in`

import io.toasting.domain.message.entity.ChatRoom
import io.toasting.domain.message.entity.Message
import java.time.LocalDateTime

class SendMessageInput(
    val content: String,
) {
    fun toMessageEntity(senderId: Long, chatRoom: ChatRoom): Message {
        return Message(
            content = content,
            chatRoom = chatRoom,
            senderId = senderId,
            isRead = false,
        )
    }

    fun toChatRoomEntity(senderId: Long, chatRoom: ChatRoom): ChatRoom {
        return ChatRoom(
            id = chatRoom.id,
            postId = chatRoom.postId,
            recentSenderId = senderId,
            recentMessageContent = content,
            recentSendAt = LocalDateTime.now()
        )
    }
}
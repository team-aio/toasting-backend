package io.toasting.creator

import io.toasting.domain.message.entity.ChatRoom
import java.time.LocalDateTime

class ChatRoomCreator {
    companion object {
        fun activatedChatRoom(recentSenderId: Long, recentMessage: String, recentSendAt: LocalDateTime): ChatRoom {
            return ChatRoom(
                recentSenderId = recentSenderId,
                recentMessageContent = recentMessage,
                recentSendAt = recentSendAt,
            )
        }

        fun notActivatedChatRoom(): ChatRoom {
            return ChatRoom()
        }
    }
}
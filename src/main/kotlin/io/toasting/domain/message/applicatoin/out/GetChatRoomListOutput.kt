package io.toasting.domain.message.applicatoin.out

import io.toasting.domain.message.entity.ChatRoom
import java.time.LocalDateTime

class GetChatRoomListOutput(
    val memberId: Long,
    val profilePicture: String? = null,
    val recentMessageContent: String,
    val recentSendAt: LocalDateTime,
    val unreadMessageCount: Int,
) {
}
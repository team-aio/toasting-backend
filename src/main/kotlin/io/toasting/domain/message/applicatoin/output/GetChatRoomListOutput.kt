package io.toasting.domain.message.applicatoin.output

import java.time.LocalDateTime

class GetChatRoomListOutput(
    val chatRoomId: Long,
    val memberId: Long,
    val profilePicture: String? = null,
    val recentMessageContent: String,
    val recentSendAt: LocalDateTime,
    val unreadMessageCount: Int,
)

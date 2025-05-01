package io.toasting.domain.message.applicatoin.output

import java.time.LocalDateTime

class GetChatRoomListOutput(
    val chatRoomId: Long,
    val memberId: String,
    val nickname: String,
    val profilePicture: String? = null,
    val recentMessageContent: String,
    val recentSendAt: LocalDateTime,
    val unreadMessageCount: Int,
)

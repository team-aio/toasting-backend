import io.toasting.domain.message.applicatoin.output.GetChatRoomListOutput
import java.time.LocalDateTime
import kotlin.random.Random

class GetChatRoomListResponse(
    val chatRoomId: Long,
    val memberId: Long,
    val nickname: String,
    val profilePicture: String? = null,
    val recentMessageContent: String,
    val recentSendAt: LocalDateTime,
    val unreadMessageCount: Int,
) {
    companion object {
        fun mock() =
            GetChatRoomListResponse(
                chatRoomId = Random.nextLong(1, 10),
                Random.nextLong(1, 10),
                nickname = "nickname",
                "st",
                "last message content",
                LocalDateTime.now(),
                Random.nextInt(1, 10),
            )

        fun from(output: GetChatRoomListOutput) =
            GetChatRoomListResponse(
                chatRoomId = output.chatRoomId,
                memberId = output.memberId,
                nickname = output.nickname,
                profilePicture = output.profilePicture,
                recentMessageContent = output.recentMessageContent,
                recentSendAt = output.recentSendAt,
                unreadMessageCount = output.unreadMessageCount,
            )
    }
}

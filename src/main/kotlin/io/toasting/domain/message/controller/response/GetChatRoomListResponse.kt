import io.toasting.domain.message.applicatoin.output.GetChatRoomListOutput
import java.time.LocalDateTime
import kotlin.random.Random

class GetChatRoomListResponse(
    val memberId: Long,
    val profilePicture: String? = null,
    val recentMessageContent: String,
    val recentSendAt: LocalDateTime,
    val unreadMessageCount: Int,
) {
    companion object {
        fun mock() =
            GetChatRoomListResponse(
                Random.nextLong(1, 10),
                "st",
                "last message content",
                LocalDateTime.now(),
                Random.nextInt(1, 10),
            )

        fun from(output: GetChatRoomListOutput) = GetChatRoomListResponse(
            memberId = output.memberId,
            profilePicture = output.profilePicture,
            recentMessageContent = output.recentMessageContent,
            recentSendAt = output.recentSendAt,
            unreadMessageCount = output.unreadMessageCount,
        )
    }
}

import java.time.LocalDateTime
import kotlin.random.Random

class GetMessagesResponse(
    val memberId: Long,
    val profilePicture: String?,
    val lastMessageContent: String,
    val lastMessageCreatedAt: LocalDateTime,
    val notReadMessageCount: Int,
) {
    companion object {
        fun mock() =
            GetMessagesResponse(
                Random.nextLong(1, 10),
                "st",
                "last message content",
                LocalDateTime.now(),
                Random.nextInt(1, 10),
            )
    }
}

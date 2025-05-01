import io.swagger.v3.oas.annotations.media.Schema
import io.toasting.domain.message.applicatoin.output.GetChatRoomListOutput
import java.time.LocalDateTime
import kotlin.random.Random

class GetChatRoomListResponse(
    @Schema(description = "채팅방 id", example = "1")
    val chatRoomId: Long,
    @Schema(description = "상대방 id", example = "1")
    val memberId: Long,
    @Schema(description = "상대방 닉네임", example = "howdong")
    val nickname: String,
    @Schema(description = "상대방 프로필 사진", example = "")
    val profilePicture: String? = null,
    @Schema(description = "최근에 주고 받은 메세지 내용", example = "안녕하세요.")
    val recentMessageContent: String,
    @Schema(description = "마지막 메세지 보내거나 받은 시간", example = "2025-04-27T07:50:26.255Z")
    val recentSendAt: LocalDateTime,
    @Schema(description = "읽지 않은 메세지 개수", example = "10")
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

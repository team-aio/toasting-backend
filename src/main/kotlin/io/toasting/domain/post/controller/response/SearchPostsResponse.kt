import java.time.LocalDateTime

class SearchPostsResponse(
    val id: Long,
    val title: String,
    val content: String,
    val likeCount: Int,
    val postedAt: LocalDateTime?,
    val sourceType: String,
    val memberInfo: MemberInfo,
) {
    companion object {
        fun mock() =
            SearchPostsResponse(
                id = 1L,
                title = "mock title",
                content =
                    "mock short content mock short content mock short content mock short content mock short content " +
                        "mock short content mock short content mock short content mock short content mock short content " +
                        "mock short content mock short content mock short content mock short content mock short content " +
                        "mock short content mock short content mock short content mock short content mock short content mock short content",
                likeCount = 100,
                postedAt = LocalDateTime.now(),
                sourceType = "velog",
                memberInfo = MemberInfo.mock(),
            )
    }

    data class MemberInfo(
        val id: Long,
        val nickname: String,
        val profilePicture: String?,
    ) {
        companion object {
            fun mock() =
                MemberInfo(
                    id = 1L,
                    nickname = "mock nickname",
                    profilePicture = @Suppress("ktlint:standard:max-line-length")
                    "data:image/png;base64,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",
                )
        }
    }
}

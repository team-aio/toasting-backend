package io.toasting.domain.crumb.entity

import io.toasting.domain.model.BaseEntity
import io.toasting.global.converter.StringListConverter
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDate

private const val DEFAULT_COUNT = 0

@Entity
@Table(
    name = "crumb",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_crumbs_member_id_activity_date",
            columnNames = ["member_id", "activity_date"]
        )
    ]
)
class Crumb private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    activityCount: Int,
    val memberId: Long,
    val activityDate: LocalDate
) : BaseEntity() {
    var activityCount = activityCount
        protected set

    @Convert(converter = StringListConverter::class)
    @Column(name = "comment_ids")
    private val _commentIds = mutableListOf<Long>()

    @Convert(converter = StringListConverter::class)
    @Column(name = "post_ids")
    private val _postIds = mutableListOf<Long>()

    @Convert(converter = StringListConverter::class)
    @Column(name = "bookmark_ids")
    private val _bookmarkIds = mutableListOf<Long>()

    @Convert(converter = StringListConverter::class)
    @Column(name = "likes_ids")
    private val _likeIds = mutableListOf<Long>()

    val commentIds: List<Long>
        get() = _commentIds.toList()

    val postIds: List<Long>
        get() = _postIds.toList()

    val bookmarkIds: List<Long>
        get() = _bookmarkIds.toList()

    val likeIds: List<Long>
        get() = _likeIds.toList()

    companion object {
        fun create(memberId: Long, activityDate: LocalDate) =
            Crumb(
                activityCount = DEFAULT_COUNT,
                memberId = memberId,
                activityDate = activityDate,
            )
    }

    fun addCommentId(commentId: Long) {
        activityCount++

        _commentIds.add(commentId)
    }

    fun removeCommentId(commentId: Long) {
        check(activityCount > 0) { "activityCount는 0보다 작을 수 없습니다." }

        activityCount--

        _commentIds.remove(commentId)
    }

    fun addPostId(postId: Long) {
        activityCount++

        _postIds.add(postId)
    }

    fun removePostId(postId: Long) {
        check(activityCount > 0) { "activityCount는 0보다 작을 수 없습니다." }

        activityCount--

        _postIds.remove(postId)
    }

    fun addBookmarkId(bookmarkId: Long) {
        activityCount++

        _bookmarkIds.add(bookmarkId)
    }

    fun removeBookmarkId(bookmarkId: Long) {
        check(activityCount > 0) { "activityCount는 0보다 작을 수 없습니다." }

        activityCount--

        _bookmarkIds.remove(bookmarkId)
    }

    fun addLikeId(likeId: Long) {
        activityCount++

        _likeIds.add(likeId)
    }

    fun removeLikeId(likeId: Long) {
        check(activityCount > 0) { "activityCount는 0보다 작을 수 없습니다." }

        activityCount--

        _likeIds.remove(likeId)
    }
}
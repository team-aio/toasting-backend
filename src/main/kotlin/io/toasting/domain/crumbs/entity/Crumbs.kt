package io.toasting.domain.crumbs.entity

import io.toasting.domain.model.BaseEntity
import io.toasting.global.converter.StringListConverter
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate

private const val DEFAULT_COUNT = 0

@Entity
@Table(name = "crumbs")
class Crumbs private constructor(
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

    val commentIds: List<Long> = _commentIds
    val postIds: List<Long> = _postIds
    val bookmarkIds: List<Long> = _bookmarkIds
    val likeIds: List<Long> = _likeIds

    companion object {
        fun create(memberId: Long, activityDate: LocalDate) =
            Crumbs(
                activityCount = DEFAULT_COUNT,
                memberId = memberId,
                activityDate = activityDate,
            )
    }

}
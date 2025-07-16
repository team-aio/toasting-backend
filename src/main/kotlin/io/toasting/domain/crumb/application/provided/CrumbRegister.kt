package io.toasting.domain.crumb.application.provided

import java.time.LocalDate

/**
 * 빵가루 활성화/등록과 관련된 기능을 제공
 */
interface CrumbRegister {
    fun addCommentCrumb(memberId: Long, commentId: Long, activityDate: LocalDate)
    fun removeCommentCrumb(memberId: Long, commentId: Long, activityDate: LocalDate)

    fun addPostCrumb(memberId: Long, postId: Long, activityDate: LocalDate)
    fun removePostCrumb(memberId: Long, postId: Long, activityDate: LocalDate)

    fun addBookmarkCrumb(memberId: Long, bookmarkId: Long, activityDate: LocalDate)
    fun removeBookmarkCrumb(memberId: Long, bookmarkId: Long, activityDate: LocalDate)

    fun addLikeCrumb(memberId: Long, likeId: Long, activityDate: LocalDate)
    fun removeLikeCrumb(memberId: Long, likeId: Long, activityDate: LocalDate)

}
package io.toasting.domain.crumb.application.output

import io.toasting.domain.crumb.entity.Crumb
import java.time.LocalDate

data class FindCrumbOutput(
    val activityCount: Int,
    val activityDate: LocalDate,
    val commentIds: List<Long>,
    val postIds: List<Long>,
    val bookmarkIds: List<Long>,
    val likeIds: List<Long>,
) {
    companion object {
        fun from(crumb: Crumb): FindCrumbOutput = FindCrumbOutput(
            activityCount = crumb.activityCount,
            activityDate = crumb.activityDate,
            commentIds = crumb.commentIds,
            postIds = crumb.postIds,
            bookmarkIds = crumb.bookmarkIds,
            likeIds = crumb.likeIds,
        )
    }
}

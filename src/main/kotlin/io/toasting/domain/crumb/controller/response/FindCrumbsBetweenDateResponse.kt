package io.toasting.domain.crumb.controller.response

import io.toasting.domain.crumb.application.output.FindCrumbOutput

data class FindCrumbsBetweenDateResponse(
    val activityCount: Int,
    val activityDate: String,
    val commentIds: List<Long>,
    val postIds: List<Long>,
    val bookmarkIds: List<Long>,
    val likeIds: List<Long>,
) {
    companion object {
        fun from(output: FindCrumbOutput): FindCrumbsBetweenDateResponse {
            return FindCrumbsBetweenDateResponse(
                activityCount = output.activityCount,
                activityDate = output.activityDate.toString(),
                commentIds = output.commentIds,
                postIds = output.postIds,
                bookmarkIds = output.bookmarkIds,
                likeIds = output.likeIds,
            )
        }
    }
}

package io.toasting.domain.crumb.controller.response

import io.toasting.domain.crumb.application.output.FindCrumbOutput

data class FindCrumbsBetweenDateResponse(
    val activityCount: Int,
    val activityDate: String,
) {
    companion object {
        fun from(output: FindCrumbOutput): FindCrumbsBetweenDateResponse {
            return FindCrumbsBetweenDateResponse(
                activityCount = output.activityCount,
                activityDate = output.activityDate.toString(),
            )
        }
    }
}

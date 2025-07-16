package io.toasting.domain.crumb.controller.request

data class FindCrumbsBetweenDateRequest(
    val startDate: String,
    val endDate: String
)

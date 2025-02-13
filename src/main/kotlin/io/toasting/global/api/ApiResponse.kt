package io.toasting.global.api

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import io.toasting.api.code.status.SuccessStatus

@JsonPropertyOrder("isSuccess", "message")
class ApiResponse<T> private constructor(
    @JsonProperty("isSuccess")
    val isSuccess: Boolean,
    val status: String,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val data: T?,
) {
    companion object {
        fun <T> onSuccess(
            status: String,
            data: T?,
        ) = ApiResponse(
            isSuccess = true,
            status = status,
            data = data,
        )

        fun onSuccess(): ApiResponse<Unit> = onSuccess(SuccessStatus.OK.status, null)
    }
}

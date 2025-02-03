package io.toasting.global.api

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("isSuccess", "message")
class ApiResponse<T> private constructor(
    @JsonProperty("isSuccess")
    val isSuccess: Boolean,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val data: T?,
) {
    companion object {
        fun <T> onSuccess(data: T?) =
            ApiResponse(
                isSuccess = true,
                data = data,
            )

        fun onSuccess(): ApiResponse<Unit> = onSuccess(null)
    }
}

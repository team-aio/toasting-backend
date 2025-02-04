package io.toasting.global.api

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("isSuccess", "status", "data", "message")
class ErrorApiResponse<T> private constructor(
    @JsonProperty("isSuccess")
    val isSuccess: Boolean,
    val status: String,
    val message: String,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val data: T?,
) {
    companion object {
        fun <T> onFailure(
            status: String,
            message: String,
            data: T?,
        ) = ErrorApiResponse(false, status, message, data)
    }
}

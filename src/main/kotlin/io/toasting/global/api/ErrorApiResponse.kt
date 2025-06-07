package io.toasting.global.api

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("isSuccess", "status", "message")
class ErrorApiResponse private constructor(
    @JsonProperty("isSuccess")
    val isSuccess: Boolean,
    val status: String,
    val message: String,
) {
    companion object {
        fun onFailure(
            status: String,
            message: String,
        ) = ErrorApiResponse(false, status, message)
    }
}

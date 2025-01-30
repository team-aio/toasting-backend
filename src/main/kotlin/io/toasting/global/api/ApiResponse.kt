package io.toasting.api

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import io.toasting.api.code.BaseCode
import io.toasting.api.code.status.SuccessStatus

@JsonPropertyOrder("isSuccess", "status", "data", "message")
class ApiResponse<T> private constructor(
    @JsonProperty("isSuccess")
    val isSuccess: Boolean,
    val status: String,
    val message: String,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val data: T?,
) {
    companion object {
        fun <T> onSuccess(data: T?) =
            ApiResponse(
                isSuccess = true,
                status = SuccessStatus.OK.code,
                message = SuccessStatus.OK.message,
                data = data,
            )

        fun <T> onSuccess(
            code: BaseCode,
            data: T,
        ) = ApiResponse(
            isSuccess = true,
            status = code.getReason().code,
            message = code.getReason().message,
            data = data,
        )

        fun <T> onFailure(
            status: String,
            message: String,
            data: T?,
        ) = ApiResponse(false, status, message, data)
    }
}

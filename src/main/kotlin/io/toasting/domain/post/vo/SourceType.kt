package io.toasting.domain.post.vo

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class SourceType(
    @get:JsonValue val value: String
) {
    VELOG("velog"),
    TISTORY("tistory");

    companion object {
        @JsonCreator
        fun from(value: String): SourceType {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown SourceType enum value: $value")
        }
    }
}
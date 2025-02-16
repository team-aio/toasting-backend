package io.toasting.domain.member.vo

enum class SocialType {
    GOOGLE,
    ;

    companion object {
        fun from(snsType: String): SocialType =
            when (snsType.lowercase()) {
                "google" -> GOOGLE
                else -> throw IllegalArgumentException("Invalid SNS Type")
            }
    }
}

package io.toasting.domain.member.repository

import io.toasting.domain.member.entity.SocialLogin
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SocialLoginRepository : JpaRepository<SocialLogin, Long> {
    fun findBySocialTypeAndExternalId(
        socialType: String,
        externalId: String,
    ): SocialLogin?
}

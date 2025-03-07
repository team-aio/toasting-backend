package io.toasting.domain.member.repository

import io.toasting.domain.member.entity.SocialLogin
import io.toasting.domain.member.vo.SocialType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SocialLoginRepository : JpaRepository<SocialLogin, Long> {
    fun findBySocialTypeAndExternalId(
        socialType: SocialType,
        externalId: String,
    ): SocialLogin?

    fun existsBySocialTypeAndExternalId(
        socialType: SocialType,
        externalId: String,
    ): Boolean
}

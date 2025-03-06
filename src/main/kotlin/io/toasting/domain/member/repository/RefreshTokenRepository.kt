package io.toasting.domain.member.repository

import io.toasting.domain.member.entity.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface RefreshTokenRepository : JpaRepository<RefreshToken, Long> {
    fun existsByToken(token: String): Boolean

    fun deleteByToken(token: String): Boolean

    fun deleteByExpiredAtBefore(dateTime: LocalDateTime): List<RefreshToken>
}

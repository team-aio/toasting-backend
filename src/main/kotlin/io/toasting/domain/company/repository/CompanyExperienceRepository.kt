package io.toasting.domain.company.repository

import io.toasting.domain.company.entity.CompanyExperience
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CompanyExperienceRepository: JpaRepository<CompanyExperience, Long> {
    fun findByMemberId(memberId: Long): List<CompanyExperience>
}
package io.toasting.domain.company.repository

import io.toasting.domain.company.entity.CustomCompanyExperience
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomCompanyExperienceRepository: JpaRepository<CustomCompanyExperience, Long> {
    fun findByMemberId(memberId: Long): List<CustomCompanyExperience>
}
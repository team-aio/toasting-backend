package io.toasting.domain.company.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomCompanyExperienceRepository : JpaRepository<CustomCompanyExperienceRepository, Long>
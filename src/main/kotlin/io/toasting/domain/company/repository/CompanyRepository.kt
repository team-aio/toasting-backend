package io.toasting.domain.company.repository

import io.toasting.domain.company.entity.Company
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CompanyRepository: JpaRepository<Company, Long> {
}
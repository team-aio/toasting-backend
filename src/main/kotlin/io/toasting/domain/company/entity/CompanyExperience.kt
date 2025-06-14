package io.toasting.domain.company.entity

import io.toasting.domain.model.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "company_experience")
class CompanyExperience(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "start_date")
    val startDate: LocalDate,
    @Column(name = "end_date")
    val endDate: LocalDate? = null,
    @Column(name = "position")
    val position: String,
    @Column(name = "activities", columnDefinition = "text")
    val activities: String,
    @Column(name = "company_id")
    val companyId: Long,
    @Column(name = "member_id")
    val memberId: Long,
    @Column(name = "is_view")
    val isView: Boolean,
) : BaseEntity()
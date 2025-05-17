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
    @Column("start_date")
    val startDate: LocalDate,
    @Column("end_date")
    val endDate: LocalDate,
    @Column("position")
    val position: String,
    @Column("activities", columnDefinition = "text")
    val activities: String,
    @Column("profile_image")
    val profileImage: String,
    @Column("company_id")
    val companyId: Long,
    @Column("member_id")
    val memberId: Long,
    @Column("is_view")
    val isView: Boolean,
) : BaseEntity()
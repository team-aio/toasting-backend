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
@Table(name = "custom_company_experience")
class CustomCompanyExperience(
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
    @Column(name = "profile_image")
    val profileImage: String,
    @Column(name = "company_name")
    val companyName: String,
    @Column(name = "member_id")
    val memberId: Long,
    @Column(name = "is_view")
    val isView: Boolean,
) : BaseEntity() {
    companion object {
        fun defaultEntity(
            startDate: LocalDate,
            endDate: LocalDate?,
            position: String,
            activities: String,
            profileImage: String,
            companyName: String,
            memberId: Long,
        ) = CustomCompanyExperience(
            startDate = startDate,
            endDate = endDate,
            position = position,
            activities = activities,
            profileImage = profileImage,
            companyName = companyName,
            memberId = memberId,
            isView = true
        )
    }
}
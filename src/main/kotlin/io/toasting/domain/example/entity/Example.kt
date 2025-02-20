package io.toasting.domain.example.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.validation.constraints.NotBlank

@Entity
class Example(
    @field:NotBlank(message = "예시 : 공백으로 이름을 지을수는 없습니다.")
    val name: String,
    val number: Long,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null,
)

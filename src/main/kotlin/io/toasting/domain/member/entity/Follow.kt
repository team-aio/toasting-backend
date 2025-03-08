package io.toasting.domain.member.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class Follow(
    @ManyToOne
    @JoinColumn(name = "from_member_id")
    val fromMember: Member,
    @ManyToOne
    @JoinColumn(name = "to_member_id")
    val toMember: Member,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
)

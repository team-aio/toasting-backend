package io.toasting.domain.member.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class Follow(
    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "from_member_id")
    val fromMember: Member,
    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "to_member_id")
    val toMember: Member,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
)

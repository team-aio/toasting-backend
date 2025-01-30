import io.toasting.domain.model.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val profilePicture: String? = null,
    val velogId: Long? = null,
    val tistoryId: Long? = null,
    val nickname: String,
    val email: String,
) : BaseEntity() {
    companion object {
        fun defaultMember(
            nickname: String,
            email: String,
        ) = Member(
            nickname = nickname,
            email = email,
        )
    }

    fun updateWith(
        nickname: String,
        email: String,
    ): Member =
        Member(
            id = id,
            profilePicture = profilePicture,
            velogId = velogId,
            tistoryId = tistoryId,
            nickname = nickname,
            email = email,
        )
}

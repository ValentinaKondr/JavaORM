package v.orm.domain.user.entity

import jakarta.persistence.*
import v.orm.domain.base.BaseEntity

@Entity
@Table(name = "user_profile", schema = "orm")
class UserProfile(

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) var user: User,

    var bio: String? = null,

    @Column(name = "is_active") var isActive: Boolean? = true

) : BaseEntity()
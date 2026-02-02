package v.orm.domain.user.entity

import jakarta.persistence.*
import v.orm.domain.base.BaseEntity
import v.orm.domain.course.entity.Course
import v.orm.domain.enrollment.entity.Enrollment

@Entity
@Table(name = "user", schema = "orm")
class User(

    @Column(nullable = false) var name: String,

    @Column(nullable = false, unique = true) var email: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) var role: UserRole,

    @Column(name = "is_active") var isActive: Boolean? = true

) : BaseEntity() {

    @OneToOne(
        mappedBy = "user",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL]
    )
    var profile: UserProfile? = null

    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    var coursesTaught: MutableList<Course> = mutableListOf()

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    var enrollments: MutableList<Enrollment> = mutableListOf()
}
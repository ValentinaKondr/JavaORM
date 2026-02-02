package v.orm.domain.enrollment.entity

import jakarta.persistence.*
import v.orm.domain.base.BaseEntity
import v.orm.domain.course.entity.Course
import v.orm.domain.user.entity.User
import java.time.Instant

@Entity
@Table(
    name = "enrollment",
    schema = "orm",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["user_id", "course_id"])
    ]
)
class Enrollment(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var student: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    var course: Course,

    @Enumerated(EnumType.STRING)
    var status: EnrollmentStatus = EnrollmentStatus.ACTIVE,

    @Column(name = "enroll_date")
    var enrollDate: Instant? = null

) : BaseEntity()
package v.orm.domain.course.entity

import jakarta.persistence.*
import v.orm.domain.base.BaseEntity
import v.orm.domain.user.entity.User

@Entity
@Table(
    name = "course_review",
    schema = "orm",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["student_id", "course_id"])
    ]
)
class CourseReview(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false) var course: Course,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false) var student: User,

    @Column(nullable = false) var rating: Int,

    var comment: String? = null

) : BaseEntity()
package v.orm.domain.submission.entity

import jakarta.persistence.*
import v.orm.domain.assignment.entity.Assignment
import v.orm.domain.base.BaseEntity
import v.orm.domain.user.entity.User
import java.time.Instant

@Entity
@Table(
    name = "submission",
    schema = "orm",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["student_id", "assignment_id"])
    ]
)
class Submission(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    var assignment: Assignment,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    var student: User,

    @Column(columnDefinition = "text")
    var content: String? = null,

    var score: Int? = null,

    @Column(columnDefinition = "text")
    var feedback: String? = null,

    @Column(name = "submitted_at")
    var submittedAt: Instant? = null

) : BaseEntity()
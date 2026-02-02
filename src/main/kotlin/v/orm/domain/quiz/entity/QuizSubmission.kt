package v.orm.domain.quiz.entity

import jakarta.persistence.*
import v.orm.domain.base.BaseEntity
import v.orm.domain.user.entity.User
import java.time.Instant


@Entity
@Table(
    name = "quiz_submission",
    schema = "orm",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["student_id", "quiz_id"])
    ]
)
class QuizSubmission(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false) var quiz: Quiz,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false) var student: User,

    var score: Int? = null,
    var takenAt: Instant? = null,

    @Column(name = "is_active") var isActive: Boolean? = true

) : BaseEntity()
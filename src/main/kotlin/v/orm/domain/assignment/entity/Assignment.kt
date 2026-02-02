package v.orm.domain.assignment.entity

import jakarta.persistence.*
import v.orm.domain.base.BaseEntity
import v.orm.domain.lesson.entity.Lesson
import v.orm.domain.submission.entity.Submission
import java.time.Instant

@Entity
@Table(name = "assignment", schema = "orm")
class Assignment(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    val lesson: Lesson? = null,

    @Column(nullable = false)
    val title: String? = null,

    val description: String? = null,

    var dueDate: Instant? = null,

    @Column(name = "max_score")
    private val maxScore: Int? = null,
) : BaseEntity() {
    @OneToMany(mappedBy = "assignment", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    val submissions: MutableList<Submission?> = ArrayList<Submission?>()
}
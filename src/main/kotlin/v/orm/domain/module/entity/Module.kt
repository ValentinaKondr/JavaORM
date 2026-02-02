package v.orm.domain.module.entity

import jakarta.persistence.*
import v.orm.domain.base.BaseEntity
import v.orm.domain.course.entity.Course
import v.orm.domain.lesson.entity.Lesson
import v.orm.domain.quiz.entity.Quiz

@Entity
@Table(name = "module", schema = "orm")
class Module(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false) var course: Course,

    @Column(nullable = false) var title: String,

    var description: String? = null,
    var orderIndex: Int? = null,

    @Column(name = "is_active") var isActive: Boolean? = true

) : BaseEntity() {

    @OneToMany(mappedBy = "module", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var lessons: MutableList<Lesson> = mutableListOf()

    @OneToOne(mappedBy = "module")
    var quiz: Quiz? = null
}
package v.orm.domain.lesson.entity

import jakarta.persistence.*
import v.orm.domain.assignment.entity.Assignment
import v.orm.domain.base.BaseEntity
import v.orm.domain.module.entity.Module

@Entity
@Table(name = "lesson", schema = "orm")
class Lesson(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false) var module: Module,

    @Column(nullable = false) var title: String,

    var content: String? = null,
    var videoUrl: String? = null

) : BaseEntity() {

    @OneToMany(mappedBy = "lesson", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var assignments: MutableList<Assignment> = mutableListOf()
}
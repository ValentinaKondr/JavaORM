package v.orm.domain.quiz.entity

import jakarta.persistence.*
import v.orm.domain.base.BaseEntity
import v.orm.domain.module.entity.Module

@Entity
@Table(name = "quiz", schema = "orm")
class Quiz(

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id") var module: Module? = null,

    @Column(nullable = false) var title: String,

    var timeLimit: Int? = null,

    @Column(name = "is_active") var isActive: Boolean? = true,

) : BaseEntity() {

    @OneToMany(mappedBy = "quiz", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var questions: MutableList<Question> = mutableListOf()
}
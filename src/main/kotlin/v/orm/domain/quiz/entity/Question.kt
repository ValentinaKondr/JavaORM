package v.orm.domain.quiz.entity

import jakarta.persistence.*
import v.orm.domain.base.BaseEntity

@Entity
@Table(name = "question", schema = "orm")
class Question(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false) var quiz: Quiz,

    @Column(nullable = false) var text: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) var type: QuestionType

) : BaseEntity() {

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var options: MutableList<AnswerOption> = mutableListOf()
}
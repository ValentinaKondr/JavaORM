package v.orm.domain.quiz.entity

import jakarta.persistence.*
import v.orm.domain.base.BaseEntity

@Entity
@Table(name = "answer_option", schema = "orm")
class AnswerOption(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false) var question: Question,

    @Column(nullable = false) var text: String,

    @Column(name = "is_correct") var isCorrect: Boolean = false

) : BaseEntity()
package v.orm.domain.quiz.repository

import org.springframework.data.jpa.repository.JpaRepository
import v.orm.domain.quiz.entity.Quiz
import java.util.*

interface QuizRepository : JpaRepository<Quiz, UUID>
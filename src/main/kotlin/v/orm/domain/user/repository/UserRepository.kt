package v.orm.domain.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import v.orm.domain.user.entity.User
import java.util.*

interface UserRepository : JpaRepository<User, UUID> {
    fun findByEmail(email: String): User?
}
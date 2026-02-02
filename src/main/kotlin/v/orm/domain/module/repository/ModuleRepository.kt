package v.orm.domain.module.repository

import org.springframework.data.jpa.repository.JpaRepository
import v.orm.domain.module.entity.Module
import java.util.*

interface ModuleRepository : JpaRepository<Module, UUID> {
    fun findByCourse_Id(courseId: UUID): List<Module>
}
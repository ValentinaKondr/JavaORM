package v.orm.domain.base

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass

import java.time.Instant
import java.util.*


@MappedSuperclass
open class BaseEntity(

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid") var id: UUID? = null,

    @Column(name = "created_at", updatable = false) var createdAt: Instant = Instant.now()
)
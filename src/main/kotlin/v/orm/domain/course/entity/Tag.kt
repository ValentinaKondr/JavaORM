package v.orm.domain.course.entity

import jakarta.persistence.*
import v.orm.domain.base.BaseEntity

@Entity
@Table(name = "tag", schema = "orm")
class Tag(

    @Column(nullable = false, unique = true) var name: String

) : BaseEntity() {

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    var courses: MutableSet<Course> = mutableSetOf()
}
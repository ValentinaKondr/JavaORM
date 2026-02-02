package v.orm.domain.category.entity

import jakarta.persistence.*
import v.orm.domain.base.BaseEntity
import v.orm.domain.course.entity.Course

@Entity
@Table(name = "category", schema = "orm")
class Category(

    @Column(nullable = false, unique = true) var name: String

) : BaseEntity() {

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    var courses: MutableList<Course> = mutableListOf()
}
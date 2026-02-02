package v.orm.domain.course.entity

import jakarta.persistence.*
import v.orm.domain.base.BaseEntity
import v.orm.domain.category.entity.Category
import v.orm.domain.module.entity.Module
import v.orm.domain.user.entity.User
import java.time.Instant

@Entity
@Table(name = "course", schema = "orm")
class Course(
    var title: String,
    var description: String? = null,

    var duration: Int? = null,

    @Column(name = "start_date")
    var startDate: Instant? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    var category: Category? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    var teacher: User? = null,

    @Column(name = "is_active") var isActive: Boolean? = true
) : BaseEntity() {

    @OneToMany(
        mappedBy = "course",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    var modules: MutableList<Module> = mutableListOf()

    @ManyToMany
    @JoinTable(
        name = "course_tag",
        schema = "orm",
        joinColumns = [JoinColumn(name = "course_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    var tags: MutableSet<Tag> = mutableSetOf()
}
--liquibase formatted sql
--changeset v:15
--comment: таблица связи курсов и тегов

create table if not exists orm.course_tag
(
    course_id uuid references orm.course (id),
    tag_id    uuid references orm.tag (id),

    constraint pk_course_tag primary key (course_id, tag_id)
);
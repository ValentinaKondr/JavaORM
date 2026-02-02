--liquibase formatted sql
--changeset v:13
--comment: таблица отзывов на курсы

create table if not exists orm.course_review
(
    id         uuid      default uuid_generate_v4() primary key,
    course_id  uuid    not null references orm.course (id),
    student_id uuid    not null references orm.user (id),
    rating     integer not null,
    comment    text,
    created_at timestamp default CURRENT_TIMESTAMP,

    constraint uk_course_review_student_course unique (student_id, course_id)
);
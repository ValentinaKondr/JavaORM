--liquibase formatted sql
--changeset v:16
--comment: таблица записей на курсы

create table if not exists orm.enrollment
(
    id          uuid        default uuid_generate_v4() primary key,
    user_id     uuid not null references orm.user (id),
    course_id   uuid not null references orm.course (id),
    enroll_date timestamp   default CURRENT_TIMESTAMP,
    status      varchar(50) default 'ACTIVE',
    created_at  timestamp   default CURRENT_TIMESTAMP,

    constraint uk_enrollment_user_course unique (user_id, course_id)
);
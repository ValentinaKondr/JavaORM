--liquibase formatted sql
--changeset v:08
--comment: таблица ответов

create table if not exists orm.submission
(
    id            uuid      default uuid_generate_v4() primary key,
    assignment_id uuid not null references orm.assignment (id),
    student_id    uuid not null references orm.user (id),
    content       text,
    submitted_at  timestamp default CURRENT_TIMESTAMP,
    score         integer,
    feedback      text,
    created_at    timestamp default CURRENT_TIMESTAMP,

    constraint uk_submission_student_assignment unique (student_id, assignment_id)
);
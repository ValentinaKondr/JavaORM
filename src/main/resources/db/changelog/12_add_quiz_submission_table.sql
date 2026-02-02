--liquibase formatted sql
--changeset v:12
--comment: таблица отправленных ответов на тесты

create table if not exists orm.quiz_submission
(
    id         uuid      default uuid_generate_v4() primary key,
    quiz_id    uuid not null references orm.quiz (id),
    student_id uuid not null references orm.user (id),
    score      integer,
    taken_at   timestamp default CURRENT_TIMESTAMP,
    created_at timestamp default CURRENT_TIMESTAMP,
    is_active  boolean   default true,

    constraint uk_quiz_submission_student_quiz unique (student_id, quiz_id)
);
--liquibase formatted sql
--changeset v:07
--comment: таблица заданий

create table if not exists orm.assignment
(
    id          uuid      default uuid_generate_v4() primary key,
    lesson_id   uuid         not null references orm.lesson (id),
    title       varchar(200) not null,
    description text,
    due_date    timestamp,
    max_score   integer   default 100,
    created_at  timestamp default CURRENT_TIMESTAMP
);
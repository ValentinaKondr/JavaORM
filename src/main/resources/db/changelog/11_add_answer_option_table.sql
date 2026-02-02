--liquibase formatted sql
--changeset v:11
--comment: таблица с вариантами ответа

create table if not exists orm.answer_option
(
    id          uuid      default uuid_generate_v4() primary key,
    question_id uuid not null references orm.question (id),
    text        text not null,
    is_correct  boolean   default false,
    created_at  timestamp default CURRENT_TIMESTAMP
);
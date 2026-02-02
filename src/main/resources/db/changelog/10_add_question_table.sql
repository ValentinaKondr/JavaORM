--liquibase formatted sql
--changeset v:10
--comment: таблица вопросов

create table if not exists orm.question
(
    id         uuid      default uuid_generate_v4() primary key,
    quiz_id    uuid        not null references orm.quiz (id),
    text       text        not null,
    type       varchar(50) not null,
    created_at timestamp default CURRENT_TIMESTAMP
);
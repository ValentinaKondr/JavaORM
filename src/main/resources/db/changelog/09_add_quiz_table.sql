--liquibase formatted sql
--changeset v:09
--comment: таблица тестов

create table if not exists orm.quiz
(
    id         uuid      default uuid_generate_v4() primary key,
    module_id  uuid references orm.module (id),
    title      varchar(200) not null,
    time_limit integer,
    created_at timestamp default CURRENT_TIMESTAMP,
    is_active  boolean   default true
);
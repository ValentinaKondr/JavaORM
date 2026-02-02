--liquibase formatted sql
--changeset v:01
--comment: таблица пользователей

create table if not exists orm.user
(
    id         uuid      default uuid_generate_v4() primary key,
    name       varchar(100)        not null,
    email      varchar(100) unique not null,
    role       varchar(100)        not null,
    created_at timestamp default CURRENT_TIMESTAMP,
    is_active  boolean   default true
);


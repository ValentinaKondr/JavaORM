--liquibase formatted sql
--changeset v:14
--comment: таблица тегов

create table if not exists orm.tag
(
    id         uuid      default uuid_generate_v4() primary key,
    name       varchar(100) not null unique,
    created_at timestamp default CURRENT_TIMESTAMP
);
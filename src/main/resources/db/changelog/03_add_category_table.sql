--liquibase formatted sql
--changeset v:03
--comment: таблица категорий

create table if not exists orm.category
(
    id         uuid      default uuid_generate_v4() primary key,
    name       varchar(100) not null unique
);
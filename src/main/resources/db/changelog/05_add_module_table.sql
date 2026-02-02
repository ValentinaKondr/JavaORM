--liquibase formatted sql
--changeset v:05
--comment: таблица модулей

create table if not exists orm.module
(
    id          uuid      default uuid_generate_v4() primary key,
    course_id   uuid         not null references orm.course (id),
    title       varchar(200) not null,
    description text,
    order_index integer,
    created_at  timestamp default CURRENT_TIMESTAMP,
    is_active   boolean   default true
);
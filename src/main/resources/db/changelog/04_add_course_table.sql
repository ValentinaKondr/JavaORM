--liquibase formatted sql
--changeset v:04
--comment: таблица курсов

create table if not exists orm.course
(
    id          uuid      default uuid_generate_v4() primary key,
    title       varchar(200) not null,
    description text,
    category_id uuid references orm.category (id),
    teacher_id  uuid references orm.user (id),
    duration    integer,
    start_date  timestamp,
    created_at  timestamp default CURRENT_TIMESTAMP,
    is_active   boolean   default true
);
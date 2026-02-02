--liquibase formatted sql
--changeset v:06
--comment: таблица уроков

create table if not exists orm.lesson
(
    id         uuid      default uuid_generate_v4() primary key,
    module_id  uuid         not null references orm.module (id),
    title      varchar(200) not null,
    content    text,
    video_url  varchar(500),
    created_at timestamp default CURRENT_TIMESTAMP
);
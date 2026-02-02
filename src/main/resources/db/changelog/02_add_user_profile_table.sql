--liquibase formatted sql
--changeset v:02
--comment: таблица профиля пользователя


create table if not exists orm.user_profile
(
    id         uuid      default uuid_generate_v4() primary key,
    user_id    uuid not null references orm.user (id),
    bio        varchar(255),
    created_at timestamp default CURRENT_TIMESTAMP,
    is_active  boolean   default true
)
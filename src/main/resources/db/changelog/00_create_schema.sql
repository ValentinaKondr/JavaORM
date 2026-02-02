--liquibase formatted sql
--changeset v:00
--comment: инициализация схемы и расширений

create schema if not exists orm;
create extension if not exists "uuid-ossp";
--liquibase formatted sql

--changeset system:1

CREATE TABLE users
(
    id                 NUMBER(10),
    username           VARCHAR2(60),
    name               VARCHAR2(100),
    password_signature VARCHAR2(60),
    profile_picture    BLOB,
    version            NUMBER(5)
);

CREATE TABLE user_roles
(
    user_id NUMBER(10),
    roles   VARCHAR2(20)
);


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

--changeset system:2

insert into users (id, username, name, password_signature, profile_picture, version) values (1, 'user','John Normal', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe',
null, 1);
insert into user_roles (user_id, roles) values (1, 'USER');
insert into users (id, username, name, password_signature, profile_picture, version) values (2, 'admin','Emma Executive','$2a$10$jpLNVNeA7Ar/ZQ2DKbKCm.MuT2ESe.Qop96jipKMq7RaUgCoQedV.',
null, 1);
insert into user_roles (user_id, roles) values (2, 'USER');
insert into user_roles (user_id, roles) values (2, 'ADMIN');

--liquibase formatted sql

--changeset system:1

CREATE TABLE customers
(
    id         NUMBER(10),
    first_name VARCHAR2(50),
    last_name  VARCHAR2(50),
    email      VARCHAR2(100),
    phone      VARCHAR2(50),
    birth_date DATE,
    occupation VARCHAR2(100),
    role       VARCHAR2(20),
    vip        NUMBER(1),
    version    NUMBER(5)
);


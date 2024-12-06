--liquibase formatted sql

--changeset system:1

CREATE TABLE contracts
(
    id     NUMBER(10)   NOT NULL,
    ref_no VARCHAR2(10) NOT NULL
);

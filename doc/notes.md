# Liquibase
https://www.youtube.com/watch?v=guF3zORY1jI

# Oracle
podman run -d --name tech-bank -p 1521:1521 -e ORACLE_PASSWORD=qwert6 docker.io/gvenzl/oracle-free:latest

~~~SQL
CREATE USER techbank IDENTIFIED BY "techbank"
    DEFAULT TABLESPACE USERS
    QUOTA UNLIMITED ON USERS;

ALTER SESSION SET CURRENT_SCHEMA =techbank;

GRANT CREATE MATERIALIZED VIEW,
    CREATE PROCEDURE,
    CREATE SEQUENCE,
    CREATE SESSION,
    CREATE SYNONYM,
    CREATE TABLE,
    CREATE TRIGGER,
    CREATE TYPE,
    CREATE VIEW
    TO techbank;
~~~

# Vaadin

## Katalog ikon
https://icons8.com/line-awesome


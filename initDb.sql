SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

CREATE SCHEMA demo;

SET default_table_access_method = heap;

CREATE TABLE demo.person
(
    p_id      integer NOT NULL PRIMARY KEY,
    firstname text    NOT NULL,
    surname   text    NOT NULL,
    city      text    NOT NULL,
    UNIQUE (firstname, surname)
);

ALTER TABLE demo.person ALTER COLUMN p_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME demo.person_p_id
    START WITH 0
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1
);

CREATE TABLE demo.outbox
(
    event_id            UUID    NOT NULL PRIMARY KEY,
    version             integer NOT NULL,
    root_aggregate_type text    NOT NULL,
    root_aggregate_id   text    NOT NULL,
    aggregate_type      text    NOT NULL,
    aggregate_id        text    NOT NULL,
    event_type          text    NOT NULL,
    payload             text    NOT NULL
);

ALTER TABLE demo.outbox OWNER TO postgres;
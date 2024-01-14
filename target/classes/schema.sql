-- drop table

DROP TABLE IF EXISTS author;
DROP TABLE IF EXISTS book;

-- create table

CREATE TABLE IF NOT EXISTS author (
    id    SERIAL PRIMARY KEY,
    age   SMALLINT NOT NULL,
    genre VARCHAR(255) NOT NULL,
    name  VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS book (
    id        SERIAL PRIMARY KEY,
    isbn      VARCHAR(255) NOT NULL,
    title     VARCHAR(255) NOT NULL,
    author_id INTEGER NOT NULL,
    CONSTRAINT fk_book_to_author FOREIGN KEY(author_id) REFERENCES author(id)
);


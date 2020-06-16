CREATE TABLE IF NOT EXISTS sections (
    id IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR NOT NULL,
    created_at TIMESTAMP DEFAULT LOCALTIMESTAMP
);

CREATE TABLE IF NOT EXISTS geological_classes (
    id IDENTITY NOT NULL PRIMARY KEY,
    section_id BIGINT,
    name VARCHAR NOT NULL,
    code VARCHAR NOT NULL,
    created_at TIMESTAMP DEFAULT LOCALTIMESTAMP,
    FOREIGN KEY (section_id) REFERENCES sections (id)
);

CREATE TABLE IF NOT EXISTS import_queue (
    id IDENTITY NOT NULL PRIMARY KEY,
    file BLOB NOT NULL,
    status VARCHAR NOT NULL,
    message VARCHAR
);

CREATE TABLE IF NOT EXISTS export_queue (
    id IDENTITY NOT NULL PRIMARY KEY,
    file BLOB,
    status VARCHAR  NOT NULL,
    message VARCHAR
);

CREATE TABLE IF NOT EXISTS users (
    id IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR UNIQUE NOT NULL,
    password VARCHAR NOT NULL,
    created_at TIMESTAMP DEFAULT LOCALTIMESTAMP
);
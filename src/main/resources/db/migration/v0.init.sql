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

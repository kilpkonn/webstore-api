CREATE TABLE IF NOT EXISTS webstore.news (
    id          SERIAL PRIMARY KEY,
    headline    VARCHAR(255),
    content     VARCHAR(255),
    created_at   TIMESTAMP
);

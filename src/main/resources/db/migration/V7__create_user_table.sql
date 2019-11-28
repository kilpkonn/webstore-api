CREATE TABLE webstore.user (
    id  SERIAL PRIMARY KEY,
    username    VARCHAR(255),
    password    VARCHAR(255),
    role        VARCHAR(255)
);

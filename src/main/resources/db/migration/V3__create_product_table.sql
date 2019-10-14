CREATE TABLE IF NOT EXISTS webstore.product (
    id          SERIAL PRIMARY KEY,
    amount      INT,
    description VARCHAR(255),
    name        VARCHAR(255),
    price       DOUBLE PRECISION,
    category INT REFERENCES webstore.category
);

create index product_category on webstore.product(category);

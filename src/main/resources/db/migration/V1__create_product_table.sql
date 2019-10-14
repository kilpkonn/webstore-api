CREATE TABLE IF NOT EXISTS product (
    id          SERIAL PRIMARY KEY,
    amount      INT,
    description VARCHAR(255),
    name        VARCHAR(255),
    price       DOUBLE PRECISION,
    category_id INT REFERENCES category
);

create index product_category on product(category_id);

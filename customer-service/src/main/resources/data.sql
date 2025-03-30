CREATE TABLE IF NOT EXISTS customer (
    id          UUID PRIMARY KEY ,
    name        VARCHAR(255)        NOT NULL,
    email       VARCHAR(255)        NOT NULL,
    address     VARCHAR(255) UNIQUE NOT NULL,
    register_date DATE              NOT NULL
    );

INSERT INTO customer (id, name, email, address, register_date)
SELECT '123e4567-e89b-12d3-a456-426614174000',
       'John Doe',
       'john.doe@example.com',
       '123 Main St, Springfield',
       '2024-01-10'
    WHERE NOT EXISTS (SELECT 1
                  FROM customer
                  WHERE id = '123e4567-e89b-12d3-a456-426614174000');

INSERT INTO customer (id, name, email, address, register_date)
SELECT '123e4567-e89b-12d3-a456-426614174001',
       'Jane Smith',
       'jane.smith@example.com',
       '456 Elm St, Shelbyville',
       '2023-12-01'
    WHERE NOT EXISTS (SELECT 1
                  FROM customer
                  WHERE id = '123e4567-e89b-12d3-a456-426614174001');

INSERT INTO customer (id, name, email, address, register_date)
SELECT '123e4567-e89b-12d3-a456-426614174002',
       'Alice Johnson',
       'alice.johnson@example.com',
       '789 Oak St, Capital City',
       '2022-06-20'
    WHERE NOT EXISTS (SELECT 1
                  FROM customer
                  WHERE id = '123e4567-e89b-12d3-a456-426614174002');
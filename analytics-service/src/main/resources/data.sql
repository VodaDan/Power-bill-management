-- CustomerAnalytics Queries

CREATE TABLE IF NOT EXISTS customer_analytics
(
    id              UUID    PRIMARY KEY,
    registered_date DATE    NOT NULL
);

INSERT INTO customer_Analytics (id, registered_date)
SELECT '123e4567-e89b-12d3-a456-426614174000',
       '2024-01-10'
WHERE NOT EXISTS (SELECT 1
                  FROM customer_analytics
                  WHERE id = '123e4567-e89b-12d3-a456-426614174000');


-- BillAnalytics Queries

CREATE TABLE IF NOT EXISTS bill_analytics
(
    id              UUID            PRIMARY KEY ,
    customer_id     UUID            NOT NULL,
    amount          DECIMAL(0,2)    NOT NULL,
    issue_date      DATE            NOT NULL,
    due_date        DATE            NOT NULL
);

INSERT INTO bill_analytics (id, customer_id, amount, issue_date, due_date)
SELECT '123e4567-e89b-12d3-a456-426634144002',
       '123e4567-e89b-12d3-a456-426614174000',
       325,
       '2025-03-29',
       '2025-04-29'
    WHERE NOT EXISTS (SELECT 1
                  FROM bill_analytics
                  WHERE id = '123e4567-e89b-12d3-a456-426634144002');
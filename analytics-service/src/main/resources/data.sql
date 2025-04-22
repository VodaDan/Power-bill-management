-- CustomerAnalytics Queries

CREATE TABLE IF NOT EXISTS customer_analytics
(
    id              UUID    PRIMARY KEY,
    registered_date   DATE    NOT NULL
);

INSERT INTO customer_analytics (id, registered_date)
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
    amount          DECIMAL(10, 2)  NOT NULL,
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

-- Analytics Entity Queries

CREATE TABLE IF NOT EXISTS analytics
(
    id                      UUID            PRIMARY KEY ,
    total_customers         INT             NOT NULL,
    total_bills             INT             NOT NULL,
    total_revenue           DECIMAL(10, 2)  NOT NULL,
    average_bill_revenue    DECIMAL(10, 2)  NOT NULL,
    generation_date         DATE            NOT NULL
);

INSERT INTO analytics (id, total_customers, total_bills, total_revenue, average_bill_revenue, generation_date)
SELECT  '123e4567-e89b-12d3-a456-426634144002',
        1,
        1,
        324,
        120,
        '2025-04-29'
WHERE NOT EXISTS (SELECT 1
                  FROM analytics
                  WHERE id = '123e4567-e89b-12d3-a456-426634144002');
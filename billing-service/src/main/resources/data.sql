CREATE TABLE IF NOT EXISTS bill (
    id           UUID           PRIMARY KEY,
    customer_id  UUID           NOT NULL,
    amount       DECIMAL(10, 2)      NOT NULL,
    issue_date   DATE           NOT NULL,
    due_date     DATE           NOT NULL
    );

INSERT INTO bill (id, customer_id, amount, issue_date, due_date)
SELECT '123e4567-e89b-12d3-a456-426634144002',
       '123e4567-e89b-12d3-a456-426614174000',
       325,
       '2025-03-29',
       '2025-04-29'
    WHERE NOT EXISTS (SELECT 1
                  FROM bill
                  WHERE id = '123e4567-e89b-12d3-a456-426634144002');







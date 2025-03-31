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




INSERT INTO bill (id, customer_id, amount, issue_date, due_date)
VALUES
    ('123e4567-e89b-12d3-a456-426634144003',
     '123e4567-e89b-12d3-a456-426614174001',
     150.50,
     '2025-03-30',
     '2025-05-01'),

    ('123e4567-e89b-12d3-a456-426634144004',
     '123e4567-e89b-12d3-a456-426614174002',
     250.00,
     '2025-03-31',
     '2025-06-01'),

    ('123e4567-e89b-12d3-a456-426634144005',
     '123e4567-e89b-12d3-a456-426614174003',
     175.75,
     '2025-04-01',
     '2025-05-15'),

    ('123e4567-e89b-12d3-a456-426634144006',
     '123e4567-e89b-12d3-a456-426614174004',
     120.00,
     '2025-04-02',
     '2025-06-10'),

    ('123e4567-e89b-12d3-a456-426634144007',
     '123e4567-e89b-12d3-a456-426614174005',
     190.30,
     '2025-04-03',
     '2025-06-15');


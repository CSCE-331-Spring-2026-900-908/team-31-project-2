
-- SELECT 'This is special query #1';
-- SELECT 'pseudocode: select count of orders grouped by week 
        -- about: given a specific week, how many orders were placed?
        -- example: "week 1 has 98765 orders"';
-- \prompt 'Which week would you like to see? ' weekchosen

DO $$
DECLARE 
    weekvar INT := 7;
    order_amount INT;
BEGIN 
    SELECT COUNT(*)
    INTO order_amount
    FROM "order"
    WHERE EXTRACT(WEEK FROM created_at) = weekvar;

    RAISE NOTICE 'Week % has % orders', weekvar, order_amount;
END $$;

-- prompt 'Which week would you like to see? ' weekchosen

-- SELECT COUNT(*)
-- FROM "order"
-- WHERE EXTRACT(WEEK FROM created_at) = CAST(:weekchosen AS INT);




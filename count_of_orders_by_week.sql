
SELECT 'This is special query #1';
SELECT 'pseudocode: select count of orders grouped by week 
        about: given a specific week, how many orders were placed?
        example: "week 1 has 98765 orders"';
\prompt 'Which week would you like to see? ' weekchosen

DO $$ 
DECLARE                
    weekvar INT := :weekchosen; 
    order_ammount INT ; 
BEGIN 
    SELECT COUNT(*)
    INTO order_ammount
    FROM "order"
    WHERE EXTRACT(WEEK FROM "order".created_at) = weekvar;

    RAISE NOTICE 'WEEK % has % orders', weekvar, order_ammount; 
END $$;

-- prompt 'Which week would you like to see? ' weekchosen

-- SELECT COUNT(*)
-- FROM "order"
-- WHERE EXTRACT(WEEK FROM created_at) = CAST(:weekchosen AS INT);




-- Special Query #2: "Peak Sales Day"
WITH top_orders AS (
    SELECT
        DATE(created_at) as order_day,
        total_final,
        ROW_NUMBER() OVER (
            PARTITION BY DATE(created_at)
            ORDER BY total_final DESC
        ) as rn
    FROM "order"
)
SELECT 
    order_day,
    TRUNC(SUM(total_final)::NUMERIC, 2)
FROM top_orders
WHERE rn <= 10
GROUP BY order_day
ORDER BY order_day ASC;
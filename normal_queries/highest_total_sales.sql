-- Highest Total Sales Day
WITH top_orders AS (
    SELECT
        DATE(created_at) as order_day,
        total_final
    FROM "order"
)
SELECT 
    order_day,
    TRUNC(SUM(total_final)::NUMERIC, 2) as total_sales
FROM top_orders
GROUP BY order_day
ORDER BY total_sales DESC;
-- Special Query #1: "Weekly Sales History"

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
    TRUNC(SUM(total_final)::NUMERIC, 2) as top10_total
FROM top_orders
WHERE rn <= 10
GROUP BY order_day
ORDER BY order_day ASC;

-- Special Query #3: "Realistic Sales History"
SELECT
    TO_CHAR(created_at, 'FMHH12AM') AS hour_label,
    COUNT(*) AS num_orders,
    TRUNC(SUM(total_final)::NUMERIC, 2) AS total_sales
FROM "order"
GROUP BY hour_label, EXTRACT(HOUR FROM created_at)
ORDER BY EXTRACT(HOUR FROM created_at) ASC;

-- Special Query #4: Menu Item Inventory
SELECT p.name AS menu_item, COUNT(pi.item_id) AS inventory_item_count
FROM product p
JOIN productingredient pi ON p.product_id = pi.product_id
GROUP BY p.product_id, p.name
ORDER BY p.name;

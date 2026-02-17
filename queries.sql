-- Special Query #1: "Weekly Sales History"

-- Special Query #2: "Peak Sales Day"

-- Special Query #3: "Realistic Sales History"
SELECT
    TO_CHAR(created_at, 'HH12AM') AS hour_label,
    COUNT(*) AS num_orders,
    TRUNC(SUM(total_final)::NUMERIC, 2) AS total_sales
FROM "order"
GROUP BY hour_label
ORDER BY hour_label;

-- Special Query #4: Menu Item Inventory
SELECT p.name AS menu_item, COUNT(pi.item_id) AS inventory_item_count
FROM product p
JOIN productingredient pi ON p.product_id = pi.product_id
GROUP BY p.product_id, p.name
ORDER BY p.name;

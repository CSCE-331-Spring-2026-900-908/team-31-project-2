SELECT
    i.item_name AS "Inventory Item",
    ROUND(CAST(SUM(pi.quantity_used) AS numeric), 2) AS "Quantity Used",
    i.unit_type AS "Unit"
FROM "order" o
JOIN OrderDetail od ON o.id = od.order_id
JOIN ProductIngredient pi ON od.product_id = pi.product_id
JOIN Inventory i ON pi.item_id = i.item_id
WHERE 1=1 {{DATE_FILTER}}
GROUP BY i.item_name, i.unit_type
ORDER BY "Quantity Used" DESC;
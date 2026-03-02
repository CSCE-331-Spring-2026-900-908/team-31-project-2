SELECT
    p.name AS "Menu Item",
    COUNT(od.id) AS "Quantity Sold",
    SUM(od.sold_price) AS "Total Sales ($)"
FROM "order" o
JOIN OrderDetail od ON o.id = od.order_id
JOIN Product p ON od.product_id = p.product_id
WHERE 1=1 {{DATE_FILTER}}
GROUP BY p.name
ORDER BY "Total Sales ($)" DESC;
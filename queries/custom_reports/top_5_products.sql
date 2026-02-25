SELECT
    p.name AS "Product Name",
    COUNT(od.id) AS "Quantity Sold"
FROM OrderDetail od
    JOIN Product p ON od.product_id = p.product_id
    JOIN "order" o ON od.order_id = o.id
WHERE
    1 = 1
    {{DATE_FILTER}}
GROUP BY
    p.name
ORDER BY "Quantity Sold" DESC;
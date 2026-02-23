SELECT
    p.name AS "Product Name",
    COUNT(od.id) AS "Quantity Sold"
FROM OrderDetail od
    JOIN Product p ON od.product_id = p.product_id
GROUP BY
    p.name
ORDER BY "Quantity Sold" DESC
LIMIT 5;
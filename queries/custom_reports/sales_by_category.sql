SELECT p.category_name AS "Category", ROUND(
        CAST(SUM(od.sold_price) AS numeric), 2
    ) AS "Total Revenue"
FROM OrderDetail od
    JOIN Product p ON od.product_id = p.product_id
WHERE
    p.category_name IS NOT NULL
GROUP BY
    p.category_name
ORDER BY "Total Revenue" DESC;
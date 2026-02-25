SELECT p.category_name AS "Category", ROUND(
        CAST(SUM(od.sold_price) AS numeric), 2
    ) AS "Total Revenue"
FROM OrderDetail od
    JOIN Product p ON od.product_id = p.product_id
    JOIN "order" o ON od.order_id = o.id
WHERE
    p.category_name IS NOT NULL
    {{DATE_FILTER}}
GROUP BY
    p.category_name
ORDER BY "Total Revenue" DESC;
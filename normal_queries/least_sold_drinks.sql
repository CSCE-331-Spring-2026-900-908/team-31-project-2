-- Least Sold Drinks
SELECT
    p.name AS product_name,
    COUNT(od.product_id) AS order_count
FROM orderdetail od
JOIN product p ON od.product_id = p.product_id
GROUP BY product_name
ORDER BY order_count ASC
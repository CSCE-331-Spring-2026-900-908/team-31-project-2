\echo 'Total January Sales: '
SELECT SUM(total_final) FROM "order" WHERE created_at < '1/31/2026';
<<<<<<< HEAD
SELECT * FROM "order" WHERE total_final > 20;
=======
SELECT
    TO_CHAR(created_at, 'HH12AM') AS hour_label,
    COUNT(*) AS num_orders,
    SUM(total_final) AS total_sales
FROM "order"
GROUP BY hour_label
ORDER BY hour_label;
>>>>>>> 51add1c5538fac48ffd80966ba0a34530de6585c

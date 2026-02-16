SELECT
    TO_CHAR(created_at, 'HH12AM') AS hour_label,
    COUNT(*) AS num_orders,
    SUM(total_final) AS total_sales
FROM "order"
GROUP BY hour_label
ORDER BY hour_label;

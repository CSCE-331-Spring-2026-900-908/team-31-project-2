SELECT 
    TO_CHAR(created_at, 'FMHH12') || TO_CHAR(created_at, 'AM') AS hour_label,
    COUNT(*) AS num_orders,
    TRUNC(SUM(total_final)::NUMERIC, 2) AS total_sales
FROM "order"
GROUP BY TO_CHAR(created_at, 'FMHH12'), TO_CHAR(created_at, 'AM')
ORDER BY MIN(EXTRACT(HOUR FROM created_at));
-- Special Query #3: "Realistic Sales History"
SELECT
    TO_CHAR(created_at, 'FMHH12AM') AS hour_label,
    COUNT(*) AS num_orders,
    TRUNC(SUM(total_final)::NUMERIC, 2) AS total_sales
FROM "order"
GROUP BY hour_label, EXTRACT(HOUR FROM created_at)
ORDER BY EXTRACT(HOUR FROM created_at) ASC;

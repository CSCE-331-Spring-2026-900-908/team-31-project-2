SELECT TO_CHAR(
        DATE_TRUNC('month', o.created_at), 'YYYY-MM'
    ) AS "Month", ROUND(
        CAST(SUM(o.total_final) AS numeric), 2
    ) AS "Total Revenue"
FROM "order" o
GROUP BY
    DATE_TRUNC('month', o.created_at)
ORDER BY "Month"
LIMIT 12;
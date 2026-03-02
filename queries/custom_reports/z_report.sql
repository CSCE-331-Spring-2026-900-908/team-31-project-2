SELECT
    DATE(o.created_at) AS "Date",
    COUNT(o.id) AS "Total Orders",
    SUM(o.total_tax) AS "Total Tax ($)",
    SUM(o.total_final) AS "Total Sales ($)",
    0 AS "Returns ($)",
    0 AS "Voids ($)"
FROM "order" o
WHERE
    o.z_report_run = FALSE
GROUP BY
    DATE(o.created_at)
ORDER BY "Date";
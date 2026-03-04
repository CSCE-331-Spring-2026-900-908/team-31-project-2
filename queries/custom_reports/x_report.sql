SELECT
    EXTRACT(HOUR FROM o.created_at) AS "Hour of Day",
    COUNT(o.id) AS "Total Orders",
    SUM(o.total_tax) AS "Total Tax ($)",
    SUM(o.total_final) AS "Total Sales ($)",
    0 AS "Returns ($)",
    0 AS "Voids ($)"
FROM "order" o
WHERE
    o.z_report_run = FALSE
    AND DATE(o.created_at) = CURRENT_DATE
    AND o.total_final > 0
GROUP BY
    "Hour of Day"
ORDER BY "Hour of Day";
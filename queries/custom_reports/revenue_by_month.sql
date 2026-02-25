SELECT TO_CHAR(
        DATE_TRUNC('{{TIME_BUCKET}}', o.created_at), '{{TIME_LABEL_FORMAT}}'
    ) AS "Period", ROUND(
        CAST(SUM(o.total_final) AS numeric), 2
    ) AS "Total Revenue"
FROM "order" o
WHERE
    1 = 1
    {{DATE_FILTER}}
GROUP BY
    DATE_TRUNC('{{TIME_BUCKET}}', o.created_at)
ORDER BY
    DATE_TRUNC('{{TIME_BUCKET}}', o.created_at);
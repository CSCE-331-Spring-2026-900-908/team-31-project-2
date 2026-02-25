SELECT
    TO_CHAR(o.created_at, 'HH24:00') AS "Hour",
    COUNT(o.id) AS "Orders"
FROM "order" o
WHERE
    1 = 1
    {{DATE_FILTER}}
GROUP BY
    EXTRACT(HOUR FROM o.created_at),
    TO_CHAR(o.created_at, 'HH24:00')
ORDER BY
    EXTRACT(HOUR FROM o.created_at);
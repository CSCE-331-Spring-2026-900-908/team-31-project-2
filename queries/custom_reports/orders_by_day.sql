SELECT
    TRIM(TO_CHAR(o.created_at, 'Day')) AS "Day of Week",
    COUNT(o.id) AS "Total Orders"
FROM "order" o
WHERE
    1 = 1
    {{DATE_FILTER}}
GROUP BY
    TO_CHAR(o.created_at, 'Day'),
    EXTRACT(
        ISODOW
        FROM o.created_at
    )
ORDER BY EXTRACT(
        ISODOW
        FROM o.created_at
    );
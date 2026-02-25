SELECT
    e.name AS "Employee",
    ROUND(CAST(SUM(o.total_final) AS numeric), 2) AS "Revenue"
FROM "order" o
JOIN Employee e ON o.employee_id = e.id
WHERE
    1 = 1
    {{DATE_FILTER}}
GROUP BY
    e.name
ORDER BY
    "Revenue" DESC;
-- Employee of the Month (employee with highest sales)
SELECT
    e.name,
    TRUNC(SUM(o.total_final)::NUMERIC, 2) AS total_sales
FROM "employee" e
JOIN "order" o ON e.id = o.employee_id
GROUP BY e.name
ORDER BY total_sales DESC;
\echo 'Total January Sales: '
SELECT SUM(total_final) FROM "order" WHERE created_at < '1/31/2026';
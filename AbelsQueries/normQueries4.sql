SELECT 'A list of the most expesvie purchase in Novemeber';
SELECT total_final FROM "order"  
WHERE "order".created_at >= DATE '2026-11-01' AND "order".created_at < DATE '2026-12-01' 
ORDER BY total_final  DESC LIMIT (10);
SELECT 'Which modifier appears the least in July'
SELECT snapshot_name, COUNT(*) AS times_sold 
FROM "order" JOIN ordermodifier 
ON "order".id = ordermodifier.order_detail_id 
WHERE "order".created_at >= DATE '2026-07-01' 
AND "order".created_at < DATE '2026-08-01'  
GROUP BY snapshot_name ORDER BY times_sold ASC LIMIT 1; 
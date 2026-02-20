SELECT 'Which modifier apears the most in march'; 
SELECT snapshot_name, COUNT(*) AS times_sold 
FROM "order" 
JOIN ordermodifier 
ON "order".id = ordermodifier.order_detail_id 
WHERE "order".created_at >= 
DATE '2026-03-01' 
AND "order".created_at < DATE '2026-04-01'  
GROUP BY snapshot_name 
ORDER BY times_sold DESC LIMIT 1; 
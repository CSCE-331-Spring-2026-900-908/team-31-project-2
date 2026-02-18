-- SELECT 'This is noraml query #1'
-- SELECT 'This will find every element that has 70% sugar and was sold in the month of January'
SELECT * 
FROM "order" 
JOIN ordermodifier 
ON "order".id = ordermodifier.order_detail_id 
WHERE "order".created_at < '2026-01-31' 
AND ordermodifier.snapshot_name = '70% Sugar';
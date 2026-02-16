-- Abel's changes getting the lowest item
SELECT * FROM "order" 
WHERE lowestPrice = (SELECT MIN(total_final) FROM "order"); 
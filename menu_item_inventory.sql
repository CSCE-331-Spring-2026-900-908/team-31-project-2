SELECT p.name AS menu_item, COUNT(pi.item_id) AS inventory_item_count
FROM product p
JOIN productingredient pi ON p.product_id = pi.product_id
GROUP BY p.product_id, p.name
ORDER BY p.name;

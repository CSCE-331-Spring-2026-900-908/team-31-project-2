-- Most Popular Toppings
SELECT
    mo.name AS topping_name,
    COUNT(om.modifier_option_id) AS times_ordered
FROM ordermodifier om
JOIN modifieroption mo ON om.modifier_option_id = mo.option_id
WHERE mo.category = 'Topping'
GROUP BY topping_name
ORDER BY times_ordered DESC

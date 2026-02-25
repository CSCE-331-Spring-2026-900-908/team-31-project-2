SELECT
    item_name AS "Ingredient",
    ROUND(CAST(quantity AS numeric), 2) AS "Current Stock",
    unit_type AS "Unit"
FROM Inventory
ORDER BY item_name;
SELECT
    item_name AS "Ingredient",
    ROUND(CAST(quantity AS numeric), 2) AS "Current Stock",
    ROUND(CAST(target_val AS numeric), 2) AS "Target Stock",
    unit_type AS "Unit",
    CASE
        WHEN quantity < target_val THEN 'Restock Needed'
        ELSE 'OK'
    END AS "Status"
FROM Inventory
ORDER BY item_name;
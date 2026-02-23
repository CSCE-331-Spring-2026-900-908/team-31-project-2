SELECT mo.name AS "Modifier", COUNT(om.id) AS "Times Ordered"
FROM
    OrderModifier om
    JOIN ModifierOption mo ON om.modifier_option_id = mo.option_id
GROUP BY
    mo.name
ORDER BY "Times Ordered" DESC
LIMIT 5;
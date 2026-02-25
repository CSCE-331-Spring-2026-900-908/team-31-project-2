SELECT mo.name AS "Modifier", COUNT(om.id) AS "Times Ordered"
FROM
    OrderModifier om
    JOIN ModifierOption mo ON om.modifier_option_id = mo.option_id
    JOIN OrderDetail od ON om.order_detail_id = od.id
    JOIN "order" o ON od.order_id = o.id
WHERE
    1 = 1
    {{DATE_FILTER}}
GROUP BY
    mo.name
ORDER BY "Times Ordered" DESC
LIMIT 5;
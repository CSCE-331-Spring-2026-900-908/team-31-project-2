-- Special Query #5: "Best of the Worst"

WITH daily_sales AS (
    SELECT
        DATE(o.created_at)                        AS order_day,
        DATE_PART('week', o.created_at)::INT      AS week_num,
        TRUNC(SUM(o.total_final)::NUMERIC, 2)     AS total_sales
    FROM "order" o
    GROUP BY order_day, week_num
),
worst_day_per_week AS (
    SELECT DISTINCT ON (week_num)
        week_num,
        order_day,
        total_sales
    FROM daily_sales
    ORDER BY week_num, total_sales ASC
),
daily_item_counts AS (
    SELECT
        DATE(o.created_at)   AS order_day,
        p.name               AS product_name,
        COUNT(*)             AS item_count
    FROM "order" o
    JOIN OrderDetail od ON od.order_id = o.id
    JOIN Product p      ON p.product_id = od.product_id
    GROUP BY order_day, product_name
),
top_item_per_day AS (
    SELECT DISTINCT ON (order_day)
        order_day,
        product_name,
        item_count
    FROM daily_item_counts
    ORDER BY order_day, item_count DESC
)
SELECT
    w.week_num,
    w.order_day,
    w.total_sales,
    t.product_name  AS top_seller,
    t.item_count    AS top_seller_count
FROM worst_day_per_week w
JOIN top_item_per_day   t ON t.order_day = w.order_day
ORDER BY w.week_num;

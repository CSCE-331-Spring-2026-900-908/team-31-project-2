-- Script to populate ProductIngredient table more comprehensively
TRUNCATE TABLE ProductIngredient RESTART IDENTITY;

-- Ingredients for Milk Foam Green Tea
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Milk Foam Green Tea' AND i.item_name = 'Green Tea Leaves';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Milk Foam Green Tea' AND i.item_name = 'Milk Foam Powder';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Milk Foam Green Tea' AND i.item_name = 'Ice';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Milk Foam Green Tea' AND i.item_name = 'Sugar';

-- Ingredients for Milk Foam Black Tea
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Milk Foam Black Tea' AND i.item_name = 'Black Tea Leaves';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Milk Foam Black Tea' AND i.item_name = 'Milk Foam Powder';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Milk Foam Black Tea' AND i.item_name = 'Ice';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Milk Foam Black Tea' AND i.item_name = 'Sugar';

-- Ingredients for Milk Foam Earl Grey Tea
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Milk Foam Earl Grey Tea' AND i.item_name = 'Earl Grey Tea Leaves';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Milk Foam Earl Grey Tea' AND i.item_name = 'Milk Foam Powder';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Milk Foam Earl Grey Tea' AND i.item_name = 'Ice';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Milk Foam Earl Grey Tea' AND i.item_name = 'Sugar';

-- Ingredients for Milk Foam Wintermelon
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Milk Foam Wintermelon' AND i.item_name = 'Wintermelon Syrup';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Milk Foam Wintermelon' AND i.item_name = 'Milk Foam Powder';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Milk Foam Wintermelon' AND i.item_name = 'Ice';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Milk Foam Wintermelon' AND i.item_name = 'Sugar';

-- Ingredients for Creme Brulee Brown Sugar Milk Tea
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Creme Brulee Brown Sugar Milk Tea' AND i.item_name = 'Black Tea Leaves';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Creme Brulee Brown Sugar Milk Tea' AND i.item_name = 'Milk';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Creme Brulee Brown Sugar Milk Tea' AND i.item_name = 'Brown Sugar Syrup';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Creme Brulee Brown Sugar Milk Tea' AND i.item_name = 'Tapioca Pearls';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Creme Brulee Brown Sugar Milk Tea' AND i.item_name = 'Milk Foam Powder';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Creme Brulee Brown Sugar Milk Tea' AND i.item_name = 'Ice';

-- Ingredients for Pearl Milk Tea
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Pearl Milk Tea' AND i.item_name = 'Black Tea Leaves';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Pearl Milk Tea' AND i.item_name = 'Milk';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Pearl Milk Tea' AND i.item_name = 'Tapioca Pearls';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Pearl Milk Tea' AND i.item_name = 'Ice';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Pearl Milk Tea' AND i.item_name = 'Sugar';

-- Ingredients for Black Milk Tea
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Black Milk Tea' AND i.item_name = 'Black Tea Leaves';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Black Milk Tea' AND i.item_name = 'Milk';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Black Milk Tea' AND i.item_name = 'Ice';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Black Milk Tea' AND i.item_name = 'Sugar';

-- Ingredients for Taro Milk Tea
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Taro Milk Tea' AND i.item_name = 'Black Tea Leaves';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Taro Milk Tea' AND i.item_name = 'Milk';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Taro Milk Tea' AND i.item_name = 'Taro Powder';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Taro Milk Tea' AND i.item_name = 'Ice';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Taro Milk Tea' AND i.item_name = 'Sugar';

-- Ingredients for Earl Grey Milk Tea w/ 3J
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Earl Grey Milk Tea w/ 3J' AND i.item_name = 'Earl Grey Tea Leaves';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Earl Grey Milk Tea w/ 3J' AND i.item_name = 'Milk';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Earl Grey Milk Tea w/ 3J' AND i.item_name = 'Tapioca Pearls';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Earl Grey Milk Tea w/ 3J' AND i.item_name = 'Pudding';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Earl Grey Milk Tea w/ 3J' AND i.item_name = 'Herbal Jelly';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Earl Grey Milk Tea w/ 3J' AND i.item_name = 'Ice';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Earl Grey Milk Tea w/ 3J' AND i.item_name = 'Sugar';

-- Ingredients for Brown Sugar Milk Tea
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Brown Sugar Milk Tea' AND i.item_name = 'Black Tea Leaves';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Brown Sugar Milk Tea' AND i.item_name = 'Milk';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Brown Sugar Milk Tea' AND i.item_name = 'Brown Sugar Syrup';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Brown Sugar Milk Tea' AND i.item_name = 'Ice';

-- Ingredients for Strawberry Milk Tea
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Strawberry Milk Tea' AND i.item_name = 'Black Tea Leaves';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Strawberry Milk Tea' AND i.item_name = 'Milk';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Strawberry Milk Tea' AND i.item_name = 'Strawberry Jam';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Strawberry Milk Tea' AND i.item_name = 'Ice';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Strawberry Milk Tea' AND i.item_name = 'Sugar';

-- Ingredients for Oolong Milk Tea
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Oolong Milk Tea' AND i.item_name = 'Oolong Tea Leaves';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Oolong Milk Tea' AND i.item_name = 'Milk';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Oolong Milk Tea' AND i.item_name = 'Ice';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Oolong Milk Tea' AND i.item_name = 'Sugar';

-- Ingredients for Mango Green Tea
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Mango Green Tea' AND i.item_name = 'Green Tea Leaves';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Mango Green Tea' AND i.item_name = 'Mango Puree';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Mango Green Tea' AND i.item_name = 'Ice';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Mango Green Tea' AND i.item_name = 'Sugar';

-- Ingredients for Passionfruit Green Tea
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Passionfruit Green Tea' AND i.item_name = 'Green Tea Leaves';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Passionfruit Green Tea' AND i.item_name = 'Passionfruit Jam';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Passionfruit Green Tea' AND i.item_name = 'Ice';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Passionfruit Green Tea' AND i.item_name = 'Sugar';

-- Ingredients for Lemon Wintermelon w/ Basil Seeds
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Lemon Wintermelon w/ Basil Seeds' AND i.item_name = 'Wintermelon Syrup';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Lemon Wintermelon w/ Basil Seeds' AND i.item_name = 'Lemon Juice';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Lemon Wintermelon w/ Basil Seeds' AND i.item_name = 'Basil Seeds';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Lemon Wintermelon w/ Basil Seeds' AND i.item_name = 'Ice';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Lemon Wintermelon w/ Basil Seeds' AND i.item_name = 'Sugar';

-- Ingredients for Lychee Oolong Tea
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Lychee Oolong Tea' AND i.item_name = 'Oolong Tea Leaves';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Lychee Oolong Tea' AND i.item_name = 'Lychee Syrup';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Lychee Oolong Tea' AND i.item_name = 'Ice';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Lychee Oolong Tea' AND i.item_name = 'Sugar';

-- Ingredients for Hibiscus Green Tea
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Hibiscus Green Tea' AND i.item_name = 'Green Tea Leaves';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Hibiscus Green Tea' AND i.item_name = 'Hibiscus Syrup';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Hibiscus Green Tea' AND i.item_name = 'Ice';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Hibiscus Green Tea' AND i.item_name = 'Sugar';

-- Ingredients for Lemon Ai-Yu w/ White Pearl
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Lemon Ai-Yu w/ White Pearl' AND i.item_name = 'Green Tea Leaves';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Lemon Ai-Yu w/ White Pearl' AND i.item_name = 'Lemon Juice';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Lemon Ai-Yu w/ White Pearl' AND i.item_name = 'Ai-Yu Jelly';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Lemon Ai-Yu w/ White Pearl' AND i.item_name = 'White Pearls';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Lemon Ai-Yu w/ White Pearl' AND i.item_name = 'Ice';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Lemon Ai-Yu w/ White Pearl' AND i.item_name = 'Sugar';

-- Ingredients for Jasmine Green Tea
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Jasmine Green Tea' AND i.item_name = 'Green Tea Leaves';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Jasmine Green Tea' AND i.item_name = 'Ice';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Jasmine Green Tea' AND i.item_name = 'Sugar';

-- Ingredients for Black Tea
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Black Tea' AND i.item_name = 'Black Tea Leaves';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Black Tea' AND i.item_name = 'Ice';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Black Tea' AND i.item_name = 'Sugar';

-- Ingredients for Oolong Tea
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Oolong Tea' AND i.item_name = 'Oolong Tea Leaves';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Oolong Tea' AND i.item_name = 'Ice';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Oolong Tea' AND i.item_name = 'Sugar';

-- Ingredients for Earl Grey Tea
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Earl Grey Tea' AND i.item_name = 'Earl Grey Tea Leaves';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Earl Grey Tea' AND i.item_name = 'Ice';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Earl Grey Tea' AND i.item_name = 'Sugar';

-- Ingredients for Milk Foam Black Coffee
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Milk Foam Black Coffee' AND i.item_name = 'Coffee Beans';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Milk Foam Black Coffee' AND i.item_name = 'Milk Foam Powder';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Milk Foam Black Coffee' AND i.item_name = 'Ice';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Milk Foam Black Coffee' AND i.item_name = 'Sugar';

-- Ingredients for Coffee Milk Tea
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Coffee Milk Tea' AND i.item_name = 'Coffee Beans';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Coffee Milk Tea' AND i.item_name = 'Black Tea Leaves';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Coffee Milk Tea' AND i.item_name = 'Milk';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Coffee Milk Tea' AND i.item_name = 'Ice';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Coffee Milk Tea' AND i.item_name = 'Sugar';

-- Ingredients for Taro Slush
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Taro Slush' AND i.item_name = 'Taro Powder';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Taro Slush' AND i.item_name = 'Milk';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Taro Slush' AND i.item_name = 'Ice';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Taro Slush' AND i.item_name = 'Sugar';

-- Ingredients for Matcha Milk Slush
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Matcha Milk Slush' AND i.item_name = 'Matcha Powder';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Matcha Milk Slush' AND i.item_name = 'Milk';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Matcha Milk Slush' AND i.item_name = 'Ice';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Matcha Milk Slush' AND i.item_name = 'Sugar';

-- Ingredients for Peach Slush
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Peach Slush' AND i.item_name = 'Peach Syrup';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Peach Slush' AND i.item_name = 'Ice';
INSERT INTO ProductIngredient (product_id, item_id)
SELECT p.product_id, i.item_id
FROM Product p, Inventory i
WHERE p.name = 'Peach Slush' AND i.item_name = 'Sugar';

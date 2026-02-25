-- Populate Database with Gong Cha Menu and Inventory

TRUNCATE TABLE OrderModifier, OrderDetail, "order", ProductModifier, ProductIngredient, ModifierOption, Product, Inventory, Employee RESTART IDENTITY CASCADE;

INSERT INTO Employee (name, role, pin_hash) VALUES 
('Vivian', 'Manager', 'hash123'),
('Noah', 'Cashier', 'hash456'),
('Abel', 'Cashier', 'hash789'),
('Nitheesh', 'Cashier', 'hash1011'),
('Long', 'Cashier', 'hash1213'),
('Sanjit', 'Cashier', 'hash1415');
--I EDITED THIS TO HAVE A VALUE CALLED TARGET_VAL NEEDED FOR INVETORY MENU (ABEL)
INSERT INTO Inventory (item_name, quantity, unit_type, expiration_date, target_val) VALUES 
('Black Tea Leaves', 500.0, 'grams', NOW() + INTERVAL '1 year',0),
('Green Tea Leaves', 500.0, 'grams', NOW() + INTERVAL '1 year',0),
('Earl Grey Tea Leaves', 500.0, 'grams', NOW() + INTERVAL '1 year',0),
('Oolong Tea Leaves', 500.0, 'grams', NOW() + INTERVAL '1 year',0),
('Wintermelon Syrup', 200.0, 'liters', NOW() + INTERVAL '6 months',0),
('Milk', 100.0, 'gallons', NOW() + INTERVAL '1 week',0),
('Non-Dairy Creamer', 50.0, 'kg', NOW() + INTERVAL '6 months',0),
('Almond Milk', 20.0, 'gallons', NOW() + INTERVAL '2 weeks',0),
('Oat Milk', 20.0, 'gallons', NOW() + INTERVAL '2 weeks',0),
('Soy Milk', 20.0, 'gallons', NOW() + INTERVAL '2 weeks',0),
('Brown Sugar Syrup', 50.0, 'liters', NOW() + INTERVAL '6 months',0),
('Wintermelon Syrup', 200.0, 'liters', NOW() + INTERVAL '6 months',0),
('Honey', 20.0, 'liters', NOW() + INTERVAL '1 year',0),
('Lemon Juice', 30.0, 'liters', NOW() + INTERVAL '1 month',0),
('Strawberry Jam', 20.0, 'kg', NOW() + INTERVAL '3 months',0),
('Mango Puree', 20.0, 'kg', NOW() + INTERVAL '3 months',0),
('Passionfruit Jam', 20.0, 'kg', NOW() + INTERVAL '3 months',0),
('Lychee Syrup', 20.0, 'liters', NOW() + INTERVAL '6 months',0),
('Hibiscus Syrup', 20.0, 'liters', NOW() + INTERVAL '6 months',0),
('Peach Syrup', 20.0, 'liters', NOW() + INTERVAL '6 months',0),
('Ginger Syrup', 10.0, 'liters', NOW() + INTERVAL '6 months',0), 
('Tapioca Pearls', 100.0, 'kg', NOW() + INTERVAL '2 days',0),
('White Pearls', 50.0, 'kg', NOW() + INTERVAL '5 days',0),
('Pudding', 30.0, 'kg', NOW() + INTERVAL '5 days',0),
('Herbal Jelly', 30.0, 'kg', NOW() + INTERVAL '5 days',0),
('Coconut Jelly', 30.0, 'kg', NOW() + INTERVAL '30 days',0),
('Basil Seeds', 10.0, 'kg', NOW() + INTERVAL '1 year',0),
('Ai-Yu Jelly', 20.0, 'kg', NOW() + INTERVAL '5 days',0),
('Oreo Crumbs', 20.0, 'kg', NOW() + INTERVAL '3 months',0),
('Milk Foam Powder', 50.0, 'kg', NOW() + INTERVAL '1 year',0),
('Red Bean', 20.0, 'kg', NOW() + INTERVAL '10 days',0), 
('Coffee Beans', 50.0, 'kg', NOW() + INTERVAL '3 months',0),
('Taro Powder', 40.0, 'kg', NOW() + INTERVAL '1 year',0),
('Matcha Powder', 30.0, 'kg', NOW() + INTERVAL '1 year',0),
('Chocolate Powder', 30.0, 'kg', NOW() + INTERVAL '1 year',0),
('Ice', 1000.0, 'kg', NOW() + INTERVAL '1 day',0),
('Sugar', 500.0, 'kg', NOW() + INTERVAL '1 year',0),
('Cane Sugar Syrup', 100.0, 'liters', NOW() + INTERVAL '1 year',0);

-- Milk Foam Series
INSERT INTO Product (name, base_price, category_name, description, color_code, is_active) VALUES
('Milk Foam Green Tea', 5.75, 'Milk Foam Series', 'Jasmine green tea topped with signature milk foam.', '#E6E6FA', TRUE),
('Milk Foam Black Tea', 5.75, 'Milk Foam Series', 'Rich black tea topped with signature milk foam.', '#E6E6FA', TRUE),
('Milk Foam Earl Grey Tea', 5.75, 'Milk Foam Series', 'Citrusy Earl Grey tea topped with signature milk foam.', '#E6E6FA', TRUE),
('Milk Foam Wintermelon', 5.75, 'Milk Foam Series', 'Caffeine-free wintermelon drink with salty-sweet foam.', '#E6E6FA', TRUE),
('Creme Brulee Brown Sugar Milk Tea', 7.00, 'Milk Foam Series', 'Milk tea with brown sugar pearls and torched creme brulee foam.', '#E6E6FA', TRUE);

-- Milk Tea Series
INSERT INTO Product (name, base_price, category_name, description, color_code, is_active) VALUES
('Pearl Milk Tea', 5.70, 'Milk Tea Series', 'The classic. Black milk tea with tapioca pearls included.', '#F5DEB3', TRUE),
('Black Milk Tea', 4.90, 'Milk Tea Series', 'Smooth and creamy black tea.', '#F5DEB3', TRUE),
('Taro Milk Tea', 5.70, 'Milk Tea Series', 'Creamy purple taro root drink.', '#F5DEB3', TRUE),
('Earl Grey Milk Tea w/ 3J', 6.50, 'Milk Tea Series', 'Earl Grey milk tea with Pearls, Pudding, and Herbal Jelly.', '#F5DEB3', TRUE),
('Brown Sugar Milk Tea', 5.40, 'Milk Tea Series', 'Milk tea sweetened with rich brown sugar syrup.', '#F5DEB3', TRUE),
('Strawberry Milk Tea', 5.70, 'Milk Tea Series', 'Sweet strawberry jam blended with milk tea.', '#F5DEB3', TRUE),
('Oolong Milk Tea', 4.90, 'Milk Tea Series', 'Roasted oolong tea with milk.', '#F5DEB3', TRUE);

-- Creative Mix Series
INSERT INTO Product (name, base_price, category_name, description, color_code, is_active) VALUES
('Mango Green Tea', 5.40, 'Creative Mix Series', 'Jasmine green tea with mango puree.', '#FFB6C1', TRUE),
('Passionfruit Green Tea', 5.40, 'Creative Mix Series', 'Tart passionfruit jam with green tea and seeds.', '#FFB6C1', TRUE),
('Lemon Wintermelon w/ Basil Seeds', 5.95, 'Creative Mix Series', 'Sweet wintermelon, tart lemon, and crunchy basil seeds.', '#FFB6C1', TRUE),
('Lychee Oolong Tea', 5.40, 'Creative Mix Series', 'Roasted oolong tea with sweet lychee syrup.', '#FFB6C1', TRUE),
('Hibiscus Green Tea', 5.40, 'Creative Mix Series', 'Tart ruby-red hibiscus flower tea.', '#FFB6C1', TRUE),
('Lemon Ai-Yu w/ White Pearl', 5.95, 'Creative Mix Series', 'Lemon green tea with Ai-Yu jelly and crystal boba.', '#FFB6C1', TRUE);

-- Brewed Tea Series
INSERT INTO Product (name, base_price, category_name, description, color_code, is_active) VALUES
('Jasmine Green Tea', 4.50, 'Brewed Tea Series', 'Pure jasmine green tea.', '#90EE90', TRUE),
('Black Tea', 4.50, 'Brewed Tea Series', 'Classic brewed black tea.', '#90EE90', TRUE),
('Oolong Tea', 4.50, 'Brewed Tea Series', 'Roasted oolong tea.', '#90EE90', TRUE),
('Earl Grey Tea', 4.50, 'Brewed Tea Series', 'Citrusy earl grey tea.', '#90EE90', TRUE);

-- Coffee Series
INSERT INTO Product (name, base_price, category_name, description, color_code, is_active) VALUES
('Milk Foam Black Coffee', 5.95, 'Coffee Series', 'Iced black coffee topped with milk foam.', '#D2691E', TRUE),
('Coffee Milk Tea', 5.40, 'Coffee Series', 'A blend of black coffee and milk tea.', '#D2691E', TRUE);

-- Slush Series
INSERT INTO Product (name, base_price, category_name, description, color_code, is_active) VALUES
('Taro Slush', 6.25, 'Slush Series', 'Iced blended taro drink.', '#ADD8E6', TRUE),
('Matcha Milk Slush', 6.75, 'Slush Series', 'Japanese matcha powder blended with ice and milk.', '#ADD8E6', TRUE),
('Peach Slush', 6.75, 'Slush Series', 'Sweet peach syrup blended with ice.', '#ADD8E6', TRUE);


-- Toppings
INSERT INTO ModifierOption (name, category, price_adjustment, inventory_item_id)
VALUES 
('Pearl (Boba)', 'Topping', 0.50, (SELECT item_id FROM Inventory WHERE item_name = 'Tapioca Pearls')),
('White Pearl (Crystal)', 'Topping', 0.75, (SELECT item_id FROM Inventory WHERE item_name = 'White Pearls')),
('Milk Foam', 'Topping', 0.75, (SELECT item_id FROM Inventory WHERE item_name = 'Milk Foam Powder')),
('Pudding', 'Topping', 0.50, (SELECT item_id FROM Inventory WHERE item_name = 'Pudding')),
('Herbal Jelly', 'Topping', 0.50, (SELECT item_id FROM Inventory WHERE item_name = 'Herbal Jelly')),
('Coconut Jelly', 'Topping', 0.50, (SELECT item_id FROM Inventory WHERE item_name = 'Coconut Jelly')),
('Basil Seeds', 'Topping', 0.50, (SELECT item_id FROM Inventory WHERE item_name = 'Basil Seeds')),
('Ai-Yu Jelly', 'Topping', 0.50, (SELECT item_id FROM Inventory WHERE item_name = 'Ai-Yu Jelly')),
('Oreo Crumbs', 'Topping', 0.50, (SELECT item_id FROM Inventory WHERE item_name = 'Oreo Crumbs'));

-- Sugar Levels
INSERT INTO ModifierOption (name, category, price_adjustment, is_default, inventory_item_id)
VALUES 
('100% Sugar', 'Sugar Level', 0.0, TRUE, (SELECT item_id FROM Inventory WHERE item_name = 'Cane Sugar Syrup')),
('70% Sugar', 'Sugar Level', 0.0, FALSE, (SELECT item_id FROM Inventory WHERE item_name = 'Cane Sugar Syrup')),
('50% Sugar', 'Sugar Level', 0.0, FALSE, (SELECT item_id FROM Inventory WHERE item_name = 'Cane Sugar Syrup')),
('30% Sugar', 'Sugar Level', 0.0, FALSE, (SELECT item_id FROM Inventory WHERE item_name = 'Cane Sugar Syrup')),
('0% Sugar', 'Sugar Level', 0.0, FALSE, NULL);

-- Ice Levels
INSERT INTO ModifierOption (name, category, price_adjustment, is_default)
VALUES 
('Regular Ice', 'Ice Level', 0.0, TRUE),
('Less Ice', 'Ice Level', 0.0, FALSE),
('No Ice', 'Ice Level', 0.0, FALSE),
('Extra Ice', 'Ice Level', 0.0, FALSE);

-- Cup Sizes
INSERT INTO ModifierOption (name, category, price_adjustment, is_default)
VALUES 
('Medium', 'Size', 0.0, TRUE),
('Large', 'Size', 0.75, FALSE);

-- Milk Options
INSERT INTO ModifierOption (name, category, price_adjustment, is_default, inventory_item_id)
VALUES 
('Whole Milk', 'Milk Type', 0.0, TRUE, (SELECT item_id FROM Inventory WHERE item_name = 'Milk')),
('Almond Milk', 'Milk Type', 0.50, FALSE, (SELECT item_id FROM Inventory WHERE item_name = 'Almond Milk')),
('Oat Milk', 'Milk Type', 0.50, FALSE, (SELECT item_id FROM Inventory WHERE item_name = 'Oat Milk')),
('Soy Milk', 'Milk Type', 0.50, FALSE, (SELECT item_id FROM Inventory WHERE item_name = 'Soy Milk'));


-- General Modifiers apply to ALL products
INSERT INTO ProductModifier (product_id, option_id)
SELECT p.product_id, m.option_id
FROM Product p
CROSS JOIN ModifierOption m
WHERE m.category IN ('Sugar Level', 'Ice Level', 'Topping', 'Size');

-- Milk Type Modifiers 
INSERT INTO ProductModifier (product_id, option_id)
SELECT p.product_id, m.option_id
FROM Product p
CROSS JOIN ModifierOption m
WHERE m.category = 'Milk Type'
  AND (
      p.category_name = 'Milk Tea Series' 
      OR p.category_name = 'Slush Series' 
      OR p.name LIKE '%Milk Tea%' 
      OR p.name LIKE '%Latte%'
  );
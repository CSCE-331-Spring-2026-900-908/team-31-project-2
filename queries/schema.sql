DROP TABLE IF EXISTS OrderModifier CASCADE;
DROP TABLE IF EXISTS OrderDetail CASCADE;
DROP TABLE IF EXISTS "Order" CASCADE;
DROP TABLE IF EXISTS ProductModifier CASCADE;
DROP TABLE IF EXISTS ModifierOption CASCADE;
DROP TABLE IF EXISTS ProductIngredient CASCADE;
DROP TABLE IF EXISTS Product CASCADE;
DROP TABLE IF EXISTS Inventory CASCADE;
DROP TABLE IF EXISTS Employee CASCADE;

CREATE TABLE Employee (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    pin_hash VARCHAR(255) NOT NULL
);

CREATE TABLE Inventory (
    item_id SERIAL PRIMARY KEY,
    item_name VARCHAR(255) NOT NULL,
    quantity FLOAT NOT NULL, 
    unit_type VARCHAR(20) NOT NULL,
    expiration_date TIMESTAMP
);

CREATE TABLE Product (
    product_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    base_price FLOAT NOT NULL,
    category_name VARCHAR(50), 
    color_code VARCHAR(20), 
    description TEXT,
    sticker_code VARCHAR(50),
    image_url VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE ProductIngredient (
    product_id INT REFERENCES Product(product_id),
    item_id INT REFERENCES Inventory(item_id),
    PRIMARY KEY (product_id, item_id)
);

CREATE TABLE ModifierOption (
    option_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(50),
    price_adjustment FLOAT NOT NULL,
    is_default BOOLEAN DEFAULT FALSE,
    inventory_item_id INT REFERENCES Inventory(item_id) 
);

CREATE TABLE ProductModifier (
    product_id INT REFERENCES Product(product_id),
    option_id INT REFERENCES ModifierOption(option_id),
    PRIMARY KEY (product_id, option_id)
);

CREATE TABLE "Order" (
    id SERIAL PRIMARY KEY,
    employee_id INT REFERENCES Employee(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_tax FLOAT DEFAULT 0.0,
    total_final FLOAT DEFAULT 0.0
);

CREATE TABLE OrderDetail (
    id SERIAL PRIMARY KEY,
    order_id INT REFERENCES "Order"(id),
    product_id INT REFERENCES Product(product_id),
    sold_price FLOAT NOT NULL,
    snapshot_name VARCHAR(255)
);

CREATE TABLE OrderModifier (
    id SERIAL PRIMARY KEY,
    order_detail_id INT REFERENCES OrderDetail(id),
    modifier_option_id INT REFERENCES ModifierOption(option_id),
    price_charged FLOAT NOT NULL,
    snapshot_name VARCHAR(255)
);


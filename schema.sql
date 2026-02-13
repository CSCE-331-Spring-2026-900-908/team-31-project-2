CREATE TABLE IF NOT EXISTS Employee (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    pin_hash VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Inventory (
    item_id SERIAL PRIMARY KEY,
    item_name VARCHAR(255) NOT NULL,
    quantity_on_hand FLOAT NOT NULL,
    unit_type VARCHAR(20) NOT NULL,
    expiration_date TIMESTAMP
);

CREATE TABLE IF NOT EXISTS Category (
    category_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    color_code VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS Product (
    product_id SERIAL PRIMARY KEY,
    category_id INT REFERENCES Category(category_id),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    sticker_code VARCHAR(50),
    image_url VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS ProductIngredient (
    product_id INT REFERENCES Product(product_id),
    item_id INT REFERENCES Inventory(item_id),
    amount_used FLOAT NOT NULL,
    PRIMARY KEY (product_id, item_id)
);

CREATE TABLE IF NOT EXISTS ProductSize (
    size_id SERIAL PRIMARY KEY,
    product_id INT REFERENCES Product(product_id),
    size_label VARCHAR(50) NOT NULL,
    base_price FLOAT NOT NULL,
    calories INT
);

CREATE TABLE IF NOT EXISTS ModifierGroup (
    group_id SERIAL PRIMARY KEY,
    group_name VARCHAR(255) NOT NULL,
    is_required BOOLEAN DEFAULT FALSE,
    min_selection INT DEFAULT 0,
    max_selection INT DEFAULT 1
);

CREATE TABLE IF NOT EXISTS ModifierOption (
    option_id SERIAL PRIMARY KEY,
    group_id INT REFERENCES ModifierGroup(group_id),
    name VARCHAR(255) NOT NULL,
    price_override FLOAT,
    is_default BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ProductModifierLink (
    product_id INT REFERENCES Product(product_id),
    group_id INT REFERENCES ModifierGroup(group_id),
    PRIMARY KEY (product_id, group_id)
);

CREATE TABLE IF NOT EXISTS "Order" (
    id SERIAL PRIMARY KEY,
    employee_id INT REFERENCES Employee(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_tax FLOAT DEFAULT 0.0,
    total_final FLOAT DEFAULT 0.0
);

CREATE TABLE IF NOT EXISTS OrderDetail (
    id SERIAL PRIMARY KEY,
    order_id INT REFERENCES "Order"(id),
    product_id INT REFERENCES Product(product_id),
    sold_price FLOAT NOT NULL,
    snapshot_name VARCHAR(255)
);

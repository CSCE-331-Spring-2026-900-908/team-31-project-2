CREATE TABLE employee (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    role VARCHAR(50),
    pin_hash VARCHAR(128)
);

CREATE TABLE order (
    id INT PRIMARY KEY,
    employee_id INT,
    created_at DATETIME,
    total_pre_tax FLOAT,
    total_final FLOAT
);

CREATE TABLE order_detail (
    id INT PRIMARY KEY,
    order_id INT,
    product_id INT,
    sold_price FLOAT,
    snapshot_name VARCHAR(100)
);

CREATE TABLE product (
    product_id INT PRIMARY KEY,
    category_id INT,
    name VARCHAR(100),
    description VARCHAR(300),
    sticker_code VARCHAR(50),
    image_url VARCHAR(128),
    is_active BOOLEAN
);

CREATE TABLE product_ingredient (
    product_id INT,
    item_id INT,
    amount_used FLOAT
);

CREATE TABLE inventory (
    item_id INT PRIMARY KEY,
    item_name VARCHAR(100),
    quantity_on_hand FLOAT,
    unit_type VARCHAR(100),
    expiration_date DATETIME
);

CREATE TABLE product_size (
    size_id INT PRIMARY KEY,
    product_id INT,
    size_label VARCHAR(20),
    base_price FLOAT,
    calories INT
);

CREATE TABLE category (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    color_code VARCHAR(20)
);

CREATE TABLE product_modifier_link (
    product_id INT,
    group_id INT
);

CREATE TABLE modifier_group (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    is_required BOOLEAN,
    min_selection INT,
    max_selection INT
);

CREATE TABLE modifier_option (
    id INT PRIMARY KEY,
    group_id INT,
    name VARCHAR(100),
    price_override FLOAT,
    is_default BOOLEAN
);
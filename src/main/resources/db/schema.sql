-- users
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at DATE
);

-- products
    CREATE TABLE IF NOT EXISTS products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2),
    stock INT,
    category VARCHAR(100),
    created_at DATE
);

-- orders
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT PRIMARY KEY,
    user_id BIGINT,
    total_amount DECIMAL(10,2),
    status VARCHAR(50),
    created_at DATE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- order_items
CREATE TABLE IF NOT EXISTS order_items (
   id BIGINT PRIMARY KEY,
   order_id BIGINT,
   user_id BIGINT,
   product_id BIGINT,
   quantity INT,
   price DECIMAL(10,2),
   FOREIGN KEY (order_id) REFERENCES orders(id),
   FOREIGN KEY (user_id) REFERENCES users(id),
   FOREIGN KEY (product_id) REFERENCES products(id)
);

-- shopping_cart
CREATE TABLE IF NOT EXISTS shopping_cart (
    id BIGINT PRIMARY KEY,
    user_id BIGINT UNIQUE,
    created_at DATE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- cart_items
CREATE TABLE IF NOT EXISTS cart_items (
  id BIGINT PRIMARY KEY,
  cart_id BIGINT,
  user_id BIGINT,
  product_id BIGINT,
  quantity INT,
  FOREIGN KEY (cart_id) REFERENCES shopping_cart(id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (product_id) REFERENCES products(id)
);
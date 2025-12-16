-- =========================================
-- 1. 客户表 (Customer)
-- =========================================
CREATE TABLE customer (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    email VARCHAR(255)
);

-- =========================================
-- 2. 餐厅表 (Restaurant)
-- =========================================
CREATE TABLE restaurant (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    phone VARCHAR(50)
);

-- =========================================
-- 3. 餐桌表 (Restaurant Table)
-- =========================================
CREATE TABLE restaurant_table (
    id BIGSERIAL PRIMARY KEY,
    table_number INT NOT NULL,
    capacity INT NOT NULL,
    available BOOLEAN DEFAULT TRUE,
    restaurant_id BIGINT,
    CONSTRAINT fk_table_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurant(id)
);

-- =========================================
-- 4. 用户表 (Users) - 必须要有这个，否则 Security 会报错
-- =========================================
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

-- =========================================
-- 5. 预订表 (Reservation)
-- =========================================
CREATE TABLE reservation (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT,
    table_id BIGINT,
    reservation_time TIMESTAMP,
    duration_minutes INT DEFAULT 60,
    status VARCHAR(50) DEFAULT 'PENDING',
    
    CONSTRAINT fk_reservation_customer FOREIGN KEY (customer_id) REFERENCES customer(id),
    CONSTRAINT fk_reservation_table FOREIGN KEY (table_id) REFERENCES restaurant_table(id)
);

-- =========================================
-- 6. 初始管理员数据 (让你可以登录)
-- 用户名: admin, 密码: password
-- =========================================
INSERT INTO users (username, password, role) 
VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ROLE_ADMIN');
-- =========================================
-- 1. 客户表 (Customer)
-- =========================================
CREATE TABLE customer (
    id BIGSERIAL PRIMARY KEY, -- 改为 BIGSERIAL
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    email VARCHAR(255)
);

-- =========================================
-- 2. 餐厅表 (Restaurant)
-- =========================================
CREATE TABLE restaurant (
    id BIGSERIAL PRIMARY KEY, -- 改为 BIGSERIAL
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    phone VARCHAR(50)
);

-- =========================================
-- 3. 餐桌表 (Restaurant Table)
-- =========================================
CREATE TABLE restaurant_table (
    id BIGSERIAL PRIMARY KEY, -- 改为 BIGSERIAL
    table_number INT NOT NULL,
    capacity INT NOT NULL,
    available BOOLEAN DEFAULT TRUE,
    restaurant_id BIGINT,
    -- 建议给外键起个名字，方便报错时排查
    CONSTRAINT fk_table_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurant(id)
);

-- =========================================
-- 4. 预订表 (Reservation)
-- =========================================
CREATE TABLE reservation (
    id BIGSERIAL PRIMARY KEY, -- 改为 BIGSERIAL
    customer_id BIGINT,
    table_id BIGINT,
    reservation_time TIMESTAMP,
    duration_minutes INT DEFAULT 60,
    status VARCHAR(50) DEFAULT 'PENDING',
    
    CONSTRAINT fk_reservation_customer FOREIGN KEY (customer_id) REFERENCES customer(id),
    CONSTRAINT fk_reservation_table FOREIGN KEY (table_id) REFERENCES restaurant_table(id)
);
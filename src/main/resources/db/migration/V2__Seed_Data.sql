-- 插入客户
INSERT INTO customer(name, phone, email) VALUES('Alice', '12345678', 'alice@example.com');
INSERT INTO customer(name, phone, email) VALUES('Bob', '87654321', 'bob@example.com');
INSERT INTO customer(name, phone, email) VALUES('Charlie', '13572468', 'charlie@example.com');

-- 插入餐厅
INSERT INTO restaurant(name, address, phone) VALUES('McDonald', 'Street 1', '12345678');
INSERT INTO restaurant(name, address, phone) VALUES('KFC', 'Street 2', '87654321');
INSERT INTO restaurant(name, address, phone) VALUES('Burger King', 'Street 3', '13572468');

-- 插入餐桌
INSERT INTO restaurant_table(table_number, capacity, available, restaurant_id) VALUES(1, 4, TRUE, 1);
INSERT INTO restaurant_table(table_number, capacity, available, restaurant_id) VALUES(2, 2, TRUE, 1);
INSERT INTO restaurant_table(table_number, capacity, available, restaurant_id) VALUES(1, 6, TRUE, 2);
INSERT INTO restaurant_table(table_number, capacity, available, restaurant_id) VALUES(2, 4, TRUE, 2);
INSERT INTO restaurant_table(table_number, capacity, available, restaurant_id) VALUES(1, 4, TRUE, 3);

-- 插入预订 (注意：Postgres 对时间格式要求比较严格，最好用单引号)
INSERT INTO reservation(customer_id, table_id, reservation_time, duration_minutes, status) 
VALUES(1, 1, '2025-12-03 18:30:00', 90, 'CONFIRMED');

INSERT INTO reservation(customer_id, table_id, reservation_time, duration_minutes, status) 
VALUES(2, 3, '2025-12-03 19:00:00', 60, 'PENDING');

INSERT INTO reservation(customer_id, table_id, reservation_time, duration_minutes, status) 
VALUES(3, 5, '2025-12-03 20:00:00', 120, 'CONFIRMED');
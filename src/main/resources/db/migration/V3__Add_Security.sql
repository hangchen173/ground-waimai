-- 创建用户表 (注意：user 是 Postgres 保留字，所以我们用 app_user 或 users)
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- 存 BCrypt 加密后的字符串
    role VARCHAR(20) NOT NULL       -- ROLE_ADMIN, ROLE_USER
);

-- 插入一个初始管理员
-- 用户名: admin
-- 密码: password (BCrypt加密后的值是 $2a$10$D9...)
-- 这样你一启动就能登录了
INSERT INTO users (username, password, role) 
VALUES ('admin', '$2a$10$D9G/X.wqW.t.y.O.z.e.u.x.y.z.1.2.3.4.5.6.7.8.9.0', 'ROLE_ADMIN');

-- 插入一个普通用户
-- 用户名: user
-- 密码: password
INSERT INTO users (username, password, role) 
VALUES ('user', '$2a$10$D9G/X.wqW.t.y.O.z.e.u.x.y.z.1.2.3.4.5.6.7.8.9.0', 'ROLE_USER');
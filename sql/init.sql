-- ===============================================
-- 校园快递代取平台 - 数据库初始化脚本（全量表）
-- 数据库: PostgreSQL
-- 地址: 10.125.205.180:15432
-- 库名: express_db
-- 用户: omm
-- ===============================================

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id          BIGSERIAL       PRIMARY KEY,
    username    VARCHAR(64)     NOT NULL UNIQUE,
    password    VARCHAR(256)    NOT NULL,
    real_name   VARCHAR(32),
    phone       VARCHAR(20),
    student_id  VARCHAR(32),
    role        VARCHAR(32)     NOT NULL DEFAULT 'USER',
    created_at  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 索引
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_role     ON users(role);

COMMENT ON TABLE  users             IS '用户表';
COMMENT ON COLUMN users.id          IS '主键';
COMMENT ON COLUMN users.username    IS '用户名（登录用）';
COMMENT ON COLUMN users.password    IS '密码';
COMMENT ON COLUMN users.real_name   IS '真实姓名';
COMMENT ON COLUMN users.phone       IS '手机号';
COMMENT ON COLUMN users.student_id  IS '学号';
COMMENT ON COLUMN users.role        IS '角色: USER/RUNNER/ADMIN';
COMMENT ON COLUMN users.created_at  IS '创建时间';
COMMENT ON COLUMN users.updated_at  IS '更新时间';

-- 跑腿员表
CREATE TABLE IF NOT EXISTS runner (
    id              BIGSERIAL       PRIMARY KEY,
    user_id         BIGINT          NOT NULL,
    real_name       VARCHAR(32)     NOT NULL,
    phone           VARCHAR(20)     NOT NULL,
    student_id      VARCHAR(32)     NOT NULL,
    id_card_front   VARCHAR(512),
    id_card_back    VARCHAR(512),
    status          VARCHAR(32)     NOT NULL DEFAULT 'PENDING',
    reject_reason   VARCHAR(256),
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_runner_user_id ON runner(user_id);
CREATE INDEX IF NOT EXISTS idx_runner_status  ON runner(status);

COMMENT ON TABLE  runner               IS '跑腿员表';
COMMENT ON COLUMN runner.id            IS '主键';
COMMENT ON COLUMN runner.user_id       IS '关联的用户ID';
COMMENT ON COLUMN runner.real_name     IS '真实姓名';
COMMENT ON COLUMN runner.phone         IS '联系电话';
COMMENT ON COLUMN runner.student_id    IS '学号';
COMMENT ON COLUMN runner.id_card_front IS '身份证正面照片URL';
COMMENT ON COLUMN runner.id_card_back  IS '身份证背面照片URL';
COMMENT ON COLUMN runner.status        IS '审核状态: PENDING/APPROVED/REJECTED';
COMMENT ON COLUMN runner.reject_reason IS '拒绝原因';
COMMENT ON COLUMN runner.created_at    IS '创建时间';
COMMENT ON COLUMN runner.updated_at    IS '更新时间';

-- 订单表
CREATE TABLE IF NOT EXISTS orders (
    id                  BIGSERIAL       PRIMARY KEY,
    publisher_id        BIGINT          NOT NULL,
    runner_id           BIGINT,
    station_name        VARCHAR(128)    NOT NULL,
    building            VARCHAR(128)    NOT NULL,
    reward              DECIMAL(10,2)   NOT NULL,
    item_info           VARCHAR(512),
    tracking_number     VARCHAR(64),
    status              VARCHAR(32)     NOT NULL DEFAULT 'PENDING',
    evaluation          INTEGER,
    evaluation_content  VARCHAR(512),
    timeout_at          TIMESTAMP,
    created_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_orders_publisher_id ON orders(publisher_id);
CREATE INDEX IF NOT EXISTS idx_orders_runner_id    ON orders(runner_id);
CREATE INDEX IF NOT EXISTS idx_orders_status       ON orders(status);

COMMENT ON TABLE  orders                    IS '订单表';
COMMENT ON COLUMN orders.id                 IS '主键';
COMMENT ON COLUMN orders.publisher_id       IS '发布者用户ID';
COMMENT ON COLUMN orders.runner_id          IS '接单跑腿员用户ID';
COMMENT ON COLUMN orders.station_name       IS '快递驿站名称';
COMMENT ON COLUMN orders.building           IS '配送楼栋';
COMMENT ON COLUMN orders.reward             IS '酬劳金额';
COMMENT ON COLUMN orders.item_info          IS '物品信息描述';
COMMENT ON COLUMN orders.tracking_number    IS '快递单号';
COMMENT ON COLUMN orders.status             IS '订单状态: PENDING/ACCEPTED/DELIVERING/DELIVERED/CONFIRMED/CANCELLED';
COMMENT ON COLUMN orders.evaluation         IS '评价星级 (1-5)';
COMMENT ON COLUMN orders.evaluation_content IS '评价内容';
COMMENT ON COLUMN orders.timeout_at         IS '超时自动取消时间';
COMMENT ON COLUMN orders.created_at         IS '创建时间';
COMMENT ON COLUMN orders.updated_at         IS '更新时间';

-- 插入测试用户数据
INSERT INTO users (username, password, real_name, phone, student_id, role) VALUES
('zhangsan', '123456', '张三', '13800138001', '20240001', 'USER'),
('lisi',     '123456', '李四', '13800138002', '20240002', 'USER'),
('wangwu',   '123456', '王五', '13800138003', '20240003', 'USER')
ON CONFLICT (username) DO NOTHING;

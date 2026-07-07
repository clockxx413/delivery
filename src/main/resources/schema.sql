-- ============================================
-- 校园快递代取互助平台 - 数据库初始化脚本
-- PostgreSQL
-- ============================================

-- 用户表
CREATE TABLE IF NOT EXISTS "user" (
    id          BIGSERIAL PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,   -- 用户名（登录用）
    password    VARCHAR(100) NOT NULL,           -- 密码
    real_name   VARCHAR(50)  NOT NULL,           -- 真实姓名
    student_id  VARCHAR(20)  NOT NULL UNIQUE,   -- 学号
    phone       VARCHAR(20)  NOT NULL,           -- 手机号
    role        VARCHAR(20)  DEFAULT 'USER',     -- 角色: USER/RUNNER/ADMIN
    dormitory   VARCHAR(100),                    -- 楼栋/宿舍
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 订单表
CREATE TABLE IF NOT EXISTS "order" (
    id               BIGSERIAL PRIMARY KEY,
    order_no         VARCHAR(30)  NOT NULL UNIQUE,   -- 订单编号
    publisher_id     BIGINT       NOT NULL,           -- 发布者用户ID
    runner_id        BIGINT,                           -- 接单跑腿员ID
    station          VARCHAR(100) NOT NULL,           -- 快递驿站名称
    building         VARCHAR(100) NOT NULL,           -- 送达楼栋
    reward           DECIMAL(10,2) NOT NULL,          -- 酬劳金额
    item_description VARCHAR(500) NOT NULL,           -- 物品描述
    item_category    VARCHAR(50),                      -- 物品分类
    status           VARCHAR(20)  DEFAULT 'PENDING',  -- PENDING/ACCEPTED/IN_PROGRESS/COMPLETED/CANCELLED
    evaluation       VARCHAR(500),                     -- 评价内容
    evaluation_score INTEGER,                          -- 评价分数 1-5
    cancel_reason    VARCHAR(200),                     -- 取消原因
    accepted_at      TIMESTAMP,                        -- 接单时间
    completed_at     TIMESTAMP,                        -- 完成时间
    cancelled_at     TIMESTAMP,                        -- 取消时间
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 索引
CREATE INDEX IF NOT EXISTS idx_order_publisher_id ON "order"(publisher_id);
CREATE INDEX IF NOT EXISTS idx_order_runner_id ON "order"(runner_id);
CREATE INDEX IF NOT EXISTS idx_order_status ON "order"(status);
CREATE INDEX IF NOT EXISTS idx_order_created_at ON "order"(created_at);

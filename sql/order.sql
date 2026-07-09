-- ===============================================
-- 校园快递代取平台 - 订单模块建表脚本
-- 数据库: PostgreSQL
-- 成员D - 周三任务
-- ===============================================

-- 订单表
CREATE TABLE IF NOT EXISTS "order" (
    id              BIGSERIAL       PRIMARY KEY,
    order_no        VARCHAR(32)     NOT NULL UNIQUE,
    publisher_id    BIGINT          NOT NULL,
    runner_id       BIGINT,
    express_company VARCHAR(64)     NOT NULL,
    express_code    VARCHAR(128)    NOT NULL,
    pickup_address  VARCHAR(256)   NOT NULL,
    delivery_address VARCHAR(256)  NOT NULL,
    recipient_phone VARCHAR(20)     NOT NULL,
    description     VARCHAR(512),
    tip             DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    status          VARCHAR(32)     NOT NULL DEFAULT 'PENDING',
    cancel_reason   VARCHAR(256),
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 索引
CREATE INDEX IF NOT EXISTS idx_order_order_no      ON "order"(order_no);
CREATE INDEX IF NOT EXISTS idx_order_publisher_id  ON "order"(publisher_id);
CREATE INDEX IF NOT EXISTS idx_order_runner_id     ON "order"(runner_id);
CREATE INDEX IF NOT EXISTS idx_order_status        ON "order"(status);
CREATE INDEX IF NOT EXISTS idx_order_created_at    ON "order"(created_at DESC);

COMMENT ON TABLE  "order"                  IS '快递订单表';
COMMENT ON COLUMN "order".id               IS '主键';
COMMENT ON COLUMN "order".order_no         IS '订单编号（唯一）';
COMMENT ON COLUMN "order".publisher_id     IS '发布者用户ID';
COMMENT ON COLUMN "order".runner_id        IS '接单跑腿员ID';
COMMENT ON COLUMN "order".express_company  IS '快递公司名称';
COMMENT ON COLUMN "order".express_code     IS '取件码/快递单号';
COMMENT ON COLUMN "order".pickup_address   IS '取件地址';
COMMENT ON COLUMN "order".delivery_address IS '送达地址';
COMMENT ON COLUMN "order".recipient_phone  IS '收件人手机号';
COMMENT ON COLUMN "order".description      IS '包裹描述';
COMMENT ON COLUMN "order".tip              IS '小费/报酬金额';
COMMENT ON COLUMN "order".status           IS '订单状态: PENDING/ACCEPTED/DELIVERING/COMPLETED/CANCELLED';
COMMENT ON COLUMN "order".cancel_reason    IS '取消原因';
COMMENT ON COLUMN "order".created_at       IS '创建时间';
COMMENT ON COLUMN "order".updated_at       IS '更新时间';

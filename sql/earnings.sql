-- ===============================================
-- 校园快递代取平台 - 收益模块建表脚本
-- 数据库: PostgreSQL
-- ===============================================

-- 收益记录表
CREATE TABLE IF NOT EXISTS earnings (
    id              BIGSERIAL       PRIMARY KEY,
    runner_id       BIGINT          NOT NULL,
    order_id        BIGINT          NOT NULL,
    amount          DECIMAL(10,2)   NOT NULL DEFAULT 0.00,
    type            VARCHAR(32)     NOT NULL DEFAULT 'DELIVERY_FEE',
    status          VARCHAR(32)     NOT NULL DEFAULT 'PENDING',
    description     VARCHAR(256),
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 索引
CREATE INDEX IF NOT EXISTS idx_earnings_runner_id ON earnings(runner_id);
CREATE INDEX IF NOT EXISTS idx_earnings_order_id  ON earnings(order_id);
CREATE INDEX IF NOT EXISTS idx_earnings_status    ON earnings(status);

COMMENT ON TABLE  earnings               IS '收益记录表';
COMMENT ON COLUMN earnings.id            IS '主键';
COMMENT ON COLUMN earnings.runner_id     IS '跑腿员ID';
COMMENT ON COLUMN earnings.order_id      IS '订单ID';
COMMENT ON COLUMN earnings.amount        IS '收益金额';
COMMENT ON COLUMN earnings.type          IS '收益类型: DELIVERY_FEE-配送费, TIP-小费, BONUS-奖励';
COMMENT ON COLUMN earnings.status        IS '状态: PENDING-待结算, SETTLED-已结算, WITHDRAWN-已提现';
COMMENT ON COLUMN earnings.description   IS '描述';
COMMENT ON COLUMN earnings.created_at    IS '创建时间';
COMMENT ON COLUMN earnings.updated_at    IS '更新时间';

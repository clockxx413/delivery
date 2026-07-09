-- ===============================================
-- 校园快递代取平台 - 跑腿员模块建表脚本
-- 数据库: PostgreSQL
-- 端口: 15432, 库名: express_db
-- ===============================================

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

-- 索引
CREATE INDEX IF NOT EXISTS idx_runner_user_id  ON runner(user_id);
CREATE INDEX IF NOT EXISTS idx_runner_status   ON runner(status);

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

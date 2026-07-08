-- 跑腿员表（PostgreSQL / openGauss 语法）
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
CREATE INDEX idx_runner_user_id ON runner(user_id);
CREATE INDEX idx_runner_status ON runner(status);

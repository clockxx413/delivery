-- ===============================================
-- 校园快递代取平台 - 评价模块建表脚本
-- 数据库: PostgreSQL
-- 成员D - 周四任务
-- ===============================================

-- 评价表
CREATE TABLE IF NOT EXISTS review (
    id              BIGSERIAL       PRIMARY KEY,
    order_id        BIGINT          NOT NULL,
    reviewer_id     BIGINT          NOT NULL,
    target_user_id  BIGINT          NOT NULL,
    score           INT             NOT NULL CHECK (score >= 1 AND score <= 5),
    content         VARCHAR(512),
    type            VARCHAR(32)     NOT NULL,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 索引
CREATE INDEX IF NOT EXISTS idx_review_order_id       ON review(order_id);
CREATE INDEX IF NOT EXISTS idx_review_reviewer_id    ON review(reviewer_id);
CREATE INDEX IF NOT EXISTS idx_review_target_user_id ON review(target_user_id);
CREATE INDEX IF NOT EXISTS idx_review_type           ON review(type);

COMMENT ON TABLE  review                 IS '评价表';
COMMENT ON COLUMN review.id              IS '主键';
COMMENT ON COLUMN review.order_id        IS '关联的订单ID';
COMMENT ON COLUMN review.reviewer_id     IS '评价者用户ID';
COMMENT ON COLUMN review.target_user_id  IS '被评价用户ID';
COMMENT ON COLUMN review.score           IS '评分（1-5星）';
COMMENT ON COLUMN review.content         IS '评价内容';
COMMENT ON COLUMN review.type            IS '评价类型: RUNNER_REVIEW/PUBLISHER_REVIEW';
COMMENT ON COLUMN review.created_at      IS '创建时间';

-- ===============================================
-- 校园快递代取平台 - 跑腿员模块建表脚本
-- 数据库: MySQL
-- ===============================================

-- 先创建数据库（如未建）
CREATE DATABASE IF NOT EXISTS express_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE express_db;

-- 跑腿员表
CREATE TABLE IF NOT EXISTS runner (
    id              BIGINT          PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    user_id         BIGINT          NOT NULL               COMMENT '关联的用户ID',
    real_name       VARCHAR(32)     NOT NULL               COMMENT '真实姓名',
    phone           VARCHAR(20)     NOT NULL               COMMENT '联系电话',
    student_id      VARCHAR(32)     NOT NULL               COMMENT '学号',
    id_card_front   VARCHAR(512)                           COMMENT '身份证正面照片URL',
    id_card_back    VARCHAR(512)                           COMMENT '身份证背面照片URL',
    status          VARCHAR(32)     NOT NULL DEFAULT 'PENDING' COMMENT '审核状态: PENDING/APPROVED/REJECTED',
    reject_reason   VARCHAR(256)                           COMMENT '拒绝原因',
    created_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='跑腿员表';

-- 索引
CREATE INDEX idx_runner_user_id ON runner(user_id);
CREATE INDEX idx_runner_status  ON runner(status);

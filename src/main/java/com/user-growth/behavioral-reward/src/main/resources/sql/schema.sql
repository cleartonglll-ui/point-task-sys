-- 行为积分奖励系统数据库初始化脚本

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `behavioral_reward` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `behavioral_reward`;

-- ==========================================
-- 1. 用户积分账户表
-- ==========================================
DROP TABLE IF EXISTS `user_point_account`;
CREATE TABLE `user_point_account` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    `user_name` VARCHAR(64) NOT NULL COMMENT '用户昵称',
    `total_points` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '总积分',
    `available_points` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '可用积分',
    `frozen_points` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '冻结积分',
    `total_earned` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '累计获得积分',
    `total_spent` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '累计消费积分',
    `level` TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '会员等级',
    `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标记 0:未删除 1:已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    KEY `idx_level` (`level`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户积分账户表';

-- ==========================================
-- 2. 积分明细流水表
-- ==========================================
DROP TABLE IF EXISTS `point_detail`;
CREATE TABLE `point_detail` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    `point_type` TINYINT UNSIGNED NOT NULL COMMENT '积分类型 1:获得 2:消费 3:冻结 4:解冻',
    `biz_type` VARCHAR(32) NOT NULL COMMENT '业务类型 签到/作业/视频/互动/答题/邀请/兑换',
    `biz_id` VARCHAR(64) DEFAULT NULL COMMENT '业务ID',
    `task_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '任务ID',
    `point_amount` INT NOT NULL COMMENT '积分变动数量 正数为增加 负数为减少',
    `before_points` INT NOT NULL COMMENT '变动前积分',
    `after_points` INT NOT NULL COMMENT '变动后积分',
    `remark` VARCHAR(256) DEFAULT NULL COMMENT '备注',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_biz_type` (`biz_type`),
    KEY `idx_task_id` (`task_id`),
    KEY `idx_created_at` (`created_at`),
    KEY `idx_user_time` (`user_id`, `created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='积分明细流水表';

-- ==========================================
-- 3. 任务规则配置表
-- ==========================================
DROP TABLE IF EXISTS `task_rule`;
CREATE TABLE `task_rule` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `task_code` VARCHAR(32) NOT NULL COMMENT '任务编码',
    `task_name` VARCHAR(64) NOT NULL COMMENT '任务名称',
    `task_type` TINYINT UNSIGNED NOT NULL COMMENT '任务类型 1:签到 2:作业 3:视频 4:互动 5:答题 6:邀请',
    `point_award` INT UNSIGNED NOT NULL COMMENT '奖励积分数',
    `daily_limit` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT '每日完成次数限制',
    `status` TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态 0:禁用 1:启用',
    `start_time` DATETIME DEFAULT NULL COMMENT '开始时间',
    `end_time` DATETIME DEFAULT NULL COMMENT '结束时间',
    `priority` TINYINT UNSIGNED NOT NULL DEFAULT 10 COMMENT '优先级 数字越大优先级越高',
    `description` VARCHAR(256) DEFAULT NULL COMMENT '任务描述',
    `target_tags` VARCHAR(256) DEFAULT NULL COMMENT '目标用户标签 多个用逗号分隔',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_task_code` (`task_code`),
    KEY `idx_task_type` (`task_type`),
    KEY `idx_status` (`status`),
    KEY `idx_priority` (`priority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务规则配置表';

-- ==========================================
-- 4. 奖励配置表
-- ==========================================
DROP TABLE IF EXISTS `reward_config`;
CREATE TABLE `reward_config` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `reward_name` VARCHAR(64) NOT NULL COMMENT '奖励名称',
    `reward_type` TINYINT UNSIGNED NOT NULL COMMENT '奖励类型 1:学习资料 2:课程优惠券 3:虚拟勋章 4:实物奖品',
    `point_cost` INT UNSIGNED NOT NULL COMMENT '兑换所需积分',
    `stock` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '库存数量',
    `user_limit` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT '每人限兑数量',
    `status` TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态 0:下架 1:上架',
    `image_url` VARCHAR(256) DEFAULT NULL COMMENT '图片URL',
    `description` TEXT DEFAULT NULL COMMENT '奖励描述',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_reward_type` (`reward_type`),
    KEY `idx_status` (`status`),
    KEY `idx_point_cost` (`point_cost`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='奖励配置表';

-- ==========================================
-- 5. 奖励兑换记录表
-- ==========================================
DROP TABLE IF EXISTS `reward_claim_record`;
CREATE TABLE `reward_claim_record` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    `user_name` VARCHAR(64) NOT NULL COMMENT '用户昵称',
    `reward_id` BIGINT UNSIGNED NOT NULL COMMENT '奖励ID',
    `reward_name` VARCHAR(64) NOT NULL COMMENT '奖励名称',
    `point_cost` INT UNSIGNED NOT NULL COMMENT '消耗积分',
    `claim_status` TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '兑换状态 1:待发货 2:已发货 3:已完成',
    `delivery_time` DATETIME DEFAULT NULL COMMENT '发货时间',
    `address` VARCHAR(256) DEFAULT NULL COMMENT '收货地址',
    `remark` VARCHAR(256) DEFAULT NULL COMMENT '备注',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_reward_id` (`reward_id`),
    KEY `idx_status` (`claim_status`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='奖励兑换记录表';

-- ==========================================
-- 6. 用户任务完成记录表
-- ==========================================
DROP TABLE IF EXISTS `user_task_record`;
CREATE TABLE `user_task_record` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    `task_id` BIGINT UNSIGNED NOT NULL COMMENT '任务ID',
    `task_code` VARCHAR(32) NOT NULL COMMENT '任务编码',
    `task_name` VARCHAR(64) NOT NULL COMMENT '任务名称',
    `complete_date` DATE NOT NULL COMMENT '完成日期',
    `complete_count` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT '完成次数',
    `point_awarded` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '已奖励积分',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_task_id` (`task_id`),
    KEY `idx_complete_date` (`complete_date`),
    UNIQUE KEY `uk_user_task_date` (`user_id`, `task_id`, `complete_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户任务完成记录表';

-- ==========================================
-- 7. 用户标签表
-- ==========================================
DROP TABLE IF EXISTS `user_tag`;
CREATE TABLE `user_tag` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    `tag_code` VARCHAR(32) NOT NULL COMMENT '标签编码',
    `tag_name` VARCHAR(64) NOT NULL COMMENT '标签名称',
    `tag_value` VARCHAR(128) DEFAULT NULL COMMENT '标签值',
    `tag_type` TINYINT UNSIGNED NOT NULL COMMENT '标签类型 1:系统标签 2:行为标签 3:运营标签',
    `expire_time` DATETIME DEFAULT NULL COMMENT '过期时间 NULL为永久',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_tag_code` (`tag_code`),
    KEY `idx_tag_type` (`tag_type`),
    KEY `idx_expire_time` (`expire_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户标签表';

-- ==========================================
-- 8. 精细化运营任务推荐表
-- ==========================================
DROP TABLE IF EXISTS `task_recommendation`;
CREATE TABLE `task_recommendation` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    `task_id` BIGINT UNSIGNED NOT NULL COMMENT '任务ID',
    `recommend_reason` VARCHAR(256) DEFAULT NULL COMMENT '推荐理由',
    `priority` TINYINT UNSIGNED NOT NULL DEFAULT 10 COMMENT '推荐优先级',
    `show_count` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '展示次数',
    `click_count` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '点击次数',
    `complete_count` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '完成次数',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_task_id` (`task_id`),
    KEY `idx_priority` (`priority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='精细化运营任务推荐表';

-- ==========================================
-- 初始化数据
-- ==========================================

-- 插入任务规则配置
INSERT INTO `task_rule` (`task_code`, `task_name`, `task_type`, `point_award`, `daily_limit`, `status`, `priority`, `description`) VALUES
('SIGN_IN', '上课签到', 1, 10, 1, 1, 90, '每节课签到可获得10积分'),
('COMPLETE_HOMEWORK', '完成作业', 2, 50, 3, 1, 80, '每次完成作业可获得50积分，每天最多3次'),
('WATCH_VIDEO', '观看视频', 3, 20, 10, 1, 70, '每观看一个完整视频可获得20积分'),
('CLASS_INTERACTION', '课堂互动', 4, 15, 20, 1, 60, '每次课堂互动可获得15积分'),
('QUIZ', '课后答题', 5, 30, 5, 1, 85, '每次完成课后答题可获得30积分，每天最多5次'),
('INVITE_FRIEND', '邀请好友', 6, 100, 10, 1, 95, '每邀请一位好友注册可获得100积分');

-- 插入奖励配置
INSERT INTO `reward_config` (`reward_name`, `reward_type`, `point_cost`, `stock`, `user_limit`, `status`, `description`) VALUES
('学习笔记模板', 1, 50, 99999, 10, 1, '优质学习笔记模板，帮助整理知识点'),
('课程5折优惠券', 2, 200, 1000, 5, 1, '任意课程5折优惠券，有效期30天'),
('学习先锋勋章', 3, 300, 99999, 1, 1, '虚拟勋章，展示在学习中心'),
('课程资料包', 1, 100, 500, 20, 1, '精选课程资料电子版'),
('课程8折优惠券', 2, 100, 2000, 10, 1, '任意课程8折优惠券，有效期30天'),
('学习达人勋章', 3, 500, 99999, 1, 1, '高级虚拟勋章，彰显学习成就'),
('精美笔记本', 4, 1000, 100, 2, 1, '品牌精美实体笔记本'),
('课程免费兑换券', 2, 500, 50, 3, 1, '指定课程免费兑换券');

-- ==========================================
-- 完成初始化
-- ==========================================
SELECT 'Database initialization completed successfully!' AS message;

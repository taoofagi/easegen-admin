-- ============================================
-- 为课程表添加平台类型字段
-- ============================================
-- 说明：为 digitalcourse_courses 表新增 platform_type 字段
-- 执行时间：2025-01-07
-- ============================================

-- 新增平台类型字段（1-2D easegen，2-3D 魔珐星云）
ALTER TABLE `digitalcourse_courses`
ADD COLUMN `platform_type` TINYINT DEFAULT 1 COMMENT '平台类型：1-2D（easegen），2-3D（魔珐星云）' AFTER `page_mode`;

-- 为现有数据设置默认值（确保向后兼容）
UPDATE `digitalcourse_courses`
SET `platform_type` = 1
WHERE `platform_type` IS NULL;

-- ============================================
-- 升级完成
-- ============================================
-- 注意事项：
-- 1. 现有数据的 platform_type 默认为 1（2D模式），保持向后兼容
-- 2. 建议在业务低峰期执行此脚本
-- ============================================

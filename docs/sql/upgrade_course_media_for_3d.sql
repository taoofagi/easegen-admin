-- ============================================
-- 数字人视频合成表结构升级脚本（支持3D）
-- ============================================
-- 说明：为 digitalcourse_course_media 表新增字段，支持魔珐星云3D数字人视频合成
-- 执行时间：2025-01-06
-- ============================================

-- 1. 新增平台类型字段（1-2D easegen，2-3D 魔珐星云）
ALTER TABLE `digitalcourse_course_media` 
ADD COLUMN `platform_type` TINYINT DEFAULT 1 COMMENT '平台类型：1-2D（easegen），2-3D（魔珐星云）' AFTER `id`;

-- 2. 新增平台任务ID字段（存储第三方平台的任务ID）
ALTER TABLE `digitalcourse_course_media` 
ADD COLUMN `platform_task_id` VARCHAR(64) COMMENT '平台任务ID（如魔珐星云的task_id）' AFTER `platform_type`;

-- 3. 新增3D数字人相关字段
ALTER TABLE `digitalcourse_course_media` 
ADD COLUMN `look_name` VARCHAR(128) COMMENT '数字人形象名称（3D使用）' AFTER `platform_task_id`,
ADD COLUMN `tts_vcn_name` VARCHAR(128) COMMENT '音色名称（3D使用）' AFTER `look_name`,
ADD COLUMN `studio_name` VARCHAR(128) COMMENT '演播室名称（3D使用）' AFTER `tts_vcn_name`,
ADD COLUMN `sub_title` VARCHAR(10) DEFAULT 'on' COMMENT '字幕开关：on/off（3D使用）' AFTER `studio_name`,
ADD COLUMN `if_aigc_mark` TINYINT DEFAULT 1 COMMENT '是否显示AI生成标识：0-否，1-是（3D使用）' AFTER `sub_title`,
ADD COLUMN `parse_ppt_file_name` VARCHAR(128) COMMENT 'PPT解析文件名（3D使用）' AFTER `if_aigc_mark`;

-- 4. 新增3D合成状态相关字段
ALTER TABLE `digitalcourse_course_media` 
ADD COLUMN `synth_status` VARCHAR(20) COMMENT '合成状态：not_send/waiting/processing/finished/error/cancel（3D使用）' AFTER `parse_ppt_file_name`,
ADD COLUMN `synth_start_time` DATETIME COMMENT '合成开始时间（3D使用）' AFTER `synth_status`,
ADD COLUMN `synth_finish_time` DATETIME COMMENT '合成完成时间（3D使用）' AFTER `synth_start_time`;

-- 5. 为现有数据设置默认值（确保向后兼容）
UPDATE `digitalcourse_course_media` 
SET `platform_type` = 1 
WHERE `platform_type` IS NULL;

-- 6. 创建索引（优化查询性能）
CREATE INDEX `idx_platform_task_id` ON `digitalcourse_course_media` (`platform_task_id`);

-- ============================================
-- 升级完成
-- ============================================
-- 注意事项：
-- 1. 现有数据的 platform_type 默认为 1（2D模式），保持向后兼容
-- 2. 3D相关字段在2D模式下可以为空
-- 3. 建议在业务低峰期执行此脚本
-- ============================================


SET FOREIGN_KEY_CHECKS=0;

ALTER TABLE `easegen`.`digitalcourse_course_scene_audios` ADD INDEX `digitalcourse_course_scene_audios_scene_id_IDX`(`scene_id`) USING BTREE;

ALTER TABLE `easegen`.`digitalcourse_course_scene_backgrounds` ADD INDEX `digitalcourse_course_scene_backgrounds_scene_id_IDX`(`scene_id`) USING BTREE;

ALTER TABLE `easegen`.`digitalcourse_course_scene_components` ADD INDEX `digitalcourse_course_scene_components_scene_id_IDX`(`scene_id`) USING BTREE;

ALTER TABLE `easegen`.`digitalcourse_course_scene_texts` ADD INDEX `digitalcourse_course_scene_texts_scene_id_IDX`(`scene_id`) USING BTREE;

ALTER TABLE `easegen`.`digitalcourse_course_scene_voices` ADD INDEX `digitalcourse_course_scene_voices_scene_id_IDX`(`scene_id`) USING BTREE;

CREATE TABLE `easegen`.`digitalcourse_template`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `show_background` tinyint(0) NOT NULL COMMENT '0 不显示，1显示',
  `show_digital_human` tinyint(0) NOT NULL COMMENT '0 不显示，1显示',
  `show_ppt` tinyint(0) NOT NULL COMMENT '0 不显示，1显示',
  `ppt_w` decimal(10, 3) NOT NULL COMMENT 'ppt宽',
  `ppt_h` decimal(10, 3) NOT NULL COMMENT 'ppt高',
  `ppt_x` decimal(10, 3) NOT NULL COMMENT 'ppt距离顶部位置',
  `ppt_y` decimal(10, 3) NOT NULL COMMENT 'ppt距离左侧位置',
  `human_w` decimal(10, 3) NOT NULL COMMENT '数字人宽',
  `human_h` decimal(10, 3) NOT NULL COMMENT '数字人高',
  `human_x` decimal(10, 3) NOT NULL COMMENT '数字人距离顶部位置',
  `human_y` decimal(10, 3) NOT NULL COMMENT '数字人距离左侧位置',
  `bg_image` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '背景图片',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '租户编号',
  `preview_image` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '效果图',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '模板表' ROW_FORMAT = Dynamic;

CREATE TABLE `easegen`.`member_work_center`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '收件地址编号',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '租户编号',
  `industry` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '行业',
  `scene` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景',
  `language` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '语种',
  `work_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '作品类型',
  `work_duration` bigint(0) NULL DEFAULT NULL COMMENT '作品时长',
  `cover_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '封面地址',
  `work_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '作品名称',
  `work_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '作品地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '作品中心' ROW_FORMAT = Dynamic;


INSERT INTO `easegen`.`digitalcourse_template`(`id`, `show_background`, `show_digital_human`, `show_ppt`, `ppt_w`, `ppt_h`, `ppt_x`, `ppt_y`, `human_w`, `human_h`, `human_x`, `human_y`, `bg_image`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`, `preview_image`) VALUES (1, 1, 1, 1, 672.000, 379.000, 122.000, 20.000, 150.000, 198.000, 0.000, 252.000, 'http://36.103.251.108:48084/6f76ef1671f70f5676ae7e08eeb9dc240f16eaff0107c5ffeef38c9cbcf83d2d.png', '1', '2024-11-01 18:33:22', '1', '2024-11-07 17:22:17', b'0', 0, 'http://36.103.251.108:48084/81cf41ad6bddb692a976700d813a3abe8a0ecd883d8281dac9e92ff644022b1e.png');

INSERT INTO `easegen`.`digitalcourse_template`(`id`, `show_background`, `show_digital_human`, `show_ppt`, `ppt_w`, `ppt_h`, `ppt_x`, `ppt_y`, `human_w`, `human_h`, `human_x`, `human_y`, `bg_image`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`, `preview_image`) VALUES (2, 1, 1, 1, 672.000, 379.000, 122.000, 20.000, 150.000, 198.000, 0.000, 252.000, 'http://36.103.251.108:48084/e4118100e71818c3d81477fb1a80756c68670337865ae7717a6f5e3b050a58fb.png', '1', '2024-11-01 18:33:22', '1', '2024-11-07 17:22:58', b'0', 0, 'http://36.103.251.108:48084/a440e8c0606b23807d62129fa6490eaf90d65b5483abe6c5ab5c500b04fb50b1.png');

INSERT INTO `easegen`.`digitalcourse_template`(`id`, `show_background`, `show_digital_human`, `show_ppt`, `ppt_w`, `ppt_h`, `ppt_x`, `ppt_y`, `human_w`, `human_h`, `human_x`, `human_y`, `bg_image`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`, `preview_image`) VALUES (3, 1, 1, 1, 672.000, 379.000, 122.000, 20.000, 150.000, 198.000, 0.000, 252.000, 'http://36.103.251.108:48084/874d9f3a4af1d82fbd4888e225ba2bf6a97f3f5e23ae9ff90e6e3f9c396c3ccb.png', '1', '2024-11-01 18:33:22', '1', '2024-11-07 17:23:31', b'0', 0, 'http://36.103.251.108:48084/d3b0c9a6e0a651b57fc23cfb122d266e2ccc5b824836f58931bf8529c49a983b.png');

INSERT INTO `easegen`.`digitalcourse_template`(`id`, `show_background`, `show_digital_human`, `show_ppt`, `ppt_w`, `ppt_h`, `ppt_x`, `ppt_y`, `human_w`, `human_h`, `human_x`, `human_y`, `bg_image`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`, `preview_image`) VALUES (4, 1, 1, 1, 800.000, 450.000, 0.000, 0.000, 150.000, 198.000, 650.000, 252.000, '', '1', '2024-11-01 18:33:22', '1', '2024-11-07 17:24:32', b'0', 0, 'http://36.103.251.108:48084/3ebeff2dc6c13ac9e5a91dacf6dac2e454dc02a54d667ecc010ac95d1f98242f.png');

INSERT INTO `easegen`.`digitalcourse_template`(`id`, `show_background`, `show_digital_human`, `show_ppt`, `ppt_w`, `ppt_h`, `ppt_x`, `ppt_y`, `human_w`, `human_h`, `human_x`, `human_y`, `bg_image`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`, `preview_image`) VALUES (5, 1, 1, 1, 321.000, 32111.000, 321.000, 321.000, 321.000, 321.000, 321.000, 321.000, 'http://36.103.251.108:48084/a6f0844edbd78d79875a9118fd2767f469c3369970ce4fc236e1b247175b167e.png', '1', '2024-11-07 19:29:46', '1', '2024-11-07 19:29:55', b'0', 0, 'http://36.103.251.108:48084/9bab10c2d1713b15377a4368a1fc1b71c336fa49a4c38f611bb396a46fe4cfaf.png');

INSERT INTO `easegen`.`infra_config`(`id`, `category`, `type`, `name`, `config_key`, `value`, `visible`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (22, 'sys', 2, '密码登录开关', 'password-login-switch', '1', b'1', '', '1', '2024-10-30 18:38:40', '1', '2024-10-31 09:50:14', b'0');

SET FOREIGN_KEY_CHECKS=1;
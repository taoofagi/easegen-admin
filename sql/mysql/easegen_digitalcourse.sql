/*
 Navicat Premium Data Transfer

 Source Server         : 36.103.251.108（宁夏教育开发环境）
 Source Server Type    : MySQL
 Source Server Version : 80024
 Source Host           : 36.103.251.108:23309
 Source Schema         : easegen

 Target Server Type    : MySQL
 Target Server Version : 80024
 File Encoding         : 65001

 Date: 26/07/2024 14:21:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for digitalcourse_backgrounds
-- ----------------------------
DROP TABLE IF EXISTS `digitalcourse_backgrounds`;
CREATE TABLE `digitalcourse_backgrounds`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `background_type` tinyint(0) NOT NULL COMMENT '背景类型 (1: PPT, 2: 板书, 3: 插图, 4: 字幕, 5: 其他)',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '背景名称',
  `original_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '原始图片URL',
  `picture_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '图片URL',
  `width` int(0) NULL COMMENT '图片宽度',
  `height` int(0) NULL COMMENT '图片高度',
  `size` bigint(0) NULL COMMENT '文件大小 (字节)',
  `duration` int(0) NULL COMMENT '时长（秒）',
  `preset` tinyint(0) NOT NULL COMMENT '预设标识 (0: 否, 1: 是)',
  `status` tinyint(0) NOT NULL COMMENT '状态 (0: 正常, 1: 异常)',
  `fail_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '失败原因',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '存储各种背景信息，包括PPT背景、板书、插图、字幕等' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for digitalcourse_course_ppts
-- ----------------------------
DROP TABLE IF EXISTS `digitalcourse_course_ppts`;
CREATE TABLE `digitalcourse_course_ppts`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '课程PPT ID',
  `course_id` int(0) NOT NULL COMMENT '课程ID，关联digitalcourse_courses表',
  `filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名',
  `size` bigint(0) NOT NULL COMMENT '文件大小（字节）',
  `md5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件MD5校验值',
  `doc_type` tinyint(0) NOT NULL COMMENT '文档类型',
  `ext_info` json NOT NULL COMMENT '扩展信息',
  `resolve_type` tinyint(0) NOT NULL COMMENT '解析类型',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `status` tinyint(0) NOT NULL COMMENT '状态 (0: 正常, 1: 异常)',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '存储课程的PPT信息，包括文件名、文件大小、类型等' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for digitalcourse_digital_humans
-- ----------------------------
DROP TABLE IF EXISTS `digitalcourse_digital_humans`;
CREATE TABLE `digitalcourse_digital_humans`  (
                                                 `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '课程PPT ID',
                                                 `account_id` int(0) NOT NULL COMMENT '账户ID',
                                                 `app_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用名称',
                                                 `expire_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '过期状态',
                                                 `finish_time` datetime(0) NOT NULL COMMENT '完成时间',
                                                 `gender` tinyint(0) NOT NULL COMMENT '性别',
                                                 `matting` tinyint(0) NOT NULL COMMENT '抠图标识',
                                                 `models` json NOT NULL COMMENT '模型列表',
                                                 `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数字人名称',
                                                 `picture_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '图片URL',
                                                 `posture` tinyint(0) NOT NULL COMMENT '姿势',
                                                 `snapshot_height` int(0) NOT NULL COMMENT '快照高度',
                                                 `snapshot_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '快照URL',
                                                 `snapshot_width` int(0) NOT NULL COMMENT '快照宽度',
                                                 `submit_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '提交ID',
                                                 `type` tinyint(0) NOT NULL COMMENT '类型',
                                                 `use_general_model` tinyint(0) NOT NULL COMMENT '使用通用模型',
                                                 `use_model` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '使用模型类型',
                                                 `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
                                                 `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                                                 `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
                                                 `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
                                                 `status` tinyint(0) NOT NULL COMMENT '状态 (0: 正常, 1: 异常)',
                                                 `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
                                                 `tenant_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '租户编号',
                                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数字人模型' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for digitalcourse_course_scene_audios
-- ----------------------------
DROP TABLE IF EXISTS `digitalcourse_course_scene_audios`;
CREATE TABLE `digitalcourse_course_scene_audios`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '场景音频ID',
  `scene_id` int(0) NOT NULL COMMENT '场景ID，关联digitalcourse_course_scenes表',
  `audio_id` int(0) NOT NULL COMMENT '音频ID',
  `use_video_background_audio` tinyint(0) NOT NULL COMMENT '使用视频背景音频 (0: 否, 1: 是)',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `status` tinyint(0) NOT NULL COMMENT '状态 (0: 正常, 1: 异常)',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '存储场景中的音频信息，包括音频ID和使用视频背景音频的标志等' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for digitalcourse_course_scene_backgrounds
-- ----------------------------
DROP TABLE IF EXISTS `digitalcourse_course_scene_backgrounds`;
CREATE TABLE `digitalcourse_course_scene_backgrounds`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '场景背景ID',
  `scene_id` int(0) NOT NULL COMMENT '场景ID，关联digitalcourse_course_scenes表',
  `background_type` tinyint(0) NOT NULL COMMENT '背景类型 (1: PPT, 2: 板书, 3: 插图, 4: 字幕, 5: 其他)',
  `entity_id` int(0) NOT NULL COMMENT '背景实体ID',
  `src` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '背景资源URL',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '背景封面URL',
  `width` int(0) NOT NULL COMMENT '背景宽度',
  `height` int(0) NOT NULL COMMENT '背景高度',
  `depth` int(0) NOT NULL COMMENT '深度',
  `origin_width` int(0) NOT NULL COMMENT '原始宽度',
  `origin_height` int(0) NOT NULL COMMENT '原始高度',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `status` tinyint(0) NOT NULL COMMENT '状态 (0: 正常, 1: 异常)',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '存储每个场景的背景信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for digitalcourse_course_scene_components
-- ----------------------------
DROP TABLE IF EXISTS `digitalcourse_course_scene_components`;
CREATE TABLE `digitalcourse_course_scene_components`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '组件ID',
  `scene_id` int(0) NOT NULL COMMENT '场景ID，关联digitalcourse_course_scenes表',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '组件名称',
  `src` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源URL',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '封面URL',
  `width` int(0) NOT NULL COMMENT '组件宽度',
  `height` int(0) NOT NULL COMMENT '组件高度',
  `origin_width` int(0) NOT NULL COMMENT '原始宽度',
  `origin_height` int(0) NOT NULL COMMENT '原始高度',
  `category` tinyint(0) NOT NULL COMMENT '类别 (1: PPT, 2: 数字人, 3: 其他)',
  `depth` int(0) NOT NULL COMMENT '深度',
  `top` int(0) NOT NULL COMMENT '上边距',
  `left` int(0) NOT NULL COMMENT '左边距',
  `entity_id` int(0) NOT NULL COMMENT '实体ID',
  `entity_type` tinyint(0) NOT NULL COMMENT '实体类型 (0: 其他, 1: 数字人)',
  `business_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务ID',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `status` tinyint(0) NOT NULL COMMENT '状态 (0: 正常, 1: 异常)',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '存储每个场景中的组件信息，包括PPT、数字人等' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for digitalcourse_course_scene_texts
-- ----------------------------
DROP TABLE IF EXISTS `digitalcourse_course_scene_texts`;
CREATE TABLE `digitalcourse_course_scene_texts`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '场景文本ID',
  `scene_id` int(0) NOT NULL COMMENT '场景ID，关联digitalcourse_course_scenes表',
  `pitch` int(0) NOT NULL COMMENT '音调',
  `speed` float NOT NULL COMMENT '速度',
  `volume` float NOT NULL COMMENT '音量',
  `smart_speed` float NOT NULL COMMENT '智能速度',
  `speech_rate` float NOT NULL COMMENT '语速',
  `text_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文本内容（JSON格式）',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `status` tinyint(0) NOT NULL COMMENT '状态 (0: 正常, 1: 异常)',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '存储场景中的文本信息，包括文本内容、音调、速度等' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for digitalcourse_course_scene_voices
-- ----------------------------
DROP TABLE IF EXISTS `digitalcourse_course_scene_voices`;
CREATE TABLE `digitalcourse_course_scene_voices`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '场景声音ID',
  `scene_id` int(0) NOT NULL COMMENT '场景ID，关联digitalcourse_course_scenes表',
  `voice_id` int(0) NOT NULL COMMENT '声音ID，关联digitalcourse_voices表',
  `tone_pitch` int(0) NOT NULL COMMENT '音调（0-100）',
  `voice_type` tinyint(0) NOT NULL COMMENT '声音类型 (0: 标准, 1: 特殊)',
  `speech_rate` float NOT NULL COMMENT '语速',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '自定义名称',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `status` tinyint(0) NOT NULL COMMENT '状态 (0: 正常, 1: 异常)',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '存储每个场景中的声音信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for digitalcourse_course_scenes
-- ----------------------------
DROP TABLE IF EXISTS `digitalcourse_course_scenes`;
CREATE TABLE `digitalcourse_course_scenes`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '场景ID',
  `course_id` int(0) NOT NULL COMMENT '课程ID，关联digitalcourse_courses表',
  `order_no` int(0) NOT NULL COMMENT '场景顺序号',
  `duration` int(0) NOT NULL COMMENT '时长（秒）',
  `driver_type` tinyint(0) NOT NULL COMMENT '驱动类型',
  `business_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务ID',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `status` tinyint(0) NOT NULL COMMENT '状态 (0: 正常, 1: 异常)',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '存储课程的场景信息，包括背景、组件、声音等' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for digitalcourse_courses
-- ----------------------------
DROP TABLE IF EXISTS `digitalcourse_courses`;
CREATE TABLE `digitalcourse_courses`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '课程ID',
  `account_id` int(0) NOT NULL COMMENT '账户ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '课程名称',
  `aspect` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '屏幕比例',
  `duration` int(0) NOT NULL COMMENT '时长（秒）',
  `height` int(0) NOT NULL COMMENT '高度（像素）',
  `width` int(0) NOT NULL COMMENT '宽度（像素）',
  `matting` tinyint(0) NOT NULL COMMENT '抠图标识',
  `page_mode` tinyint(0) NOT NULL COMMENT '页面模式',
  `status` tinyint(0) NOT NULL COMMENT '状态 (0: 正常, 1: 异常)',
  `page_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '页面信息',
  `subtitles_style` json NOT NULL COMMENT '字幕样式',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '存储课程的基本信息，包括课程名称、时长、状态等' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for digitalcourse_fonts
-- ----------------------------
DROP TABLE IF EXISTS `digitalcourse_fonts`;
CREATE TABLE `digitalcourse_fonts`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '字体ID',
  `alias` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字体别名',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字体名称',
  `choice_preview_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '选择预览URL',
  `view_preview_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '查看预览URL',
  `order_no` int(0) NOT NULL COMMENT '排序号',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `status` tinyint(0) NOT NULL COMMENT '状态 (0: 正常, 1: 异常)',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '存储字体的信息，包括字体的别名、预览URL、名称等' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for digitalcourse_ppt_materials
-- ----------------------------
DROP TABLE IF EXISTS `digitalcourse_ppt_materials`;
CREATE TABLE `digitalcourse_ppt_materials`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'PPT课件ID',
  `ppt_id` int(0) NOT NULL COMMENT '课程PPT ID，关联digitalcourse_course_ppts表',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '课件名称',
  `background_type` tinyint(0) NOT NULL COMMENT '背景类型 (1: PPT, 2: 板书, 3: 插图, 4: 字幕, 5: 其他)',
  `picture_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '图片URL',
  `original_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '原始图片URL',
  `width` int(0) NOT NULL COMMENT '宽度',
  `height` int(0) NOT NULL COMMENT '高度',
  `index_no` int(0) NOT NULL COMMENT '页面索引',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `status` tinyint(0) NOT NULL COMMENT '状态 (0: 正常, 1: 异常)',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '存储PPT课件的具体内容信息，包括课件的图片URL、页面索引等' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for digitalcourse_prompts
-- ----------------------------
DROP TABLE IF EXISTS `digitalcourse_prompts`;
CREATE TABLE `digitalcourse_prompts`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '提示词模板ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '提示词名称',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '提示词内容',
  `order` int(0) NOT NULL COMMENT '排序',
  `type` tinyint(0) NOT NULL COMMENT '类型 (0: 用户, 1: 系统, 2: 管理员)',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `status` tinyint(0) NOT NULL COMMENT '状态 (0: 正常, 1: 异常)',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '存储提示词模板的信息，包括提示词的名称、类型、排序等信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for digitalcourse_voices
-- ----------------------------
DROP TABLE IF EXISTS `digitalcourse_voices`;
CREATE TABLE `digitalcourse_voices`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '声音ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '声音名称',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '声音代码',
  `audition_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '试听URL',
  `category` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类别',
  `gender` tinyint(0) NOT NULL COMMENT '性别',
  `introduction` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '介绍',
  `quality` tinyint(0) NOT NULL COMMENT '音质评分 (1-10)',
  `voice_type` tinyint(0) NOT NULL COMMENT '声音类型 (0: 标准, 1: 特殊)',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `status` tinyint(0) NOT NULL COMMENT '状态 (0: 正常, 1: 异常)',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '存储声音信息，包括试听URL、类别、性别、音质等信息' ROW_FORMAT = Dynamic;

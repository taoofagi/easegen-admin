package cn.iocoder.yudao.module.digitalcourse.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * System 错误码枚举类
 *
 * system 系统，使用 1-002-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== digitalcourse 模块 1-0010-000-000 ==========
    ErrorCode DIGITAL_HUMANS_NOT_EXISTS = new ErrorCode(1-0010-000-000, "数字人模型不存在");
    // ========== digitalcourse 模块 1-0010-000-001 ==========
    ErrorCode BACKGROUNDS_NOT_EXISTS = new ErrorCode(1-0010-001-001, "背景信息（PPT背景、板书、插图、字幕等）不存在");
    // ========== digitalcourse 模块 1-0010-000-002 ==========
    ErrorCode VOICES_NOT_EXISTS = new ErrorCode(1-0010-002-002, "声音模型不存在");
    // ========== digitalcourse 模块 1-0010-000-003 ==========
    ErrorCode COURSES_NOT_EXISTS = new ErrorCode(1-0010-003-000, "存储课程的基本信息，包括课程名称、时长、状态等不存在");
    // ========== digitalcourse 模块 1-0010-000-003 ==========
    ErrorCode COURSES_UPDATE_ERROR = new ErrorCode(1-0010-003-001, "数字人课程保存失败");
    // ========== digitalcourse 模块 1-0010-000-004 ==========
    ErrorCode COURSE_SCENES_NOT_EXISTS = new ErrorCode(1-0010-000-004, "存储课程的场景信息，包括背景、组件、声音等不存在");
    // ========== digitalcourse 模块 1-0010-000-005 ==========
    ErrorCode COURSE_SCENE_BACKGROUNDS_NOT_EXISTS = new ErrorCode(1-0010-000-005, "存储每个场景的背景信息不存在");
    // ========== digitalcourse 模块 1-0010-000-006 ==========
    ErrorCode COURSE_SCENE_COMPONENTS_NOT_EXISTS = new ErrorCode(1-0010-000-006, "存储每个场景中的组件信息，包括PPT、数字人等不存在");
    // ========== digitalcourse 模块 1-0010-000-007 ==========
    ErrorCode COURSE_SCENE_VOICES_NOT_EXISTS = new ErrorCode(1-0010-000-007, "存储每个场景中的声音信息不存在");
    // ========== digitalcourse 模块 1-0010-000-010 ==========
    ErrorCode COURSE_SCENE_TEXTS_NOT_EXISTS = new ErrorCode(1-0010-000-010, "存储场景中的文本信息，包括文本内容、音调、速度等不存在");
    // ========== digitalcourse 模块 1-0010-000-011 ==========
    ErrorCode COURSE_SCENE_AUDIOS_NOT_EXISTS = new ErrorCode(1-0010-000-011, "存储场景中的音频信息，包括音频ID和使用视频背景音频的标志等不存在");
    // ========== digitalcourse 模块 1-0010-000-012 ==========
    ErrorCode PPT_MATERIALS_NOT_EXISTS = new ErrorCode(1-0010-000-012, "存储PPT课件的具体内容信息，包括课件的图片URL、页面索引等不存在");
    // ========== digitalcourse 模块 1-0010-000-013 ==========
    ErrorCode PROMPTS_NOT_EXISTS = new ErrorCode(1-0010-000-013, "存储提示词模板的信息，包括提示词的名称、类型、排序等信息不存在");
    // ========== digitalcourse 模块 1-0010-000-014 ==========
    ErrorCode FONTS_NOT_EXISTS = new ErrorCode(1-0010-000-014, "存储字体的信息，包括字体的别名、预览URL、名称等不存在");
    ErrorCode COURSE_PPTS_NOT_EXISTS = new ErrorCode(1-0010-000-003, "存储课程的PPT信息，包括文件名、文件大小、类型等不存在");
    ErrorCode COURSE_MEDIA_NOT_EXISTS = new ErrorCode(1-0010-000-015, "课程媒体不存在");
}

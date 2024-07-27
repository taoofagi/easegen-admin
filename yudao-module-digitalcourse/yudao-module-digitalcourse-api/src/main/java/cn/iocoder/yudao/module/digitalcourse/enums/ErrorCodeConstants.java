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
    ErrorCode BACKGROUNDS_NOT_EXISTS = new ErrorCode(1-0010-000-001, "背景信息（PPT背景、板书、插图、字幕等）不存在");

}

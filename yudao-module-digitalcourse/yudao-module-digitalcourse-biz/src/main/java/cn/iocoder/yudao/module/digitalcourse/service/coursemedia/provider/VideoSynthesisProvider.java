package cn.iocoder.yudao.module.digitalcourse.service.coursemedia.provider;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursemedia.vo.CourseMediaMegerVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursemedia.CourseMediaDO;

/**
 * 视频合成提供者接口
 * 采用策略模式，支持不同的视频合成平台
 *
 * @author easegen
 */
public interface VideoSynthesisProvider {

    /**
     * 获取平台类型：1-2D（easegen），2-3D（魔珐星云）
     *
     * @return 平台类型
     */
    Integer getPlatformType();

    /**
     * 创建视频合成任务
     *
     * @param courseMedia 课程媒体对象
     * @param mergeVO 合并请求VO
     */
    void createSynthesisTask(CourseMediaDO courseMedia, CourseMediaMegerVO mergeVO);

    /**
     * 查询任务状态
     *
     * @param courseMedia 课程媒体对象
     */
    void queryTaskStatus(CourseMediaDO courseMedia);

    /**
     * 取消任务
     *
     * @param courseMediaId 课程媒体ID
     */
    void cancelTask(Long courseMediaId);

    /**
     * 是否支持该平台类型
     *
     * @param platformType 平台类型
     * @return 是否支持
     */
    boolean supports(Integer platformType);
}


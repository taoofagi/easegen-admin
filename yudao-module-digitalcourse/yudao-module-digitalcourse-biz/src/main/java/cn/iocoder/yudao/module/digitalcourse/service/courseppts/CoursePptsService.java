package cn.iocoder.yudao.module.digitalcourse.service.courseppts;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courseppts.vo.AppCoursePptsPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courseppts.vo.AppCoursePptsSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.courseppts.CoursePptsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * 存储课程的PPT信息，包括文件名、文件大小、类型等 Service 接口
 *
 * @author 芋道源码
 */
public interface CoursePptsService {

    /**
     * 创建存储课程的PPT信息，包括文件名、文件大小、类型等
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCoursePpts(@Valid AppCoursePptsSaveReqVO createReqVO);

    /**
     * 更新存储课程的PPT信息，包括文件名、文件大小、类型等
     *
     * @param updateReqVO 更新信息
     */
    void updateCoursePpts(@Valid AppCoursePptsSaveReqVO updateReqVO);

    /**
     * 删除存储课程的PPT信息，包括文件名、文件大小、类型等
     *
     * @param id 编号
     */
    void deleteCoursePpts(Long id);

    /**
     * 获得存储课程的PPT信息，包括文件名、文件大小、类型等
     *
     * @param id 编号
     * @return 存储课程的PPT信息，包括文件名、文件大小、类型等
     */
    CoursePptsDO getCoursePpts(Long id);

    /**
     * 获得存储课程的PPT信息，包括文件名、文件大小、类型等分页
     *
     * @param pageReqVO 分页查询
     * @return 存储课程的PPT信息，包括文件名、文件大小、类型等分页
     */
    PageResult<CoursePptsDO> getCoursePptsPage(AppCoursePptsPageReqVO pageReqVO);

    CommonResult getSchedule(Long id);

    ResponseEntity<Object> mergePPTs(MultipartFile[] files);

    ResponseEntity<Map<String, Object>> convertPptToImages(MultipartFile file);

    Boolean cancelAnalysis(Long id);

}
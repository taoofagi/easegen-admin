package cn.iocoder.yudao.module.digitalcourse.service.courseppts;

import cn.hutool.json.JSONObject;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courseppts.vo.AppCoursePptsPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courseppts.vo.AppCoursePptsSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.util.PPTUtil;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.courseppts.CoursePptsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.digitalcourse.dal.mysql.courseppts.CoursePptsMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.digitalcourse.enums.ErrorCodeConstants.*;

/**
 * 存储课程的PPT信息，包括文件名、文件大小、类型等 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class CoursePptsServiceImpl implements CoursePptsService {

    @Resource
    private CoursePptsMapper coursePptsMapper;

   /* @Resource
    private RedisMQTemplate redisMQTemplate;*/

    @Resource
    private PPTUtil pptUtil;

    @Override
    public Long createCoursePpts(AppCoursePptsSaveReqVO createReqVO) {
        // 插入
        CoursePptsDO coursePpts = BeanUtils.toBean(createReqVO, CoursePptsDO.class);
        coursePptsMapper.insert(coursePpts);
        pptUtil.analysisPpt(coursePpts.getUrl(),coursePpts.getId());
        // 返回
        return coursePpts.getId();
    }

    @Override
    public void updateCoursePpts(AppCoursePptsSaveReqVO updateReqVO) {
        // 校验存在
        validateCoursePptsExists(updateReqVO.getId());
        // 更新
        CoursePptsDO updateObj = BeanUtils.toBean(updateReqVO, CoursePptsDO.class);
        coursePptsMapper.updateById(updateObj);
    }

    @Override
    public void deleteCoursePpts(Long id) {
        // 校验存在
        validateCoursePptsExists(id);
        // 删除
        coursePptsMapper.deleteById(id);
    }

    private void validateCoursePptsExists(Long id) {
        if (coursePptsMapper.selectById(id) == null) {
            throw exception(COURSE_PPTS_NOT_EXISTS);
        }
    }

    @Override
    public CoursePptsDO getCoursePpts(Long id) {
        return coursePptsMapper.selectById(id);
    }

    @Override
    public PageResult<CoursePptsDO> getCoursePptsPage(AppCoursePptsPageReqVO pageReqVO) {
        return coursePptsMapper.selectPage(pageReqVO);
    }

    @Override
    public CommonResult getSchedule(Long id) {
        JSONObject map = PPTUtil.getMap(id);
        if (map == null){
            return CommonResult.success(null);
            //从数据库中查询
//            return
        }
        Double schedule = Double.valueOf(String.valueOf(map.get("schedule")));
        if (schedule.compareTo(1.0)<0) return CommonResult.success(schedule);
        //返回图片
        return CommonResult.success(map.get("url"));
    }

}
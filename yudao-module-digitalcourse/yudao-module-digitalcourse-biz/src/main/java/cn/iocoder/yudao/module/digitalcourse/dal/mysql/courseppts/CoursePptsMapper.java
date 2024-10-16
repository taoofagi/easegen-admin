package cn.iocoder.yudao.module.digitalcourse.dal.mysql.courseppts;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courseppts.vo.AppCoursePptsPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.courseppts.CoursePptsDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 存储课程的PPT信息，包括文件名、文件大小、类型等 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface CoursePptsMapper extends BaseMapperX<CoursePptsDO> {

    default PageResult<CoursePptsDO> selectPage(AppCoursePptsPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CoursePptsDO>()
                .eqIfPresent(CoursePptsDO::getCourseId, reqVO.getCourseId())
                .likeIfPresent(CoursePptsDO::getFilename, reqVO.getFilename())
                .eqIfPresent(CoursePptsDO::getSize, reqVO.getSize())
                .eqIfPresent(CoursePptsDO::getMd5, reqVO.getMd5())
                .eqIfPresent(CoursePptsDO::getDocType, reqVO.getDocType())
                .eqIfPresent(CoursePptsDO::getExtInfo, reqVO.getExtInfo())
                .eqIfPresent(CoursePptsDO::getResolveType, reqVO.getResolveType())
                .betweenIfPresent(CoursePptsDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(CoursePptsDO::getStatus, reqVO.getStatus())
                .orderByDesc(CoursePptsDO::getId));
    }

}
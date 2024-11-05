package cn.iocoder.yudao.module.member.service.workcenter;

import cn.iocoder.yudao.module.infra.api.file.FileApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.io.File;
import java.io.IOException;
import java.util.*;
import cn.iocoder.yudao.module.member.controller.admin.workcenter.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.workcenter.WorkCenterDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.member.dal.mysql.workcenter.WorkCenterMapper;
import org.springframework.web.multipart.MultipartFile;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;
/**
 * 作品中心 Service 实现类
 *
 * @author 管理员
 */
@Slf4j
@Service
@Validated
public class WorkCenterServiceImpl implements WorkCenterService {

    @Resource
    private WorkCenterMapper workCenterMapper;
    @Resource
    private FileApi fileApi;

    @Override
    public Long createWorkCenter(WorkCenterSaveReqVO createReqVO) {
        // 插入
        WorkCenterDO workCenter = BeanUtils.toBean(createReqVO, WorkCenterDO.class);
        workCenterMapper.insert(workCenter);
        // 返回
        return workCenter.getId();
    }

    @Override
    public Map uploadWorkCenter(MultipartFile file) throws IOException {
        Map resultMap = new HashMap<>();
        String filename = file.getOriginalFilename();
        resultMap.put("workName", filename);

        String filePath = fileApi.createFile(filename, file.getBytes());
        resultMap.put("filePath",filePath);
        long duration = 0;
        File tempFile = null;
        try {
            tempFile = File.createTempFile(String.valueOf(UUID.randomUUID()),"."+getExtension(file));
            file.transferTo(tempFile);

//            MultimediaObject multimediaObject = new MultimediaObject(new File("C:\\Users\\79333\\Desktop\\财务监管.wmv"));
            /*MultimediaObject instance = new MultimediaObject(new File("C:\\Users\\79333\\Desktop\\财务监管.wmv"));
            MultimediaInfo info = instance.getInfo();*/
//            duration = (long) Math.ceil((double) info.getDuration() / 1000);
            resultMap.put("duration", duration);
        } catch (Exception e) {
            log.error("获取视频时长失败");
            throw new RuntimeException(e);
        } finally {
            if (tempFile != null) {
                tempFile.delete();
            }
        }
        return resultMap;
    }

    private String getExtension(MultipartFile file) {
        String filename = file.getOriginalFilename();
        return filename.substring(filename.lastIndexOf(".")+1);
    }



    @Override
    public void updateWorkCenter(WorkCenterSaveReqVO updateReqVO) {
        // 校验存在
        validateWorkCenterExists(updateReqVO.getId());
        // 更新
        WorkCenterDO updateObj = BeanUtils.toBean(updateReqVO, WorkCenterDO.class);
        workCenterMapper.updateById(updateObj);
    }

    @Override
    public void deleteWorkCenter(Long id) {
        // 校验存在
        validateWorkCenterExists(id);
        // 删除
        workCenterMapper.deleteById(id);
    }

    private void validateWorkCenterExists(Long id) {
        if (workCenterMapper.selectById(id) == null) {
            throw exception(WORK_CENTER_NOT_EXISTS);
        }
    }

    @Override
    public WorkCenterDO getWorkCenter(Long id) {
        return workCenterMapper.selectById(id);
    }

    @Override
    public PageResult<WorkCenterDO> getWorkCenterPage(WorkCenterPageReqVO pageReqVO) {
        return workCenterMapper.selectPage(pageReqVO);
    }

}
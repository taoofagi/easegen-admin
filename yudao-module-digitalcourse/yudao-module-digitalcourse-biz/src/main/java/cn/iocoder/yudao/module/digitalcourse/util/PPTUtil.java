package cn.iocoder.yudao.module.digitalcourse.util;



import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.courseppts.CoursePptsDO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.pptmaterials.PptMaterialsDO;
import cn.iocoder.yudao.module.digitalcourse.dal.mysql.courseppts.CoursePptsMapper;
import cn.iocoder.yudao.module.digitalcourse.service.pptmaterials.PptMaterialsService;
import cn.iocoder.yudao.module.infra.api.config.ConfigApi;
import cn.iocoder.yudao.module.infra.api.file.FileApi;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

/**
 * @author fmi110
 * @description ppt 转图片；图片转pdf 工具
 * @date 2021/8/19 20:16
 */

@Component
@Slf4j
public class PPTUtil {


    private static final String EASEGEN_CORE_PPT_ANALYSIS_URL = "easegen.core.ppt.analysis.url";
     @Resource
    private FileApi fileApi;
    @Resource
    private ConfigApi configApi;
     @Resource
     private PptMaterialsService pptMaterialsService;
    @Resource
    private CoursePptsMapper coursePptsMapper;



    private static String getTimeStamp(){
            return String.valueOf(System.currentTimeMillis());
    }

    @Async
    public void analysisPpt(String file,Long pptId){
        Boolean b = false;
        try {
            log.info("开始解析ppt,pptId:"+pptId+",file:"+file);
//            String path = file.substring(file.lastIndexOf("/")+1);
//            byte[] fileContent = fileApi.getFileContent(4L, path);
//            Path tempFilePath = Files.createTempFile(path,".pptx");
//            File tempFile = tempFilePath.toFile();
//            tempFile.deleteOnExit();
//            try (InputStream inputStream = new ByteArrayInputStream(fileContent);
//                 java.io.FileOutputStream outputStream = new java.io.FileOutputStream(tempFile)) {
//                byte[] buffer = new byte[1024];
//                int len;
//                while ((len = inputStream.read(buffer)) != -1) {
//                    outputStream.write(buffer, 0, len);
//                }
//            }
            //直接传文件url
            HashMap<String, Object> param = new HashMap<>();
            param.put("file",file);
            param.put("pptId",pptId);
            String apiUrl = configApi.getConfigValueByKey(EASEGEN_CORE_PPT_ANALYSIS_URL) + "/admin-api/digitalcourse/course-ppts/analysisPpt";
            String body = HttpRequest.post(apiUrl)
                    .form("file", file)
                    .form("pptId", pptId)
                    .execute().body();
            JSONObject entries = JSONUtil.parseObj(body);
            JSONArray data = JSONUtil.parseArray(entries.getStr("data"));
            List<PptMaterialsDO> list = new ArrayList<>();
            log.info("解析ppt数据："+data);

            for (int i = 0; i < data.size(); i++) {
                PptMaterialsDO pptMaterialsDO = new PptMaterialsDO();
                JSONObject obj = BeanUtils.toBean(data.get(i), JSONObject.class);
                String picFile = obj.getStr("file");
                try {
//                    byte[] bytes = Files.readAllBytes(Path.of(picFile));
//                    String picPath = savePicture(bytes, getTimeStamp());
                    pptMaterialsDO.setPptId(pptId);
                    pptMaterialsDO.setName(picFile);
                    pptMaterialsDO.setPictureUrl(picFile);
                    pptMaterialsDO.setOriginalUrl(picFile);
                    pptMaterialsDO.setIndexNo(i);
                    pptMaterialsDO.setBackgroundType(1);
                    pptMaterialsDO.setPptRemark(String.valueOf(obj.get("text")));
                    list.add(pptMaterialsDO);
                }catch (Exception exception){
                    exception.printStackTrace();
                }
            }

            b = pptMaterialsService.batchInsert(list);
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            CoursePptsDO coursePptsDO = new CoursePptsDO();
            coursePptsDO.setStatus(b ? 0 : 1);
            coursePptsDO.setId(pptId);
            coursePptsMapper.updateById(coursePptsDO);
        }
    }
    private String savePicture(final byte[] data, final String fileName) throws IOException {
        String file = fileApi.createFile(data);
        return file;
    }



}
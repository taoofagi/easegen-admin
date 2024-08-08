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
import cn.iocoder.yudao.module.infra.api.file.FileApi;
import jakarta.annotation.Resource;
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
public class PPTUtil {



     @Resource
    private FileApi fileApi;
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
            String path = file.substring(file.lastIndexOf("/")+1);
            byte[] fileContent = fileApi.getFileContent(4L, path);
            Path tempFilePath = Files.createTempFile(path,".pptx");
            File tempFile = tempFilePath.toFile();
            tempFile.deleteOnExit();
            try (InputStream inputStream = new ByteArrayInputStream(fileContent);
                 java.io.FileOutputStream outputStream = new java.io.FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
            }
            HashMap<String, Object> param = new HashMap<>();
            param.put("file",tempFile);
            param.put("pptId",pptId);
            String body = HttpRequest.post("http://localhost:48082/admin-api/digitalcourse/course-ppts/analysisPpt")
                    .form("file", tempFile)
                    .form("pptId", pptId)
                    .execute().body();
            JSONObject entries = JSONUtil.parseObj(body);
            JSONArray data = JSONUtil.parseArray(entries.getStr("data"));
            List<PptMaterialsDO> list = new ArrayList<>();

            for (int i = 0; i < data.size(); i++) {
                PptMaterialsDO pptMaterialsDO = new PptMaterialsDO();
                JSONObject obj = BeanUtils.toBean(data.get(i), JSONObject.class);
                String picFile = obj.getStr("file");
                try {
                    byte[] bytes = Files.readAllBytes(Path.of(picFile));
                    String picPath = savePicture(bytes, getTimeStamp());
                    pptMaterialsDO.setPptId(pptId);
                    pptMaterialsDO.setName(picPath);
                    pptMaterialsDO.setPictureUrl(picPath);
                    pptMaterialsDO.setOriginalUrl(picPath);
                    pptMaterialsDO.setIndexNo(i);
                    pptMaterialsDO.setBackgroundType(1);
                    pptMaterialsDO.setPptRemark(String.valueOf(obj.get("text")));
                    list.add(pptMaterialsDO);
                }catch (IOException exception){
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
package cn.iocoder.yudao.module.digitalcourse.util;



import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONObject;
import cn.iocoder.yudao.module.infra.api.file.FileApi;
import com.github.yulichang.toolkit.SpringContentUtils;
import lombok.Data;
import org.apache.poi.xslf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
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

     private static Map<Long, JSONObject> cache = new HashMap<>();

     public static JSONObject getMap(Long id){
         return cache.get(id);
     }


     @Resource
    private FileApi fileApi;




    private static String getTimeStamp(){
            return String.valueOf(System.currentTimeMillis());
    }

    @Async
    public Boolean analysisPpt(String file,Long pptId){
        try {
            ArrayList<String> urlList = new ArrayList<>();
            JSONObject jsonObject = new JSONObject();

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
            cache.put(pptId,jsonObject);

            FileInputStream is = new FileInputStream(tempFile);
            XMLSlideShow ppt = new XMLSlideShow(is);
            List<XSLFSlide> xslfSlideList = ppt.getSlides();
            Dimension pageSize = ppt.getPageSize();
            for (int i = 0; i < xslfSlideList.size(); i++) {
                XSLFNotes notesSlide = ppt.getNotesSlide(xslfSlideList.get(i));
                XSLFTextShape[] placeholders = notesSlide.getPlaceholders();
                String text = placeholders[1].getText();
                System.out.println(text);

                BufferedImage bufferedImage = new BufferedImage(pageSize.width, pageSize.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = bufferedImage.createGraphics();
                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0,0,pageSize.width, pageSize.height));
                xslfSlideList.get(i).draw(graphics);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage,"png",outputStream);
                byte[] byteArray = outputStream.toByteArray();

                String s = savePicture(byteArray, getTimeStamp());
                urlList.add(s);
                jsonObject.put("url",urlList);
                jsonObject.put("schedule",(double) (i + 1) /xslfSlideList.size());
                cache.put(pptId,jsonObject);
            }


            //批量插入数据库,图片背景，文本信息，（一个场景）




            //删除缓存(保存后删除)
//            cache.remove(pptId);
        }catch (Exception e){
            throw new RuntimeException(e);

        }
        return true;
    }
    private String savePicture(final byte[] data, final String fileName) throws IOException {
        String file = fileApi.createFile(data);
        return file;
    }



}
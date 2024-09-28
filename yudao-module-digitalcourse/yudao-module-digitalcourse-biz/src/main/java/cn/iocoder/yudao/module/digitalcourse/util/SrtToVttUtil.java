package cn.iocoder.yudao.module.digitalcourse.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.iocoder.yudao.module.infra.api.file.FileApi;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Service
public class SrtToVttUtil {

    // 替换为你实际的文件 API
    @Resource
    private FileApi fileApi;


    /**
     * 将 .srt 文件从 URL 下载并转换为 .vtt 文件，上传后返回 .vtt 文件 URL。
     * @param srtUrl .srt 文件的 URL
     * @return 上传后 .vtt 文件的 URL
     * @throws IOException 下载或转换过程中的异常
     */
    public String convertAndUploadSrtToVtt(String srtUrl) throws IOException {
        // 为每个请求生成唯一文件名，确保文件名不冲突
        String uniqueId = UUID.randomUUID().toString();
        String vttFileName = "converted_" + uniqueId + ".vtt";

        // 下载 .srt 文件
        byte[] srtContent = downloadFile(srtUrl);

        // 创建临时文件名
        File tempSrtFile = new File(System.getProperty("java.io.tmpdir"), "tempSrt_" + uniqueId + ".srt");
        File tempVttFile = new File(System.getProperty("java.io.tmpdir"), "tempVtt_" + uniqueId + ".vtt");

        try {
            // 写入 .srt 内容到临时文件
            FileUtil.writeBytes(srtContent, tempSrtFile);

            // 转换 .srt 为 .vtt
            convertSrtToVtt(tempSrtFile, tempVttFile);

            // 上传 .vtt 文件并获取 URL
            return uploadFile(vttFileName, tempVttFile);
        } catch (IOException e) {
            log.error("Error during conversion or upload: " + e.getMessage());
            throw e; // 重新抛出异常
        } finally {
            // 始终删除临时文件，确保资源释放
            if (tempSrtFile.exists()) tempSrtFile.delete();
            if (tempVttFile.exists()) tempVttFile.delete();
        }
    }

    /**
     * 下载文件内容
     * @param fileUrl 文件 URL
     * @return 文件内容字节数组
     */
    private byte[] downloadFile(String fileUrl) throws IOException {
        log.info("Downloading file from URL: " + fileUrl);
        try {
            return HttpUtil.downloadBytes(fileUrl);
        } catch (Exception e) {
            log.error("Error downloading file: " + e.getMessage());
            throw new IOException("Failed to download file from URL: " + fileUrl, e);
        }
    }

    /**
     * 将 .srt 文件转换为 .vtt 格式
     * @param srtFile 输入 .srt 文件
     * @param vttFile 输出 .vtt 文件
     * @throws IOException 文件操作异常
     */
    private void convertSrtToVtt(File srtFile, File vttFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(srtFile, StandardCharsets.UTF_8));
             BufferedWriter writer = new BufferedWriter(new FileWriter(vttFile, StandardCharsets.UTF_8))) {

            // 在 .vtt 文件开头添加 `WEBVTT` 头
            writer.write("WEBVTT\n\n");

            String line;
            while ((line = reader.readLine()) != null) {
                // 转换时间戳格式，将 `,` 替换为 `.`
                if (line.contains("-->")) {
                    line = line.replace(",", ".");
                }
                // 写入 .vtt 文件
                writer.write(line);
                writer.write("\n");
            }

            log.info("Successfully converted .srt to .vtt");
        } catch (IOException e) {
            log.error("Error converting .srt to .vtt: " + e.getMessage());
            throw e;
        }
    }

    /**
     * 上传文件到文件服务
     * @param fileName 文件名称，带有 .vtt 扩展名
     * @param file 要上传的文件
     * @return 上传后文件的 URL
     * @throws IOException 文件读取异常
     */
    private String uploadFile(String fileName, File file) throws IOException {
        try {
            byte[] fileContent = FileUtil.readBytes(file);
            // 指定文件路径（你可以根据实际情况设置）
            String filePath = "uploads/vtt/"+fileName;
            // 上传文件到文件服务
            String fileUrl = fileApi.createFile(fileName, filePath, fileContent);
            System.out.println("File uploaded successfully. File URL: " + fileUrl);
            return fileUrl;
        } catch (Exception e) {
            System.err.println("Error uploading file: " + e.getMessage());
            throw new IOException("Failed to upload file", e);
        }
    }
}
package cn.iocoder.yudao.framework.ai.midjourney.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.core.io.FileSystemResource;

/**
 * 附件
 * <p>
 * author: fansili
 * time: 2024/4/7 17:18
 */
@Data
@Accessors(chain = true)
public class Attachments {

    /**
     * 创建文件系统资源对象
     */
    private FileSystemResource fileSystemResource;

}

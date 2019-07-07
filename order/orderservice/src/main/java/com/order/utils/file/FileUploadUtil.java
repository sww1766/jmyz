package com.order.utils.file;

import com.order.model.Files;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 附件上传
 * @author admin
 */
@Log4j2
public class FileUploadUtil {
    public static Files upFile(MultipartFile file, Long fileIndex, HttpServletRequest request) {
        Files files1 = new Files();
        try {
            // 下载本地文件
            String recTime = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
            String fileName = recTime + (int) (Math.random() * 900) + 100;
            String filePath = System.getProperty("java.io.tmpdir") + "img\\" + new String(fileName.getBytes(),
                    StandardCharsets.UTF_8) + ".png";
            File upFile = new File(filePath);
            if (!upFile.exists() || upFile.isDirectory()) {
                upFile.createNewFile();
            }
            file.transferTo(upFile);


            files1.setFileId(UUID.randomUUID().toString().replaceAll("-", ""));
            files1.setFileName(fileName);
            files1.setDataStatus(1);
            files1.setFileUrl(request.getRequestURI() + "/sys/img/" + fileName);
            files1.setRecTime(recTime);
            files1.setFileIndex(fileIndex);
        } catch (Exception e) {
            log.error("附件上传失败: {}", e.getMessage());
            e.printStackTrace();
        }

        return files1;
    }
}

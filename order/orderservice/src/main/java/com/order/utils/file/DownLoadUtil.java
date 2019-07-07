package com.order.utils.file;

import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 下载文件
 * @author admin
 */
@Log4j2
public class DownLoadUtil {
	public static void downLoad(String fileName, HttpServletResponse response){
        // 读到流中
        InputStream inStream = null;// 文件的存放路径
        try {
            inStream = new FileInputStream(System.getProperty("java.io.tmpdir")+"img\\" +
                    new String(fileName.getBytes(), StandardCharsets.UTF_8) + ".png");
        } catch (FileNotFoundException e) {
            log.error("读取文件失败：{}" + e.getMessage());
            e.printStackTrace();
        }
        // 设置输出的格式
        response.setContentType("text/html; charset=UTF-8");
        response.setContentType("image/jpeg");

        OutputStream os = null;
        try {
            os = response.getOutputStream();
            int count;
            byte[] buffer = new byte[1024 * 1024];
            while ((count = Objects.requireNonNull(inStream).read(buffer)) != -1) {
                Objects.requireNonNull(os).write(buffer, 0, count);
            }
            Objects.requireNonNull(os).flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}
}

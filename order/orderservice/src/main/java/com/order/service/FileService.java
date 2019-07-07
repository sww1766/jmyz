package com.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.order.model.Files;


public interface FileService extends IService<Files> {

    int saveFiles(Files filesList);

    String getFileNameById(String fileId);
}

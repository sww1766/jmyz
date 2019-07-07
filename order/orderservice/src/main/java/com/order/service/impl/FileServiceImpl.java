package com.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.order.model.Files;
import com.order.mapper.FilesMapper;
import com.order.service.FileService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * <p>
 * 地区行政编码 服务实现类
 * </p>
 */
@Service
public class FileServiceImpl extends ServiceImpl<FilesMapper, Files> implements FileService {
    @Resource
    private FilesMapper filesMapper;

    @Override
    public int saveFiles(Files files) {
        return filesMapper.insert(files);
    }

    public String getFileNameById(String fileId) {
        return Objects.requireNonNull(filesMapper.selectById(fileId)).getFileName();
    }

}

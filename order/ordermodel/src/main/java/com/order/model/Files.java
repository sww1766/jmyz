package com.order.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("文件表")
public class Files extends Model<Files> {
    @TableId(value = "file _id", type = IdType.UUID)
    private String fileId;
    private String fileName;
    @ApiModelProperty(value = "索引，用于排序", example = "1")
    private Long fileIndex;
    @ApiModelProperty(value = "文件类型,预留字段，product、productType、account", example = "product")
    private String fileType;
    private String fileUrl;
    @ApiModelProperty(value = "记录生成时间戳, 自动维护, 可用于排序", hidden = true)
    private String recTime;
    @ApiModelProperty(value = "状态，1-有效，0-无，默认1", example = "1", hidden = true)
    private int dataStatus;

}

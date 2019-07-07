package com.order.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("评价表")
public class Evaluate extends Model<Evaluate> {
    @TableId(value = "evaluate_id", type = IdType.UUID)
    private String evaluateId;
    private String accountId;
    private String productId;
    private String orderId;
    @ApiModelProperty(value = "评价星级", example = "1")
    private Long evaluateLevel;
    private String evaluateContent;
    @ApiModelProperty(value = "记录生成时间戳, 自动维护, 可用于排序", hidden = true)
    private String recTime;
    @ApiModelProperty(value = "状态，1-有效，0-无，默认1", example = "1", hidden = true)
    private int dataStatus;

    @TableField(exist = false)
    @ApiModelProperty(value = "开始时间，格式yyyy-MM-dd HH:mm:ss")
    private String beginTime;
    @TableField(exist = false)
    @ApiModelProperty(value = "结束时间,格式yyyy-MM-dd HH:mm:ss")
    private String endTime;
    @TableField(exist = false)
    @ApiModelProperty(value = "第几页，默认1",example = "1")
    private int page;
    @TableField(exist = false)
    @ApiModelProperty(value = "每页数量，默认10",example = "10")
    private int pageSize;
}

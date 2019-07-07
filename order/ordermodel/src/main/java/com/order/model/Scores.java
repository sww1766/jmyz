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
@ApiModel("积分表")
public class Scores extends Model<Scores> {
    @TableId(value = "scores_id", type = IdType.UUID)
    private String scoresId;
    private String accountId;
    @ApiModelProperty(value = "积分数量", example = "200.00")
    private Double scoresAmounts;
    @ApiModelProperty(value = "有效时长，单位秒", example = "60")
    private Long verifyTime;
    @ApiModelProperty(value = "记录生成时间戳, 自动维护, 可用于排序", hidden = true)
    private String recTime;
    @ApiModelProperty(value = "状态，1-有效，0-无，默认1", example = "1", hidden = true)
    private int dataStatus;

}

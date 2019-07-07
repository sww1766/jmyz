package com.order.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotNull;

/**
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("汇率表")
public class ScoreRate extends Model<ScoreRate> {
    @TableId(value = "rate_id", type = IdType.UUID)
    private String rateId;
    @NotNull(message = "金额兑换积分的比例必填")
    @ApiModelProperty(value = "金额兑换积分的比例,例如10块钱兑换10积分，兑换比例100%", example = "1.00")
    private Double rmbScoreRate;
    @NotNull(message = "积分兑换金额的比率必填")
    @ApiModelProperty(value = "积分兑换金额的比率，例如1000积分兑换100人民币，则兑换比率10%", example = "0.10")
    private Double scoreRmbRate;
    @ApiModelProperty(value = "状态，1-有效，0-无，默认1", example = "1", hidden = true)
    private int dataStatus;
}

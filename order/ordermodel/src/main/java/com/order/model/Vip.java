package com.order.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("会员表")
public class Vip extends Model<Vip> {
    @TableId(value = "vip_id", type = IdType.UUID)
    private String vipId;
    @ApiModelProperty(value = "积分数量", example = "1000.00")
    @NotNull(message = "积分数量必填")
    private Double scoreAmounts;
    @ApiModelProperty(value = "会员等级", example = "1")
    @NotNull(message = "会员等级必填")
    private Long level;
    @ApiModelProperty(value = "vip等级的打折率，例如vip等级1，打95折(95%)，则1000块钱需付950块", example = "0.95")
    @NotBlank(message = "vip等级折扣率必填")
    private String vipRmbRate;
}

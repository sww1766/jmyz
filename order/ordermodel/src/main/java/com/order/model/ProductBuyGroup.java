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
@ApiModel("产品购买组合分类表")
public class ProductBuyGroup extends Model<ProductBuyGroup> {
    @TableId(value = "product_buy_group_id", type = IdType.UUID)
    private String productBuyGroupId;
    private String productBuyGroupName;
    @ApiModelProperty(value = "状态，1-有效，0-无，默认1", example = "1", hidden = true)
    private int dataStatus;

}

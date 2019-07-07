package com.order.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.order.model.validator.group.ProductTypeGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("产品类别表")
public class ProductType extends Model<ProductType> {
    @TableId(value = "product_type_id", type = IdType.UUID)
    private String productTypeId;
    private String productTypeName;
    @ApiModelProperty(value = "文件id", example = "1ew1")
    private String fileId;
    @NotBlank(message = "是否热销产品类别，1是，0否",groups = {ProductTypeGroup.class})
    @ApiModelProperty(value = "是否热销产品类别，1是，0否，默认0", example = "0")
    private String isHot;
    @ApiModelProperty(value = "状态，1-有效，0-无，默认1", example = "1", hidden = true)
    private int dataStatus;

    @TableField(exist = false)
    @NotBlank(message = "是否下架，1是，0否,不能为空")
    @ApiModelProperty(value = "是否下架，1是，0否，默认0", example = "0")
    private String isDown;

}

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
@ApiModel("服务表")
public class ProductFw extends Model<ProductFw> {
    @TableId(value = "service_id", type = IdType.UUID)
    private String serviceId;
    private String serviceName;
    @ApiModelProperty(value = "状态，1-有效，0-无，默认1", example = "1", hidden = true)
    private int dataStatus;

}

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
@ApiModel("产品表")
public class Product extends Model<Product> {
    @TableId(value = "product_id", type = IdType.UUID)
    private String productId;
    @ApiModelProperty(value = "产品分类ids,用逗号分开", example = "'1','2'")
    private String productTypeIds;
    @ApiModelProperty(value = "产品组ids,用逗号分开", example = "'1','2'")
    private String productBuyGroupIds;
    @ApiModelProperty(value = "产品服务ids，用逗号分开", example = "'1','2'")
    private String serviceIds;
    private String productName;
    @ApiModelProperty(value = "文件id", example = "1ew1")
    private String fileId;
    private String productCode;
    @ApiModelProperty(value = "记录生成时间戳, 自动维护, 可用于排序", hidden = true)
    private String recTime;
    @ApiModelProperty(value = "vip价", example = "1.11")
    private Double vipPrice;
    @ApiModelProperty(value = "原价", example = "2.00")
    private Double costPrice;
    @ApiModelProperty(value = "库存数量", example = "200")
    private Long amount;
    @ApiModelProperty(value = "销售数量", example = "100")
    private Long sellAmount;
    @ApiModelProperty(value = "是否热销产品，1是，0否，默认0", example = "0")
    private String isHot;
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
    @TableField(exist = false)
    @ApiModelProperty(value = "排序规则，sellAmount，costPrice，vipPrice",example = "sellAmount")
    private String orderByType;
}

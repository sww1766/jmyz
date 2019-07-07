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
@ApiModel("订单表")
public class Orders extends Model<Orders> {
    @TableId(value = "account_id", type = IdType.UUID)
    private String orderId;
    private String accountId;
    private String orderCode;
    @ApiModelProperty(value = "支付金额，不需要传值，后台自行计算", example = "100.00")
    private Double payPrice;
    @ApiModelProperty(value = "产品id", example = "343")
    private String productId;
    @ApiModelProperty(value = "产品购买时分组id", example = "343")
    private String productBuyGroupId;
    @ApiModelProperty(value = "产品服务id", example = "343")
    private String productFwId;
    private String addressId;
    @ApiModelProperty(value = "记录生成时间戳, 自动维护, 可用于排序", hidden = true)
    private String recTime;
    @ApiModelProperty(value = "订单状态，1:待支付，2：待发货，3：发货中，4：已完成,查询时为0则查所有", example = "4")
    private int orderStatus;
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

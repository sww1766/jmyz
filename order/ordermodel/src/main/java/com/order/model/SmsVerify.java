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
@ApiModel("短信验证码")
public class SmsVerify extends Model<SmsVerify> {
    @TableId(value = "sms_id", type = IdType.UUID)
    private String smsId;
    private String smsCode;
    @ApiModelProperty(value = "有效时长，单位秒", example = "60")
    private Long verify_time;
    @ApiModelProperty(value = "记录生成时间戳, 自动维护, 可用于排序", hidden = true)
    private String recTime;
    @ApiModelProperty(value = "状态，1-有效，0-无，默认1", example = "1", hidden = true)
    private int dataStatus;

}

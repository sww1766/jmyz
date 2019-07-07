package com.order.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("地址表")
public class Address extends Model<Address> {
    @TableId(value = "address_id", type = IdType.UUID)
    private String addressId;
    @ApiModelProperty(value = "省代码，从region获取", example = "1")
    private int provinceCode;
    @ApiModelProperty(value = "市代码，从region获取", example = "1")
    private int cityCode;
    @ApiModelProperty(value = "区代码，从region获取", example = "1")
    private int districtCode;
    @ApiModelProperty(value = "街道代码，从region获取", example = "1")
    private int streetCode;
    @ApiModelProperty(value = "详细地址", example = "XX")
    private String address;
    @ApiModelProperty(value = "联系人姓名", example = "XX")
    private String contact;
    @ApiModelProperty(value = "手机号", example = "XX")
    private String mobile;
    @ApiModelProperty(value = "座机号", example = "XX")
    private String tel;
    @ApiModelProperty(value = "备注", example = "XX")
    private String remark;
    @ApiModelProperty(value = "创建人id", example = "XX")
    private String createAccountId;
    @ApiModelProperty(value = "状态，1-有效，0-无，默认1", example = "1", hidden = true)
    private int dataStatus;
    @ApiModelProperty(value = "生成时间", example = "2019-10-10 10:10:10")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date recTime;

}

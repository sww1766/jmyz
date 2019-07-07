package com.sso.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("账号表")
public class Accounts extends Model<Accounts>{
    @TableId(value = "account_id", type = IdType.ID_WORKER)
    private String accountId;
    @ApiModelProperty(value = "账号别名，非必填项", example = "1ew1")
    private String accountName;
    @ApiModelProperty(value = "登录名", example = "1ew1",required = true)
    private String loginName;
    @ApiModelProperty(value = "手机号", example = "188",required = true)
    private String loginMobile;
    @ApiModelProperty(value = "登录密码", example = "123456",required = true)
    private String loginPwd;
    @ApiModelProperty(value = "文件id", example = "1ew1")
    private String fileId;
    @ApiModelProperty(value = "会员，1-是，0-否，默认0",example = "0")
    private int isVip;

    @ApiModelProperty(value = "记录生成时间戳, 自动维护, 可用于排序", hidden = true)
    private String recTime;

    @ApiModelProperty(value = "状态，1-有效，0-无，默认1",example = "1",hidden = true)
    private int dataStatus;
}

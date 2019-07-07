package com.sso.model.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("角色表")
public class SysRole extends Model<SysRole> {
    @TableId(value = "role_id", type = IdType.ID_WORKER)
    private String roleId;
    private String roleName;
    private String remark;
    @ApiModelProperty(value = "创建人ID", example = "1")
    private String createAccountId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "状态，1-有效，0-无，默认1",example = "1",hidden = true)
    private int dataStatus;
}

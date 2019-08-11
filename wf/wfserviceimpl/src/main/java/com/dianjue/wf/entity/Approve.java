package com.dianjue.wf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("审批表")
public class Approve extends Model<Approve> {
  @TableId(value = "approve_id", type = IdType.UUID)
  private String approveId;
  private String modelId;
  private String applyUserId;
  private String applyUserName;
  private String approveCode;
  private String processInstanceId;
  private String approveTitle;
  @ApiModelProperty(value = "记录生成时间戳, 自动维护, 可用于排序", hidden = true)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime recTime;
  private String tenantId;
  private String departId;
  private String accountId;
}

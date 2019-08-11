package com.dianjue.wf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@ApiModel("审批流水表")
public class ApproveFlows extends Model<ApproveFlows> {
  @TableId(value = "flow_id", type = IdType.UUID)
  private String flowId;
  private String approveId;
  private String currentNodeIndex;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime approveTime;
  private int operaType;
  private String verProcessIsEnd;
  private String approveSuggest;
  private int approveStatus;
  @ApiModelProperty(value = "记录生成时间戳, 自动维护, 可用于排序", hidden = true)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime recTime;
  private String tenantId;
  private String departId;
  private String accountId;
  @TableField(exist = false)
  private String approveCode;
  @TableField(exist = false)
  private String approveTitle;
  @TableField(exist = false)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime beginTime;
  @TableField(exist = false)
  private int approveTypeId;

}

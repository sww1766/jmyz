package com.dianjue.wf.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("审批流水入参")
public class ApproveFlowsRequest extends Model<ApproveFlowsRequest> {
  @ApiModelProperty(value = "模板id", example = "模板id")
  private String modelId;
  @ApiModelProperty(value = "申请人id", example = "申请人id")
  private String applyUserId;
  @ApiModelProperty(value = "申请人名称", example = "申请人名称")
  private String applyUserName;
  @ApiModelProperty(value = "标题", example = "标题")
  private String approveTitle;
  @ApiModelProperty(value = "操作类型", example = "1:通过，2:驳回")
  private int operaType;
  @ApiModelProperty(value = "审批意见", example = "同意驳回等等")
  private String approveSuggest;
}

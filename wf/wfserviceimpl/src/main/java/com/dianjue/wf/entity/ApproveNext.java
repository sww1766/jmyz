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
@ApiModel("审批下一步")
public class ApproveNext extends Model<ApproveNext> {
  @ApiModelProperty(value = "审批ID", example = "审批ID")
  private String approveId;
  @ApiModelProperty(value = "操作类型", example = "1:通过，2:驳回")
  private int operaType;
  @ApiModelProperty(value = "审批意见", example = "同意驳回等等")
  private String approveSuggest;

}

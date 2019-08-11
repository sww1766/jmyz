package com.dianjue.wf.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("审批模型入参")
public class ApproveModelRequest extends Model<ApproveModelRequest> {
  @ApiModelProperty(value = "流程模板ID", example = "1")
  private String modelId;
  @ApiModelProperty(value = "模板状态", example = "1:启用，2：禁用")
  private int status;
  @ApiModelProperty(value = "节点", example = "")
  private List<ApproveNode> approveNodeList;

}

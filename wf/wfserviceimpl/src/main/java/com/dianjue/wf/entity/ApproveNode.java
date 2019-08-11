package com.dianjue.wf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("节点参数")
public class ApproveNode {
  @ApiModelProperty(value = "节点id", hidden = true)
  @TableId(value = "node_id", type = IdType.UUID)
  private String nodeId;
  @ApiModelProperty(value = "审批人,格式：json数组", example = "[{'userId':'1','userName':'3'}]")
  private String approveUser;
  @ApiModelProperty(value = "抄送人,格式：json数组", example = "[{'ccId':'1','ccName':'2'}]")
  private String ccUser;
  @ApiModelProperty(value = "节点名称", example = "XXX节点")
  private String nodeName;
  @ApiModelProperty(value = "节点索引，从1开始", example = "1")
  private String nodeIndex;
  @ApiModelProperty(value = "节点类型，审批人:node, 抄送人:read", example = "node")
  private String nodeType;
  @ApiModelProperty(value = "模板id", example = "543e",hidden = true)
  private String modelId;
  @ApiModelProperty(hidden = true)
  private String tenantId;

}

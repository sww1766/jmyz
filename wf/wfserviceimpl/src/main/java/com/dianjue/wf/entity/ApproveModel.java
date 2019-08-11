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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("审批模型表")
public class ApproveModel extends Model<ApproveModel> {
  @TableId(value = "model_id", type = IdType.UUID)
  private String modelId;
  private String modelCode;
  private String modelName;
  private String processKey;
  //类型，0：全部，1：特殊费，2：赔付
  private int approveTypeId;
  private String approveTypeName;
  private int status; // 1:启用，2：禁用
  @ApiModelProperty(value = "记录生成时间戳, 自动维护, 可用于排序", hidden = true)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime recTime;
  private String tenantId;
  private String departId;
  private String accountId;
}

package com.order.model.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@ApiModel(value = "地区行政查询类")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RegionVo {

  @ApiModelProperty(value = "行政地区名称")
  private String regionName;

  @ApiModelProperty(value = "1:省（province ）2:市（city） 3:区，县（district） 默认为2市")
  private Integer level = 2;

  @ApiModelProperty(value = "父id")
  private Integer parentId;
}
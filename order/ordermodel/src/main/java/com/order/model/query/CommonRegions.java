package com.order.model.query;

import com.order.model.Region;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CommonRegions {

  @ApiModelProperty(value = "省")
  private Region province;

  @ApiModelProperty(value = "市")
  private Region city;

  @ApiModelProperty(value = "区")
  private Region district;

}

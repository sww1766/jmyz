package com.order.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("行政表")
public class Region extends Model<Region> {
    @ApiModelProperty(value = "父级地区行政信息[扩展]",example = "")
    @TableField(exist = false)
    private Region parentRegion;

    @ApiModelProperty(value = "主键，自增列")
    @TableId(value = "region_id", type = IdType.AUTO)
    private Integer regionId;

    @ApiModelProperty(value = "行政地区名称")
    private String regionName;

    @ApiModelProperty(value = "1:省（province ）2:市（city） 3:区，县（district）")
    private Integer level;

    @ApiModelProperty(value = "父id")
    private Integer parentId;

    @ApiModelProperty(value = "地址编码")
    private Integer adcode;

    @ApiModelProperty(value = "数据状态 0-删除 1-有效", hidden = true)
    private String dataStatus;

    @Override
    protected Serializable pkVal() {
        return this.regionId;
    }

}

package com.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.order.model.Region;
import com.order.model.query.CommonRegions;
import com.order.model.query.RegionVo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 地区行政编码 服务类
 * </p>
 */
public interface RegionService extends IService<Region> {
    Set<Map<String, Object>> getRegionTree(Integer parentId);
    Set<Map<String, Object>> getRegionByLevel(Integer pId, Integer level);
    /**
     * @description 通过城市名称检索 省区列表
     */
    List<Region> queryList(RegionVo region);
    CommonRegions getSysRegion(String adcode);
}

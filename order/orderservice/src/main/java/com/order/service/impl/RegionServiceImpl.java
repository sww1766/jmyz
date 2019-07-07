package com.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.order.mapper.RegionMapper;
import com.order.model.Region;
import com.order.model.query.CommonRegions;
import com.order.model.query.RegionVo;
import com.order.service.RegionService;
import com.order.utils.enumutil.DataStatusEnum;
import com.order.utils.enumutil.LevelEnums;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 地区行政编码 服务实现类
 * </p>
 */
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements RegionService {

    @Resource
    private RegionService regionService;

    @Override
    public Set<Map<String, Object>> getRegionTree(Integer parentId) {
        List<Region> list = regionService
                .list(new QueryWrapper<Region>().lambda().eq(Region::getParentId, parentId));
        Set<Map<String, Object>> mapSet = new HashSet<>();
        for (Region pRegion : list) {
            Map<String, Object> pMap = new HashMap<>();
            pMap.put("label", pRegion.getRegionName());
            pMap.put("value", pRegion.getRegionId());
            Set<Map<String, Object>> cSet = getRegionTree(pRegion.getRegionId());
            if (!cSet.isEmpty()) {
                pMap.put("children", cSet);
            }
            mapSet.add(pMap);
        }
        return mapSet;
    }

    @Override
    public Set<Map<String, Object>> getRegionByLevel(Integer pId, Integer level) {
        List<Region> list = regionService.list(
                new QueryWrapper<Region>().lambda().eq(Region::getDataStatus, DataStatusEnum.OK.getStatus())
                        .eq(Region::getParentId, pId).le(Region::getLevel, level));
        Set<Map<String, Object>> mapSet = new HashSet<>();
        for (Region pRegion : list) {
            Map<String, Object> pMap = new HashMap<>();
            pMap.put("value", pRegion.getRegionId());
            pMap.put("label", pRegion.getRegionName());
            Set<Map<String, Object>> cSet = getRegionByLevel(pRegion.getRegionId(), level);
            if (!cSet.isEmpty()) {
                pMap.put("children", cSet);
            }
            mapSet.add(pMap);
        }
        return mapSet;
    }

    @Override
    public List<Region> queryList(RegionVo region) {
        QueryWrapper<Region> q = new QueryWrapper<>();
        q.lambda().eq(Region::getDataStatus, DataStatusEnum.OK.getStatus());

        if (Objects.nonNull(region.getParentId())) {
            q.lambda().eq(Region::getParentId, region.getParentId());
        }

        if (StringUtils.isNotBlank(region.getRegionName())) {
            q.lambda().like(Region::getRegionName, region.getRegionName());
        }

        if (Objects.nonNull(region.getLevel())) {
            q.lambda().eq(Region::getLevel, region.getLevel());
        }

        List<Region> list = super.list(q);

        if (region.getLevel() == LevelEnums.CITY.getId()) {
            List<Integer> ids = list
                    .stream()
                    .filter(Objects::nonNull)
                    .map(Region::getParentId)
                    .collect(Collectors.toList());

            Map<Integer, Region> pMaps = super.listByIds(ids)
                    .stream()
                    .collect(Collectors.toMap(Region::getRegionId, x -> x));

            list.forEach(x -> {
                Region r = pMaps.get(x.getParentId());
                if (Objects.nonNull(r)) {
                    x.setParentRegion(r);
                }
            });
        }

        return list;
    }

    @Override
    public CommonRegions getSysRegion(String adcode) {
        if (StringUtils.isEmpty(adcode)) {
            return new CommonRegions();
        }

        CommonRegions sysCommonRegions = new CommonRegions();

        int district = Integer.valueOf(adcode);
        int city = district / 100;
        int province = district / 1000;

        QueryWrapper<Region> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Region::getDataStatus, DataStatusEnum.OK.getStatus())
                .eq(Region::getAdcode, province * 1000)
                .eq(Region::getLevel, 1);
        Region provinceRegion = regionService.getOne(queryWrapper);
        sysCommonRegions.setProvince(provinceRegion);

        queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Region::getDataStatus, DataStatusEnum.OK.getStatus())
                .eq(Region::getAdcode, city * 100)
                .eq(Region::getLevel, 2);
        Region cityRegion = regionService.getOne(queryWrapper);
        sysCommonRegions.setCity(cityRegion);

        queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Region::getDataStatus, DataStatusEnum.OK.getStatus())
                .eq(Region::getAdcode, district)
                .eq(Region::getLevel, 3);
        Region districtRegion = regionService.getOne(queryWrapper);
        sysCommonRegions.setDistrict(districtRegion);

        return sysCommonRegions;
    }

}

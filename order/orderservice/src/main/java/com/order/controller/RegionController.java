package com.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.order.model.Region;
import com.order.model.query.RegionVo;
import com.order.service.RegionService;
import com.order.utils.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 地区行政编码 前端控制器
 */
@RestController
@Api(value = "地区行政编码", tags = {"地区行政编码, 不需要token"})
@RequestMapping("/region")
public class RegionController {

    @Resource
    private RegionService regionService;

    @GetMapping("/getRegion")
    @ApiOperation(value = "获取行政地区【级别不填查询全部；1-查询省；2-查询省市；3-查询省市区；4-查询省市区街道】")
    public ResponseData<Set<Map<String, Object>>> getRegion(Integer level) {
        Set<Map<String, Object>> regionSet;
        if (level != null && level > 0) {
            int i = level;
            List<Integer> arr = Lists.newArrayList(1, 2, 3, 4);
            if (!arr.contains(i)) {
                return new ResponseData<>();
            }
            regionSet = regionService.getRegionByLevel(0, level);
        } else {
            regionSet = regionService.getRegionTree(0);
        }
        return new ResponseData<>(regionSet);
    }

    @GetMapping("/getRegionByIds")
    @ApiOperation(value = "取得行政地区详情")
    public ResponseData<List<Region>> getRegionByIds(
            @RequestParam("regionIds") List<Integer> regionIds) {
        List<Region> list = regionService.listByIds(regionIds)
                .stream()
                .sorted((a, b) -> {
                            if (Objects.isNull(a.getLevel()) || Objects.isNull(b.getLevel())) {
                                return 0;
                            }
                            if (a.getLevel() > b.getLevel()) {
                                return 1;
                            } else if (a.getLevel() < b.getLevel()) {
                                return -1;
                            }
                            return 0;
                        }
                ).collect(Collectors.toList());
        return new ResponseData<>(list);
    }

    @GetMapping("/getRegionByLevel")
    @ApiOperation(value = "取得市级行政地区列表")
    public ResponseData getRegionByLevel() {
        List<Region> list =
                regionService.list(new QueryWrapper<Region>().lambda().eq(Region::getLevel, 2));
        return new ResponseData<>(list);
    }

    @GetMapping("/getRegionList")
    @ApiOperation(value = "根据对象查询列表")
    public ResponseData<List<Region>> getRegionList(Region region) {
        List<Region> list = regionService.list(new QueryWrapper<>(region));
        return new ResponseData<>(list);
    }

    @GetMapping("/queryList")
    @ApiOperation(value = "功能1(通过城市名称来模糊查找市的列表)：参数level=2&regionName=市名|返回的省信息在parentRegion字段上   " +
            "功能2(通过市ID查询城市对应区县列表)：level=3&parentId=城市ID|返回区县信息列表")
    public ResponseData<List<Region>> queryList(RegionVo region) {
        List<Region> list = regionService.queryList(region);
        return new ResponseData<>(list);
    }
}

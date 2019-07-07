package com.order.utils.enumutil;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * mysql中表状态定义
 */
public enum DataStatusEnum {
    OK(1, "正常"),
    DEL(0, "删除");

    private int status;

    private String des;

    DataStatusEnum(int status, String des) {
        this.status = status;
        this.des = des;
    }

    public String getDes() {
        return des;
    }

    public int getStatus() {
        return status;
    }

    public boolean isValid(int status) {
        if (status == DataStatusEnum.OK.status) {
            return true;
        }
        return false;
    }

    public static DataStatusEnum of(int status) {
        for (int i = 0; i < DataStatusEnum.values().length; i++) {
            if (DataStatusEnum.values()[i].getStatus() == status) {
                return DataStatusEnum.values()[i];
            }
        }
        return DataStatusEnum.DEL;
    }

    public static final String CODE = "code";
    public static final String NAME = "name";

    /**
     * 通用枚举列表
     */
    public static List<Map> normalList() {
        return Stream.of(DataStatusEnum.DEL, DataStatusEnum.OK)
                .map(ds -> {
                    Map<String, Object> map = Maps.newHashMap();
                    map.put(DataStatusEnum.CODE, ds.getStatus());
                    map.put(DataStatusEnum.NAME, ds.getDes());
                    return map;
                }).collect(Collectors.toList());
    }

    /**
     * 通用map
     */
    public static Map<Integer, DataStatusEnum> customBizMap() {
        return Stream.of(values()).collect(Collectors.toMap(DataStatusEnum::getStatus, customBizTypeEnum -> customBizTypeEnum));
    }

}

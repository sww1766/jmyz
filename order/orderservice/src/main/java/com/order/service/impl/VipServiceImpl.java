package com.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.order.mapper.VipMapper;
import com.order.model.Vip;
import com.order.service.ScoresService;
import com.order.service.VipService;
import com.order.utils.ResponseData;
import com.sso.model.Accounts;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

@Log4j2
@Service
public class VipServiceImpl extends ServiceImpl<VipMapper, Vip> implements VipService {

    @Resource
    private VipMapper vipMapper;

    @Resource
    private ScoresService scoresService;

    @Override
    public ResponseData operaVip(Vip vip) {
        int result;
        if(StringUtils.isBlank(vip.getVipId())){
            vip.setVipId(UUID.randomUUID().toString().replaceAll("-",""));
            result = vipMapper.insert(vip);
        }else{
            result = vipMapper.updateById(vip);
        }
        return new ResponseData<>(result==1?0:100,"操作结束");
    }

    @Override
    public ResponseData queryVip(Accounts accounts) {
        JSONObject jsonObject = (JSONObject) scoresService.queryScores(accounts).getData();
        Double scoreAmounts = jsonObject.getDouble("scoreAmounts");

        QueryWrapper<Vip> vipQueryWrapper = new QueryWrapper<>();
        vipQueryWrapper.lambda().le(Vip::getScoreAmounts, scoreAmounts).orderByDesc(Vip::getScoreAmounts);

        Vip vip = vipMapper.selectList(vipQueryWrapper).stream().findFirst().
                filter(v -> StringUtils.isNotBlank(v.getVipId())).get();

        return new ResponseData<>(0, vip);
    }
}

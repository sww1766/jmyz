package com.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.order.mapper.EvaluateMapper;
import com.order.model.Evaluate;
import com.order.service.EvaluateService;
import com.order.utils.ResponseData;
import com.sso.model.Accounts;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Log4j2
@Service
public class EvaluateServiceImpl extends ServiceImpl<EvaluateMapper, Evaluate> implements EvaluateService {

    @Resource
    private EvaluateMapper evaluateMapper;

    @Override
    public ResponseData create(Accounts accounts, Evaluate evaluate) {
        evaluate.setAccountId(accounts.getAccountId());
        evaluate.setDataStatus(1);
        evaluate.setRecTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return new ResponseData<>(0, evaluateMapper.insert(evaluate));
    }

    @Override
    public ResponseData listForAdmin(Evaluate evaluate) {
        Page<Evaluate> evaluatePage = new Page<>(evaluate.getPage(),evaluate.getPageSize());
        IPage<Evaluate> evaluateIPage;

        QueryWrapper<Evaluate> evaluateQueryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(evaluate.getAccountId())){
            evaluateQueryWrapper.lambda().eq(Evaluate::getAccountId,evaluate.getAccountId());
        }
        if(StringUtils.isNotBlank(evaluate.getBeginTime()) && StringUtils.isNotBlank(evaluate.getEndTime())){
            evaluateQueryWrapper.lambda().between(Evaluate::getRecTime,evaluate.getBeginTime(),evaluate.getEndTime());
        }
        evaluateQueryWrapper.lambda().orderByDesc(Evaluate::getRecTime);

        evaluateIPage = evaluateMapper.selectPage(evaluatePage, evaluateQueryWrapper);
        return new ResponseData<>(0,evaluateIPage);
    }

    @Override
    public ResponseData list(Evaluate evaluate) {
        Page<Evaluate> evaluatePage = new Page<>(evaluate.getPage(),evaluate.getPageSize());
        IPage<Evaluate> evaluateIPage;

        QueryWrapper<Evaluate> evaluateQueryWrapper = new QueryWrapper<>();
        evaluateQueryWrapper.lambda().eq(Evaluate::getProductId,evaluate.getProductId()).orderByDesc(Evaluate::getRecTime);

        evaluateIPage = evaluateMapper.selectPage(evaluatePage, evaluateQueryWrapper);
        return new ResponseData<>(0,evaluateIPage);
    }

    @Override
    public ResponseData detail(Accounts accounts,Evaluate evaluate) {
        QueryWrapper<Evaluate> evaluateQueryWrapper = new QueryWrapper<>();

        evaluateQueryWrapper.lambda().eq(Evaluate::getAccountId,accounts.getAccountId()).
                eq(Evaluate::getEvaluateId,evaluate.getEvaluateId());

        return new ResponseData<>(0, evaluateMapper.selectList(evaluateQueryWrapper).stream().findFirst().
                filter(ev -> ev.getEvaluateId()!=null).get());
    }

    @Override
    public ResponseData delete(Accounts accounts,Evaluate evaluate) {
        UpdateWrapper<Evaluate> evaluateUpdateWrapper = new UpdateWrapper<>();
        evaluateUpdateWrapper.lambda().eq(Evaluate::getAccountId,accounts.getAccountId()).
                eq(Evaluate::getEvaluateId,evaluate.getEvaluateId());
        evaluate.setDataStatus(0);
        evaluate.setRecTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return new ResponseData<>(0, evaluateMapper.update(evaluate, evaluateUpdateWrapper));
    }
}

package com.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.order.mapper.OrdersMapper;
import com.order.model.Orders;
import com.order.service.OrdersService;
import com.order.utils.ResponseData;
import com.sso.model.Accounts;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Log4j2
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Resource
    private OrdersMapper ordersMapper;

    //TODO
    /* 一、创建订单时计算需付金额，
     1、计算购买的产品一共需支付的金额。
     2、查询该用户的积分总额，并查VIP表计算该用户的VIP等级，
     3、根据VIP等级的兑换比率，计算需付金额。
     二、支付时根据用户选择积分计算实际需要支付的金额，
     1、根据用户选择使用的的积分，查rate表计算积分兑换金额的比率，并计算出可以抵扣多少金额，
     2、根据需付金额减去积分抵扣金额，算出实际需要支付的金额
     三、根据支付金额，查询rate表计算金额兑换积分的比率，算出可以得到的积分，并更新进score积分表数量*/

    @Override
    public ResponseData create(Accounts accounts, Orders orders) {
        orders.setOrderId(UUID.randomUUID().toString().replaceAll("-", ""));
        orders.setOrderCode(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + (int) (Math.random() * 900) + 100);
        orders.setRecTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        orders.setAccountId(accounts.getAccountId());
        orders.setDataStatus(1);
        ordersMapper.insert(orders);


        return new ResponseData<>(0, "创建订单成功");
    }

    @Override
    public ResponseData list(Accounts accounts, Orders orders) {
        Page<Orders> ordersPage = new Page<>(orders.getPage(),orders.getPageSize());
        IPage<Orders> ordersIPage;

        QueryWrapper<Orders> ordersQueryWrapper = new QueryWrapper<>();
        ordersQueryWrapper.lambda().eq(Orders::getAccountId, accounts.getAccountId());

        if (StringUtils.isNotBlank(orders.getBeginTime()) && StringUtils.isNotBlank(orders.getEndTime())) {
            ordersQueryWrapper.lambda().between(
                    Orders::getRecTime, orders.getBeginTime(), orders.getEndTime());
        }

        if (orders.getOrderStatus() != 0) {
            ordersQueryWrapper.lambda().eq(
                    Orders::getOrderStatus, orders.getOrderStatus());
        }

        ordersQueryWrapper.lambda().eq(Orders::getDataStatus,1);
        ordersQueryWrapper.orderByDesc("rec_time");
        ordersIPage = ordersMapper.selectPage(ordersPage, ordersQueryWrapper);

        return new ResponseData<>(0, ordersIPage);
    }

    @Override
    public ResponseData detail(Accounts accounts, Orders orders) {
        QueryWrapper<Orders> ordersQueryWrapper = new QueryWrapper<>();
        ordersQueryWrapper.lambda().eq(Orders::getAccountId, accounts.getAccountId()).and(
                ord -> ord.eq(Orders::getOrderId, orders.getOrderId()).or(
                        ord1 -> ord1.eq(Orders::getOrderCode, orders.getOrderCode())
                )
        );

        return new ResponseData<>(ordersMapper.selectList(ordersQueryWrapper).stream().findFirst().filter(o -> o.getOrderId() != null).get());
    }

}

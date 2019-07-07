package com.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.order.mapper.AddressMapper;
import com.order.model.Address;
import com.order.service.AddressService;
import com.order.utils.ResponseData;
import com.sso.model.Accounts;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
 * @author admin
 */
@Log4j2
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    @Resource
    private AddressMapper addressMapper;

    @Override
    public ResponseData createOrUpdate(Accounts accounts, Address address) {
        //插入
        if(StringUtils.isBlank(address.getAddressId())){
            address.setAddressId(UUID.randomUUID().toString().replaceAll("-", ""));
            address.setRecTime(new Date());
            address.setCreateAccountId(accounts.getAccountId());
            address.setDataStatus(1);
            addressMapper.insert(address);
        }else{
            address.setRecTime(new Date());
            address.setCreateAccountId(accounts.getAccountId());
            addressMapper.updateById(address);
        }

        return new ResponseData<>(0, "创建或修改收货地址成功");
    }

    @Override
    public ResponseData list(Accounts accounts, String createAmountId) {
        QueryWrapper<Address> addressQueryWrapper = new QueryWrapper<>();
        addressQueryWrapper.lambda().eq(Address::getCreateAccountId, accounts.getAccountId());

        return new ResponseData<>(0, addressMapper.selectList(addressQueryWrapper));
    }

    @Override
    public ResponseData detail(Accounts accounts, String addressId) {
        QueryWrapper<Address> addressQueryWrapper = new QueryWrapper<>();
        addressQueryWrapper.lambda().eq(Address::getCreateAccountId, accounts.getAccountId()).
                eq(Address::getAddressId,addressId);

        return new ResponseData<>(addressMapper.selectList(addressQueryWrapper).stream().findFirst().
                filter(ad -> ad.getAddressId() != null).get());
    }


}

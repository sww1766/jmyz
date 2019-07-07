package com.order.service;

import com.order.model.Address;
import com.order.utils.ResponseData;
import com.sso.model.Accounts;

public interface AddressService {
    ResponseData createOrUpdate(Accounts accounts, Address address);
    ResponseData list(Accounts accounts, String createAmountId);
    ResponseData detail(Accounts accounts, String addressId);
}

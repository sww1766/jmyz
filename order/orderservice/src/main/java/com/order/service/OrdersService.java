package com.order.service;

import com.order.model.Orders;
import com.order.utils.ResponseData;
import com.sso.model.Accounts;

public interface OrdersService {
    ResponseData create(Accounts accounts, Orders orders);
    ResponseData list(Accounts accounts, Orders orders);
    ResponseData detail(Accounts accounts, Orders orders);
}

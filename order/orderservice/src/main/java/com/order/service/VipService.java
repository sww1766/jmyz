package com.order.service;

import com.order.model.Vip;
import com.order.utils.ResponseData;
import com.sso.model.Accounts;

public interface VipService {

    ResponseData operaVip(Vip vip);

    ResponseData queryVip(Accounts accounts);
}

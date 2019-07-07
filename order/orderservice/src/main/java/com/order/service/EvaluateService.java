package com.order.service;

import com.order.model.Evaluate;
import com.order.utils.ResponseData;
import com.sso.model.Accounts;

public interface EvaluateService {
    ResponseData create(Accounts accounts, Evaluate evaluate);
    ResponseData listForAdmin(Evaluate evaluate);
    ResponseData list(Evaluate evaluate);
    ResponseData detail(Accounts accounts,Evaluate evaluate);
    ResponseData delete(Accounts accounts,Evaluate evaluate);
}

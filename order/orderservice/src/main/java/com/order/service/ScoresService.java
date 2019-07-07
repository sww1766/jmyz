package com.order.service;

import com.order.utils.ResponseData;
import com.sso.model.Accounts;

public interface ScoresService {

    ResponseData queryScores(Accounts accounts);
}

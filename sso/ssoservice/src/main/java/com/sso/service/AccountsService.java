package com.sso.service;

import com.sso.model.Accounts;
import com.sso.model.sys.SysRole;
import com.sso.utils.ResponseData;

import java.util.List;

public interface AccountsService {

    ResponseData register(Accounts accounts);

    ResponseData login(String mobileOrLoginName, String password);

    Accounts getAccountsByUserName(String userName);

    List<SysRole> getSysRole(String accessToken, String accountId);

    ResponseData<Accounts> getAccountsByToken(String accessToken);

    ResponseData<List<SysRole>> getRolesByToken(String accessToken);
}

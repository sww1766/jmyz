package com.order.api;

import com.order.utils.ResponseData;
import com.sso.model.Accounts;
import com.sso.model.sys.SysRole;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author admin
 */
@FeignClient(value = "SSO-SERVICE",fallback = AccountServiceFallBack.class)
public interface AccountService {

  /**
   * 根据token获取用户信息
   * @param accessToken
   * @return
   */
  @PostMapping("/account/getAccountsByToken")
  ResponseData<Accounts> getAccountsByToken(@RequestParam("accessToken") String accessToken);

  /**
   * 根据token获取用户角色列表
   * @param accessToken
   * @return
   */
  @PostMapping("/account/getRolesByToken")
  ResponseData<List<SysRole>> getRolesByToken(@RequestParam("accessToken") String accessToken);
}
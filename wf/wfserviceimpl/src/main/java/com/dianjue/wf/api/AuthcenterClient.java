package com.dianjue.wf.api;

import com.dianjue.commonutils.data.TokenUtils;
import com.dianjue.commonutils.responseinfo.ResponseData;
import com.dianjue.sso.dto.UserInfoDto;
import com.dianjue.wf.api.fallback.AuthCenterClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
@FeignClient(name = "AUTHCENTERSERVICE-SWW", fallback = AuthCenterClientFallback.class)
public interface AuthcenterClient {
  /**
   * @description 获取当前用户登录信息
   */
  @GetMapping(value = "/user/getToken")
  ResponseData<UserInfoDto> getToken(
      @RequestHeader(name = TokenUtils.AUTHORIZATION) String token);


}

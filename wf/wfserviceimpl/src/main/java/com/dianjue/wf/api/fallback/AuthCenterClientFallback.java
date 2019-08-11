package com.dianjue.wf.api.fallback;

import com.dianjue.commonutils.enumutils.SysResponseEnum;
import com.dianjue.commonutils.responseinfo.ResponseData;
import com.dianjue.sso.dto.UserInfoDto;
import com.dianjue.wf.api.AuthcenterClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * @author wangpeng
 * @description 用户服务的降级
 * @date
 */
@Log4j2
@Component
public class AuthCenterClientFallback implements AuthcenterClient {
  @Override
  public ResponseData<UserInfoDto> getToken(String token) {
    log.error("NETWORK_ERROR getToken  token =" + token);
    return new ResponseData<UserInfoDto>().result(SysResponseEnum.NETWORK_ERROR);
  }

}

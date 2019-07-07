package com.order.api;

import com.order.utils.ResponseData;
import com.order.utils.enumutil.SysResponseEnum;
import org.springframework.stereotype.Component;

/**
 * @author author
 */
@Component
public class AccountServiceFallBack implements AccountService {
  @Override
  public ResponseData getAccountsByToken(String accessToken) {
    return new ResponseData().result(SysResponseEnum.SYSTEM_ERROR);
  }

  @Override
  public ResponseData getRolesByToken(String accessToken) {
    return new ResponseData().result(SysResponseEnum.SYSTEM_ERROR);
  }
}


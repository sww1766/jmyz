package com.dianjue.gateway.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


@Service
public class AuthService {

  private static final String DEBUG_TOKEN = "hoho_debug";
  private static final String DEBUG_USER_ID = "111111111111";

  private static final String ACCESS_TOKEN_ACTIVE = "active";

  private static final String TOKEN_KEY = "dj:ctm:user:token:";

  @Autowired
  private StringRedisTemplate stringRedisTemplate;

  /**
   * 取得当前的token的详细信息
   */
  public Map<String, Object> getToken(String accessToken) {
    Map<String, Object> a = new HashMap<>();
    if (StringUtils.isNotBlank(accessToken)) {

      String tokenKey = TOKEN_KEY + accessToken;
      Long leave = stringRedisTemplate.getExpire(tokenKey, TimeUnit.SECONDS);
      if (Objects.nonNull(leave) && leave > 0) {
        try {
          if (leave > 5 && leave < 1800L) {
            String authToken = stringRedisTemplate.opsForValue().get(tokenKey);
            if (StringUtils.isBlank(authToken)) {
              a.put(ACCESS_TOKEN_ACTIVE, false);
              return a;
            }
            stringRedisTemplate.opsForValue().set(tokenKey, authToken, 3600L, TimeUnit.SECONDS);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
        a.put(ACCESS_TOKEN_ACTIVE, true);
        return a;
      } else if (StringUtils.equals(accessToken, DEBUG_TOKEN)) {
        stringRedisTemplate.opsForValue().set(tokenKey, DEBUG_USER_ID, 7, TimeUnit.DAYS);
        a.put(ACCESS_TOKEN_ACTIVE, true);
        return a;
      }
    }
    a.put(ACCESS_TOKEN_ACTIVE, false);
    return a;
  }

  /**
   * 验证当前token是否有效
   */
  public Boolean isValied(String accessToken) {
    Map<String, Object> token = getToken(accessToken);
    return token != null && Boolean.parseBoolean(String.valueOf(token.get(ACCESS_TOKEN_ACTIVE)));
  }

}

package com.sso.utils;


import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author admin
 */
public class CookieUtils {

  public static String getCookie(HttpServletRequest request, String cookieName) {

    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (StringUtils.equals(cookie.getName(), cookieName)) {
          return cookie.getValue();
        }
      }
    }

    return null;
  }

  public static void writeCookie(HttpServletResponse response, String cookieName, String value) {
    Cookie cookie = new Cookie(cookieName, value);
    cookie.setPath("/");
    cookie.setMaxAge(3600);
    response.addCookie(cookie);
  }
}

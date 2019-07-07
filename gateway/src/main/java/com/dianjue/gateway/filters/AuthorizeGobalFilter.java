package com.dianjue.gateway.filters;

import com.dianjue.gateway.constant.GlobalConstant;
import com.dianjue.gateway.service.AuthService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthorizeGobalFilter implements GlobalFilter, Ordered {

    private static final List<String> WHITE_MASK_LIST = Collections
            .synchronizedList(new ArrayList<>());

    static {
        WHITE_MASK_LIST.clear();

        /**--------sso模块--------**/
        String serviceName = "authcenter";
        //swagger
        append_swagger_WhiteList(serviceName);
        //登录
        appendWhiteList(serviceName, "/user/login");
        appendWhiteList(serviceName, "/user/mobilelogin");
        appendWhiteList(serviceName, "/user/mobilevcode");
        appendWhiteList(serviceName, "/user/checkmobilevcode");
        appendWhiteList(serviceName, "/user/getKaptchaCode");
        appendWhiteList(serviceName, "/applogin/getSessionKey");
        appendWhiteList(serviceName, "/applogin/deciphering");
        appendWhiteList(serviceName, "/applogin/getAppStatus");
        appendWhiteList(serviceName, "/applogin/getMockLoginToken");


        /**--------公用模块--------**/
        serviceName = "common";
        append_swagger_WhiteList(serviceName);
        appendWhiteList(serviceName, "/index");
        appendWhiteList(serviceName, "/v1/send");

        /**--------字典模块--------**/
        serviceName = "libraries";
        append_swagger_WhiteList(serviceName);

        /**--------日志模块--------**/
        serviceName = "logbase";
        append_swagger_WhiteList(serviceName);

        /**--------资源模块--------**/
        serviceName = "datum";
        append_swagger_WhiteList(serviceName);

        /**--------订单模块--------**/
        serviceName = "order";
        append_swagger_WhiteList(serviceName);
        appendWhiteList(serviceName, "/dataio/heartbeat");
        appendWhiteList(serviceName, "/pointup/applet");
        appendWhiteList(serviceName, "/pointup/getSessionKey");
        appendWhiteList(serviceName, "/pointup/deciphering");
        appendWhiteList(serviceName, "/pointup/getAppStatus");


        /**--------财务模块--------**/
        serviceName = "finance";
        append_swagger_WhiteList(serviceName);

        /**--------财务模块--------**/
        serviceName = "settle";
        append_swagger_WhiteList(serviceName);

        /**--------平台模块--------**/
        serviceName = "platform";
        append_swagger_WhiteList(serviceName);
        appendWhiteList(serviceName, "/column/show/tree/list");
        appendWhiteList(serviceName, "/article/show/detail");
        appendWhiteList(serviceName, "/article/list");

        /**--------导入导出模块--------**/
        serviceName = "dataio";
        append_swagger_WhiteList(serviceName);

    }

    static void appendWhiteList(String serviceName, String path) {
        WHITE_MASK_LIST.add("/" + serviceName + path);
    }

    /**
     * swagger 白名单增加
     */
    static void append_swagger_WhiteList(String serviceName) {
        WHITE_MASK_LIST.add("/" + serviceName + "/swagger-ui");
        WHITE_MASK_LIST.add("/" + serviceName + "/swagger-resources");
        WHITE_MASK_LIST.add("/" + serviceName + "/v2/api-docs");
        WHITE_MASK_LIST.add("/" + serviceName + "/webjars");
    }

    @Autowired
    AuthService authService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        String token = null;

        List<List<HttpCookie>> hc = request.getCookies().values().stream()
                .filter(n -> n.stream()
                        .anyMatch(m -> StringUtils.equals(m.getName(), GlobalConstant.ACCESS_TOKEN)))
                .collect(Collectors.toList());

        if (hc.size() > 0) {
            List<String> tokens = hc.get(0).stream()
                    .filter(m -> StringUtils.equals(m.getName(), GlobalConstant.ACCESS_TOKEN))
                    .map(HttpCookie::getValue)
                    .collect(Collectors.toList());

            if (tokens.size() > 0) {
                token = tokens.get(0);
            }
        }

        if (token == null) {
            HttpHeaders headers = request.getHeaders();
            token = headers.getFirst(GlobalConstant.ACCESS_TOKEN);
        }

        if (token == null) {
            token = request.getQueryParams().getFirst(GlobalConstant.ACCESS_TOKEN);
        }

        ServerHttpResponse response = exchange.getResponse();
        final String requesStr = request.getURI().getPath().toString();
        if (WHITE_MASK_LIST.stream().noneMatch(requesStr::contains) && !authService
                .isValied(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}


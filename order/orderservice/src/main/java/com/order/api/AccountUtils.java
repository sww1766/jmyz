package com.order.api;

import com.order.utils.TokenUtils;
import com.order.utils.redis.RedisUtil;
import com.sso.model.sys.SysRole;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author admin
 */
@Component
public class AccountUtils {
    @Resource
    private RedisUtil redisUtil;

    public boolean isHasRole(HttpServletRequest request, AccountService accountService) {
        if (CollectionUtils.isNotEmpty((List<SysRole>)redisUtil.get("ROLELIST-" + TokenUtils.getRequestToken(request)))){
            List<SysRole> roles = (List<SysRole>)redisUtil.get("ROLELIST-" + TokenUtils.getRequestToken(request));
            return roles.stream().noneMatch(r -> "admin".equals(r.getRoleName()));
        }else {
            if(CollectionUtils.isEmpty(accountService.getRolesByToken(TokenUtils.getRequestToken(request)).getData())){
                return true;
            }else{
                List<SysRole> roles = accountService.getRolesByToken(TokenUtils.getRequestToken(request)).getData();
                return roles.stream().noneMatch(r -> "admin".equals(r.getRoleName()));
            }
        }
    }
}

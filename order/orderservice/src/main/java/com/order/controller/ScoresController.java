package com.order.controller;

import com.order.api.AccountService;
import com.order.service.ScoresService;
import com.order.utils.ResponseData;
import com.order.utils.TokenUtils;
import com.sso.model.Accounts;
import com.sso.model.sys.SysRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author admin
 */
@Api(value = "积分管理接口", tags = "积分管理接口，需要token，且必须是管理员角色账号")
@Log4j2
@RestController
@RequestMapping("score")
public class ScoresController {

	@Resource
	private ScoresService scoresService;

	@Resource
	private AccountService accountService;

	/**
	 * 查询用户的可用积分和可以兑换的RMB
	 */
	@ApiOperation(value = "查询用户的可用积分",
			notes = "查询用户的可用积分")
	@PostMapping(value = "queryScores")
	public ResponseData queryScores(HttpServletRequest request) {
		if(accountService.getRolesByToken(TokenUtils.getRequestToken(request)).getData()==null){
			return new ResponseData(100,"没有权限.");
		}
		ResponseData<List<SysRole>> res = accountService.getRolesByToken(TokenUtils.getRequestToken(request));
		if(!res.getData().stream().collect(Collectors.toMap(SysRole::getRoleId,SysRole::getRoleName)).containsValue("admin")){
			return new ResponseData(100,"没有权限.");
		}
		Accounts accounts = accountService.getAccountsByToken(TokenUtils.getRequestToken(request)).getData();
		return scoresService.queryScores(accounts);
	}

}

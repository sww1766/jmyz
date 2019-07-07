package com.order.controller;

import com.order.api.AccountService;
import com.order.model.Orders;
import com.order.service.OrdersService;
import com.order.utils.ResponseData;
import com.order.utils.TokenUtils;
import com.sso.model.Accounts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author admin
 */
@Api(value = "订单接口", tags = "订单接口,需要token")
@Log4j2
@RestController
@RequestMapping("order")
public class OrderController {

	@Resource
	private OrdersService ordersService;

	@Resource
	private AccountService accountService;

	/**
	 * 生成订单
	 */
	@ApiOperation(value = "生成订单", notes = "生成订单")
	@PostMapping(value = "create")
	public ResponseData create(Orders orders, HttpServletRequest request) {
		if(accountService.getAccountsByToken(TokenUtils.getRequestToken(request)).getData()==null){
			return new ResponseData(100,"请先登录.");
		}
		Accounts accounts = accountService.getAccountsByToken(TokenUtils.getRequestToken(request)).getData();
		return ordersService.create(accounts, orders);
	}


	/**
	 * 查询我的订单列表
	 */
	@ApiOperation(value = "查询我的订单列表", notes = "查询我的订单列表")
	@ApiImplicitParams({
			@ApiImplicitParam(
					name = "page",
					value = "当前页码(page)",
					example = "1",
					dataType = "int",
					required = true),
			@ApiImplicitParam(
					name = "pageSize",
					value = "每页展示条数(pageSize)",
					example = "10",
					dataType = "int",
					required = true)
	})
	@GetMapping(value = "list")
	public ResponseData list(Orders orders, HttpServletRequest request) {
		if(accountService.getAccountsByToken(TokenUtils.getRequestToken(request)).getData()==null){
			return new ResponseData(100,"请先登录.");
		}
		Accounts accounts = accountService.getAccountsByToken(TokenUtils.getRequestToken(request)).getData();
		return ordersService.list(accounts, orders);
	}

	/**
	 * 查询我的订单详情
	 */
	@ApiOperation(value = "查询我的订单详情,入参 id或code", notes = "查询我的订单详情")
	@GetMapping(value = "detail")
	public ResponseData detail(Orders orders, HttpServletRequest request) {
		if(accountService.getAccountsByToken(TokenUtils.getRequestToken(request)).getData()==null){
			return new ResponseData(100,"请先登录.");
		}
		Accounts accounts = accountService.getAccountsByToken(TokenUtils.getRequestToken(request)).getData();
		return ordersService.detail(accounts, orders);
	}

}

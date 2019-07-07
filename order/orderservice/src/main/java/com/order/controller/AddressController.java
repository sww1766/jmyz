package com.order.controller;

import com.order.api.AccountService;
import com.order.model.Address;
import com.order.service.AddressService;
import com.order.utils.ResponseData;
import com.order.utils.TokenUtils;
import com.sso.model.Accounts;
import io.swagger.annotations.Api;
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
@Api(value = "收货地址接口", tags = "收货地址接口,需要token")
@Log4j2
@RestController
@RequestMapping("address")
public class AddressController {

	@Resource
	private AddressService addressService;

	@Resource
	private AccountService accountService;

	/**
	 * 创建收货地址
	 */
	@ApiOperation(value = "创建收货地址", notes = "创建收货地址")
	@PostMapping(value = "createOrUpdate")
	public ResponseData createOrUpdate(Address address, HttpServletRequest request) {
		if(accountService.getAccountsByToken(TokenUtils.getRequestToken(request)).getData()==null){
			return new ResponseData(100,"请先登录.");
		}
		Accounts accounts = (Accounts) accountService.getAccountsByToken(TokenUtils.getRequestToken(request)).getData();
		return addressService.createOrUpdate(accounts, address);
	}

	/**
	 * 查询我的收货地址列表
	 */
	@ApiOperation(value = "查询我的收货地址,入参createAmountId", notes = "查询我的收货地址")
	@GetMapping(value = "list")
	public ResponseData list(String createAmountId, HttpServletRequest request) {
		if(accountService.getAccountsByToken(TokenUtils.getRequestToken(request)).getData()==null){
			return new ResponseData(100,"请先登录.");
		}
		Accounts accounts = (Accounts) accountService.getAccountsByToken(TokenUtils.getRequestToken(request)).getData();
		return addressService.list(accounts, createAmountId);
	}

	/**
	 * 查询我的收货地址详情
	 */
	@ApiOperation(value = "查询我的收货地址,入参 addressId", notes = "查询我的收货地址")
	@GetMapping(value = "detail")
	public ResponseData detail(String addressId, HttpServletRequest request) {
		if(accountService.getAccountsByToken(TokenUtils.getRequestToken(request)).getData()==null){
			return new ResponseData(100,"请先登录.");
		}
		Accounts accounts = (Accounts) accountService.getAccountsByToken(TokenUtils.getRequestToken(request)).getData();
		return addressService.detail(accounts, addressId);
	}
	
}

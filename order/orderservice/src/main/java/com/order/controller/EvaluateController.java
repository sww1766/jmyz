package com.order.controller;

import com.order.api.AccountService;
import com.order.api.AccountUtils;
import com.order.model.Evaluate;
import com.order.service.EvaluateService;
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
@Api(value = "评论接口", tags = "评论接口,需要token")
@Log4j2
@RestController
@RequestMapping("evaluate")
public class EvaluateController {

	@Resource
	private EvaluateService evaluateService;

	@Resource
	private AccountService accountService;

	@Resource
	private AccountUtils accountUtils;

	/**
	 * 创建评论
	 */
	@ApiOperation(value = "创建评论", notes = "创建评论")
	@PostMapping(value = "create")
	public ResponseData create(Evaluate evaluate, HttpServletRequest request) {
		if(accountService.getAccountsByToken(TokenUtils.getRequestToken(request)).getData()==null){
			return new ResponseData(100,"请先登录.");
		}
		Accounts accounts = accountService.getAccountsByToken(TokenUtils.getRequestToken(request)).getData();
		return evaluateService.create(accounts, evaluate);
	}

	/**
	 * 查询评价列表
	 */
	@ApiOperation(value = "查询产品的评价列表", notes = "查询产品的评价列表，不需要token")
	@ApiImplicitParams({
			@ApiImplicitParam(
					name = "productId",
					value = "查询某个人的评价列表，如果accountId为空，则查询所有人的列表",
					example = "xxx",
					dataType = "string"),
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
	public ResponseData list(Evaluate evaluate) {
		return evaluateService.list(evaluate);
	}


	/**
	 * 管理员查询评价列表
	 */
	@ApiOperation(value = "查询评价列表", notes = "专供管理员查询评价列表，查询所有或者某个人所有的")
	@ApiImplicitParams({
			@ApiImplicitParam(
					name = "accountId",
					value = "查询某个人的评价列表，如果accountId为空，则查询所有人的列表",
					example = "xxx",
					dataType = "string"),
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
	@GetMapping(value = "listForAdmin")
	public ResponseData listForAdmin(Evaluate evaluate, HttpServletRequest request) {
		if (accountUtils.isHasRole(request, accountService)){
			return new ResponseData(100, "没有权限.");
		}

		return evaluateService.listForAdmin(evaluate);
	}

	/**
	 * 查询我的评价详情
	 */
	@ApiOperation(value = "查询我的订单详情,入参 id或code", notes = "查询我的订单详情")
	@GetMapping(value = "detail")
	public ResponseData detail(Evaluate evaluate, HttpServletRequest request) {
		if(accountService.getAccountsByToken(TokenUtils.getRequestToken(request)).getData()==null){
			return new ResponseData(100,"请先登录.");
		}
		Accounts accounts = accountService.getAccountsByToken(TokenUtils.getRequestToken(request)).getData();
		return evaluateService.detail(accounts,evaluate);
	}


	/**
	 * 删除评价
	 */
	@ApiOperation(value = "删除评价,入参 id,accountId", notes = "删除评价")
	@GetMapping(value = "delete")
	public ResponseData delete(Evaluate evaluate, HttpServletRequest request) {
		if(accountService.getAccountsByToken(TokenUtils.getRequestToken(request)).getData()==null){
			return new ResponseData(100,"请先登录.");
		}
		Accounts accounts = accountService.getAccountsByToken(TokenUtils.getRequestToken(request)).getData();
		return evaluateService.delete(accounts, evaluate);
	}
}

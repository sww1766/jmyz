package com.sso.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.sso.model.Accounts;
import com.sso.model.sys.SysRole;
import com.sso.service.AccountsService;
import com.sso.utils.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * @author admin
 */
@Api(value = "注册登录接口", tags = "注册登录接口，不需要token")
@Log4j2
@RestController
@RequestMapping("account")
public class AccountsController {
	@Resource
	private Producer producer;

	@Resource
	private AccountsService accountsService;

	@ApiOperation(value = "图片验证码", notes = "图片验证码",hidden = true)
	@RequestMapping("captcha.jpg")
	public void captcha(HttpServletResponse response)throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //生成文字验证码
        String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);
        //保存到shiro session
		SecurityUtils.getSubject().getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, text);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
		out.flush();
	}

	/**
	 * 注册
	 */
	@ApiOperation(value = "注册", notes = "注册")
	@PostMapping(value = "register")
	public ResponseData register(Accounts accounts) {
		//TODO 手机验证码

		return accountsService.register(accounts);
	}
	
	/**
	 * 登录
	 */
	@ApiOperation(value = "登录", notes = "登录")
	@PostMapping(value = "login")
	public ResponseData login(String mobileOrLoginName, String password) {
		return accountsService.login(mobileOrLoginName, password);
	}

	@ApiOperation(value = "根据token获取账号信息", notes = "根据token获取账号信息")
	@PostMapping(value = "getAccountsByToken")
	public ResponseData<Accounts> getAccountsByToken(@RequestParam("accessToken") String accessToken){
		return accountsService.getAccountsByToken(accessToken);
	}

	@ApiOperation(value = "根据token获取角色信息", notes = "根据token获取角色信息")
	@PostMapping(value = "getRolesByToken")
	public ResponseData<List<SysRole>> getRolesByToken(@RequestParam("accessToken") String accessToken){
		return accountsService.getRolesByToken(accessToken);
	}

}

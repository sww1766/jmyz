package com.dianjue.wf.api;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dianjue.wf.entity.SysAppVersionEntity;
import com.dianjue.wf.service.SysAppVersionService;
import com.dianjue.wf.utils.ChkUtil;
import com.dianjue.wf.utils.CommonResponse;
import com.dianjue.wf.utils.annotation.IgnoreAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * app检查版本
 * @author fq
 *
 */
@RestController
@RequestMapping("/api/version")
@Api("app检查版本接口")
public class ApiAppVersionController {
	 @Autowired
	 private SysAppVersionService sysAppVersionService;
	 
	 @IgnoreAuth
	 @ApiOperation(value="检查app是否需要更新", notes="")
	 @RequestMapping(value ="/check/{level}", method = RequestMethod.GET)
	 public CommonResponse check(@PathVariable("level")Long level){
		CommonResponse response = new CommonResponse();
		
		if (ChkUtil.isEmpty(level)){
			response.setSuccess(false);
			response.setCode("0");
			response.setMsg("参数不能为空！");
			return response;
		}
		
		try {
			long code = 3;
			SysAppVersionEntity app = sysAppVersionService.queryLast();
			if (app != null){
				if (level < app.getLevel()){
					code = app.getCode();
				}
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", code);
			map.put("level", app.getLevel());
			if (!ChkUtil.isEmpty(code) &&  code != 3){
				map.put("id", app.getId());
				map.put("name", app.getName());
				try {
					InputStream in = new FileInputStream(app.getUrl());
					if (in != null){
						try {
							map.put("size", in.available());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			response.setData(map);
			response.setSuccess(true);
			response.setCode("1");
			response.setMsg("操作成功");
		} catch (RuntimeException e) {
			response.setSuccess(false);
			response.setCode("0");
			response.setMsg("系统错误,请稍后重试或者联系管理员");
		}
		
		return response;
	 }
	 
	 
	 @IgnoreAuth
	 @ApiOperation(value="下载app", notes="")
	 @RequestMapping(value ="/download/{id}", method = RequestMethod.GET)
	 public void download(@PathVariable("id") Long id,HttpServletResponse response, HttpServletRequest request) throws Exception {
		SysAppVersionEntity app = sysAppVersionService.queryObject(id);
		if (app != null){
			downLoad(response, request, app.getLevel(), app.getUrl());
		}
	 }
	
	private static void downLoad(HttpServletResponse response, HttpServletRequest request,long level, String downPath) throws FileNotFoundException{
		try {
			request.setCharacterEncoding("UTF-8");
			String name = "appversion" + level;//获取要下载的文件名
		    //第一步：设置响应类型
			response.setContentType("multipart/form-data");  
		    //第二读取文件
		    InputStream in = new FileInputStream(downPath);
		    //设置响应头，对文件进行url编码
		    response.setHeader("Content-Disposition", "attachment;fileName="+name+"."+downPath.split("\\.")[1]);     
		    
		    //第三步：老套路，开始copy
		    OutputStream out = response.getOutputStream();
		    byte[] b = new byte[1024];
		    int len = 0;
		    while((len = in.read(b))!=-1){
		      out.write(b, 0, len);
		    }
		    out.flush();
		    out.close();
		    in.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

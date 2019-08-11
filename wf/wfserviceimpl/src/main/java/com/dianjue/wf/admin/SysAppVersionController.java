package com.dianjue.wf.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dianjue.wf.entity.SysAppVersionEntity;
import com.dianjue.wf.service.SysAppVersionService;
import com.dianjue.wf.utils.Constant;
import com.dianjue.wf.utils.DateUtil;
import com.dianjue.wf.utils.PageUtils;
import com.dianjue.wf.utils.Query;
import com.dianjue.wf.utils.R;
import com.dianjue.wf.utils.RRException;



/**
 * app检测更新版本
 *
 * @author fengqiang
 * @email 283726588@qq.com
 * @date 2018-06-12 14:00:38
 */
@RestController
@RequestMapping("sys/appversion")
public class SysAppVersionController extends AbstractController{
    @Autowired
    private SysAppVersionService sysAppVersionService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:appversion:list")
    public R list(@RequestParam Map<String, Object> params){
    	//只有超级管理员，才能查看所有管理员列表
		if(getUserId() != Constant.SUPER_ADMIN){
			params.put("createUserId", getUserId());
		}
		
		//查询列表数据
		Query query = new Query(params);
		List<SysAppVersionEntity> userList = sysAppVersionService.queryList(query);
		int total = sysAppVersionService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(userList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        return R.ok().put("appversion", sysAppVersionService.queryObject(id));
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:appversion:save")
    public R save(@RequestBody SysAppVersionEntity sysAppVersion){
    	if (sysAppVersion != null){
    		sysAppVersion.setCreateTime(DateUtil.getCurrentTime());
    	}
    	sysAppVersionService.save(sysAppVersion);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:appversion:update")
    public R update(@RequestBody SysAppVersionEntity sysAppVersion){
    	sysAppVersionService.update(sysAppVersion);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:appversion:delete")
    public R delete(@RequestBody Long[] ids){
    	sysAppVersionService.deleteBatch(ids);

        return R.ok();
    }
    
    /**
	 * 上传文件
	 */
	@RequestMapping("/upload")
	public R upload(@RequestParam("file") MultipartFile file) throws Exception {
		if (file.isEmpty()) {
			throw new RRException("上传文件不能为空");
		}

		//上传文件
		String fileName = file.getOriginalFilename();
		String suffixName = UUID.randomUUID() + fileName.substring(fileName.lastIndexOf("."));
		String pathBase = "/home/eqpt/eqptFiles/appFiles" + File.separator + DateUtil.getCurrentTime("yyyy-MM-dd");
//		String pathBase = "D:\\home\\eqpt\\appFiles" + File.separator + DateUtil.getCurrentTime("yyyy-MM-dd");
		
		//上传到服务器
		File upfile = new File(pathBase + File.separator +suffixName);
		// 检测是否存在目录
        if (!upfile.getParentFile().exists()) {
        	upfile.getParentFile().mkdirs();
        }
		file.transferTo(upfile);

		return R.ok().put("path", pathBase + File.separator +suffixName);
	}
	
	@RequestMapping("/download/{id}")
	public void download(@PathVariable("id") Long id,HttpServletResponse response, HttpServletRequest request) throws Exception {
		SysAppVersionEntity app = sysAppVersionService.queryObject(id);
		if (app != null){
			downLoad(response, request, app.getLevel(), app.getUrl());
		}
	}
	
	private static void downLoad(HttpServletResponse response, HttpServletRequest request,long level, String downPath) throws FileNotFoundException{
		
        // 读到流中
        InputStream inStream = new FileInputStream(downPath);// 文件的存放路径
        // 设置输出的格式
        response.reset();
        response.setContentType("application/octet-stream;charset=ISO8859-1");  
        response.setContentType("bin");
        response.addHeader("Pargam", "no-cache"); 
        /* 
         * 解决各浏览器的中文乱码问题 
         */ 
        try {
        	 String userAgent = request.getHeader("User-Agent");  
             response.setHeader("Content-disposition",  
                     String.format("attachment; filename=\"%s\"", "appversion"+level));  
		} catch (Exception e) {
			// TODO: handle exception
		}
        // 循环取出流中的数据
        byte[] b = new byte[100];
        int len;
        try {
            while ((len = inStream.read(b)) > 0)
                response.getOutputStream().write(b, 0, len);
            inStream.close();
        } catch (IOException e) {
        }
	}
}

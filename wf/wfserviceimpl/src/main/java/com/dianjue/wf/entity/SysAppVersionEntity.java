package com.dianjue.wf.entity;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

/**
 * app检测更新版本
 * 
 * @author fengqiang
 * @email 283726588@qq.com
 * @date 2018-06-12 14:00:38
 */
public class SysAppVersionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private Long id;
	/**
	 * app版本等级
	 */
	@NotBlank(message="app版本等级参数值不能为空")
	private Long level;
	/**
	 * 更新级别编码
	 */
	@NotBlank(message="更新级别编码参数值不能为空")
	private Long code;
	/**
	 * 更新url
	 */
	@NotBlank(message="更新url参数值不能为空")
	private String url;
	
	/**
	 * 名称
	 */
	@NotBlank(message="名称不能为空")
	private String name;
	/**
	 * 
	 */
	private String createTime;

	/**
	 * 设置：
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：app版本等级
	 */
	public void setLevel(Long level) {
		this.level = level;
	}
	/**
	 * 获取：app版本等级
	 */
	public Long getLevel() {
		return level;
	}
	/**
	 * 设置：更新级别编码
	 */
	public void setCode(Long code) {
		this.code = code;
	}
	/**
	 * 获取：更新级别编码
	 */
	public Long getCode() {
		return code;
	}
	/**
	 * 设置：更新url
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * 获取：更新url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * 设置：
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：
	 */
	public String getCreateTime() {
		return createTime;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}

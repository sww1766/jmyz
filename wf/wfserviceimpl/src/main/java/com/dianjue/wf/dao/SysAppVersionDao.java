package com.dianjue.wf.dao;

import com.dianjue.wf.entity.SysAppVersionEntity;

/**
 * app检测更新版本
 * 
 * @author fengqiang
 * @email 283726588@qq.com
 * @date 2018-06-12 14:00:38
 */
public interface SysAppVersionDao extends BaseDao<SysAppVersionEntity> {
	
	public SysAppVersionEntity queryLast();
}

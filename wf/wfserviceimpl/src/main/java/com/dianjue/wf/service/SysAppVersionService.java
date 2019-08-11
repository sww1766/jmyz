package com.dianjue.wf.service;

import java.util.List;
import java.util.Map;

import com.dianjue.wf.entity.SysAppVersionEntity;

public interface SysAppVersionService {


	SysAppVersionEntity queryObject(Long id);
	
	List<SysAppVersionEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SysAppVersionEntity appversion);
	
	void update(SysAppVersionEntity appversion);
	
	void deleteBatch(Long[] ids);
	
	SysAppVersionEntity queryLast();
}


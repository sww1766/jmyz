package com.dianjue.wf.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianjue.wf.dao.SysAppVersionDao;
import com.dianjue.wf.entity.SysAppVersionEntity;
import com.dianjue.wf.service.SysAppVersionService;


@Service("sysAppVersionService")
public class SysAppVersionServiceImpl implements SysAppVersionService {
	
	@Autowired
	private SysAppVersionDao sysAppVersionDao;

	@Override
	public SysAppVersionEntity queryObject(Long id) {
		// TODO Auto-generated method stub
		return sysAppVersionDao.queryObject(id);
	}

	@Override
	public List<SysAppVersionEntity> queryList(Map<String, Object> map) {
		return sysAppVersionDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return sysAppVersionDao.queryTotal(map);
	}

	@Override
	public void save(SysAppVersionEntity appversion) {
		// TODO Auto-generated method stub
		sysAppVersionDao.save(appversion);
	}

	@Override
	public void update(SysAppVersionEntity appversion) {
		// TODO Auto-generated method stub
		sysAppVersionDao.update(appversion);
	}

	@Override
	public void deleteBatch(Long[] ids) {
		// TODO Auto-generated method stub
		sysAppVersionDao.deleteBatch(ids);
	}

	@Override
	public SysAppVersionEntity queryLast() {
		// TODO Auto-generated method stub
		return sysAppVersionDao.queryLast();
	}
}

package com.dianjue.wf.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianjue.wf.dao.RuTaskDao;
import com.dianjue.wf.entity.ActRuTask;
import com.dianjue.wf.service.RuTaskService;

/**
 * 功能说明：
 * @author lss
 * 修改人: 
 * 修改原因：
 * 修改时间：
 * 修改内容：
 * 创建日期：2017年8月29日 下午3:51:16
 * Copyright zzl-apt
 */
@Service("ruTaskService")
public class RuTaskServiceImpl implements RuTaskService{
	
	@Autowired
	private RuTaskDao ruTaskDao;

	@Override
	public ActRuTask queryObject(ActRuTask actRuTask) {
		return ruTaskDao.queryObject(actRuTask);
	}

}

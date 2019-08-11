package com.dianjue.wf.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dianjue.wf.dao.ExecutionDao;
import com.dianjue.wf.entity.ActRuExecution;
import com.dianjue.wf.service.ExecutionService;

/**
 * 功能说明：
 * @author lss
 * 修改人: 
 * 修改原因：
 * 修改时间：
 * 修改内容：
 * 创建日期：2017年8月28日 下午5:11:50
 * Copyright zzl-apt
 */
@Service("executionService")
public class ExecutionServiceImpl implements ExecutionService{
	@Autowired
	private ExecutionDao executionDao;

	@Override
	public void update(ActRuExecution actRuExecution) {
		executionDao.update(actRuExecution);
		
	}

	@Override
	public ActRuExecution queryObject(ActRuExecution actRuExecution) {
		
		return executionDao.queryObject(actRuExecution);
	}


}

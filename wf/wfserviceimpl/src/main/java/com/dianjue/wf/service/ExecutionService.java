package com.dianjue.wf.service;

import com.dianjue.wf.entity.ActRuExecution;

public interface ExecutionService {
	
	void update(ActRuExecution actRuExecution);
	
	ActRuExecution queryObject(ActRuExecution actRuExecution);

}

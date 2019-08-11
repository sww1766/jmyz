package com.dianjue.wf.service;

import com.dianjue.wf.entity.SysJmsNoticeMessageEntity;

import java.util.List;
import java.util.Map;

public interface SysJmsNoticeMessageService {
	
	SysJmsNoticeMessageEntity queryObject(Long id);
	
	List<SysJmsNoticeMessageEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SysJmsNoticeMessageEntity sysJmsNoticeMessage);
	
	void update(SysJmsNoticeMessageEntity sysJmsNoticeMessage);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);
}

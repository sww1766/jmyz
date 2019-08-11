package com.dianjue.wf.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.dianjue.wf.dao.SysJmsNoticeMessageDao;
import com.dianjue.wf.entity.SysJmsNoticeMessageEntity;
import com.dianjue.wf.service.SysJmsNoticeMessageService;



@Service("sysJmsNoticeMessageService")
public class SysJmsNoticeMessageServiceImpl implements SysJmsNoticeMessageService {
	@Autowired
	private SysJmsNoticeMessageDao sysJmsNoticeMessageDao;
	
	@Override
	public SysJmsNoticeMessageEntity queryObject(Long id){
		return sysJmsNoticeMessageDao.queryObject(id);
	}
	
	@Override
	public List<SysJmsNoticeMessageEntity> queryList(Map<String, Object> map){
		return sysJmsNoticeMessageDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return sysJmsNoticeMessageDao.queryTotal(map);
	}
	
	@Override
	public void save(SysJmsNoticeMessageEntity sysJmsNoticeMessage){
		sysJmsNoticeMessageDao.save(sysJmsNoticeMessage);
	}
	
	@Override
	public void update(SysJmsNoticeMessageEntity sysJmsNoticeMessage){
		sysJmsNoticeMessageDao.update(sysJmsNoticeMessage);
	}
	
	@Override
	public void delete(Long id){
		sysJmsNoticeMessageDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		sysJmsNoticeMessageDao.deleteBatch(ids);
	}
	
}

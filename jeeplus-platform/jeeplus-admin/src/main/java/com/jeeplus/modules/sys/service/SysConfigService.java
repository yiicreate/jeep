/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.service;

import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.sys.entity.SysConfig;
import com.jeeplus.modules.sys.mapper.SysConfigMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统配置Service
 * @author 刘高峰
 * @version 2018-10-18
 */
@Service
@Transactional(readOnly = true)
public class SysConfigService extends CrudService<SysConfigMapper, SysConfig> {

	public SysConfig get(String id) {
		if(CacheUtils.get("sys.config",id) == null){
			CacheUtils.put("sys.config",id,super.get(id));
		}
		return (SysConfig)CacheUtils.get("sys.config",id);
	}
	
	public List<SysConfig> findList(SysConfig sysConfig) {
		return super.findList(sysConfig);
	}
	
	public Page<SysConfig> findPage(Page<SysConfig> page, SysConfig sysConfig) {
		return super.findPage(page, sysConfig);
	}

	@Transactional(readOnly = false)
	public void save(SysConfig sysConfig) {
		super.save(sysConfig);
		CacheUtils.remove("sys.config", sysConfig.getId());
	}

	@Transactional(readOnly = false)
	public void delete(SysConfig sysConfig) {
		super.delete(sysConfig);
		CacheUtils.remove("sys.config", sysConfig.getId());
	}
	
}
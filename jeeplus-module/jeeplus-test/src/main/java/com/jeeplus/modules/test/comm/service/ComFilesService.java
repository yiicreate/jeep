/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.comm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.test.comm.entity.ComFiles;
import com.jeeplus.modules.test.comm.mapper.ComFilesMapper;

import static com.jeeplus.modules.test.comm.config.FilesType.NEWS_FILE;

/**
 * 公共Service
 * @author lh
 * @version 2021-03-12
 */
@Service
@Transactional(readOnly = true)
public class ComFilesService extends CrudService<ComFilesMapper, ComFiles> {

	public ComFiles get(String id) {
		return super.get(id);
	}
	
	public List<ComFiles> findList(ComFiles comFiles) {
		return super.findList(comFiles);
	}
	
	public Page<ComFiles> findPage(Page<ComFiles> page, ComFiles comFiles) {
		return super.findPage(page, comFiles);
	}
	
	@Transactional(readOnly = false)
	public void save(ComFiles comFiles) {
		super.save(comFiles);
	}
	
	@Transactional(readOnly = false)
	public void delete(ComFiles comFiles) {
		super.delete(comFiles);
	}


	public List<ComFiles> findListBySourceAOwnerId(ComFiles comFiles) {
		return mapper.findListBySourceAOwnerId(comFiles);
	}

	public int deleteBySource(ComFiles comFiles){
		return mapper.deleteBySource(comFiles);
	};


	@Transactional(readOnly = false)
	public void save(List<ComFiles> comFilesList,String source,String id) {
		List<ComFiles> oldList = mapper.findList(new ComFiles(id,source));
		if(oldList == null||oldList.size()==0){
			for (int i= 0;i<comFilesList.size();i++){
				ComFiles sf = comFilesList.get(i);
				sf.setOwnerId(id);
				sf.setSource(source);
				super.save(sf);
			}
		}else {
			Map<String,ComFiles> old = new HashMap<>();
			for (ComFiles l:oldList) {
				old.put(l.getId(),l);
			}

			for (ComFiles k:comFilesList) {
				if(k.getId()!=null && k.getId() != "" ){
					if(old.get(k.getId()).getId()!="" && old.get(k.getId()).getId() != null){
						old.remove(k.getId());
					}
				}else {
					k.setOwnerId(id);
					k.setSource(source);
					super.save(k);
				}
			}

			for (String key: old.keySet()) {
				super.delete(old.get(key));
			}
		}
	}
}
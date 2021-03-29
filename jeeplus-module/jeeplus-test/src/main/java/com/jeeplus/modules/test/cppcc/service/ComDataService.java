/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.cppcc.service;

import java.util.List;

import com.jeeplus.modules.test.cppcc.entity.ComComments;
import com.jeeplus.modules.test.cppcc.entity.ComData;
import com.jeeplus.modules.test.cppcc.mapper.ComCommentsMapper;
import com.jeeplus.modules.test.cppcc.mapper.ComDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;

/**
 * 政协通讯Service
 * @author lc
 * @version 2021-03-15
 */
@Service
@Transactional(readOnly = true)
public class ComDataService extends CrudService<ComDataMapper, ComData> {

	@Autowired
	private ComCommentsMapper comCommentsMapper;

	public ComData get(String id) {
		ComData comData = super.get(id);
		comData.setComCommentsList(comCommentsMapper.findList(new ComComments(comData)));
		return comData;
	}

	public List<ComData> findList(ComData comData) {
		return super.findList(comData);
	}

	public Page<ComData> findPage(Page<ComData> page, ComData comData) {
		return super.findPage(page, comData);
	}

	@Transactional(readOnly = false)
	public void save(ComData comData) {
		super.save(comData);
		for (ComComments comComments : comData.getComCommentsList()){
			if (comComments.getId() == null){
				continue;
			}
			if (ComComments.DEL_FLAG_NORMAL.equals(comComments.getDelFlag())){
				if (StringUtils.isBlank(comComments.getId())){
					comComments.setComData(comData);
					comComments.preInsert();
					comCommentsMapper.insert(comComments);
				}else{
					comComments.preUpdate();
					comCommentsMapper.update(comComments);
				}
			}else{
				comCommentsMapper.delete(comComments);
			}
		}
	}

	@Transactional(readOnly = false)
	public void delete(ComData comData) {
		super.delete(comData);
		comCommentsMapper.delete(new ComComments(comData));
	}

}
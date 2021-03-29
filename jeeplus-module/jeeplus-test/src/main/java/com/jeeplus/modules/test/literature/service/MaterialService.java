/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.literature.service;

import java.util.List;

import com.jeeplus.modules.test.comm.entity.ComFiles;
import com.jeeplus.modules.test.comm.service.ComFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.test.literature.entity.Material;
import com.jeeplus.modules.test.literature.mapper.MaterialMapper;

import static com.jeeplus.modules.test.comm.config.FilesType.MATERIAL_FILE;

/**
 * 数字文史Service
 * @author lh
 * @version 2021-03-12
 */
@Service
@Transactional(readOnly = true)
public class MaterialService extends CrudService<MaterialMapper, Material> {
	@Autowired
	ComFilesService comFilesService;

	public Material get(String id) {
		Material material =  super.get(id);
//		material.setComFiles(comFilesService.findListBySourceAOwnerId(new ComFiles(id,MATERIAL_FILE)));
		return  material;
	}
	
	public List<Material> findList(Material material) {
		return super.findList(material);
	}
	
	public Page<Material> findPage(Page<Material> page, Material material) {
		return super.findPage(page, material);
	}
	
	@Transactional(readOnly = false)
	public void save(Material material) {
		super.save(material);
	}
	
	@Transactional(readOnly = false)
	public void delete(Material material) {
		super.delete(material);
//		comFilesService.deleteBySource(new ComFiles(material.getId(),MATERIAL_FILE));
	}
	
}
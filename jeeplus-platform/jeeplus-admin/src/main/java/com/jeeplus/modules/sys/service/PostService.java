/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.sys.entity.Post;
import com.jeeplus.modules.sys.mapper.PostMapper;

/**
 * 岗位Service
 * @author 刘高峰
 * @version 2020-08-17
 */
@Service
@Transactional(readOnly = true)
public class PostService extends CrudService<PostMapper, Post> {

	public Post get(String id) {
		return super.get(id);
	}
	
	public List<Post> findList(Post post) {
		return super.findList(post);
	}
	
	public Page<Post> findPage(Page<Post> page, Post post) {
		return super.findPage(page, post);
	}
	
	@Transactional(readOnly = false)
	public void save(Post post) {
		super.save(post);
	}
	
	@Transactional(readOnly = false)
	public void delete(Post post) {
		super.delete(post);
	}
	
}
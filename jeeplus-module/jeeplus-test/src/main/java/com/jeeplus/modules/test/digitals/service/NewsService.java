/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.digitals.service;

import java.util.List;

import com.jeeplus.modules.test.comm.entity.ComFiles;
import com.jeeplus.modules.test.comm.service.ComFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.test.digitals.entity.News;
import com.jeeplus.modules.test.digitals.mapper.NewsMapper;

import static com.jeeplus.modules.test.comm.config.FilesType.NEWS_FILE;

/**
 * 数字化办公-新闻宣传Service
 * @author lh
 * @version 2021-03-12
 */
@Service
@Transactional(readOnly = true)
public class NewsService extends CrudService<NewsMapper, News> {
	@Autowired
	ComFilesService comFilesService;


	public News get(String id) {
		News news = super.get(id);
//		news.setComFiles(comFilesService.findListBySourceAOwnerId(new ComFiles(id,NEWS_FILE)));
		return news;
	}
	
	public List<News> findList(News news) {
		return super.findList(news);
	}
	
	public Page<News> findPage(Page<News> page, News news) {
		return super.findPage(page, news);
	}
	
	@Transactional(readOnly = false)
	public void save(News news) {
		super.save(news);
	}
	
	@Transactional(readOnly = false)
	public void delete(News news) {
		super.delete(news);
//		comFilesService.deleteBySource(new ComFiles(news.getId(),NEWS_FILE));
	}
	
}
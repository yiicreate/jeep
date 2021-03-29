/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.av.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.test.av.entity.AudioVideo;
import com.jeeplus.modules.test.av.mapper.AudioVideoMapper;

/**
 * 音频视频Service
 * @author lc
 * @version 2021-03-19
 */
@Service
@Transactional(readOnly = true)
public class AudioVideoService extends CrudService<AudioVideoMapper, AudioVideo> {

	public AudioVideo get(String id) {
		return super.get(id);
	}
	
	public List<AudioVideo> findList(AudioVideo audioVideo) {
		return super.findList(audioVideo);
	}
	
	public Page<AudioVideo> findPage(Page<AudioVideo> page, AudioVideo audioVideo) {
		return super.findPage(page, audioVideo);
	}
	
	@Transactional(readOnly = false)
	public void save(AudioVideo audioVideo) {
		super.save(audioVideo);
	}
	
	@Transactional(readOnly = false)
	public void delete(AudioVideo audioVideo) {
		super.delete(audioVideo);
	}
	
}
/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.av.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 音频视频Entity
 * @author lc
 * @version 2021-03-19
 */
@Data
public class AudioVideo extends DataEntity<AudioVideo> {
	
	private static final long serialVersionUID = 1L;
	@ExcelField(title="资源内容", align=2, sort=1)
	private String avContent;		// 资源内容
	@ExcelField(title="资源名称", align=2, sort=2)
	private String avName;		// 资源名称
	
	public AudioVideo() {
		super();
	}
	
	public AudioVideo(String id){
		super(id);
	}
}
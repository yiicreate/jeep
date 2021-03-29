/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.shop.entity;

import com.jeeplus.modules.test.shop.entity.Category;
import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 商品Entity
 * @author liugf
 * @version 2021-01-05
 */
@Data
public class Goods extends DataEntity<Goods> {
	
	private static final long serialVersionUID = 1L;
	@ExcelField(title="商品名称", align=2, sort=1)
	private String name;		// 商品名称
    @NotNull(message="所属类型不能为空")
	@ExcelField(title="所属类型", fieldType=Category.class, value="category.name", align=2, sort=2)
	private Category category;		// 所属类型
	@ExcelField(title="价格", align=2, sort=3)
	private String price;		// 价格
	
	public Goods() {
		super();
	}
	
	public Goods(String id){
		super(id);
	}
}
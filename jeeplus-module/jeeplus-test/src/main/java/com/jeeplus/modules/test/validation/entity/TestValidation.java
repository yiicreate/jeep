/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.validation.entity;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 测试校验功能Entity
 * @author lgf
 * @version 2021-01-05
 */
@Data
public class TestValidation extends DataEntity<TestValidation> {
	
	private static final long serialVersionUID = 1L;
    @NotNull(message="浮点数字不能为空")
	@ExcelField(title="浮点数字", align=2, sort=1)
	private Double num;		// 浮点数字
    @NotNull(message="整数不能为空")
	@ExcelField(title="整数", align=2, sort=2)
	private Integer num2;		// 整数
    @Length(min=5, max=65, message="字符串长度必须介于 5 和 65 之间")
	@ExcelField(title="字符串", align=2, sort=3)
	private String str;		// 字符串
    @Length(min=10, max=60, message="邮件长度必须介于 10 和 60 之间")
	@ExcelField(title="邮件", align=2, sort=4)
	private String email;		// 邮件
    @Length(min=10, max=30, message="网址长度必须介于 10 和 30 之间")
	@ExcelField(title="网址", align=2, sort=5)
	private String url;		// 网址
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message="日期不能为空")
	@ExcelField(title="日期", align=2, sort=6)
	private Date newDate;		// 日期
	@ExcelField(title="浮点数小于等于0", align=2, sort=8)
	private String c1;		// 浮点数小于等于0
	@ExcelField(title="身份证号码", align=2, sort=9)
	private String c2;		// 身份证号码
	@ExcelField(title="QQ号码", align=2, sort=10)
	private String c3;		// QQ号码
	@ExcelField(title="手机号码", align=2, sort=11)
	private String c4;		// 手机号码
	@ExcelField(title="中英文数字下划线", align=2, sort=12)
	private String c5;		// 中英文数字下划线
	@ExcelField(title="合法字符(a-z A-Z 0-9)", align=2, sort=13)
	private String c6;		// 合法字符(a-z A-Z 0-9)
	@ExcelField(title="英语", align=2, sort=14)
	private String en;		// 英语
	@ExcelField(title="汉子", align=2, sort=15)
	private String zn;		// 汉子
	@ExcelField(title="汉英字符", align=2, sort=16)
	private String enzn;		// 汉英字符
	
	public TestValidation() {
		super();
	}
	
	public TestValidation(String id){
		super(id);
	}
}
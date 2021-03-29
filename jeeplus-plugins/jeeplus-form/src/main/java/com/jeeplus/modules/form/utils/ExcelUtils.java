package com.jeeplus.modules.form.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;

/**
 * excel操作辅助类
 * @author lc
 * @version 2021-03-11
 */
public class ExcelUtils {
	
//	private static Logger log = LoggerFactory.getLogger(ExcelUtils.class);

    /**
     * 把EXCEL Cell原有数据转换成String类型
	 * @param cell
     * @return
     */
	public static String getCellString(Cell cell) {
		if(cell==null) return "";
		String cellSring="";
		switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_STRING: // 字符串
				cellSring = cell.getStringCellValue();
				break;
			case HSSFCell.CELL_TYPE_NUMERIC: // 数字
				cellSring=String.valueOf(cell.getNumericCellValue());
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
				cellSring=String.valueOf(cell.getBooleanCellValue());
				break;
			case HSSFCell.CELL_TYPE_FORMULA: // 公式
				cellSring=String.valueOf(cell.getCellFormula());
				break;
			case HSSFCell.CELL_TYPE_BLANK: // 空值
				cellSring="";
				break;
			case HSSFCell.CELL_TYPE_ERROR: // 故障
				cellSring="";
				break;
			default:
				cellSring="ERROR";
				break;
		}
		return cellSring;
	}

}

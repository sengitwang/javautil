package com.senit.javautil.model;


import org.apache.commons.collections.map.LinkedMap;

import java.util.List;

public class ExcelEntity {
	// Excel SheetName
	private String sheetName;
	// Excel 表格内容
	private List<LinkedMap> content;
	// Excel 表格title keys
	private List<String> keys;
	// Excel 表格title maps
	private LinkedMap columns;

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public List<LinkedMap> getContent() {
		return content;
	}

	public void setContent(List<LinkedMap> content) {
		this.content = content;
	}

	public List<String> getKeys() {
		return keys;
	}

	public void setKeys(List<String> keys) {
		this.keys = keys;
	}

	public LinkedMap getColumns() {
		return columns;
	}

	public void setColumns(LinkedMap columns) {
		this.columns = columns;
	}

	
}
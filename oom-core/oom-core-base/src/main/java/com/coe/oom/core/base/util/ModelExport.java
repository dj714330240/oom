package com.coe.oom.core.base.util;

import java.util.List;

/**
 * 用于导出复杂excel
 * @author lqg
 *
 */
public class ModelExport {
	private String[] heads;
	
	private List<String[]> rows;
	
	private String title;

	//头部索引 第一行是0
	private Integer headIndex=0;
	 

	public List<String[]> getRows() {
		return rows;
	}

	public void setRows(List<String[]> rows) {
		this.rows = rows;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String[] getHeads() {
		return heads;
	}

	public void setHeads(String[] heads) {
		this.heads = heads;
	}

	public Integer getHeadIndex() {
		return headIndex;
	}

	public void setHeadIndex(Integer headIndex) {
		this.headIndex = headIndex;
	}
	
	
}

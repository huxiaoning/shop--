package org.hxn.shopxx.util;

/**
 * 分页 类， 用于实现分页功能
 */
public class Page {

	private static final Integer DEFAULTPAGENUM = 1;

	private static final Integer DEFAULTPAGESIZE = 5;

	private Integer pageNum = DEFAULTPAGENUM; // 页码

	private Integer pageSize = DEFAULTPAGESIZE; // 每显示记录数

	public Page() {

	}

	public Page(Integer pageNum, Integer pageSize) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}

package com.jyss.bacon.entity;

import com.github.pagehelper.PageInfo;

import java.util.ArrayList;
import java.util.List;

public class Page<E> {
	private long total;
	private boolean next;        //是否还有下一页
	private List<E> rows = new ArrayList<E>();

	public Page() {
	}

	public Page(PageInfo<E> pageInfo) {
		this.total = pageInfo.getTotal();
		this.next = pageInfo.isHasNextPage();
		this.rows = pageInfo.getList();
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<E> getRows() {
		return rows;
	}

	public void setRows(List<E> rows) {
		this.rows = rows;
	}

	public boolean isNext() {
		return next;
	}

	public void setNext(boolean next) {
		this.next = next;
	}
}

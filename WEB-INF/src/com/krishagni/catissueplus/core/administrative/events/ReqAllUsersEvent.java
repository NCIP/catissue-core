package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ReqAllUsersEvent extends RequestEvent {
	public static enum SortType {
		ASC,
		DESC
	}
	
	private String searchString;
	
	private int startAt;
	
	private int maxRecords;
	
	private boolean countReq;
	
	private String sortBy;
	
	private SortType sort;

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public int getStartAt() {
		return startAt;
	}

	public void setStartAt(int startAt) {
		this.startAt = startAt;
	}

	public int getMaxRecords() {
		return maxRecords;
	}

	public void setMaxRecords(int maxRecords) {
		this.maxRecords = maxRecords;
	}

	public boolean isCountReq() {
		return countReq;
	}

	public void setCountReq(boolean countReq) {
		this.countReq = countReq;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public SortType getSort() {
		return sort;
	}

	public void setSort(SortType sort) {
		this.sort = sort;
	}
	
}

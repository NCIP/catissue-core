package com.krishagni.catissueplus.core.administrative.repository;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class DpListCriteria extends AbstractListCriteria<DpListCriteria> {
	
	private String title;
	
	private Long piId;
	
	private Long instituteId;
	
	private String activityStatus;

	@Override
	public DpListCriteria self() {
		return this;
	}
	
	public String title() {
		return this.title;
	}
	
	public DpListCriteria title(String title) {
		this.title = title;
		return self();
	}
	
	public Long piId() {
		return this.piId;
	}
	
	public DpListCriteria piId(Long piId) {
		this.piId = piId;
		return self();
	}
	
	public Long instituteId() {
		return instituteId;
	}
	
	public DpListCriteria instituteId(Long instituteId) {
		this.instituteId = instituteId;
		return self();
	}
	
	public String activityStatus() {
		return this.activityStatus;
	}
	
	public DpListCriteria activityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
		return self();
	}
}

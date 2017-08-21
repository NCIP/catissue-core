package com.krishagni.catissueplus.core.administrative.repository;

import java.util.Set;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class DpListCriteria extends AbstractListCriteria<DpListCriteria> {
	
	private String title;
	
	private Long piId;
	
	private String receivingInstitute;
	
	private Set<Long> siteIds;

	private boolean excludeExpiredDps;
	
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
	
	public String receivingInstitute() {
		return receivingInstitute;
	}
	
	public DpListCriteria receivingInstitute(String receivingInstitute) {
		this.receivingInstitute = receivingInstitute;
		return self();
	}
	
	public Set<Long> siteIds() {
		return siteIds;
	}
	
	public DpListCriteria siteIds(Set<Long> siteIds) {
		this.siteIds = siteIds;
		return self();
	}

	public boolean excludeExpiredDps() {
		return excludeExpiredDps;
	}

	public DpListCriteria excludeExpiredDps(boolean excludeExpiredDps) {
		this.excludeExpiredDps = excludeExpiredDps;
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

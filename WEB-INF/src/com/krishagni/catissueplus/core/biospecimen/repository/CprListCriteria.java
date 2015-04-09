package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.Set;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class CprListCriteria extends AbstractListCriteria<CprListCriteria> {
	
	private Long cpId;
	
	private Set<Long> siteIds;

	@Override
	public CprListCriteria self() {
		return this;
	}
	
	public Long cpId() {
		return cpId;
	}
	
	public CprListCriteria cpId(Long cpId) {
		this.cpId = cpId;
		return self();
	}
	
	public Set<Long> siteIds() {
		return siteIds;
	}
	
	public void siteIds(Set<Long> siteIds) {
		this.siteIds = siteIds;
	}
}

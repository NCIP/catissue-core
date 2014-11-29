package com.krishagni.catissueplus.core.biospecimen.repository;

import com.krishagni.catissueplus.core.common.repository.impl.AbstractListCriteria;

public class VisitsListCriteria extends AbstractListCriteria<VisitsListCriteria> {
	private Long cprId;
	
	@Override
	public VisitsListCriteria self() {
		return this;
	}
	
	
	public Long cprId() {
		return this.cprId;
	}
	
	public VisitsListCriteria cprId(Long cprId) {
		this.cprId = cprId;
		return self();
	}
}

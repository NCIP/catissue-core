package com.krishagni.catissueplus.core.biospecimen.repository;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class CprListCriteria extends AbstractListCriteria<CprListCriteria> {
	
	private Long cpId;

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
}

package com.krishagni.catissueplus.core.biospecimen.repository;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class VisitsListCriteria extends AbstractListCriteria<VisitsListCriteria> {
	private Long cprId;

	private String name;

	private String sprNumber;

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

	public String name() {
		return this.name;
	}

	public VisitsListCriteria name(String name) {
		this.name = name;
		return self();
	}

	public String sprNumber() {
		return this.sprNumber;
	}

	public VisitsListCriteria sprNumber(String sprNumber) {
		this.sprNumber = sprNumber;
		return self();
	}
}

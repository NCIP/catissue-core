package com.krishagni.catissueplus.core.biospecimen.repository;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class VisitsNameSprListCriteria extends AbstractListCriteria<VisitsNameSprListCriteria> {
	private String name;
	
	private String sprNumber;
	
	@Override
	public VisitsNameSprListCriteria self() {
		return this;
	}
	
	
	public String name() {
		return this.name;
	}
	
	public VisitsNameSprListCriteria name(String name) {
		this.name = name;
		return self();
	}

	public String sprNumber() {
		return this.sprNumber;
	}

	public VisitsNameSprListCriteria sprNumber(String sprNumber) {
		this.sprNumber = sprNumber;
		return self();
	}
}

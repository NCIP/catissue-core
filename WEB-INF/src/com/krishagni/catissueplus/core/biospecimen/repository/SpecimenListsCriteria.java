package com.krishagni.catissueplus.core.biospecimen.repository;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class SpecimenListsCriteria extends AbstractListCriteria<SpecimenListsCriteria> {

	private Long userId;

	@Override
	public SpecimenListsCriteria self() {
		return this;
	}

	public Long userId() {
		return userId;
	}

	public SpecimenListsCriteria userId(Long userId) {
		this.userId = userId;
		return self();
	}
}

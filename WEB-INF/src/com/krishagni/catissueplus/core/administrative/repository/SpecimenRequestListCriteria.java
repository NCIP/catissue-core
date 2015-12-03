package com.krishagni.catissueplus.core.administrative.repository;

import java.util.Set;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class SpecimenRequestListCriteria extends AbstractListCriteria<SpecimenRequestListCriteria> {

	private Long cpId;

	private Set<Long> siteIds;

	private Long requestorId;

	@Override
	public SpecimenRequestListCriteria self() {
		return this;
	}

	public SpecimenRequestListCriteria cpId(Long cpId) {
		this.cpId = cpId;
		return self();
	}

	public Long cpId() {
		return this.cpId;
	}

	public SpecimenRequestListCriteria siteIds(Set<Long> siteIds) {
		this.siteIds = siteIds;
		return self();
	}

	public Set<Long> siteIds() {
		return siteIds;
	}

	public SpecimenRequestListCriteria requestorId(Long requestorId) {
		this.requestorId = requestorId;
		return self();
	}

	public Long requestorId() {
		return requestorId;
	}
}

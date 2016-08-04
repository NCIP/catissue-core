package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;

public class SpecimenQueryCriteria extends EntityQueryCriteria {
	private String cpShortTitle;

	public SpecimenQueryCriteria(Long id) {
		super(id);
	}

	public SpecimenQueryCriteria(String cpShortTitle, String label) {
		super(label);
		this.cpShortTitle = cpShortTitle;
	}

	public String getCpShortTitle() {
		return cpShortTitle;
	}

	public void setCpShortTitle(String cpShortTitle) {
		this.cpShortTitle = cpShortTitle;
	}
}

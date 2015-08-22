package com.krishagni.catissueplus.core.biospecimen.events;

public class SpecimenDeleteCriteria {

	private Long id;

	private String label;

	private Boolean forceDelete;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Boolean getForceDelete() {
		return forceDelete;
	}

	public void setForceDelete(Boolean forceDelete) {
		this.forceDelete = forceDelete;
	}
}

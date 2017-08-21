package com.krishagni.catissueplus.core.biospecimen.events;

public class CollectionEventDetail extends SpecimenEventDetail {
	private String procedure;
	
	private String container;

	public String getProcedure() {
		return procedure;
	}

	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}

	public String getContainer() {
		return container;
	}

	public void setContainer(String container) {
		this.container = container;
	}
}

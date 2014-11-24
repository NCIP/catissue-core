package com.krishagni.catissueplus.core.biospecimen.events;

public class CollectionEventParametersDetail extends SpecimenEventParametersDetail {
	private String collectionProcedure;
	
	private String container;

	public String getCollectionProcedure() {
		return collectionProcedure;
	}

	public void setCollectionProcedure(String collectionProcedure) {
		this.collectionProcedure = collectionProcedure;
	}

	public String getContainer() {
		return container;
	}

	public void setContainer(String container) {
		this.container = container;
	}
}

package com.krishagni.catissueplus.core.biospecimen.domain;

import org.hibernate.envers.Audited;

@Audited
public class ClinicalDiagnosis {
	private Long id;
	
	private String name;
	
	private CollectionProtocol collectionProtocol;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CollectionProtocol getCollectionProtocol() {
		return collectionProtocol;
	}

	public void setCollectionProtocol(CollectionProtocol collectionProtocol) {
		this.collectionProtocol = collectionProtocol;
	};
}

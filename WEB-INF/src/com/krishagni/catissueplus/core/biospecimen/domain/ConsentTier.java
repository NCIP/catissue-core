package com.krishagni.catissueplus.core.biospecimen.domain;

import org.hibernate.envers.Audited;

@Audited
public class ConsentTier extends BaseEntity {
	private String statement;
	
	private CollectionProtocol collectionProtocol;

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public CollectionProtocol getCollectionProtocol() {
		return collectionProtocol;
	}

	public void setCollectionProtocol(CollectionProtocol collectionProtocol) {
		this.collectionProtocol = collectionProtocol;
	}
}

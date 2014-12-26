package com.krishagni.catissueplus.core.biospecimen.domain;

public class ConsentTier {
	private Long id;
	
	private String statement;
	
	private CollectionProtocol collectionProtocol;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		return prime + ((id == null) ? 0 : id.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		ConsentTier other = (ConsentTier) obj;
		if (id != null && !id.equals(other.id)) {
			return false;
		} else if (id == null && other.id != null) {
			return false;
		}
		
		return true;
	}
}

package com.krishagni.rbac.domain;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;

public class SubjectAccess {
	private String id;
	
	private Subject subject;
	
	private CollectionProtocol collectionProtocol;
	
	private Site site;
	
	private String resource;
	
	private String operation;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public CollectionProtocol getCollectionProtocol() {
		return collectionProtocol;
	}

	public void setCollectionProtocol(CollectionProtocol collectionProtocol) {
		this.collectionProtocol = collectionProtocol;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
}

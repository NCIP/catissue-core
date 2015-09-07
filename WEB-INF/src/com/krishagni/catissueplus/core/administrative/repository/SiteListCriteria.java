package com.krishagni.catissueplus.core.administrative.repository;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;


public class SiteListCriteria extends AbstractListCriteria<SiteListCriteria> {

	private String resource;
	
	private String operation;
	
	private String institute;
	
	private boolean listAll;
	
	@Override
	public SiteListCriteria self() {
		return this;
	}
	
	public String resource() {
		return resource;
	}
	
	public SiteListCriteria resource(String resource) {
		this.resource = resource;
		return self();
	}
	
	public String operation() {
		return operation;
	}
	
	public SiteListCriteria operation(String operation) {
		this.operation = operation;
		return self();
	}

	public String institute() {
		return institute;
	}

	public SiteListCriteria institute(String institute) {
		this.institute = institute;
		return self();
	}
	
	public boolean listAll() {
		return listAll;
	}
	
	public SiteListCriteria listAll(boolean listAll) {
		this.listAll = listAll;
		return self();
	}
	
}

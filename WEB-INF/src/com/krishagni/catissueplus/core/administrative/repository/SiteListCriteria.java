package com.krishagni.catissueplus.core.administrative.repository;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;


public class SiteListCriteria extends AbstractListCriteria<SiteListCriteria> {

	private String resource;
	
	private String operation;
	
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
}

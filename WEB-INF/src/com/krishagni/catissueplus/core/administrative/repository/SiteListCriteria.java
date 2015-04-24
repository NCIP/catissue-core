package com.krishagni.catissueplus.core.administrative.repository;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;
import com.krishagni.catissueplus.core.common.events.Operation;
import com.krishagni.catissueplus.core.common.events.Resource;


public class SiteListCriteria extends AbstractListCriteria<SiteListCriteria> {

	private Resource resource;
	
	private Operation operation;
	
	@Override
	public SiteListCriteria self() {
		return this;
	}
	
	public Resource resource() {
		return resource;
	}
	
	public SiteListCriteria resource(Resource resource) {
		this.resource = resource;
		return self();
	}
	
	public Operation operation() {
		return operation;
	}
	
	public SiteListCriteria operation(Operation operation) {
		this.operation = operation;
		return self();
	}
}

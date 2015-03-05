
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class ListPvCriteria extends AbstractListCriteria<ListPvCriteria> {

	private String attribute;

	private String parentValue;
	
	private boolean includeParentValue;
	
	private String parentAttribute;
	
	public String attribute() {
		return attribute;
	}

	public ListPvCriteria attribute(String attribute) {
		this.attribute = attribute;
		return self();
	}


	public String parentValue() {
		return parentValue;
	}

	public ListPvCriteria parentValue(String parentValue) {
		this.parentValue = parentValue;
		return self();
	}
	
	public boolean includeParentValue() {
		return includeParentValue;
	}
	
	public ListPvCriteria includeParentValue(boolean includeParentValue) {
		this.includeParentValue = includeParentValue;
		return self();
	}
	
	public String parentAttribute() {
		return parentAttribute;
	}
	
	public ListPvCriteria parentAttribute(String parentAttribute) {
		this.parentAttribute = parentAttribute;
		return self();
	}
	
	@Override
	public ListPvCriteria self() {
		return this;
	}
}

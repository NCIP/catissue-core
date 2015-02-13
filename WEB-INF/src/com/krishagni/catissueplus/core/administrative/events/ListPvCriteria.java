
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class ListPvCriteria extends AbstractListCriteria<ListPvCriteria> {

	private String attribute;

	private String parentValue;
	
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

	@Override
	public ListPvCriteria self() {
		return this;
	}
}

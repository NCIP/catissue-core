package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class ListSavedQueriesCriteria extends AbstractListCriteria<ListSavedQueriesCriteria> {
	private boolean countReq;

	public boolean countReq() {
		return countReq;
	}

	public ListSavedQueriesCriteria countReq(boolean countReq) {
		this.countReq = countReq;
		return self();
	}

	@Override
	public ListSavedQueriesCriteria self() {
		return this;
	}		
}

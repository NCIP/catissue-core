
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class ListCpCriteria extends AbstractListCriteria<ListCpCriteria> {
	private boolean includePi;

	@Override
	public ListCpCriteria self() {
		return this;
	}
	
	public boolean includePi() {
		return includePi;
	}
	
	public ListCpCriteria includePi(boolean includePi) {
		this.includePi = includePi;
		return self();
	}
}

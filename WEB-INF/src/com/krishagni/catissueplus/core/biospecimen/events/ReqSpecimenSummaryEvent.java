
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ReqSpecimenSummaryEvent extends RequestEvent {

	private Long parentId;

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long specimenCollectionGroupId) {
		this.parentId = specimenCollectionGroupId;
	}

}

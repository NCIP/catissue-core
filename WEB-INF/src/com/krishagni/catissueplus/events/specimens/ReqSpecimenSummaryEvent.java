
package com.krishagni.catissueplus.events.specimens;

import com.krishagni.catissueplus.events.RequestEvent;

public class ReqSpecimenSummaryEvent extends RequestEvent {

	private Long parentId;

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long specimenCollectionGroupId) {
		this.parentId = specimenCollectionGroupId;
	}

}

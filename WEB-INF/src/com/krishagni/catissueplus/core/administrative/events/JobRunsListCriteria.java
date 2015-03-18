package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class JobRunsListCriteria extends AbstractListCriteria<JobRunsListCriteria> {

	private Long scheduledJobId;
	
	public Long scheduledJobId() {
		return scheduledJobId;
	}
	
	public JobRunsListCriteria scheduledJobId(Long scheduledJobId) {
		this.scheduledJobId = scheduledJobId;
		return self();
	}
	
	@Override
	public JobRunsListCriteria self() {
		return this;
	}
}

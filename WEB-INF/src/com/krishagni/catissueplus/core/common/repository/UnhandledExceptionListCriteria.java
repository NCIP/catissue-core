package com.krishagni.catissueplus.core.common.repository;

import java.util.Date;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class UnhandledExceptionListCriteria extends AbstractListCriteria<UnhandledExceptionListCriteria> {
	private Date fromDate;
	
	private Date toDate;

	private Long instituteId;
	
	@Override
	public UnhandledExceptionListCriteria self() {
		return this;
	}
	
	public Date fromDate() {
		return fromDate;
	}
	
	public UnhandledExceptionListCriteria fromDate(Date fromDate) {
		this.fromDate = fromDate;
		return self();
	}
	
	public Date toDate() {
		return toDate;
	}
	
	public UnhandledExceptionListCriteria toDate(Date toDate) {
		this.toDate = toDate;
		return self();
	}

	public Long instituteId() {
		return instituteId;
	}

	public UnhandledExceptionListCriteria instituteId(Long instituteId) {
		this.instituteId = instituteId;
		return self();
	}
}

package com.krishagni.catissueplus.core.importer.repository;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class ListImportJobsCriteria extends AbstractListCriteria<ListImportJobsCriteria> {
	
	private Long userId;
	
	private String objectType;

	@Override
	public ListImportJobsCriteria self() {
		return this;
	}
	
	public Long userId() {
		return userId;
	}
	
	public ListImportJobsCriteria userId(Long userId) {
		this.userId = userId;
		return self();
	}
	
	public String objectType() {
		return objectType;
	}
	
	public ListImportJobsCriteria objectType(String objectType) {
		this.objectType = objectType;
		return self();
	}
}

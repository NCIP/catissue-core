package com.krishagni.catissueplus.core.importer.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class ListImportJobsCriteria extends AbstractListCriteria<ListImportJobsCriteria> {
	
	private Long userId;
	
	private List<String> objectTypes;

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
	
	public List<String> objectTypes() {
		return objectTypes;
	}
	
	public ListImportJobsCriteria objectTypes(List<String> objectTypes) {
		this.objectTypes = objectTypes;
		return self();
	}
}

package com.krishagni.catissueplus.core.importer.repository;

import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class ListImportJobsCriteria extends AbstractListCriteria<ListImportJobsCriteria> {
	
	private Long userId;
	
	private Long instituteId;

	private List<String> objectTypes;

	private Map<String, String> params;

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

	public Long instituteId() {
		return instituteId;
	}

	public ListImportJobsCriteria instituteId(Long instituteId) {
		this.instituteId = instituteId;
		return self();
	}
	
	public List<String> objectTypes() {
		return objectTypes;
	}
	
	public ListImportJobsCriteria objectTypes(List<String> objectTypes) {
		this.objectTypes = objectTypes;
		return self();
	}

	public Map<String, String> params() {
		return params;
	}

	public ListImportJobsCriteria params(Map<String, String> params) {
		this.params = params;
		return self();
	}
}

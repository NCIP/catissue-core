package com.krishagni.catissueplus.core.de.events;

import java.util.ArrayList;
import java.util.List;

public class GenerateBoTemplateOp {
	
	private Long formId;
	
	private List<String> entityLevels = new ArrayList<String>();
	
	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public List<String> getEntityLevels() {
		return entityLevels;
	}

	public void setEntityLevel(List<String> entityLevels) {
		this.entityLevels = entityLevels;
	}
	
	public void addEntityLevel(String level) {
		this.entityLevels .add(level);
	}

	
}

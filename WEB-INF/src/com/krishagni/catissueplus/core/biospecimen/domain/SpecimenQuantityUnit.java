package com.krishagni.catissueplus.core.biospecimen.domain;

import org.springframework.beans.BeanUtils;

public class SpecimenQuantityUnit extends BaseEntity {
	private Long id;
	
	private String specimenClass;
	
	private String type;
	
	private String unit;
	
	private String htmlDisplayCode;
	
	private String activityStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSpecimenClass() {
		return specimenClass;
	}

	public void setSpecimenClass(String specimenClass) {
		this.specimenClass = specimenClass;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getHtmlDisplayCode() {
		return htmlDisplayCode;
	}

	public void setHtmlDisplayCode(String htmlDisplayCode) {
		this.htmlDisplayCode = htmlDisplayCode;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}
	
	public void update(SpecimenQuantityUnit other) {
		BeanUtils.copyProperties(other, this);
	}
}

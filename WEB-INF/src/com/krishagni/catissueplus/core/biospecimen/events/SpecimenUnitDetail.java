package com.krishagni.catissueplus.core.biospecimen.events;

public class SpecimenUnitDetail {
	private Long id;
	
	private String specimenClass;
	
	private String type;
	
	private String qtyUnit;
	
	private String qtyHtmlDisplayCode;

	private String concUnit;

	private String concHtmlDisplayCode;
	
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

	public String getQtyUnit() {
		return qtyUnit;
	}

	public void setQtyUnit(String qtyUnit) {
		this.qtyUnit = qtyUnit;
	}

	public String getQtyHtmlDisplayCode() {
		return qtyHtmlDisplayCode;
	}

	public void setQtyHtmlDisplayCode(String qtyHtmlDisplayCode) {
		this.qtyHtmlDisplayCode = qtyHtmlDisplayCode;
	}

	public String getConcUnit() {
		return concUnit;
	}

	public void setConcUnit(String concUnit) {
		this.concUnit = concUnit;
	}

	public String getConcHtmlDisplayCode() {
		return concHtmlDisplayCode;
	}

	public void setConcHtmlDisplayCode(String concHtmlDisplayCode) {
		this.concHtmlDisplayCode = concHtmlDisplayCode;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

}
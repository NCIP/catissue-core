package com.krishagni.catissueplus.bulkoperator.domain;

public class BulkOperation {
	private Long id;
	
	private String operationName;
	
	//
	// TODO: Changed from dropdownName to displayName
	//
	private String displayName;
	
	private String csvTemplate;
	
	private String xmlTemplate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String dropdownName) {
		this.displayName = dropdownName;
	}

	public String getCsvTemplate() {
		return csvTemplate;
	}

	public void setCsvTemplate(String csvTemplate) {
		this.csvTemplate = csvTemplate;
	}

	public String getXmlTemplate() {
		return xmlTemplate;
	}

	public void setXmlTemplate(String xmlTemplate) {
		this.xmlTemplate = xmlTemplate;
	}
}

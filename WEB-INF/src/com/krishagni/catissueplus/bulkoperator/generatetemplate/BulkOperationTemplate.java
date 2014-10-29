package com.krishagni.catissueplus.bulkoperator.generatetemplate;

public class BulkOperationTemplate {
	
	private String templateName;
	
	private String operationName;
	
	private String xmlTemplate;
	
	private String csvTemplate;

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getXmlTemplate() {
		return xmlTemplate;
	}

	public void setXmlTemplate(String xmlTemplate) {
		this.xmlTemplate = xmlTemplate;
	}

	public String getCsvTemplate() {
		return csvTemplate;
	}

	public void setCsvTemplate(String csvTemplate) {
		this.csvTemplate = csvTemplate;
	}
}

package com.krishagni.catissueplus.bulkoperator.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.bulkoperator.domain.BulkOperation;

public class BulkOperationDetail {
	private Long id;
	
	private String operationName;
	
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

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
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
	
	public static BulkOperationDetail from(BulkOperation bulkOperation) {
		BulkOperationDetail detail = new BulkOperationDetail();
		
		detail.setId(bulkOperation.getId());
		detail.setOperationName(bulkOperation.getOperationName());
		detail.setDisplayName(bulkOperation.getDisplayName());
		detail.setCsvTemplate(bulkOperation.getCsvTemplate());
		detail.setXmlTemplate(bulkOperation.getXmlTemplate());
		return detail;
	}
	
	public static List<BulkOperationDetail> from(List<BulkOperation> operations) {
		List<BulkOperationDetail> details = new ArrayList<BulkOperationDetail>();
		
		for (BulkOperation bo : operations) {
			details.add(from(bo));
		}
		
		return details;
	}
}

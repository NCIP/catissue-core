package com.krishagni.catissueplus.core.de.events;

import java.util.Map;

public class AddRecordEntryOp {
	
	private	Map<String,Object> recIntegrationInfo;
	
	private Long containerId;
	
	private Long recordId;
	

	public Long getContainerId() {
		return containerId;
	}

	public void setContainerId(Long containerId) {
		this.containerId = containerId;
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public Map<String,Object> getRecIntegrationInfo() {
		return recIntegrationInfo;
	}

	public void setRecIntegrationInfo(Map<String,Object> recIntegrationInfo) {
		this.recIntegrationInfo = recIntegrationInfo;
	}
	

}

package com.krishagni.rbac.events;

import com.krishagni.rbac.domain.ResourceInstanceOp;

public class ResourceInstanceOpDetails {
	private Long resourceInstanceId;
	
	private String operationName;

	public Long getResourceInstanceId() {
		return resourceInstanceId;
	}

	public void setResourceInstanceId(Long resourceInstanceId) {
		this.resourceInstanceId = resourceInstanceId;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	
	public static ResourceInstanceOpDetails fromResourceInstanceOp(ResourceInstanceOp op) {
		ResourceInstanceOpDetails rd = new ResourceInstanceOpDetails();
		rd.setOperationName(op.getOperation().getName());
		rd.setResourceInstanceId(op.getResourceInstanceId());
		return rd;
	}
}

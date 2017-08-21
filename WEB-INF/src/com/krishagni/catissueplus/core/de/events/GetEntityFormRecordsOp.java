package com.krishagni.catissueplus.core.de.events;


public class GetEntityFormRecordsOp {
	private Long entityId;
	
	private Long formCtxtId;

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public Long getFormCtxtId() {
		return formCtxtId;
	}

	public void setFormCtxtId(Long formCtxtId) {
		this.formCtxtId = formCtxtId;
	}
}

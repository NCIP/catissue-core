package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ReqEntityFormsEvent extends RequestEvent {
	public static enum EntityType {
		COLLECTION_PROTOCOL_REGISTRATION,
		SPECIMEN,
		SPECIMEN_EVENT,
		SPECIMEN_COLLECTION_GROUP
	};

	private Long entityId;
	
	private EntityType entityType;

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public EntityType getEntityType() {
		return entityType;
	}

	public void setEntityType(EntityType entityType) {
		this.entityType = entityType;
	}
}

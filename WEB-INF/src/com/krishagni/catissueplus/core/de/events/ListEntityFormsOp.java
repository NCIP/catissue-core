package com.krishagni.catissueplus.core.de.events;


public class ListEntityFormsOp  {
	public static enum EntityType {
		COLLECTION_PROTOCOL_REGISTRATION,
		
		SPECIMEN,
		
		SPECIMEN_EVENT,
		
		SPECIMEN_COLLECTION_GROUP,
		
		SITE_EXTN,
		
		CP_EXTN,
		
		PARTICIPANT_EXTN,
		
		VISIT_EXTN,
		
		SPECIMEN_EXTN,
		
		ALIQUOT_EXTN,
		
		DERIVATIVE_EXTN
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

package com.krishagni.catissueplus.core.audit.domain;

import com.krishagni.catissueplus.core.audit.domain.EntityRevision;

public class EntityRevisionDetail {
	private Long id;

	private EntityRevision revision;

	private int type;

	private String entityName;

	private Long entityId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EntityRevision getRevision() {
		return revision;
	}

	public void setRevision(EntityRevision revision) {
		this.revision = revision;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
}
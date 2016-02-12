package krishagni.catissueplus.beans;

import java.util.Date;

public class FormRecordEntryBean {

	private Long identifier;
	
	private Long formCtxtId;
	
	private Long objectId;
	
	private Long recordId;
	
	private Long updatedBy;
	
	private Date updatedTime;
	
	private Status status;
	
	private String entityType;

	public enum Status {
		ACTIVE, CLOSED
	}
	
	public Long getIdentifier() {
		return identifier;
	}

	public void setIdentifier(Long identifier) {
		this.identifier = identifier;
	}

	public Long getFormCtxtId() {
		return formCtxtId;
	}

	public void setFormCtxtId(Long formCtxtId) {
		this.formCtxtId = formCtxtId;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Status getActivityStatus() {
		return status;
	}

	public void setActivityStatus(Status status) {
	  this.status = status;
	}

	public String getActivityStatusStr() {
	  return status != null ? status.name() : null;
	}

	public void setActivityStatusStr(String status) {
	  this.status = status != null ? Status.valueOf(status) : Status.ACTIVE;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public void delete() {
		setActivityStatus(Status.CLOSED);
	}
}

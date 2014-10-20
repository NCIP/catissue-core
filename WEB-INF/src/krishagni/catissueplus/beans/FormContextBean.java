package krishagni.catissueplus.beans;

public class FormContextBean {
	
	private Long identifier;
	
	private Long containerId;
	
	private String entityType;
	
	private Long cpId;
	
	private Integer sortOrder;
	
	private boolean multiRecord;
	
	private boolean sysForm;

	public Long getIdentifier() {
		return identifier;
	}

	public void setIdentifier(Long identifier) {
		this.identifier = identifier;
	}

	public Long getContainerId() {
		return containerId;
	}

	public void setContainerId(Long containerId) {
		this.containerId = containerId;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}
	
	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public boolean isMultiRecord() {
		return multiRecord;
	}

	public void setMultiRecord(boolean isMultiRecord) {
		this.multiRecord = isMultiRecord;
	}

	public boolean isSysForm() {
		return sysForm;
	}

	public void setSysForm(boolean sysForm) {
		this.sysForm = sysForm;
	}
}

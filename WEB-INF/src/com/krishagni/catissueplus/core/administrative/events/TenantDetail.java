package com.krishagni.catissueplus.core.administrative.events;

public class TenantDetail {
	private String containerName;
	
	private Long containerId;
	
	private Long cpId;
	
	private String specimenType;
	
	private String specimenClass;

	private String lineage;

	private int numOfAliquots;

	public String getContainerName() {
		return containerName;
	}

	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}

	public Long getContainerId() {
		return containerId;
	}

	public void setContainerId(Long containerId) {
		this.containerId = containerId;
	}

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public String getSpecimenType() {
		return specimenType;
	}

	public void setSpecimenType(String specimenType) {
		this.specimenType = specimenType;
	}

	public String getSpecimenClass() {
		return specimenClass;
	}

	public void setSpecimenClass(String specimenClass) {
		this.specimenClass = specimenClass;
	}

	public String getLineage() {
		return lineage;
	}

	public void setLineage(String lineage) {
		this.lineage = lineage;
	}

	public int getNumOfAliquots() {
		return numOfAliquots;
	}

	public void setNumOfAliquots(int numOfAliquots) {
		this.numOfAliquots = numOfAliquots;
	}
}

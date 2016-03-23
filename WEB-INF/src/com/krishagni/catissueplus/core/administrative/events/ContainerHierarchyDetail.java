package com.krishagni.catissueplus.core.administrative.events;

public class ContainerHierarchyDetail extends StorageContainerDetail {
	private int numOfContainers;
	
	private Long containerTypeId;

	private String containerTypeName;

	public int getNumOfContainers() {
		return numOfContainers;
	}

	public void setNumOfContainers(int numOfContainers) {
		this.numOfContainers = numOfContainers;
	}

	public Long getTypeId() {
		return containerTypeId;
	}

	public void setTypeId(Long containerTypeId) {
		this.containerTypeId = containerTypeId;
	}

	public String getTypeName() {
		return containerTypeName;
	}

	public void setTypeName(String containerTypeName) {
		this.containerTypeName = containerTypeName;
	}
}

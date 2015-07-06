package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Date;

public class SpecimenAliquotsSpec {
	private Long parentId;
	
	private String parentLabel;
	
	private Integer noOfAliquots;
	
	private Double qtyPerAliquot;
	
	private Date createdOn;
	
	private String containerName;
	
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentLabel() {
		return parentLabel;
	}

	public void setParentLabel(String parentLabel) {
		this.parentLabel = parentLabel;
	}

	public Integer getNoOfAliquots() {
		return noOfAliquots;
	}

	public void setNoOfAliquots(Integer noOfAliquots) {
		this.noOfAliquots = noOfAliquots;
	}

	public Double getQtyPerAliquot() {
		return qtyPerAliquot;
	}

	public void setQtyPerAliquot(Double qtyPerAliquot) {
		this.qtyPerAliquot = qtyPerAliquot;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getContainerName() {
		return containerName;
	}

	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}
}
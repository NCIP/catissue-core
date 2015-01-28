package com.krishagni.catissueplus.core.biospecimen.domain;

public class AliquotSpecimensRequirement {
	private Long parentSrId;
	
	private Integer noOfAliquots;
	
	private Double qtyPerAliquot;
	
	private String storageType;
	
	private String labelFmt;

	public Long getParentSrId() {
		return parentSrId;
	}

	public void setParentSrId(Long parentSrId) {
		this.parentSrId = parentSrId;
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

	public String getStorageType() {
		return storageType;
	}

	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}

	public String getLabelFmt() {
		return labelFmt;
	}

	public void setLabelFmt(String labelFmt) {
		this.labelFmt = labelFmt;
	}
}

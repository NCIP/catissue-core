package com.krishagni.catissueplus.core.biospecimen.domain;

import java.math.BigDecimal;

public class AliquotSpecimensRequirement {
	private Long parentSrId;
	
	private Integer noOfAliquots;
	
	private BigDecimal qtyPerAliquot;
	
	private String code;
	
	private String storageType;
	
	private String labelFmt;
	
	private String labelAutoPrintMode;
	
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
	
	public BigDecimal getQtyPerAliquot() {
		return qtyPerAliquot;
	}

	public void setQtyPerAliquot(BigDecimal qtyPerAliquot) {
		this.qtyPerAliquot = qtyPerAliquot;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getLabelAutoPrintMode() {
		return labelAutoPrintMode;
	}

	public void setLabelAutoPrintMode(String labelAutoPrintMode) {
		this.labelAutoPrintMode = labelAutoPrintMode;
	}
}

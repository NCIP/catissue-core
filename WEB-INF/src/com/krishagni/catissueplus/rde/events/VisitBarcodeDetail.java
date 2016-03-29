package com.krishagni.catissueplus.rde.events;

import java.util.Map;

import com.krishagni.catissueplus.rde.services.BarcodeError;

public class VisitBarcodeDetail {
	private String barcode;
	
	private String cpCode;
	
	private Long cpId;
	
	private String cpShortTitle;
	
	private String ppid;
	
	private String ppidCode;
	
	private String cpeCode;
	
	private Long cpeId;
	
	private String cpeLabel;
	
	private String siteCode;
	
	private Long siteId;
	
	private String siteName;
	
	private String cohortCode;
	
	private String cohort;
	
	private Map<String, BarcodeError> errorMap;
	
	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getCpCode() {
		return cpCode;
	}

	public void setCpCode(String cpCode) {
		this.cpCode = cpCode;
	}

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public String getCpShortTitle() {
		return cpShortTitle;
	}

	public void setCpShortTitle(String cpShortTitle) {
		this.cpShortTitle = cpShortTitle;
	}

	public String getPpid() {
		return ppid;
	}

	public void setPpid(String ppid) {
		this.ppid = ppid;
	}
	
	public String getPpidCode() {
		return ppidCode;
	}
	
	public void setPpidCode(String ppidCode) {
		this.ppidCode = ppidCode;
	}

	public String getCpeCode() {
		return cpeCode;
	}

	public void setCpeCode(String cpeCode) {
		this.cpeCode = cpeCode;
	}

	public Long getCpeId() {
		return cpeId;
	}

	public void setCpeId(Long cpeId) {
		this.cpeId = cpeId;
	}

	public String getCpeLabel() {
		return cpeLabel;
	}

	public void setCpeLabel(String cpeLabel) {
		this.cpeLabel = cpeLabel;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getCohortCode() {
		return cohortCode;
	}

	public void setCohortCode(String cohortCode) {
		this.cohortCode = cohortCode;
	}

	public String getCohort() {
		return cohort;
	}

	public void setCohort(String cohort) {
		this.cohort = cohort;
	}

	public Map<String, BarcodeError> getErrorMap() {
		return errorMap;
	}

	public void setErrorMap(Map<String, BarcodeError> errorMap) {
		this.errorMap = errorMap;
	}
}

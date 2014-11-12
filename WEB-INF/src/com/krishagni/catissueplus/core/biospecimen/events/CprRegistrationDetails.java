package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Date;

public class CprRegistrationDetails {
	private Long cpId;
	
	private Long cprId;
	
	private String ppId;
	
	private Date registrationDate;
	
	private String cpTitle;
	
	private ConsentDetail consentResponseDetail;

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public Long getCprId() {
		return cprId;
	}

	public void setCprId(Long cprId) {
		this.cprId = cprId;
	}

	public String getPpId() {
		return ppId;
	}

	public void setPpId(String ppId) {
		this.ppId = ppId;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getCpTitle() {
		return cpTitle;
	}

	public void setCpTitle(String cpTitle) {
		this.cpTitle = cpTitle;
	}

	public ConsentDetail getConsentResponseDetail() {
		return consentResponseDetail;
	}

	public void setConsentResponseDetail(ConsentDetail consentResponseDetail) {
		this.consentResponseDetail = consentResponseDetail;
	}
}

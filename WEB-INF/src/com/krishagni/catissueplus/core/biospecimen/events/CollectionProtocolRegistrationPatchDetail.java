
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;

public class CollectionProtocolRegistrationPatchDetail {

	private ParticipantPatchDetail participantDetail;

	private List<String> modifiedAttributes = new ArrayList<String>();

	private Long id;

	private Long cpId;

	private String ppid;

	private String barcode;

	private String activityStatus;

	private Date registrationDate;

	private ConsentResponseDetail responseDetail;

	public Boolean isPpidModified() {
		return modifiedAttributes.contains("ppid");
	}

	public Boolean isBarcodeModified() {
		return modifiedAttributes.contains("barcode");
	}

	public Boolean isActivityStatusModified() {
		return modifiedAttributes.contains("activityStatus");
	}

	public Boolean isRegistrationDateModified() {
		return modifiedAttributes.contains("registrationDate");
	}

	public Boolean isParticipantModified() {
		return modifiedAttributes.contains("participantDetail");
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public ConsentResponseDetail getResponseDetail() {
		return responseDetail;
	}

	public void setResponseDetail(ConsentResponseDetail responseDetail) {
		this.responseDetail = responseDetail;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public String getPpid() {
		return ppid;
	}

	public void setPpid(String ppid) {
		this.ppid = ppid;
	}

	public ParticipantPatchDetail getParticipantDetail() {
		return participantDetail;
	}

	public void setParticipantDetail(ParticipantPatchDetail participantDetail) {
		this.participantDetail = participantDetail;
	}

	public void setModifiedAttributes(List<String> modifiedAttributes) {
		this.modifiedAttributes = modifiedAttributes;
	}

	public static CollectionProtocolRegistrationPatchDetail fromDomain(CollectionProtocolRegistration cpr) {
		CollectionProtocolRegistrationPatchDetail detail = new CollectionProtocolRegistrationPatchDetail();
		detail.setId(cpr.getId());
		detail.setCpId(cpr.getCollectionProtocol().getId());
		return detail;
	}

}

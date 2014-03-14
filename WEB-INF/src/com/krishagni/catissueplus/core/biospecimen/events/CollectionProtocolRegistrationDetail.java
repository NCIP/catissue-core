
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Date;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;

public class CollectionProtocolRegistrationDetail {

	private ParticipantDetail participantDetail;

	private Long id;

	private Long cpId;

	private String ppid;

	private String barcode;

	private Date registrationDate;

	private ConsentResponseDetail responseDetail;

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

	public ParticipantDetail getParticipantDetail() {
		return participantDetail;
	}

	public void setParticipantDetail(ParticipantDetail participantDetail) {
		this.participantDetail = participantDetail;
	}

	public static CollectionProtocolRegistrationDetail fromDomain(CollectionProtocolRegistration cpr) {
		CollectionProtocolRegistrationDetail detail = new CollectionProtocolRegistrationDetail();
		detail.setId(cpr.getId());
		detail.setCpId(cpr.getCollectionProtocol().getId());
		return detail;
	}

}

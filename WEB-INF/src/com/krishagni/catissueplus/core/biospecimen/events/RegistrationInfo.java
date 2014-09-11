
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Date;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;

public class RegistrationInfo {

	private Long id;

	private String ppId;

	private Long cpId;

	private Date registrationDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPpId() {
		return ppId;
	}

	public void setPpId(String ppId) {
		this.ppId = ppId;
	}

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public static RegistrationInfo fromDomain(CollectionProtocolRegistration cpr) {
		RegistrationInfo info = new RegistrationInfo();
		info.setCpId(cpr.getCollectionProtocol().getId());
		info.setId(cpr.getId());
		info.setPpId(cpr.getProtocolParticipantIdentifier());
		info.setRegistrationDate(cpr.getRegistrationDate());
		return info;
	}

}

package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.Date;

import com.krishagni.catissueplus.core.administrative.domain.User;

public class AnonymizeEvent extends BaseEntity {
	private CollectionProtocolRegistration cpr;

	private User anonymizedBy;

	private Date anonymizeTime;

	public CollectionProtocolRegistration getCpr() {
		return cpr;
	}

	public void setCpr(CollectionProtocolRegistration cpr) {
		this.cpr = cpr;
	}

	public User getAnonymizedBy() {
		return anonymizedBy;
	}

	public void setAnonymizedBy(User anonymizedBy) {
		this.anonymizedBy = anonymizedBy;
	}

	public Date getAnonymizeTime() {
		return anonymizeTime;
	}

	public void setAnonymizeTime(Date anonymizeTime) {
		this.anonymizeTime = anonymizeTime;
	}
}

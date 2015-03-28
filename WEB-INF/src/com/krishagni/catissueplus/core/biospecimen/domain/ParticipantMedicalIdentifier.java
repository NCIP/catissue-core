
package com.krishagni.catissueplus.core.biospecimen.domain;

import com.krishagni.catissueplus.core.administrative.domain.Site;

public class ParticipantMedicalIdentifier extends BaseEntity {
	private String medicalRecordNumber;

	private Site site;

	private Participant participant;

	public String getMedicalRecordNumber() {
		return medicalRecordNumber;
	}

	public void setMedicalRecordNumber(String medicalRecordNumber) {
		this.medicalRecordNumber = medicalRecordNumber;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}
}

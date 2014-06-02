
package com.krishagni.catissueplus.core.biospecimen.domain;

import com.krishagni.catissueplus.core.administrative.domain.Site;

public class ParticipantMedicalIdentifier {

	/**
	System generated unique id.
	 */
	private Long id;

	/**
	 * Participant's medical record number used in their medical treatment.
	 */
	private String medicalRecordNumber;

	/**
	 * Source of medical record number.
	 */
	private Site site;

	/**
	 * Participant.
	 */
	private Participant participant;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

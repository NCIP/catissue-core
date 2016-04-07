
package com.krishagni.catissueplus.core.biospecimen.domain;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.krishagni.catissueplus.core.administrative.domain.Site;

@Audited
public class ParticipantMedicalIdentifier extends BaseEntity implements Comparable<ParticipantMedicalIdentifier> {
	private static final String ENTITY_NAME = "medical_record_number";

	private String medicalRecordNumber;

	private Site site;

	private Participant participant;

	public static String getEntityName() {
		return ENTITY_NAME;
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

	@NotAudited
	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

	@Override
	public int compareTo(ParticipantMedicalIdentifier other) {
		return getId().compareTo(other.getId());
	}
}

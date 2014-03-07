
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.ParticipantMedicalIdentifier;

public class ParticipantDetail {

	private String firstName;

	private String lastName;

	private String middleName;

	private Date birthDate;

	private Date deathDate;

	private String gender;

	private Set<String> race;

	private String vitalStatus;

	private List<MedicalRecordNumberDetail> mrns;

	private String sexGenotype;

	private String ethnicity;

	private String ssn;

	private String activityStatus;

	private Long id;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Date getDeathDate() {
		return deathDate;
	}

	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Set<String> getRace() {
		return race;
	}

	public void setRace(Set<String> race) {
		this.race = race;
	}

	public String getVitalStatus() {
		return vitalStatus;
	}

	public void setVitalStatus(String vitalStatus) {
		this.vitalStatus = vitalStatus;
	}

	public List<MedicalRecordNumberDetail> getMrns() {
		return mrns;
	}

	public void setMrns(List<MedicalRecordNumberDetail> mrns) {
		this.mrns = mrns;
	}

	public String getSexGenotype() {
		return sexGenotype;
	}

	public void setSexGenotype(String sexGenotype) {
		this.sexGenotype = sexGenotype;
	}

	public String getEthnicity() {
		return ethnicity;
	}

	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static ParticipantDetail fromDomain(Participant participant) {
		ParticipantDetail participantDetail = new ParticipantDetail();
		participantDetail.setFirstName(participant.getFirstName());
		participantDetail.setLastName(participant.getLastName());
		participantDetail.setMiddleName(participant.getMiddleName());
		participantDetail.setActivityStatus(participant.getActivityStatus());
		participantDetail.setBirthDate(participant.getBirthDate());
		participantDetail.setDeathDate(participant.getDeathDate());
		participantDetail.setEthnicity(participant.getEthnicity());
		participantDetail.setGender(participant.getGender());
		participantDetail.setId(participant.getId());
		Map<String, ParticipantMedicalIdentifier> pmi = participant.getPmiCollection();
		List<MedicalRecordNumberDetail> medicalRecordNumberDetails = new ArrayList<MedicalRecordNumberDetail>();
		if (pmi != null) {
			for (ParticipantMedicalIdentifier participantMedicalIdentifier : pmi.values()) {
				MedicalRecordNumberDetail medicalRecordNumberDetail = new MedicalRecordNumberDetail();
				medicalRecordNumberDetail.setMrn(participantMedicalIdentifier.getMedicalRecordNumber());
				medicalRecordNumberDetail.setSiteName(participantMedicalIdentifier.getSite().getName());
				medicalRecordNumberDetails.add(medicalRecordNumberDetail);
			}
		}
		participantDetail.setMrns(medicalRecordNumberDetails);
		participantDetail.setRace(participant.getRaceCollection());
		participantDetail.setSexGenotype(participant.getSexGenotype());
		participantDetail.setSsn(participant.getSocialSecurityNumber());
		participantDetail.setVitalStatus(participant.getVitalStatus());
		return participantDetail;
	}
}

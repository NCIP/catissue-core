
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.ParticipantMedicalIdentifier;

import edu.wustl.catissuecore.util.global.AppUtility;

public class ParticipantPatchDetail {

	List<String> modifiedAttributes = new ArrayList<String>();

	private String firstName;

	private String lastName;

	private String middleName;

	private Date birthDate;

	private Date deathDate;

	private String gender;

	private Set<String> race;

	private String vitalStatus;

	private List<ParticipantMedicalIdentifierNumberDetail> pmiCollection;

	private String sexGenotype;

	private String ethnicity;

	private String ssn;

	private String activityStatus;

	private Long id;
	public boolean isFirstNameModified(){
		return modifiedAttributes.contains("firstName");
	}
	public boolean isLastNameModified(){
		return modifiedAttributes.contains("lastName");
	}
	public boolean isMiddleNameModified(){
		return modifiedAttributes.contains("middleName");
	}
	public boolean isBirthDateModified(){
		return modifiedAttributes.contains("birthDate");
	}
	public boolean isDeathDateModified(){
		return modifiedAttributes.contains("deathDate");
	}
	public boolean isGenderModified(){
		return modifiedAttributes.contains("gender");
	}
	public boolean isRaceModified(){
		return modifiedAttributes.contains("race");
	}
	public boolean isVitalSatusModified(){
		return modifiedAttributes.contains("vitalStatus");
	}
	public boolean isPMIModified(){
		return modifiedAttributes.contains("pmiCollection");
	}
	public boolean isSexGenotypeModified(){
		return modifiedAttributes.contains("sexGenoty");
	}
	public boolean isEthnicityModified(){
		return modifiedAttributes.contains("ethnicity");
	}
	public boolean isSSNModified(){
		return modifiedAttributes.contains("ssn");
	}
	public boolean isActivityStatusModified(){
		return modifiedAttributes.contains("activityStatus");
	}

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

	public List<ParticipantMedicalIdentifierNumberDetail> getPmiCollection() {
		return pmiCollection;
	}

	public void setPmiCollection(List<ParticipantMedicalIdentifierNumberDetail> pmiCollection) {
		this.pmiCollection = pmiCollection;
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

	public List<String> getModifiedAttributes() {
		return modifiedAttributes;
	}

	public void setModifiedAttributes(List<String> modifiedAttributes) {
		this.modifiedAttributes = modifiedAttributes;
	}

	public static ParticipantPatchDetail fromDomain(Participant participant) {
		ParticipantPatchDetail participantDetail = new ParticipantPatchDetail();
		participantDetail.setFirstName(participant.getFirstName());
		participantDetail.setLastName(participant.getLastName());
		participantDetail.setMiddleName(participant.getMiddleName());
		participantDetail.setActivityStatus(participant.getActivityStatus());
		participantDetail.setBirthDate(participant.getBirthDate());
		participantDetail.setDeathDate(participant.getDeathDate());
		participantDetail.setEthnicity(participant.getEthnicity());
		participantDetail.setGender(participant.getGender());
		participantDetail.setId(participant.getId());
		//TODO revisit
		Map<String, ParticipantMedicalIdentifier> pmi = participant.getPmiCollection();
		List<ParticipantMedicalIdentifierNumberDetail> pmiColl = new ArrayList<ParticipantMedicalIdentifierNumberDetail>();
		if (pmi != null) {
			for (ParticipantMedicalIdentifier participantMedicalIdentifier : pmi.values()) {
				ParticipantMedicalIdentifierNumberDetail medicalRecordNumberDetail = new ParticipantMedicalIdentifierNumberDetail();
				medicalRecordNumberDetail.setMrn(participantMedicalIdentifier.getMedicalRecordNumber());
				medicalRecordNumberDetail.setSiteName(participantMedicalIdentifier.getSite().getName());
				pmiColl.add(medicalRecordNumberDetail);
			}
		}
		participantDetail.setPmiCollection(pmiColl);
		participantDetail.setRace(participant.getRaceColl());
		participantDetail.setSexGenotype(participant.getSexGenotype());
		participantDetail.setSsn(participant.getSocialSecurityNumber());
		participantDetail.setVitalStatus(participant.getVitalStatus());
		return participantDetail;
	}

	public static ParticipantPatchDetail fromMap(Map<String, Object> properties) {
		Gson gson = AppUtility.initGSONBuilder().create();

		List<String> list = new ArrayList<String>(properties.keySet());
		ParticipantPatchDetail detail = gson.fromJson(gson.toJson(properties), ParticipantPatchDetail.class);
		detail.setModifiedAttributes(list);
		return detail;
	}
}

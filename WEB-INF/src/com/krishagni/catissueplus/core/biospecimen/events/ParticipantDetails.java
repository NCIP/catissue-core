
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Date;
import java.util.List;

import edu.wustl.catissuecore.domain.Participant;

public class ParticipantDetails {

	private String firstName;

	private String lastName;

	private String middleName;

	private String ppid;

	private Date dob;

	private Date deathDate;

	private String gender;

	private List<String> race;

	private String vitalStatus;

	private List<MedicalRecordNumberDetail> mrns;

	private String isConsented = "No";

	private String ethnicity;

	private String ssn;

	private Date registrationDate;

	private Long participantId;

	private Long cpId;

	private String barcode;

	private Long cprId;

	private String activityStatus;

	private Long id;

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

	public Long getParticipantId() {
		return participantId;
	}

	public void setParticipantId(Long participantId) {
		this.participantId = participantId;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getPpid() {
		return ppid;
	}

	public String getVitalStatus() {
		return vitalStatus;
	}

	public void setVitalStatus(String vitalStatus) {
		this.vitalStatus = vitalStatus;
	}

	public void setPpid(String ppid) {
		this.ppid = ppid;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Long getCprId() {
		return cprId;
	}

	public void setCprId(Long cprId) {
		this.cprId = cprId;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public List<String> getRace() {
		return race;
	}

	public void setRace(List<String> race) {
		this.race = race;
	}

	public List<MedicalRecordNumberDetail> getMrns() {
		return mrns;
	}

	public void setMrns(List<MedicalRecordNumberDetail> mrns) {
		this.mrns = mrns;
	}

	public String getIsConsented() {
		return isConsented;
	}

	public void setIsConsented(String isConsented) {
		this.isConsented = isConsented;
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

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
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

	public Date getDeathDate() {
		return deathDate;
	}

	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}

	public static ParticipantDetails fromDomain(Participant participant) {
		ParticipantDetails dto = new ParticipantDetails();
		return dto;
	}
}

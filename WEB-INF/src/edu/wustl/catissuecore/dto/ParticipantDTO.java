/**
 */

package edu.wustl.catissuecore.dto;

import java.util.Date;
import java.util.List;


/**
 * @author supriya_dankh
 *
 */
public class ParticipantDTO
{
 
	 String firstName;
	 String lastName;	 
	 String ppid;
	 Date dob;
	 String gender;
	 List<String> race;
	 List<MedicalRecordNumberDTO> mrns;
	 String isConsented = "No";
	 String ethnicity;
	 String ssn;
	 Date registrationDate;
	 Long participantId;
	 Long cpId;
	 String barcode;
	 Long cprId;
	 String activityStatus;
	 String consentDocumentName;
	 
	 
	
    public String getConsentDocumentName()
    {
        return consentDocumentName;
    }
    
    public void setConsentDocumentName(String consentDocumentName)
    {
        this.consentDocumentName = consentDocumentName;
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
	public List<MedicalRecordNumberDTO> getMrns() {
		return mrns;
	}
	public void setMrns(List<MedicalRecordNumberDTO> mrns) {
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
}

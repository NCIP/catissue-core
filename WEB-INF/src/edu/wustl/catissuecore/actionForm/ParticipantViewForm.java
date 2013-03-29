package edu.wustl.catissuecore.actionForm;

import org.apache.struts.action.ActionForm;

public class ParticipantViewForm extends ActionForm {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String lastName;
	
	private String firstName;
	
	private String birthDate;
	
	private String registrationDate;
	
	private boolean isConsented;
	
	private String race;
	
	private String gender;
	
	private String mrn;
	
	private String pId;
	
	private String cpId;
	
	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getCpId() {
		return cpId;
	}

	public void setCpId(String cpId) {
		this.cpId = cpId;
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
	
		
	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getRace() {
		return race;
	}

	public void setRace(String race) {
		this.race = race;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMrn() {
		return mrn;
	}

	public void setMrn(String mrn) {
		this.mrn = mrn;
	}
	
	public boolean getIsConsented()
	{
		return isConsented;
	}
	
	public void setIsConsented(boolean IsConsented)
	{
		this.isConsented=IsConsented;
	}
	
	public String getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}
}

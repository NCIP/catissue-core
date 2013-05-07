package edu.wustl.catissuecore.dto;

import java.util.Date;
import java.util.Map;

public class SprReportDTO {
	private String cpTitle;
	private Date birthDate;
	private String gender;
	private String data;
	private String participantName;
	private String mrnString;
	private Map<String,String> conceptReferentMap;
	private int age;
	private String ppid;
	
	public String getPpid() {
		return ppid;
	}
	public void setPpid(String ppid) {
		this.ppid = ppid;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Map<String, String> getConceptReferentMap() {
		return conceptReferentMap;
	}
	public void setConceptReferentMap(Map<String, String> conceptReferentMap) {
		this.conceptReferentMap = conceptReferentMap;
	}
	public String getMrnString() {
		return mrnString;
	}
	public void setMrnString(String mrnString) {
		this.mrnString = mrnString;
	}
	public String getParticipantName() {
		return participantName;
	}
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	public String getCpTitle() {
		return cpTitle;
	}
	public void setCpTitle(String cpTitle) {
		this.cpTitle = cpTitle;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}

}

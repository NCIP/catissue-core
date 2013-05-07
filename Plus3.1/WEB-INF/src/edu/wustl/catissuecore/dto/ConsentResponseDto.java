package edu.wustl.catissuecore.dto;

import java.util.Date;
import java.util.List;

public class ConsentResponseDto {
	
	String consentUrl;
	Date consentDate;
	String witnessName;
	Long witnessId; // User Id 
	List<ConsentTierDTO> consentTierList;
	String consentLevel; // It could be Participant,SCG or specimen Level
	Long consentLevelId;
	boolean disposeSpecimen;
	
	public boolean isDisposeSpecimen() {
		return disposeSpecimen;
	}
	public void setDisposeSpecimen(boolean disposeSpecimen) {
		this.disposeSpecimen = disposeSpecimen;
	}
	public List<ConsentTierDTO> getConsentTierList() {
		return consentTierList;
	}
	public void setConsentTierList(List<ConsentTierDTO> consentTierList) {
		this.consentTierList = consentTierList;
	}
	public Long getWitnessId() {
		return witnessId;
	}
	public void setWitnessId(Long witnessId) {
		this.witnessId = witnessId;
	}
	public String getConsentLevel() {
		return consentLevel;
	}
	public void setConsentLevel(String consentLevel) {
		this.consentLevel = consentLevel;
	}
	public Long getConsentLevelId() {
		return consentLevelId;
	}
	public void setConsentLevelId(Long consentLevelId) {
		this.consentLevelId = consentLevelId;
	}
	public String getConsentUrl() {
		return consentUrl;
	}
	public void setConsentUrl(String consentUrl) {
		this.consentUrl = consentUrl;
	}
	public Date getConsentDate() {
		return consentDate;
	}
	public void setConsentDate(Date consentDate) {
		this.consentDate = consentDate;
	}
	public String getWitnessName() {
		return witnessName;
	}
	public void setWitnessName(String witnessName) {
		this.witnessName = witnessName;
	}
	
	
}

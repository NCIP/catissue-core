package edu.wustl.catissuecore.bean;

import java.util.Map;

import edu.wustl.common.util.global.Validator;

public class ConsentDisplayBean {
	private Map consentMap;
	
	private String witnessName;
	private String consentDate;
	private String  signedConsentURL;
	private String consentTierCounter;
	private String specimenLabel;
	private StringBuilder labels = new StringBuilder();
	
	public String getWitnessName() {
		return witnessName;
	}

	public void setWitnessName(String witnessName) {
		this.witnessName = witnessName;
	}

	public String getConsentDate() {
		return consentDate;
	}

	public void setConsentDate(String consentDate) {
		this.consentDate = consentDate;
	}

	public String getSignedConsentURL() {
		return signedConsentURL;
	}

	public void setSignedConsentURL(String signedConsentURL) {
		this.signedConsentURL = signedConsentURL;
	}

	public String getConsentTierCounter() {
		return consentTierCounter;
	}

	public void setConsentTierCounter(String consentTierCounter) {
		this.consentTierCounter = consentTierCounter;
	}

	public String getSpecimenLabel() {
		return specimenLabel;
	}

	public void setSpecimenLabel(String specimenLabel) 
	{
		if(Validator.isEmpty(labels.toString()))
		{
			
			labels.append(specimenLabel);
		}
		else
		{
			labels.append(", ");
			labels.append(specimenLabel);
		}
	}

	public String getLabels() {
		return labels.toString();
	}

//	public void setLabels(String[] labels) {
//		this.labels = labels;
//	}

	private String[] consentValues = new String[6];

	public Map getConsentMap() {
		return consentMap;
	}

	public void setConsentMap(Map consentMap) {
		this.consentMap = consentMap;
	}

	public String[] getConsentValues() {
		consentValues[0] = witnessName;
		consentValues[1] = consentDate;
		consentValues[2] = signedConsentURL;
		consentValues[3] = consentTierCounter;
		consentValues[4] = getLabels();
		return consentValues;
	}

//	public void setConsentValues(String[] consentValues) {
//		this.consentValues = consentValues;
//	}
}

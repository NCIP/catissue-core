
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Date;
import java.util.List;

public class ConsentResponseDetail {

	String consentUrl;

	Date consentDate;

	String witnessName;

	List<ConsentTierDetail> consentTierList;

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

	public List<ConsentTierDetail> getConsentTierList() {
		return consentTierList;
	}

	public void setConsentTierList(List<ConsentTierDetail> consentTierList) {
		this.consentTierList = consentTierList;
	}
}
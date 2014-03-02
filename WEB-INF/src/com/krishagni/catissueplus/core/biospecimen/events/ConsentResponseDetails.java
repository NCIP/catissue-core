
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Date;
import java.util.List;

public class ConsentResponseDetails {

	String consentUrl;

	Date consentDate;

	String witnessName;

	List<ConsentTierDetails> consentTierList;

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

	public List<ConsentTierDetails> getConsentTierList() {
		return consentTierList;
	}

	public void setConsentTierList(List<ConsentTierDetails> consentTierList) {
		this.consentTierList = consentTierList;
	}
}
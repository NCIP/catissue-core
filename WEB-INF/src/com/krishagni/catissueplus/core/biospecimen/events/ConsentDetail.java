
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConsentDetail {
	
	private String consentDocumentUrl;

	private Date consentSignatureDate;

	private String witnessName;

	private List<ConsentTierResponseDetail> consentTierResponses = new ArrayList<ConsentTierResponseDetail>();
	
	private String consentDocumentName;

	public String getConsentDocumentUrl() {
		return consentDocumentUrl;
	}

	public void setConsentDocumentUrl(String consentDocumentUrl) {
		this.consentDocumentUrl = consentDocumentUrl;
	}

	public Date getConsentSignatureDate() {
		return consentSignatureDate;
	}

	public void setConsentSignatureDate(Date consentSignatureDate) {
		this.consentSignatureDate = consentSignatureDate;
	}

	public String getWitnessName() {
		return witnessName;
	}

	public void setWitnessName(String witnessName) {
		this.witnessName = witnessName;
	}

	public List<ConsentTierResponseDetail> getConsentTierResponses() {
		return consentTierResponses;
	}

	public void setConsentTierResponses(List<ConsentTierResponseDetail> consenTierStatements) {
		this.consentTierResponses = consenTierStatements;
	}

	public String getConsentDocumentName() {
		return consentDocumentName;
	}

	public void setConsentDocumentName(String consentDocumentName) {
		this.consentDocumentName = consentDocumentName;
	}
	
}
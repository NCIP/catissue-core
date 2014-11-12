
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConsentDetail {

	private String consentDocumentUrl;

	private Date consentSignatureDate;

	private String witnessName;

	private List<ConsentTierDetail> consenTierStatements = new ArrayList<ConsentTierDetail>();

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

	public List<ConsentTierDetail> getConsenTierStatements() {
		return consenTierStatements;
	}

	public void setConsenTierStatements(List<ConsentTierDetail> consenTierStatements) {
		this.consenTierStatements = consenTierStatements;
	}

}
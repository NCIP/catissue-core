
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.ConsentTierResponse;

public class ConsentDetail {
	private Long cprId;
	
	private String consentDocumentUrl;

	private Date consentSignatureDate;

	private String witnessName;

	private List<ConsentTierResponseDetail> consentTierResponses = new ArrayList<ConsentTierResponseDetail>();
	
	private String consentDocumentName;

	public Long getCprId() {
		return cprId;
	}

	public void setCprId(Long cprId) {
		this.cprId = cprId;
	}

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
	
	public static ConsentDetail fromCpr(CollectionProtocolRegistration cpr) {
		ConsentDetail consent = new ConsentDetail();
		consent.setConsentDocumentUrl(cpr.getSignedConsentDocumentUrl());
		consent.setConsentSignatureDate(cpr.getConsentSignDate());
		
		String fileName = cpr.getSignedConsentDocumentName();
		if (fileName != null) {
			fileName = fileName.split("_", 2)[1];
		}
		consent.setConsentDocumentName(fileName);
		if (cpr.getConsentWitness() != null) {
			consent.setWitnessName(cpr.getConsentWitness().getEmailAddress());
		}
		
		if (cpr.getConsentResponses() != null) {
			for (ConsentTierResponse response : cpr.getConsentResponses()) {
				ConsentTierResponseDetail stmt = new ConsentTierResponseDetail();
				if (response.getConsentTier() != null) {
					stmt.setConsentStatement(response.getConsentTier().getStatement());
					stmt.setParticipantResponse(response.getResponse());
					consent.getConsentTierResponses().add(stmt);
				}
				
			}
		}
		return consent;
	}
	
}
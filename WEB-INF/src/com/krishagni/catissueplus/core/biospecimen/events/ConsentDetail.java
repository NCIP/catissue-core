
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.ConsentTier;
import com.krishagni.catissueplus.core.biospecimen.domain.ConsentTierResponse;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class ConsentDetail {
	private Long cprId;
	
	private Long cpId;
	
	private String ppid;
	
	private Date consentSignatureDate;

	private UserSummary witness;
	
	private String comments;
	
	private List<ConsentTierResponseDetail> consentTierResponses = new ArrayList<ConsentTierResponseDetail>();
	
	private String consentDocumentName;

	public Long getCprId() {
		return cprId;
	}

	public void setCprId(Long cprId) {
		this.cprId = cprId;
	}

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public String getPpid() {
		return ppid;
	}

	public void setPpid(String ppid) {
		this.ppid = ppid;
	}

	public Date getConsentSignatureDate() {
		return consentSignatureDate;
	}

	public void setConsentSignatureDate(Date consentSignatureDate) {
		this.consentSignatureDate = consentSignatureDate;
	}

	public UserSummary getWitness() {
		return witness;
	}

	public void setWitness(UserSummary witness) {
		this.witness = witness;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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
		consent.setConsentSignatureDate(cpr.getConsentSignDate());
		consent.setComments(cpr.getConsentComments());
		
		String fileName = cpr.getSignedConsentDocumentName();
		if (fileName != null) {
			fileName = fileName.split("_", 2)[1];
		}
		consent.setConsentDocumentName(fileName);
		
		if (cpr.getConsentWitness() != null) {
			consent.setWitness(UserSummary.from(cpr.getConsentWitness()));
		}
		
		for (ConsentTier consentTier : cpr.getCollectionProtocol().getConsentTier()) {
			ConsentTierResponseDetail response = new ConsentTierResponseDetail();
			response.setStatement(consentTier.getStatement());
			for (ConsentTierResponse resp : cpr.getConsentResponses()) {
				if (consentTier.getStatement().equals(resp.getConsentTier().getStatement())) {
					response.setResponse(resp.getResponse());
					break;
				}
			}
			
			consent.getConsentTierResponses().add(response);
		}
		return consent;
	}
	
}
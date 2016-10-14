/**
 * <p>Title: ConsentTierResponse Class>
 * <p>Description:   Class for Consent Tier Responses.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on November 21,2006
 */

package com.krishagni.catissueplus.core.biospecimen.domain;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.beans.BeanUtils;

@Audited
@AuditTable(value="CAT_CONSENT_TIER_RESPONSE_AUD")
public class ConsentTierResponse {

	private static final String ENTITY_NAME = "consent_response";

	private Long id;

	private String response;

	private ConsentTier consentTier;

	private CollectionProtocolRegistration cpr;
	
	public static String getEntityName() {
		return ENTITY_NAME;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	@NotAudited
	public ConsentTier getConsentTier() {
		return consentTier;
	}

	public void setConsentTier(ConsentTier consentTier) {
		this.consentTier = consentTier;
	}

	@NotAudited
	public CollectionProtocolRegistration getCpr() {
		return cpr;
	}

	public void setCpr(CollectionProtocolRegistration cpr) {
		this.cpr = cpr;
	}

	public String getStatement() {
		return getConsentTier().getStatement();
	}

	public ConsentTierResponse copy() {
		ConsentTierResponse copy = new ConsentTierResponse();
		BeanUtils.copyProperties(this, copy);
		return copy;
	}
}
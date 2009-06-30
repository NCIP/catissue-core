/**
 * <p>Title: ConsentTierResponse Class>
 * <p>Description:   Class for Consent Tier Responses.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on November 21,2006
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author ashish_gupta
 * @hibernate.class table="CATISSUE_CONSENT_TIER_RESPONSE"
 *
 */
public class ConsentTierResponse extends AbstractDomainObject implements Serializable
{

	/**
	 * Serial Version ID of the class.
	 */
	private static final long serialVersionUID = -5511144004426312668L;
	/**
	 * System Identifier.
	 */
	protected Long id;
	/**
	 * The responses for consent tiers by participants.
	 */
	protected String response;
	/**
	 * The consent tier for this response.
	 */
	protected ConsentTier consentTier;

	/**
	 * Parameterized Constructor.
	 * @hibernate.property name="response" type="string" length="10" column="RESPONSE"
	 * @param consentTierResponse of type ConsentTierResponse.
	 */
	public ConsentTierResponse(ConsentTierResponse consentTierResponse)
	{
		super();
		this.response = consentTierResponse.getResponse();
		this.consentTier = consentTierResponse.getConsentTier();
	}

	/**
	 * Default Constructor.
	 */
	public ConsentTierResponse()
	{
		super();
	}

	/**
	 * Get Response.
	 * @return String type.
	 */
	public String getResponse()
	{
		return response;
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(String response)
	{
		this.response = response;
	}

	/**
	 * @hibernate.id unsaved-value="null" generator-class="native" type="long" column="IDENTIFIER"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_CONSENT_TIER_RES_SEQ"
	 * @return Long type.
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * Set All VAlues.
	 * @param abstractForm of IValueObject type.
	 * @throws AssignDataException assignDataException.
	 */
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		//
	}

	/**
	 * Set the id.
	 * @param identifier of Long type.
	 */
	public void setId(Long identifier)
	{
		this.id = identifier;
	}

	/**
	 * @return the consentTier
	 * @hibernate.many-to-one column="CONSENT_TIER_ID" class="edu.wustl.catissuecore.domain.ConsentTier"
	 */
	public ConsentTier getConsentTier()
	{
		return consentTier;
	}

	/**
	 * @param consentTier the consentTier to set
	 */
	public void setConsentTier(ConsentTier consentTier)
	{
		this.consentTier = consentTier;
	}
}
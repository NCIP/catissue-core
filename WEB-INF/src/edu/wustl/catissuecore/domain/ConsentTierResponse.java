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

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;


/**
 * @author ashish_gupta
 * @hibernate.class table="CATISSUE_CONSENT_TIER_RESPONSE"
 *
 */
public class ConsentTierResponse extends AbstractDomainObject implements Serializable
{
	
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
	 * @return the response
	 * @hibernate.property name="response" type="string" length="10" column="RESPONSE"
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
	 * 
	 */
	public Long getId()
	{		
		return id;
	}	
	public void setAllValues(AbstractActionForm abstractForm) throws AssignDataException
	{
		// TODO Auto-generated method stub
		
	}	
	
	public void setId(Long id)
	{
		this.id = id;		
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

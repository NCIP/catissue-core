/**
 * <p>Title: ConsentTierStatus Class>
 * <p>Description:   Class for Consent Tier Status.</p>
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
 * @hibernate.class table="CATISSUE_CONSENT_TIER_STATUS" 
 *
 */
public class ConsentTierStatus extends AbstractDomainObject implements Serializable
{
	
	private static final long serialVersionUID = -1913499915765147422L;
	/**
	 * System Identifier.
	 */
	protected Long id;
	/**
	 * The consent tier status for specimens.
	 */
	protected String status;
	/**
	 * The consent tier associated with this status for multiple specimens. 
	 */
	protected ConsentTier consentTier;
	
	
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


	/**
	 * @return the status
	 * @hibernate.property column="STATUS" 
	 */
	public String getStatus()
	{
		return status;
	}

	
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}



	/**
	 * @hibernate.id unsaved-value="null" type="long" column="IDENTIFIER" generator-class="native" 
	 * @hibernate.generator-param name="sequence" value="CATISSUE_CONSENT_TIER_STAT_SEQ"
	 */
	public Long getId()
	{
		return id;
	}
	
	public void setAllValues(AbstractActionForm abstractForm) throws AssignDataException
	{
				
	}
	
	public void setId(Long id)
	{
		this.id = id;	
	}
}

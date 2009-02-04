/**
 * <p>Title: ConsentBean Class>
 * <p>Description:	This class contains attributes to display on SpecimenCollectionGroup.jsp,NewSpecimen.jsp 
 * and responses at Participant Level,Specimen Collection Group level and Specimen Level.<p>
 *  
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Virender Mehta
 * @version 1.1
 * Created on Nov 25,2006
 */
package edu.wustl.catissuecore.bean;

import java.io.Serializable;

public class ConsentBean implements Serializable
{	
	private static final long serialVersionUID = 1L;
	/**
	 * The comments is for Id associated with every Consen Tier .
	 */
	protected String consentTierID;		
	/**
	 * The comments is for Id associated with every Participant response.
	 */
	protected String participantResponseID;
	/**
	 * The comments is for Id associated with every SCG response.
	 */
	protected String specimenCollectionGroupLevelResponseID;
	/**
	 * The comments is for Id associated with every SCG response.
	 */
	protected String specimenLevelResponseID;
	/**
	 * The comments associated with the Consent Tier.
	 */
	protected String statement;
	/**
	 * The comments associated with Participant Response at CollectionProtocolRegistration.
	 */
	protected String participantResponse;
	/**
	 * The comments associated with Participant Response at Specimen Collection Group level.
	 */
	protected String specimenCollectionGroupLevelResponse;
	/**
	 * The comments associated with Participant Response at Specimen Level.
	 */
	protected String specimenLevelResponse;		
	/**
	 * @return statement The comment associated with the Consent Tier
	 */	
	
	public String getStatement()
	{
		return statement;
	}
	
	/**
	 * @param statement The comment associated with the Consent Tier
	 */
	public void setStatement(String statement) 
	{
		this.statement = statement;
	}

	/**
	 * @return participantResponse The comments associated with Participant Response at CollectionProtocolRegistration
	 */	
	public String getParticipantResponse() 
	{
		return participantResponse;
	}
	
	/**
	 * @param participantResponse The comments associated with Participant Response at CollectionProtocolRegistration
	 */
	public void setParticipantResponse(String participantResponse)
	{
		this.participantResponse = participantResponse;
	}
	
	/**
	 * @return specimenCollectionGroupLevelResponse The comments associated with Participant Response at Specimen Collection Group level
	 */	
	public String getSpecimenCollectionGroupLevelResponse()
	{
		return specimenCollectionGroupLevelResponse;
	}
	
	/**
	 * @param specimenCollectionGroupLevelResponse The comments associated with Participant Response at Specimen Collection Group level
	 */
	public void setSpecimenCollectionGroupLevelResponse(String specimenCollectionGroupLevelResponse) 
	{
		this.specimenCollectionGroupLevelResponse = specimenCollectionGroupLevelResponse;
	}
	
	/**
	 * @return specimenLevelResponse The comments associated with Participant Response at Specimen Level
	 */	
	public String getSpecimenLevelResponse() 
	{
		return specimenLevelResponse;
	}
	
	/**
	 * @param specimenLevelResponse The comments associated with Participant Response at Specimen
	 */
	public void setSpecimenLevelResponse(String specimenLevelResponse) 
	{
		this.specimenLevelResponse = specimenLevelResponse;
	}
	
	/**
	 * @return consentTierID The Id associate dwith the Consent tier
	 */	
	public String getConsentTierID() 
	{
		return consentTierID;
	}
	
	/**
	 * @param consentTierID The Id associated with the Consent tier
	 */
	public void setConsentTierID(String consentTierID)
	{
		this.consentTierID = consentTierID;
	}

	/**
	 * @return participantResponseID The Id associate dwith the Consent tier response
	 */	
	public String getParticipantResponseID()
	{
		return participantResponseID;
	}

	/**
	 * @param participantResponseID The Id associated with the Consent tier response
	 */
	public void setParticipantResponseID(String participantResponseID)
	{
		this.participantResponseID = participantResponseID;
	}

	/**
	 * @return specimenCollectionGroupLevelResponseID The Id associate dwith the Consent tier response
	 */	
	public String getSpecimenCollectionGroupLevelResponseID()
	{
		return specimenCollectionGroupLevelResponseID;
	}

	/**
	 * @param specimenCollectionGroupLevelResponseID The Id associated with the Consent tier response
	 */
	public void setSpecimenCollectionGroupLevelResponseID(String specimenCollectionGroupLevelResponseID)
	{
		this.specimenCollectionGroupLevelResponseID = specimenCollectionGroupLevelResponseID;
	}

	/**
	 * @return specimenLevelResponseID The Id associate dwith the Consent tier response
	 */	
	public String getSpecimenLevelResponseID()
	{
		return specimenLevelResponseID;
	}
	
	/**
	 * @param specimenLevelResponseID The Id associated with the Consent tier response
	 */
	public void setSpecimenLevelResponseID(String specimenLevelResponseID)
	{
		this.specimenLevelResponseID = specimenLevelResponseID;
	}

}

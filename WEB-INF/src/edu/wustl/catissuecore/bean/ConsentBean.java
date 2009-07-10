/**
 * <p>Title: ConsentBean Class>
 * <p>Description:	This class contains attributes to display on SpecimenCollectionGroup.jsp,NewSpecimen.jsp
 * and responses at Participant Level,Specimen Collection Group level and Specimen Level.<p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Virender Mehta
 * @version 1.1
 * Created on Nov 25,2006
 */

package edu.wustl.catissuecore.bean;

import java.io.Serializable;
/**
 * @author janhavi_hasabnis
 */
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
		return this.statement;
	}

	/**
	 * @param statementParam The comment associated with the Consent Tier
	 */
	public void setStatement(String statementParam)
	{
		this.statement = statementParam;
	}

	/**
	 * @return participantResponse The comments associated with Participant Response at CollectionProtocolRegistration
	 */
	public String getParticipantResponse()
	{
		return this.participantResponse;
	}

	/**
	 * @param participantResponseParam The comments associated with Participant Response at CollectionProtocolRegistration
	 */
	public void setParticipantResponse(String participantResponseParam)
	{
		this.participantResponse = participantResponseParam;
	}

	/**
	 * @return specimenCollectionGroupLevelResponse The comments associated with Participant Response at Specimen Collection Group level
	 */
	public String getSpecimenCollectionGroupLevelResponse()
	{
		return this.specimenCollectionGroupLevelResponse;
	}

	/**
	 * @param specimenCollectionGroupLevelResponseParam The comments associated with Participant Response at Specimen Collection Group level
	 */
	public void setSpecimenCollectionGroupLevelResponse(String specimenCollectionGroupLevelResponseParam)
	{
		this.specimenCollectionGroupLevelResponse = specimenCollectionGroupLevelResponseParam;
	}

	/**
	 * @return specimenLevelResponse The comments associated with Participant Response at Specimen Level
	 */
	public String getSpecimenLevelResponse()
	{
		return this.specimenLevelResponse;
	}
    /**
     * @param specimenLevelResponseParam - specimenLevelResponseParam
     */
	public void setSpecimenLevelResponse(String specimenLevelResponseParam)
	{
		this.specimenLevelResponse = specimenLevelResponseParam;
	}

	/**
	 * @return consentTierID The Id associate dwith the Consent tier
	 */
	public String getConsentTierID()
	{
		return this.consentTierID;
	}

	/**
	 * @param consentTierIDParam The Id associated with the Consent tier
	 */
	public void setConsentTierID(String consentTierIDParam)
	{
		this.consentTierID = consentTierIDParam;
	}

	/**
	 * @return participantResponseID The Id associate dwith the Consent tier response
	 */
	public String getParticipantResponseID()
	{
		return this.participantResponseID;
	}

	/**
	 * @param participantResponseIDParam The Id associated with the Consent tier response
	 */
	public void setParticipantResponseID(String participantResponseIDParam)
	{
		this.participantResponseID = participantResponseIDParam;
	}

	/**
	 * @return specimenCollectionGroupLevelResponseID The Id associate dwith the Consent tier response
	 */
	public String getSpecimenCollectionGroupLevelResponseID()
	{
		return this.specimenCollectionGroupLevelResponseID;
	}

	/**
	 * @param specimenCollectionGroupLevelResponseIDParam The Id associated with the Consent tier response
	 */
	public void setSpecimenCollectionGroupLevelResponseID(
			String specimenCollectionGroupLevelResponseIDParam)
	{
		this.specimenCollectionGroupLevelResponseID = specimenCollectionGroupLevelResponseIDParam;
	}

	/**
	 * @return specimenLevelResponseID The Id associate dwith the Consent tier response
	 */
	public String getSpecimenLevelResponseID()
	{
		return this.specimenLevelResponseID;
	}

	/**
	 * @param specimenLevelResponseIDParam The Id associated with the Consent tier response
	 */
	public void setSpecimenLevelResponseID(String specimenLevelResponseIDParam)
	{
		this.specimenLevelResponseID = specimenLevelResponseIDParam;
	}

}

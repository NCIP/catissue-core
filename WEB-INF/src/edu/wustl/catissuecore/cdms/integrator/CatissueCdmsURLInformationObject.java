
package edu.wustl.catissuecore.cdms.integrator;

import java.io.Serializable;
import java.util.Date;

public class CatissueCdmsURLInformationObject implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6952478089651105828L;
	private String participantIdentifier;
	private String userCSMIdentifier;
	private String specimenCollectionGroupIdentifier;
	private String collectionProtocolEventIdentifier;
	private String collectionProtocolIdentifier;
	private String visitIdentifier;
	private Date previousSpecimenCollectionGroupDate;
	private Date recentSpecimenCollectionGroupDate;
	private String url;

	public final String getUrl()
	{
		return url;
	}

	public final void setUrl(String url)
	{
		this.url = url;
	}

	public final Date getPreviousSpecimenCollectionGroupDate()
	{
		return previousSpecimenCollectionGroupDate;
	}

	public final void setPreviousSpecimenCollectionGroupDate(
			Date previousSpecimenCollectionGroupDate)
	{
		this.previousSpecimenCollectionGroupDate = previousSpecimenCollectionGroupDate;
	}

	public final Date getRecentSpecimenCollectionGroupDate()
	{
		return recentSpecimenCollectionGroupDate;
	}

	public final void setRecentSpecimenCollectionGroupDate(Date recentSpecimenCollectionGroupDate)
	{
		this.recentSpecimenCollectionGroupDate = recentSpecimenCollectionGroupDate;
	}

	public final String getParticipantIdentifier()
	{
		return participantIdentifier;
	}

	public final void setParticipantIdentifier(String participantIdentifier)
	{
		this.participantIdentifier = participantIdentifier;
	}

	public final String getUserCSMIdentifier()
	{
		return userCSMIdentifier;
	}

	public final void setUserCSMIdentifier(String userCSMIdentifier)
	{
		this.userCSMIdentifier = userCSMIdentifier;
	}

	public final String getSpecimenCollectionGroupIdentifier()
	{
		return specimenCollectionGroupIdentifier;
	}

	public final void setSpecimenCollectionGroupIdentifier(String specimenCollectionGroupIdentifier)
	{
		this.specimenCollectionGroupIdentifier = specimenCollectionGroupIdentifier;
	}

	public final String getCollectionProtocolEventIdentifier()
	{
		return collectionProtocolEventIdentifier;
	}

	public final void setCollectionProtocolEventIdentifier(String collectionProtocolEventIdentifier)
	{
		this.collectionProtocolEventIdentifier = collectionProtocolEventIdentifier;
	}

	public final String getCollectionProtocolIdentifier()
	{
		return collectionProtocolIdentifier;
	}

	public final void setCollectionProtocolIdentifier(String collectionProtocolIdentifier)
	{
		this.collectionProtocolIdentifier = collectionProtocolIdentifier;
	}

	public final String getVisitIdentifier()
	{
		return visitIdentifier;
	}

	public final void setVisitIdentifier(String visitIdentifier)
	{
		this.visitIdentifier = visitIdentifier;
	}

}

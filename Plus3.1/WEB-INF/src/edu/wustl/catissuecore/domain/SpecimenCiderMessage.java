package edu.wustl.catissuecore.domain;

import java.util.Date;

/**
 * This class will note down the receipt of sending the message for specimen which are collected & details of
 * this message have been sent to the CIDER.
 * @author Pavan
 *
 */
public class SpecimenCiderMessage
{

	/**
	 * Unique identifier
	 */
	private Long id;

	/**
	 * Specimen identifier for which the message was sent
	 */
	private Long specimenIdentifier;

	/**
	 * Date or timestamp on which the message was sent.
	 */
	private Date sentDate;

	private String eventType;


	public Long getId()
	{
		return id;
	}


	public void setId(Long identifier)
	{
		id = identifier;
	}


	public Long getSpecimenIdentifier()
	{
		return specimenIdentifier;
	}


	public void setSpecimenIdentifier(Long specimenIdentifier)
	{
		this.specimenIdentifier = specimenIdentifier;
	}


	public Date getSentDate()
	{
		return sentDate;
	}


	public void setSentDate(Date sentDate)
	{
		this.sentDate = sentDate;
	}


	public String getEventType()
	{
		return eventType;
	}


	public void setEventType(String eventType)
	{
		this.eventType = eventType;
	}

}

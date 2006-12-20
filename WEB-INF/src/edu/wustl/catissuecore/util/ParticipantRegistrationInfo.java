
package edu.wustl.catissuecore.util;

import java.util.List;

/**
 * ParticipantRegistrationInfo is used to store CPId, CPTitle and participant collection registered for CP.
 * @author vaishali_khandelwal
 */
public class ParticipantRegistrationInfo
{
	
	private Long cpId;
	private String cpTitle;
	private List participantIdCollection;

	/**
	 * this method returns the CP id
	 * @return CP ID
	 */
	public Long getCpId()
	{
		return cpId;
	}

	/**
	 * This Method sets CP ID
	 * @param cpId
	 */
	public void setCpId(Long cpId)
	{
		this.cpId = cpId;
	}

	/**
	 * This method returns CP title
	 * @return CP title
	 */
	public String getCpTitle()
	{
		return cpTitle;
	}

	/**
	 * This method sets the CP title
	 * @param cpTitle
	 */
	public void setCpTitle(String cpTitle)
	{
		this.cpTitle = cpTitle;
	}

	/**
	 * This method returns the Participant ID collection
	 * @return Participant Id Collection
	 */
	public List getParticipantIdCollection()
	{
		return participantIdCollection;
	}

	/**
	 * This method sets the participant IS Collection
	 * @param participantIdCollection
	 */
	public void setParticipantIdCollection(List participantIdCollection)
	{
		this.participantIdCollection = participantIdCollection;
	}

}

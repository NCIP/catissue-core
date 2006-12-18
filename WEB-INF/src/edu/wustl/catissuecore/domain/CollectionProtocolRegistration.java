/**
 * <p>Title: CollectionProtocolRegistration Class>
 * <p>Description:  A registration of a Participant to a Collection Protocol.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ajay Sharma
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import edu.wustl.catissuecore.actionForm.CollectionProtocolRegistrationForm;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

/**
 * A registration of a Participant to a Collection Protocol.
 * @hibernate.class table="CATISSUE_COLL_PROT_REG"
 * @author gautam_shetty
 */
public class CollectionProtocolRegistration extends AbstractDomainObject implements Serializable
{
	private static final long serialVersionUID = 1234567890L;

	/**
	 * System generated unique id.
	 */
	protected Long id;

	/**
	 * A unique number given by a User to a Participant 
	 * registered to a Collection Protocol.
	 */
	protected String protocolParticipantIdentifier;
    // Change for API Search   --- Ashwin 04/10/2006
	/**
	 * Date on which the Participant is registered to the Collection Protocol.
	 */
	protected Date registrationDate;

	/**
	 * An individual from whom a specimen is to be collected.
	 */
	protected Participant participant = null;
	
    // Change for API Search   --- Ashwin 04/10/2006
	/**
	 * A set of written procedures that describe how a 
	 * biospecimen is prospectively collected.
	 */
	protected CollectionProtocol collectionProtocol;

	protected Collection specimenCollectionGroupCollection = new HashSet();
	
	/**
	 * Defines whether this CollectionProtocolRegistration record can be queried (Active) or not queried (Inactive) by any actor
	 * */
	protected String activityStatus;
	
	public CollectionProtocolRegistration()
	{

	}

	
	/**
	 * one argument constructor
	 * @param CollectionProtocolRegistrationFrom object 
	 */
	public CollectionProtocolRegistration(AbstractActionForm form) throws AssignDataException
	{
		setAllValues(form);
	}

	/**
	 * Returns the system generated unique id.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_COLL_PROT_REG_SEQ"
	 * @return the system generated unique id.
	 * @see #setId(Long)
	 * */
	public Long getId()
	{
		return id;
	}

	/**
	 * Sets the system generated unique id.
	 * @param id the system generated unique id.
	 * @see #getId()
	 * */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * Returns the unique number given by a User to a Participant 
	 * registered to a Collection Protocol.
	 * @hibernate.property name="protocolParticipantIdentifier" type="string"
	 * column="PROTOCOL_PARTICIPANT_ID" length="255"
	 * @return the unique number given by a User to a Participant 
	 * registered to a Collection Protocol.
	 * @see #setProtocolParticipantIdentifier(Long)
	 */
	public String getProtocolParticipantIdentifier()
	{
		return protocolParticipantIdentifier;
	}

	/**
	 * Sets the unique number given by a User to a Participant 
	 * registered to a Collection Protocol.
	 * @param protocolParticipantIdentifier the unique number given by a User to a Participant 
	 * registered to a Collection Protocol.
	 * @see #getProtocolParticipantIdentifier()
	 */
	public void setProtocolParticipantIdentifier(String protocolParticipantIdentifier)
	{
		this.protocolParticipantIdentifier = protocolParticipantIdentifier;
	}

	/**
	 * Returns the date on which the Participant is 
	 * registered to the Collection Protocol.
	 * @hibernate.property name="registrationDate" column="REGISTRATION_DATE" type="date"
	 * @return the date on which the Participant is 
	 * registered to the Collection Protocol.
	 * @see #setRegistrationDate(Date)
	 */
	public Date getRegistrationDate()
	{
		return registrationDate;
	}

	/**
	 * Sets the date on which the Participant is 
	 * registered to the Collection Protocol.
	 * @param registrationDate the date on which the Participant is 
	 * registered to the Collection Protocol.
	 * @see #getRegistrationDate()
	 */
	public void setRegistrationDate(Date registrationDate)
	{
		this.registrationDate = registrationDate;
	}

	/**
	 * Returns the individual from whom a specimen is to be collected.
	 * @hibernate.many-to-one column="PARTICIPANT_ID"
	 * class="edu.wustl.catissuecore.domain.Participant" constrained="true"
	 * @return the individual from whom a specimen is to be collected.
	 * @see #setParticipant(Participant)
	 */
	public Participant getParticipant()
	{
		return participant;
	}

	/**
	 * Sets the individual from whom a specimen is to be collected.
	 * @param participant the individual from whom a specimen is to be collected.
	 * @see #getParticipant()
	 */
	public void setParticipant(Participant participant)
	{
		this.participant = participant;
	}

	/**
	 * Returns the set of written procedures that describe how a 
	 * biospecimen is prospectively collected.
	 * @hibernate.many-to-one column="COLLECTION_PROTOCOL_ID" 
	 * class="edu.wustl.catissuecore.domain.CollectionProtocol" constrained="true"
	 * @return the set of written procedures that describe how a 
	 * biospecimen is prospectively collected.
	 * @see #setCollectionProtocol(CollectionProtocol)
	 */
	public CollectionProtocol getCollectionProtocol()
	{
		return collectionProtocol;
	}

	/**
	 * Sets the set of written procedures that describe how a 
	 * biospecimen is prospectively collected.
	 * @param collectionProtocol the set of written procedures that describe how a 
	 * biospecimen is prospectively collected.
	 * @see #getCollectionProtocol()
	 */
	public void setCollectionProtocol(CollectionProtocol collectionProtocol)
	{
		this.collectionProtocol = collectionProtocol;
	}

	/**
	 * Returns the activity status of the participant.
	 * @hibernate.property name="activityStatus" type="string"
	 * column="ACTIVITY_STATUS" length="50"
	 * @return Returns the activity status of the participant.
	 * @see #setActivityStatus(String)
	 */
	public String getActivityStatus()
	{
		return activityStatus;
	}

	/**
	 * Sets the activity status of the participant.
	 * @param activityStatus activity status of the participant.
	 * @see #getActivityStatus()
	 */
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}
	
	/** 
	 * Set all values from CollectionProtocolRegistrationForm to the member variables of class
	 * @param CollectionProtocolRegistrationForm object  
	 */
	public void setAllValues(AbstractActionForm abstractForm) throws AssignDataException
	{
		CollectionProtocolRegistrationForm form = (CollectionProtocolRegistrationForm) abstractForm;
		try
		{
			this.activityStatus		 = form.getActivityStatus();
	        // Change for API Search   --- Ashwin 04/10/2006
	    	if (SearchUtil.isNullobject(collectionProtocol))
	    	{
	    		collectionProtocol = new CollectionProtocol();
	    	}
	    	
	        // Change for API Search   --- Ashwin 04/10/2006	    	
	    	if (SearchUtil.isNullobject(this.registrationDate))
	    	{
	    		registrationDate  = new Date();
	    	}

			this.collectionProtocol.setId(new Long(form.getCollectionProtocolID()));
			
			if(form.getParticipantID() != -1 && form.getParticipantID() != 0)
			{
				this.participant = new Participant();
				this.participant.setId(new Long(form.getParticipantID()));
			}
			else
				this.participant = null;
			
			this.protocolParticipantIdentifier = form.getParticipantProtocolID().trim();
			if(protocolParticipantIdentifier.equals(""))
				this.protocolParticipantIdentifier = null;
			
			this.registrationDate = Utility.parseDate(form.getRegistrationDate(),Utility.datePattern(form.getRegistrationDate()));
			
			
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage());
			throw new AssignDataException();
		}
	}
	
	 /**
     * Returns message label to display on success add or edit
     * @return String
     */
	public String getMessageLabel() {
		
        // Change for API Search   --- Ashwin 04/10/2006
    	if (SearchUtil.isNullobject(collectionProtocol))
    	{
    		collectionProtocol = new CollectionProtocol();
    	}
		String message = this.collectionProtocol.title + " ";
		if (this.participant != null) {
			if (this.participant.lastName!= null && !this.participant.lastName.equals("") && this.participant.firstName != null && !this.participant.firstName.equals("")) 
			{
				message = message + this.participant.lastName + "," + this.participant.firstName;
			} 
			else if(this.participant.lastName!= null && !this.participant.lastName.equals(""))
			{
				message = message + this.participant.lastName;
			}
			else if(this.participant.firstName!= null && !this.participant.firstName.equals(""))
			{
				message = message + this.participant.firstName;
			}		
		} 		
		else if (this.protocolParticipantIdentifier != null)
		{
			message = message + this.protocolParticipantIdentifier;
		}			
		return message;
	}


	/**
	 * Returns collection of specimenCollectionGroup .
	 * @return collection of collection specimenCollectionGroup .
	 * @hibernate.set name="specimenCollectionGroupCollection" table="CATISSUE_SPECIMEN_COLL_GROUP"
	 * @hibernate.collection-key column="COLLECTION_PROTOCOL_REG_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.SpecimenCollectionGroup"
	 * @see setSpecimenCollectionGroupCollection(Collection)
	 */
	public Collection getSpecimenCollectionGroupCollection()
	{
		return specimenCollectionGroupCollection;
	}

	
	public void setSpecimenCollectionGroupCollection(Collection specimenCollectionGroupCollection)
	{
		this.specimenCollectionGroupCollection = specimenCollectionGroupCollection;
	}
}
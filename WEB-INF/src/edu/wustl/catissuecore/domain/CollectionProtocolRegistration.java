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
import java.util.Date;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.actionForm.CollectionProtocolRegistrationForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.util.logger.Logger;

/**
 * A registration of a Participant to a Collection Protocol.
 * @hibernate.class table="CATISSUE_COLLECTION_PROTOCOL_REGISTRATION"
 * @author gautam_shetty
 */
public class CollectionProtocolRegistration extends AbstractDomainObject implements Serializable
{
    private static final long serialVersionUID = 1234567890L;

    /**
     * System generated unique systemIdentifier.
     */
    protected Long systemIdentifier;

    /**
     * A unique number given by a User to a Participant 
     * registered to a Collection Protocol.
     */
    protected String protocolParticipantIdentifier;

    /**
     * Date on which the Participant is registered to the Collection Protocol.
     */
    protected Date registrationDate= new Date();

    /**
     * An individual from whom a specimen is to be collected.
     */
    private Participant participant=new Participant();

    /**
     * A set of written procedures that describe how a 
     * biospecimen is prospectively collected.
     */
    private CollectionProtocol collectionProtocol=new CollectionProtocol();

	public CollectionProtocolRegistration()
	{
		
	}
	
	public CollectionProtocolRegistration(AbstractActionForm form)
	{
		setAllValues(form);
	}


    /**
     * Returns the system generated unique systemIdentifier.
     * @hibernate.id name="systemIdentifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @return the system generated unique systemIdentifier.
     * @see #setSystemIdentifier(Long)
     * */
    public Long getSystemIdentifier()
    {
        return systemIdentifier;
    }

    /**
     * Sets the system generated unique systemIdentifier.
     * @param systemIdentifier the system generated unique systemIdentifier.
     * @see #getSystemIdentifier()
     * */
    public void setSystemIdentifier(Long systemIdentifier)
    {
        this.systemIdentifier = systemIdentifier;
    }

    /**
     * Returns the unique number given by a User to a Participant 
     * registered to a Collection Protocol.
     * @hibernate.property name="protocolParticipantIdentifier" type="string"
     * column="PROTOCOL_PARTICIPANT_IDENTIFIER" length="50"
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
    public void setProtocolParticipantIdentifier(
    		String protocolParticipantIdentifier)
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
    public void setCollectionProtocol(
            CollectionProtocol collectionProtocol)
    {
        this.collectionProtocol = collectionProtocol;
    }

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
	 */
	public void setAllValues(AbstractActionForm abstractForm) {
		                  
		CollectionProtocolRegistrationForm form = (CollectionProtocolRegistrationForm)abstractForm;
		try
		{
			this.systemIdentifier = new Long(form.getSystemIdentifier());
			this.registrationDate = Utility.parseDate(form.getRegistrationDate(),Constants.DATE_PATTERN_MM_DD_YYYY);
			this.protocolParticipantIdentifier = form.getParticipantProtocolID();
			this.participant.setSystemIdentifier(new Long(form.getParticipantID()));
			this.collectionProtocol.setSystemIdentifier(new Long(form.getCollectionProtocolID()));

		}catch(Exception e){

			Logger.out.error(e.getMessage());
		}
		
		// TODO Auto-generated method stub
		
	}
}
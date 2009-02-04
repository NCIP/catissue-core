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
import java.util.Iterator;

import edu.wustl.catissuecore.actionForm.CollectionProtocolRegistrationForm;
import edu.wustl.catissuecore.bean.ConsentBean;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.util.MapDataParser;
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

	//-----For Consent Tracking. Ashish 21/11/06
	/**
	 * The signed consent document URL.
	 */
	protected String signedConsentDocumentURL;
	/**
	 * The date on which consent document was signed.
	 */
	protected Date consentSignatureDate;
	/**
	 * The witness for the signed consent document.
	 */
	protected User consentWitness;
	/**
	 * The collection of responses of multiple participants for a particular consent.
	 */
	protected Collection consentTierResponseCollection;
	
	//Mandar 15-jan-07 
	/*
	 * To perform operation based on withdraw button clicked.
	 * Default No Action to allow normal behaviour. 
	 */
	protected String consentWithdrawalOption=Constants.WITHDRAW_RESPONSE_NOACTION;
	

	
	/**
	 * @return the consentSignatureDate
	 * @hibernate.property name="consentSignatureDate" column="CONSENT_SIGN_DATE"  
	 */
	public Date getConsentSignatureDate()
	{
		return consentSignatureDate;
	}
	
	/**
	 * @param consentSignatureDate the consentSignatureDate to set
	 */
	public void setConsentSignatureDate(Date consentSignatureDate)
	{
		this.consentSignatureDate = consentSignatureDate;
	}
	
	/**
	 * @return the signedConsentDocumentURL
	 * @hibernate.property name="signedConsentDocumentURL" type="string" length="1000" column="CONSENT_DOC_URL"
	 */
	public String getSignedConsentDocumentURL()
	{
		return signedConsentDocumentURL;
	}
	
	/**
	 * @param signedConsentDocumentURL the signedConsentDocumentURL to set
	 */
	public void setSignedConsentDocumentURL(String signedConsentDocumentURL)
	{
		this.signedConsentDocumentURL = signedConsentDocumentURL;
	}
	
	/**
	 * @return the consentTierResponseCollection
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.ConsentTierResponse" lazy="false" cascade="save-update"
	 * @hibernate.set name="consentTierResponseCollection" table="CATISSUE_CONSENT_TIER_RESPONSE" 
	 * @hibernate.collection-key column="COLL_PROT_REG_ID"
	 */
	public Collection getConsentTierResponseCollection()
	{
		return consentTierResponseCollection;
	}
	
	/**
	 * @param consentTierResponseCollection the consentTierResponseCollection to set
	 */
	public void setConsentTierResponseCollection(Collection consentTierResponseCollection)
	{
		this.consentTierResponseCollection = consentTierResponseCollection;
	}
	
	/**
	 * @return the consentWitness
	 * @hibernate.many-to-one class="edu.wustl.catissuecore.domain.User" constrained="true" column="CONSENT_WITNESS"
	 */
	public User getConsentWitness()
	{
		return consentWitness;
	}
	
	/**
	 * @param consentWitness the consentWitness to set
	 */
	public void setConsentWitness(User consentWitness)
	{
		this.consentWitness = consentWitness;
	}
	//-----Consent Tracking End
	
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
			
			
			//For Consent Tracking ----Ashish 1/12/06
			//Setting the consent sign date.
			this.consentSignatureDate = Utility.parseDate(form.getConsentDate());
			//Setting the signed doc url
			this.signedConsentDocumentURL = form.getSignedConsentUrl();
			if(signedConsentDocumentURL.equals(""))
			{
				this.signedConsentDocumentURL=null;
			}
			//Setting the consent witness
			if(form.getWitnessId()>0)
			{
				this.consentWitness = new User();
				consentWitness.setId(new Long(form.getWitnessId()));
			}
			//Preparing  Consent tier response Collection 
			this.consentTierResponseCollection = prepareParticipantResponseCollection(form);
			
			//Mandar: 16-jan-07 : - For withdraw options
			this.consentWithdrawalOption = form.getWithdrawlButtonStatus(); 
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage());
			throw new AssignDataException();
		}
	}

//Consent Tracking 	
  /**
	* For Consent Tracking
	* Setting the Domain Object 
	* @param  form CollectionProtocolRegistrationForm
	* @return consentResponseColl
	*/
	private Collection prepareParticipantResponseCollection(CollectionProtocolRegistrationForm form) 
	{
		MapDataParser mapdataParser = new MapDataParser("edu.wustl.catissuecore.bean");
        Collection beanObjColl=null;
		try
		{
			beanObjColl = mapdataParser.generateData(form.getConsentResponseValues());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
        Iterator iter = beanObjColl.iterator();
        Collection consentResponseColl = new HashSet();
        while(iter.hasNext())
        {
        	ConsentBean consentBean = (ConsentBean)iter.next();
        	ConsentTierResponse consentTierResponse = new ConsentTierResponse();
        	//Setting response
        	consentTierResponse.setResponse(consentBean.getParticipantResponse());
        	if(consentBean.getParticipantResponseID()!=null&&consentBean.getParticipantResponseID().trim().length()>0)
        	{
        		consentTierResponse.setId(Long.parseLong(consentBean.getParticipantResponseID()));
        	}
        	//Setting consent tier
        	ConsentTier consentTier = new ConsentTier();
        	consentTier.setId(Long.parseLong(consentBean.getConsentTierID()));
        	consentTierResponse.setConsentTier(consentTier);
        	consentResponseColl.add(consentTierResponse);
        }
        return consentResponseColl;
	}
//Consent Tracking
	
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
	
	//----------------------------Mandar 15-jan-07
	public String getConsentWithdrawalOption() {
		return consentWithdrawalOption;
	}

	public void setConsentWithdrawalOption(String consentWithdrawalOption) {
		this.consentWithdrawalOption = consentWithdrawalOption;
	}
	
}
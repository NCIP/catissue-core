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
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import edu.wustl.catissuecore.actionForm.CollectionProtocolRegistrationForm;
import edu.wustl.catissuecore.util.ConsentUtil;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.bizlogic.IActivityStatus;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

/**
 * A registration of a Participant to a Collection Protocol.
 * @hibernate.class table="CATISSUE_COLL_PROT_REG"
 * @author gautam_shetty
 */
public class CollectionProtocolRegistration extends AbstractDomainObject
		implements
			Serializable,
			IActivityStatus
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger
			.getLogger(CollectionProtocolRegistration.class);
	/**
	 * Serial Version ID of the class.
	 */
	private static final long serialVersionUID = 5075110651381259752L;

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

	/**
	 * specimenCollectionGroupCollection.
	 */
	protected Collection specimenCollectionGroupCollection = new HashSet();

	/**
	 * Defines whether this CollectionProtocolRegistration record can be queried
	 * Active) or not queried (Inactive) by any actor.
	 */
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

	/**
	 * To perform operation based on withdraw button clicked.
	 * Default No Action to allow normal behaviour.
	 */
	protected String consentWithdrawalOption = Constants.WITHDRAW_RESPONSE_NOACTION;

	/**
	 * isConsentAvailable.
	 */
	protected String isConsentAvailable;

	/**
	 * offset.
	 */
	protected Integer offset;

	/**
	 * barcode attribute added for Suite 1.1.
	 */
	protected String barcode;

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
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.
	 * ConsentTierResponse" lazy="true" cascade="save-update"
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
	 * @hibernate.many-to-one class="edu.wustl.catissuecore.domain.User"
	 * constrained="true" column="CONSENT_WITNESS"
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

	/**
	 * Default Constructor.
	 */
	public CollectionProtocolRegistration()
	{
		super();
	}

	/**
	 * Parameterized Constructor.
	 * @param form CollectionProtocolRegistrationFrom object.
	 * @throws AssignDataException when problem is assignement of data.
	 */
	public CollectionProtocolRegistration(AbstractActionForm form) throws AssignDataException
	{
		super();
		setAllValues(form);
	}

	/**
	 * Parameterized Constructor.
	 * @param cpr of CollectionProtocolRegistration type.
	 */
	public CollectionProtocolRegistration(CollectionProtocolRegistration cpr)
	{
		super();
		this.activityStatus = cpr.getActivityStatus();
		this.collectionProtocol = cpr.getCollectionProtocol();
		this.consentSignatureDate = cpr.getConsentSignatureDate();
		copyConsentResponseColl(cpr);
		this.consentWithdrawalOption = cpr.consentWithdrawalOption;
		this.consentWitness = cpr.getConsentWitness();
		this.isConsentAvailable = cpr.getIsConsentAvailable();
		this.participant = cpr.getParticipant();
		this.protocolParticipantIdentifier = cpr.getProtocolParticipantIdentifier();
		this.registrationDate = cpr.getRegistrationDate();
		this.signedConsentDocumentURL = cpr.getSignedConsentDocumentURL();
		this.specimenCollectionGroupCollection = new HashSet();
	}

	/**
	 * Copy the consent response collection.
	 * @param cpr of CollectionProtocolRegistration type.
	 */
	private void copyConsentResponseColl(CollectionProtocolRegistration cpr)
	{
		if (cpr.getConsentTierResponseCollection() != null)
		{
			Collection consentTierResponseCollClone = new HashSet();
			Iterator itr = cpr.getConsentTierResponseCollection().iterator();
			while (itr.hasNext())
			{
				ConsentTierResponse consentTierResponse = (ConsentTierResponse) itr.next();
				ConsentTierResponse consentTierResponseClone = new ConsentTierResponse(
						consentTierResponse);
				consentTierResponseCollClone.add(consentTierResponseClone);
			}

			this.consentTierResponseCollection = consentTierResponseCollClone;
		}
		else
		{
			this.consentTierResponseCollection = null;
		}
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
	 * @param identifier the system generated unique id.
	 * @see #getId()
	 * */
	public void setId(Long identifier)
	{
		this.id = identifier;
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
	 * Set all values from CollectionProtocolRegistrationForm to the member variables of class.
	 * @param abstractForm of IValueObject type.
	 * @throws AssignDataException when problem is assignment of data.
	 */
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		CollectionProtocolRegistrationForm form = (CollectionProtocolRegistrationForm) abstractForm;
		this.activityStatus = form.getActivityStatus();
		// Change for API Search   --- Ashwin 04/10/2006
		if (SearchUtil.isNullobject(collectionProtocol))
		{
			collectionProtocol = new CollectionProtocol();
		}

		// Change for API Search   --- Ashwin 04/10/2006	    	
		if (SearchUtil.isNullobject(this.registrationDate))
		{
			registrationDate = new Date();
		}

		this.collectionProtocol.setId(new Long(form.getCollectionProtocolID()));

		if (form.getParticipantID() != -1 && form.getParticipantID() != 0)
		{
			this.participant = new Participant();
			this.participant.setId(new Long(form.getParticipantID()));
		}
		else
			this.participant = null;

		this.protocolParticipantIdentifier = form.getParticipantProtocolID().trim();
		if (protocolParticipantIdentifier.equals(""))
			this.protocolParticipantIdentifier = null;

		try
		{
			this.registrationDate = Utility.parseDate(form.getRegistrationDate(), Utility
					.datePattern(form.getRegistrationDate()));
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.barcode = Utility.toString(form.getBarcode());

		//For Consent Tracking ----Ashish 1/12/06
		//Setting the consent sign date.
		try
		{
			this.consentSignatureDate = Utility.parseDate(form.getConsentDate());
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Setting the signed doc url
		this.signedConsentDocumentURL = form.getSignedConsentUrl();
		if (signedConsentDocumentURL.equals(""))
		{
			this.signedConsentDocumentURL = null;
		}
		//Setting the consent witness
		if (form.getWitnessId() > 0)
		{
			this.consentWitness = new User();
			consentWitness.setId(new Long(form.getWitnessId()));
		}
		//Preparing  Consent tier response Collection 
		this.consentTierResponseCollection = prepareParticipantResponseCollection(form);

		//Mandar: 16-jan-07 : - For withdraw options
		this.consentWithdrawalOption = form.getWithdrawlButtonStatus();
		// offset changes 27th 2007
		this.setOffset(new Integer(form.getOffset()));

	}

	/**
	* For Consent Tracking.
	* Setting the Domain Object
	* @param  form CollectionProtocolRegistrationForm
	* @return consentResponseColl
	*/
	private Collection prepareParticipantResponseCollection(CollectionProtocolRegistrationForm form)
	{
		MapDataParser mapdataParser = new MapDataParser("edu.wustl.catissuecore.bean");
		Collection beanObjColl = null;
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
		ConsentUtil.createConsentResponseColl(consentResponseColl, iter);
		return consentResponseColl;
	}

	//Consent Tracking

	/**
	 * Returns message label to display on success add or edit.
	 * @return String
	 */
	public String getMessageLabel()
	{
		/**
		 * Name: Vijay Pande
		 * Reviewer Name: Aarti Sharma
		 * Instead of directly accessing private variables their getter methods are called.
		 * Direct access of variables was returning null value.
		 */

		// Change for API Search   --- Ashwin 04/10/2006
		if (SearchUtil.isNullobject(collectionProtocol))
		{
			collectionProtocol = new CollectionProtocol();
		}
		StringBuffer message = new StringBuffer();
		message.append(this.collectionProtocol.getTitle() + " ");
		if (this.participant != null)
		{
			if (this.participant.getLastName() != null
					&& !this.participant.getLastName().equals("")
					&& this.participant.getFirstName() != null
					&& !this.participant.getFirstName().equals(""))
			{
				message.append(this.participant.getLastName() + ","
						+ this.participant.getFirstName());
			}
			else if (this.participant.getLastName() != null
					&& !this.participant.getLastName().equals(""))
			{
				message.append(this.participant.getLastName());
			}
			else if (this.participant.getFirstName() != null
					&& !this.participant.getFirstName().equals(""))
			{
				message.append(this.participant.getFirstName());
			}
		}
		else if (this.protocolParticipantIdentifier != null)
		{
			message.append(this.protocolParticipantIdentifier);
		}
		return message.toString();
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

	/**
	 * Set the SpecimenCollectionGroup Collection.
	 * @param specimenCollectionGroupCollection of Collection type.
	 */
	public void setSpecimenCollectionGroupCollection(Collection specimenCollectionGroupCollection)
	{
		this.specimenCollectionGroupCollection = specimenCollectionGroupCollection;
	}

	/**
	 * Get the Consent Withdrawal Option.
	 * @return String type.
	 */
	public String getConsentWithdrawalOption()
	{
		return consentWithdrawalOption;
	}

	/**
	 * Set the Consent Withdrawal Option.
	 * @param consentWithdrawalOption of String type.
	 */
	public void setConsentWithdrawalOption(String consentWithdrawalOption)
	{
		this.consentWithdrawalOption = consentWithdrawalOption;
	}

	/**
	 * Get the available consent.
	 * @return String type.
	 */
	public String getIsConsentAvailable()
	{
		return isConsentAvailable;
	}

	/**
	 * Set the available consent.
	 * @param isConsentAvailable of String type.
	 */
	public void setIsConsentAvailable(String isConsentAvailable)
	{
		this.isConsentAvailable = isConsentAvailable;
	}

	/**
	 * Get the offset.
	 * @return Integer type.
	 */
	public Integer getOffset()
	{
		return offset;
	}

	/**
	 * Set the offset.
	 * @param offset of Integer type.
	 */
	public void setOffset(Integer offset)
	{
		this.offset = offset;
	}

	/**
	 * Get the barcode.
	 * @return String type.
	 */
	public String getBarcode()
	{
		return barcode;
	}

	/**
	 * Set the barcode.
	 * @param barcode of String type.
	 */
	public void setBarcode(String barcode)
	{
		this.barcode = barcode;
		String nullString = null;
		if (Constants.DOUBLE_QUOTES.equals(barcode))
		{
			this.barcode = nullString;
		}
	}
}
/**
 * <p>Title: CollectionProtocolRegistration Class>
 * <p>Description:  A registration of a Participant to a Collection Protocol.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ajay Sharma
 * @version 1.00
 */

package edu.wustl.catissuecore.actionForm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

public class CollectionProtocolRegistrationForm extends AbstractActionForm
		implements
			ConsentTierData
{

	private static final long serialVersionUID = 1L;

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(CollectionProtocolRegistrationForm.class);

	protected Map values = new HashMap();
	/**
	 * System generated unique collection protocol Identifier
	 */
	private long collectionProtocolID;

	private String collectionProtocolShortTitle = "";
	/**
	 * System generated unique participant Identifier
	 */
	private long participantID;

	/**
	 * System generated unique participant protocol Identifier.
	 */
	protected String participantName = "";

	/**
	 * System generated unique participant protocol Identifier.
	 */
	protected String participantProtocolID = "";

	/**
	 * Date on which the Participant is registered to the Collection Protocol.
	 */
	protected String registrationDate;

	/**
	 * Date on which the Participant is registered to the Collection Protocol.
	 */
	protected String barcode;

	/**
	 * Represents the weather participant Name is selected or not.
	 */
	protected boolean checkedButton;

	//Consent Tracking Module Virender Mehta 25/11/2006  		
	/**
	 * Map for Storing responses for Consent Tiers.
	 */
	protected Map consentResponseValues = new HashMap();
	/**
	 * No of Consent Tier
	 */
	private int consentTierCounter = 0;

	/**
	 * Signed Consent URL
	 */
	protected String signedConsentUrl = "";
	/**
	 * Witness name that may be PI
	 */
	protected long witnessId;

	/**
	 * Consent Date, Date on which Consent is Signed
	 */
	protected String consentDate = "";
	/**
	 * This will be set in case of withdrawl popup
	 */
	protected String withdrawlButtonStatus = Constants.WITHDRAW_RESPONSE_NOACTION;

	protected String studyCalEvtPoint = "";
	protected int offset = 0;

	//Consent Tracking Module Virneder Mehta 25/11/2006	

	/**
	 * Default Constructor
	 */
	public CollectionProtocolRegistrationForm()
	{

	}

	/**
	 * @return Returns the values.
	 */
	public Collection getAllValues()
	{
		return this.values.values();
	}

	/** Associates the specified object with the specified key in the map.
	 * @param key the key to which the object is mapped.
	 * @param value the object which is mapped.
	 */

	public void setValue(final String key, final Object value)
	{
		if (this.isMutable())
		{
			this.values.put(key, value);
		}
	}

	/**
	 * Returns the object to which this map maps the specified key.
	 * @param key the required key.
	 * @return the object to which this map maps the specified key.
	 */
	public Object getValue(final String key)
	{
		return this.values.get(key);
	}

	/**
	 * @return values
	 */
	public Map getValues()
	{
		return this.values;
	}

	/**
	 * @param values Map
	 */
	public void setValues(final Map values)
	{
		this.values = values;
	}

	/**
	 * It will set all values of member variables from Domain Object
	 * @param abstractDomain domain object
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		final String dtePattern = CommonServiceLocator.getInstance().getDatePattern();
		final CollectionProtocolRegistration registration = (CollectionProtocolRegistration) abstractDomain;
		this.setId(registration.getId().longValue());
		this.setActivityStatus(registration.getActivityStatus());
		this.collectionProtocolID = registration.getCollectionProtocol().getId().longValue();
		final String firstName = CommonUtilities.toString(registration.getParticipant()
				.getFirstName());;
		final String lastName = CommonUtilities.toString(registration.getParticipant()
				.getLastName());
		final String birthDate = CommonUtilities.toString(registration.getParticipant()
				.getBirthDate());
		final String ssn = CommonUtilities.toString(registration.getParticipant()
				.getSocialSecurityNumber());

		if ((registration.getParticipant() != null)
				&& (firstName.trim().length() > 0 || lastName.trim().length() > 0
						|| birthDate.trim().length() > 0 || ssn.trim().length() > 0))
		{
			this.participantID = registration.getParticipant().getId().longValue();

			//Bug-2819: Performance issue due to participant drop down: Jitendra
			this.participantName = registration.getParticipant().getMessageLabel();
			//checkedButton = true;
		}
		this.participantProtocolID = CommonUtilities.toString(registration
				.getProtocolParticipantIdentifier());
		this.registrationDate = CommonUtilities.parseDateToString(registration
				.getRegistrationDate(), dtePattern);
		/**
		 * For Consent tracking setting UI attributes
		 */
		final User witness = registration.getConsentWitness();
		if (witness != null)
		{
			this.witnessId = witness.getId();
		}
		this.signedConsentUrl = CommonUtilities
				.toString(registration.getSignedConsentDocumentURL());
		this.consentDate = CommonUtilities.parseDateToString(
				registration.getConsentSignatureDate(), dtePattern);
		// Offset changes 27th Dec 2007
		//	  	this.setOffset(registration.getOffset().intValue());
		this.setOffset(0);

	}

	/**
	* @return COLLECTION_PROTOCOL_REGISTRATION_FORM_ID Returns the id assigned to form bean
	*/
	@Override
	public int getFormId()
	{
		return Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID;
	}

	/**
	 * Returns the date on which the Participant is 
	 * registered to the Collection Protocol.
	 * @return registrationDate the date on which the Participant is 
	 * registered to the Collection Protocol.
	 * @see #setRegistrationDate(String)
	 */
	public String getRegistrationDate()
	{
		return this.registrationDate;
	}

	/**
	 * Sets the date on which the Participant is 
	 * registered to the Collection Protocol.
	 * @param registrationDate the date on which the Participant is 
	 * registered to the Collection Protocol.
	 * @see #getRegistrationDate()
	 */
	public void setRegistrationDate(final String registrationDate)
	{
		this.registrationDate = registrationDate;
	}

	/**
	 * @return collectionProtocolID 
	 */
	public long getCollectionProtocolID()
	{
		return this.collectionProtocolID;
	}

	/**
	 * @param collectionProtocolID Setting Collection protocol id
	 */
	public void setCollectionProtocolID(long collectionProtocolID)
	{
		this.collectionProtocolID = collectionProtocolID;
	}

	/**
	 * @return Returns the collectionProtocolShortTitle.
	 */
	public String getCollectionProtocolShortTitle()
	{
		return this.collectionProtocolShortTitle;
	}

	/**
	 * @param collectionProtocolShortTitle The collectionProtocolShortTitle to set.
	 */
	public void setCollectionProtocolShortTitle(String collectionProtocolShortTitle)
	{
		this.collectionProtocolShortTitle = collectionProtocolShortTitle;
	}

	/**
	 * @return Returns unique participant ID 
	 */
	public long getParticipantID()
	{
		return this.participantID;
	}

	/**
	 * @param participantID sets unique participant ID 
	 */
	public void setParticipantID(final long participantID)
	{
		this.participantID = participantID;
	}

	/**
	* @return returns praticipant Protocol ID
	*/
	public String getParticipantProtocolID()
	{
		return this.participantProtocolID;
	}

	/**
	 * @param participantProtocolID sets participant protocol ID
	*/
	public void setParticipantProtocolID(String participantProtocolID)
	{
		this.participantProtocolID = participantProtocolID;
	}

	/**
	 * Overrides the validate method of ActionForm.
	 * @return error ActionErrors instance
	 * @param mapping Actionmapping instance
	 * @param request HttpServletRequest instance
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{

		final ActionErrors errors = new ActionErrors();
		final Validator validator = new Validator();
		try
		{
			this.setRedirectValue(validator);

			//check if Protocol Title is empty.
			if (this.collectionProtocolID == -1)
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties
								.getValue("collectionprotocolregistration.protocoltitle")));
			}
			// Mandar 10-apr-06 : bugid :353 
			// Error messages should be in the same sequence as the sequence of fields on the page.

			// changes as per Bugzilla Bug 287 			

			if (Validator.isEmpty(this.participantProtocolID)
					&& (this.participantID == -1 || this.participantID == 0))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
						"errors.collectionprotocolregistration.atleast"));
			}

			//  date validation according to bug id 707, 722 and 730
			final String errorKey = validator.validateDate(this.registrationDate, true);
			if (errorKey.trim().length() > 0)
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(errorKey,
						ApplicationProperties.getValue("collectionprotocolregistration.date")));
			}
			//
			if (!validator.isValidOption(this.getActivityStatus()))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties
								.getValue("collectionprotocolregistration.activityStatus")));
			}
		}
		catch (final Exception excp)
		{
			CollectionProtocolRegistrationForm.logger.error(excp.getMessage(), excp);
			excp.printStackTrace() ;
		}
		return errors;
	}

	/**
	* Resets the values of all the fields.
	* Is called by the overridden reset method defined in ActionForm.  
	* */
	@Override
	protected void reset()
	{
		//	   this.collectionProtocolID = 0;
		//	   this.participantID = 0;
		//	   this.participantProtocolID = null;
		//	   this.registrationDate = null;
		//	   this.id = 0;
		//	   this.operation = null;
	}

	//	/**
	//	 * @return returns boolean value of checkbox button
	//	 */
	//	public boolean isCheckedButton() {
	//		return checkedButton;
	//	}
	//
	//	/**
	//	 * @param checkedButton sets value of checkeButton
	//	 */
	//	public void setCheckedButton(boolean checkedButton) {
	//		this.checkedButton = checkedButton;
	//	}

	/**
	 * This method sets Identifier of Objects inserted by AddNew activity in Form-Bean which initialized AddNew action
	 * @param addNewFor - FormBean ID of the object inserted
	 *  @param addObjectIdentifier - Identifier of the Object inserted 
	 */
	@Override
	public void setAddNewObjectIdentifier(String addNewFor, Long addObjectIdentifier)
	{
		if ("collectionProtocolId".equals(addNewFor))
		{
			this.setCollectionProtocolID(addObjectIdentifier.longValue());
		}
		else if ("participantId".equals(addNewFor))
		{
			this.setParticipantID(addObjectIdentifier.longValue());
			//setCheckedButton(true);
		}
	}

	/**
	 * @return participantName
	 */
	public String getParticipantName()
	{
		return this.participantName;
	}

	/**
	 * @param participantName Setting participant name
	 */
	public void setParticipantName(final String participantName)
	{
		this.participantName = participantName;
	}

	//	 Consent Tracking Virender Mehta 		
	/**
	 * @return consentDate The Date on Which Consent is Signed
	 */
	public String getConsentDate()
	{
		return this.consentDate;
	}

	/**
	 * @param consentDate The Date on Which Consent is Signed
	 */
	public void setConsentDate(final String consentDate)
	{
		this.consentDate = consentDate;
	}

	/**
	 * @return signedConsentUrl The reference to the electric signed document(eg PDF file)
	 */
	public String getSignedConsentUrl()
	{
		return this.signedConsentUrl;
	}

	/**
	 * @param signedConsentUrl The reference to the electric signed document(eg PDF file)
	 */
	public void setSignedConsentUrl(final String signedConsentUrl)
	{
		this.signedConsentUrl = signedConsentUrl;
	}

	/**
	 * @return witnessId The name of the witness to the consent Signature(PI or coordinator of the Collection Protocol)
	 */
	public long getWitnessId()
	{
		return this.witnessId;
	}

	/**
	 * @param witnessId The name of the witness to the consent Signature(PI or coordinator of the Collection Protocol)
	 */
	public void setWitnessId(final long witnessId)
	{
		this.witnessId = witnessId;
	}

	/**
	 * @param key Key prepared for saving data.
	 * @param value Values correspponding to key
	 */
	public void setConsentResponseValue(final String key, final Object value)
	{
		if (this.isMutable())
		{
			this.consentResponseValues.put(key, value);
		}
	}

	/**
	 * 
	 * @param key Key prepared for saving data.
	 * @return consentResponseValues
	 */
	public Object getConsentResponseValue(final String key)
	{
		return this.consentResponseValues.get(key);
	}

	/**
	 * @return values in map consentResponseValues
	 */
	public Collection getAllConsentResponseValue()
	{
		return this.consentResponseValues.values();
	}

	/**
	 * @return consentResponseValues The reference to the participant Response at CollectionprotocolReg Level
	 */
	public Map getConsentResponseValues()
	{
		return this.consentResponseValues;
	}

	/**
	 * @param consentResponseValues The reference to the participant Response at CollectionprotocolReg Level
	 */
	public void setConsentResponseValues(Map consentResponseValues)
	{
		this.consentResponseValues = consentResponseValues;
	}

	/**
	 *@return consentTierCounter  This will keep track of count of Consent Tier
	 */
	public int getConsentTierCounter()
	{
		return this.consentTierCounter;
	}

	/**
	 *@param consentTierCounter  This will keep track of count of Consent Tier
	 */
	public void setConsentTierCounter(int consentTierCounter)
	{
		this.consentTierCounter = consentTierCounter;
	}

	/**
	 * It returns status of button(return,discard,reset)
	 * @return withdrawlButtonStatus
	 */
	public String getWithdrawlButtonStatus()
	{
		return this.withdrawlButtonStatus;
	}

	/**
	 * It returns status of button(return,discard,reset)
	 * @param withdrawlButtonStatus return,discard,reset
	 */
	public void setWithdrawlButtonStatus(String withdrawlButtonStatus)
	{
		this.withdrawlButtonStatus = withdrawlButtonStatus;
	}

	/**
	 * This function creates Array of String of keys and add them into the consentTiersList.
	 * @return consentTiersList
	 */
	public Collection getConsentTiers()
	{
		final Collection consentTiersList = new ArrayList();
		String[] strArray = null;
		final int noOfConsents = this.getConsentTierCounter();
		final String prestr = "consentResponseValue(ConsentBean:";
		for (int counter = 0; counter < noOfConsents; counter++)
		{
			strArray = new String[4];
			strArray[0] = prestr + counter + "_consentTierID)";
			strArray[1] = prestr + counter + "_statement)";
			//strArray[1]=(String)this.consentResponseValues.get("ConsentBean:"+counter+"_statement");
			strArray[2] = prestr + counter + "_participantResponse)";
			strArray[3] = prestr + counter + "_participantResponseID)";

			consentTiersList.add(strArray);
		}
		return consentTiersList;
	}

	/**
	 * This funtion returns the format of the response Key prepared. 
	 * @return consentResponseValue(ConsentBean:`_participantResponse)
	 */
	public String getConsentTierMap()
	{
		return "consentResponseValue(ConsentBean:`_participantResponse)";
	}

	//	Consent Tracking Virender Mehta 	

	/**
	 * @return Returns the studeCalEvtPoint.
	 */
	public String getStudyCalEvtPoint()
	{
		return this.studyCalEvtPoint;
	}

	/**
	 * 
	 * @param studyCalEvtPoint
	 */
	public void setStudyCalEvtPoint(final String studyCalEvtPoint)
	{
		this.studyCalEvtPoint = studyCalEvtPoint;
	}

	/**
	 * @return Returns the offset.
	 */
	public int getOffset()
	{
		return this.offset;
	}

	/**
	 * @param offset The offset to set.
	 */
	public void setOffset(final int offset)
	{
		this.offset = offset;
	}

	public String getBarcode()
	{
		return this.barcode;
	}

	public void setBarcode(final String barcode)
	{
		this.barcode = barcode;
	}
	//Sub Protocol 
}
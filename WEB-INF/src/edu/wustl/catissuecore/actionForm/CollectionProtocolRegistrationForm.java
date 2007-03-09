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
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;


public class CollectionProtocolRegistrationForm extends AbstractActionForm implements ConsentTierData 
{	
	protected Map values = new HashMap();
	/**
	 * System generated unique collection protocol Identifier
	 */
	private long collectionProtocolID;
    
	/**
	 * System generated unique participant Identifier
	 */	
	private long participantID;
	
	/**
	 * System generated unique participant protocol Identifier.
	 */	
	protected String participantName="";

	/**
	 * System generated unique participant protocol Identifier.
	 */	
	protected String participantProtocolID="";

	/**
	 * Date on which the Participant is registered to the Collection Protocol.
	 */
	protected String registrationDate;

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
	private int consentTierCounter=0;

	/**
	 * Signed Consent URL
	 */
	protected String signedConsentUrl="";
	/**
	 * Witness name that may be PI
	 */
	protected long witnessId;
	
	/**
	 * Consent Date, Date on which Consent is Signed
	 */
	protected String consentDate="";
	/**
	 * This will be set in case of withdrawl popup
	 */
	protected String withdrawlButtonStatus= Constants.WITHDRAW_RESPONSE_NOACTION;
	
	
	//Consent Tracking Module Virneder Mehta 25/11/2006	

	public CollectionProtocolRegistrationForm()
	{
		//reset();
	}
			
	/**
	 * @return Returns the values.
	 */
	public Collection getAllValues()
	{
		return values.values();
	}

	/** Associates the specified object with the specified key in the map.
	 * @param key the key to which the object is mapped.
	 * @param value the object which is mapped.
	 */
	
	public void setValue(String key, Object value)
	{
		if (isMutable())
		{
			values.put(key, value);
		}
	}

	/**
	 * Returns the object to which this map maps the specified key.
	 * @param key the required key.
	 * @return the object to which this map maps the specified key.
	 */
	public Object getValue(String key)
	{
		return values.get(key);
	}

	/**
	 * @return values
	 */
	public Map getValues()
	{
		return values;
	}

	/**
	 * @param values Map
	 */
	public void setValues(Map values)
	{
		this.values = values;
	}

	/**
	 * It will set all values of member variables from Domain Object
	 * @param CollectionProtocolRegistration domain object
	 */	
    public void setAllValues(AbstractDomainObject abstractDomain)
    {
		CollectionProtocolRegistration registration = (CollectionProtocolRegistration)abstractDomain;
		this.id = registration.getId().longValue();
		this.activityStatus = registration.getActivityStatus();
		this.collectionProtocolID = registration.getCollectionProtocol().getId().longValue();
		String firstName = Utility.toString(registration.getParticipant().getFirstName());;
		String lastName = Utility.toString(registration.getParticipant().getLastName());
		String birthDate = Utility.toString(registration.getParticipant().getBirthDate());
		String ssn = Utility.toString(registration.getParticipant().getSocialSecurityNumber());
			
		if((registration.getParticipant() != null) && (firstName.trim().length()>0 || lastName.trim().length()>0 || birthDate.trim().length()>0 || ssn.trim().length()>0))
	  	{
	  		this.participantID = registration.getParticipant().getId().longValue();
	  		
	  		//Bug-2819: Performance issue due to participant drop down: Jitendra
	  		this.participantName = registration.getParticipant().getMessageLabel();
	  		//checkedButton = true;
	  	}
	  	this.participantProtocolID = Utility.toString(registration.getProtocolParticipantIdentifier());
	  	this.registrationDate = Utility.parseDateToString(registration.getRegistrationDate(),Constants.DATE_PATTERN_MM_DD_YYYY);
	  	/**
	  	 * For Consent tracking setting UI attributes
	  	 */
	  	User witness= registration.getConsentWitness();
	  	if(witness!=null)
	  	{
	  		this.witnessId=witness.getId();
	  	}
	  	this.signedConsentUrl=Utility.toString(registration.getSignedConsentDocumentURL());
	  	this.consentDate=Utility.parseDateToString(registration.getConsentSignatureDate(), Constants.DATE_PATTERN_MM_DD_YYYY);
    }
 	/**
	* @return Returns the id assigned to form bean
	*/
	public int getFormId()
	{
		return Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID;
	}
	
    /**
     * Returns the date on which the Participant is 
     * registered to the Collection Protocol.
     * @return the date on which the Participant is 
     * registered to the Collection Protocol.
     * @see #setRegistrationDate(String)
     */
    public String getRegistrationDate()
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
    public void setRegistrationDate(String registrationDate)
    {
        this.registrationDate = registrationDate;
    }

	/**
	 * @return
	 */
	public long getCollectionProtocolID() {
		return collectionProtocolID;
	}

	/**
	 * @param collectionProtocolID
	 */
	public void setCollectionProtocolID(long collectionProtocolID) {
		this.collectionProtocolID = collectionProtocolID;
	}

	/**
	 * @return Returns unique participant ID 
	 */
	public long getParticipantID() {
		return participantID;
	}

	/**
	 * @param participantID sets unique participant ID 
	 */
	public void setParticipantID(long participantID) {
		this.participantID = participantID;
	}

	/**
 	* @return returns praticipant Protocol ID
 	*/
	public String getParticipantProtocolID() {
		return participantProtocolID;
	}

	/**
	 * @param participantProtocolID sets participant protocol ID
 	*/
	public void setParticipantProtocolID(String participantProtocolID) {
		this.participantProtocolID = participantProtocolID;
	}
	
	
	/**
   * Overrides the validate method of ActionForm.
   * */
   public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
   {
   	
	   ActionErrors errors = new ActionErrors();
	   Validator validator = new Validator();
	   try
	   {
	   		setRedirectValue(validator);
	   	
	   	    //check if Protocol Title is empty.
			if (collectionProtocolID==-1)
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("collectionprotocolregistration.protocoltitle")));
		  	}
         	// Mandar 10-apr-06 : bugid :353 
        	// Error messages should be in the same sequence as the sequence of fields on the page.

			// changes as per Bugzilla Bug 287 			
				
			if (validator.isEmpty(participantProtocolID))
			{
				if (participantID == -1 || participantID == 0)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.collectionprotocolregistration.atleast"));
				}
				
			}
			
			//  date validation according to bug id 707, 722 and 730
			String errorKey = validator.validateDate(registrationDate,true ); 
			if(errorKey.trim().length() >0  )
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(errorKey,ApplicationProperties.getValue("collectionprotocolregistration.date")));
			}
			

			//
			if (!validator.isValidOption(activityStatus))
			{
			    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("collectionprotocolregistration.activityStatus")));
			}
	   }
	   catch(Exception excp)
	   {
		   Logger.out.error(excp.getMessage(),excp);
	   }
	   return errors;
	}
    
	/**
	* Resets the values of all the fields.
	* Is called by the overridden reset method defined in ActionForm.  
	* */
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
     * @param formBeanId - FormBean ID of the object inserted
     *  @param addObjectIdentifier - Identifier of the Object inserted 
     */
	public void setAddNewObjectIdentifier(String addNewFor, Long addObjectIdentifier)
	 {
	     if(addNewFor.equals("collectionProtocolId"))
	     {
	         setCollectionProtocolID(addObjectIdentifier.longValue());
	     }
	     else if(addNewFor.equals("participantId"))
	     {
	         setParticipantID(addObjectIdentifier.longValue());
	         //setCheckedButton(true);
	     }
	 }
	
	public String getParticipantName()
	{
		return participantName;
	}
	
	public void setParticipantName(String participantName)
	{
		this.participantName = participantName;
	}
	
//	 Consent Tracking Virender Mehta 		
	/**
	 * @return consentDate The Date on Which Consent is Signed
	 */	
	public String getConsentDate() 
	{
		return consentDate;
	}

	/**
	 * @param consentDate The Date on Which Consent is Signed
	 */
	public void setConsentDate(String consentDate) 
	{
		this.consentDate = consentDate;
	}

	/**
	 * @return signedConsentUrl The reference to the electric signed document(eg PDF file)
	 */	
	public String getSignedConsentUrl() 
	{
		return signedConsentUrl;
	}
	/**
	 * @param signedConsentUrl The reference to the electric signed document(eg PDF file)
	 */	
	public void setSignedConsentUrl(String signedConsentUrl) 
	{
		this.signedConsentUrl = signedConsentUrl;
	}
	
	/**
	 * @return witnessId The name of the witness to the consent Signature(PI or coordinator of the Collection Protocol)
	 */	
	public long getWitnessId() 
	{
		return witnessId;
	}
	
	/**
	 * @param witnessId The name of the witness to the consent Signature(PI or coordinator of the Collection Protocol)
	 */	
	public void setWitnessId(long witnessId) 
	{
		this.witnessId = witnessId;
	}
	
	/**
     * @param key Key prepared for saving data.
     * @param value Values correspponding to key
     */
    public void setConsentResponseValue(String key, Object value) 
    {
   	 if (isMutable())
   		consentResponseValues.put(key, value);
    }
    
    /**
     * 
     * @param key Key prepared for saving data.
     * @return consentResponseValues
     */
    public Object getConsentResponseValue(String key) 
    {
        return consentResponseValues.get(key);
    }
    
	/**
	 * @return values in map consentResponseValues
	 */
	public Collection getAllConsentResponseValue() 
	{
		return consentResponseValues.values();
	}

	/**
	 * @return consentResponseValues The reference to the participant Response at CollectionprotocolReg Level
	 */	
	public Map getConsentResponseValues() 
	{
		return consentResponseValues;
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
		return consentTierCounter;
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
		return withdrawlButtonStatus;
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
		Collection consentTiersList=new ArrayList();
		String [] strArray = null;
		int noOfConsents =this.getConsentTierCounter();
		for(int counter=0;counter<noOfConsents;counter++)
		{	
			strArray = new String[4];
			strArray[0]="consentResponseValue(ConsentBean:"+counter+"_consentTierID)";
			strArray[1]="consentResponseValue(ConsentBean:"+counter+"_statement)";
			//strArray[1]=(String)this.consentResponseValues.get("ConsentBean:"+counter+"_statement");
			strArray[2]="consentResponseValue(ConsentBean:"+counter+"_participantResponse)";
			strArray[3]="consentResponseValue(ConsentBean:"+counter+"_participantResponseID)";
			
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

}
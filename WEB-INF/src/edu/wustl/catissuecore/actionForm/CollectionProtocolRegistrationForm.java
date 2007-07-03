/**
 * <p>Title: CollectionProtocolRegistration Class>
 * <p>Description:  A registration of a Participant to a Collection Protocol.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ajay Sharma
 * @version 1.00
 */

package edu.wustl.catissuecore.actionForm;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;


public class CollectionProtocolRegistrationForm extends AbstractActionForm
{
    	
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
	 *
	 */    	
	protected boolean checkedButton; 	
	
	/**
	 * Default Constructor
	 */
	public CollectionProtocolRegistrationForm()
	{
		//reset();
	}
	
	/**
	 * It will set all values of member variables from Domain Object
	 * @param abstractDomain domain object
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
    }
    
	/**
	* @return COLLECTION_PROTOCOL_REGISTRATION_FORM_ID Returns the id assigned to form bean
	*/
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
	 * @return collectionProtocolID 
	 */
	public long getCollectionProtocolID()
	{
		return collectionProtocolID;
	}

	/**
	 * @param collectionProtocolID Setting Collection protocol id
	 */
	public void setCollectionProtocolID(long collectionProtocolID) 
	{
		this.collectionProtocolID = collectionProtocolID;
	}

	/**
	 * @return Returns unique participant ID 
	 */
	public long getParticipantID()
	{
		return participantID;
	}

	/**
	 * @param participantID sets unique participant ID 
	 */
	public void setParticipantID(long participantID) 
	{
		this.participantID = participantID;
	}

	/**
 	* @return returns praticipant Protocol ID
 	*/
	public String getParticipantProtocolID()
	{
		return participantProtocolID;
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
			String errorKey = validator.validateDate(registrationDate,true); 
			if(errorKey.trim().length() >0)
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
     * @param addNewFor - FormBean ID of the object inserted
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
	
	/**
	 * @return participantName
	 */
	public String getParticipantName()
	{
		return participantName;
	}
	
	/**
	 * @param participantName Setting participant name
	 */
	public void setParticipantName(String participantName)
	{
		this.participantName = participantName;
	}
}
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

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.catissuecore.util.global.Validator;
import edu.wustl.common.util.logger.Logger;


public class CollectionProtocolRegistrationForm extends AbstractActionForm
{
    /**
     * System generated unique systemIdentifier.
     */
    protected long systemIdentifier;
    	
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
	protected String participantProtocolID="";

	/**
	 * Date on which the Participant is registered to the Collection Protocol.
	 */
	protected String registrationDate;

   	/**
	 * Represents the operation(Add/Edit) to be performed.
	 *
	 */
	private String operation;
    
	/**
	 * Represents the weather participant Name is selected or not.
	 *
	 */    	
	protected boolean checkedButton; 	
	
	public CollectionProtocolRegistrationForm()
	{
		//reset();
	}
	
	/**
	 * It will set all values of member variables from Domain Object
	 * @param CollectionProtocolRegistration domain object
	 */	
    public void setAllValues(AbstractDomainObject abstractDomain)
    {
    	try
		{
    		CollectionProtocolRegistration registration = (CollectionProtocolRegistration)abstractDomain;
  	
    		this.collectionProtocolID = registration.getCollectionProtocol().getSystemIdentifier().longValue();
  	
		  	if(registration.getParticipant() != null)
		  	{
		  		this.participantID = registration.getParticipant().getSystemIdentifier().longValue();
		  		checkedButton = true;
		  	}
  	
		  	this.participantProtocolID = Utility.toString(registration.getProtocolParticipantIdentifier());
  		  	this.registrationDate = Utility.parseDateToString(registration.getRegistrationDate(),Constants.DATE_PATTERN_MM_DD_YYYY);
		}
    	catch (Exception excp)
		{
			// use of logger as per bug 79
			Logger.out.error(excp.getMessage(),excp); 
		}
    }
	   
	/**
	* @return Returns the id assigned to form bean
	*/
	public int getFormId()
	{
		return Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID;
	}
	
	/**
	 * Checks the operation to be performed is add operation.
	 * @return Returns true if operation is equal to "add", else it returns false
	 * */
	public boolean isAddOperation()
	{
		return(getOperation().equals(Constants.ADD));
	}
	

	/**
	 * Returns the operation(Add/Edit) to be performed.
	 * @return Returns the operation.
	 * @see #setOperation(String)
	 */
	public String getOperation()
	{
		return operation;
	}

	/**
	 * Sets the operation to be performed.
	 * @param operation The operation to set.
	 * @see #getOperation()
	 */
	public void setOperation(String operation)
	{
		this.operation = operation;
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

           
	public String getActivityStatus()
	{
		return null;
	}
    

	public void setActivityStatus(String activityStatus)
	{
		// TODO Auto-generated method stub
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
	 * Returns the identifier assigned to Participant Registration.
	 * @return long identifier assigned to Participant Registration.
	 * @see #setIdentifier(long)
	 * */
	public long getSystemIdentifier()
	{
		return (this.systemIdentifier);
	}

	/**
	 * Sets an the identifier for a Participant Registration.
	 * @param systemIdentifier identifier to be assigned to Participant Registration.
	 * @see #getIdentifier()
	 * */
	public void setSystemIdentifier(long systemIdentifier)
	{
		this.systemIdentifier = systemIdentifier;
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
	   	    //check if Protocol Title is empty.
			if (collectionProtocolID==-1)
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("collectionprotocolregistration.protocoltitle")));
		  	}
		  	//check if date is empty.
			if (validator.isEmpty(registrationDate))
			{
			    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("collectionprotocolregistration.date")));
			}
		
			// changes as per Bugzilla Bug 287 
			if (checkedButton == true)
			{
				if (participantID == -1)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("collectionProtocolReg.participantName")));
				}
			} // name selected
			else
			{
				if (validator.isEmpty(participantProtocolID))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("collectionProtocolReg.participantProtocolID")));
				}
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
//	   this.systemIdentifier = 0;
//	   this.operation = null;
   }

	
	/**
	 * @return returns boolean value of checkbox button
	 */
	public boolean isCheckedButton() {
		return checkedButton;
	}

	/**
	 * @param checkedButton sets value of checkeButton
	 */
	public void setCheckedButton(boolean checkedButton) {
		this.checkedButton = checkedButton;
	}

}
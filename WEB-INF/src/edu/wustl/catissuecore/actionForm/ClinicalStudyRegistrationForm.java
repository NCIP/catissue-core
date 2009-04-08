/**
 * <p>Title: CollectionProtocolRegistration Class>
 * <p>Description:  A registration of a Participant to a Collection Protocol.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Shital Lawhale
 * @version 1.00
 */

package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.ClinicalStudyRegistration;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;


public class ClinicalStudyRegistrationForm extends AbstractActionForm 
{   
	private static final long serialVersionUID = 1L;
    /**
     * System generated unique collection protocol Identifier
     */
	
	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(ClinicalStudyRegistrationForm.class);
	
	
    private long clinicalStudyId;
    
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
    protected String participantClinicalStudyID="";

    /**
     * Date on which the Participant is registered to the Collection Protocol.
     */
    protected String registrationDate;

    /**
     * Represents the weather participant Name is selected or not.
     */     
    protected boolean checkedButton;    
    


    public ClinicalStudyRegistrationForm()
    {
    	super();
        //reset();
    }           
    
    /**
     * It will set all values of member variables from Domain Object
     * @param CollectionProtocolRegistration domain object
     */ 
    public void setAllValues(AbstractDomainObject abstractDomain)
    {
    	final ClinicalStudyRegistration registration = (ClinicalStudyRegistration)abstractDomain;
    	this.setId(registration.getId().longValue());
        this.setActivityStatus(registration.getActivityStatus());
        this.clinicalStudyId = registration.getClinicalStudy().getId().longValue();
        final String firstName = Utility.toString(registration.getParticipant().getFirstName());
        final String lastName = Utility.toString(registration.getParticipant().getLastName());
        final String birthDate = Utility.toString(registration.getParticipant().getBirthDate());
        final String ssn = Utility.toString(registration.getParticipant().getSocialSecurityNumber());
            
        if((registration.getParticipant() != null) && (firstName.trim().length()>0 || lastName.trim().length()>0 || birthDate.trim().length()>0 || ssn.trim().length()>0))
        {
            this.participantID = registration.getParticipant().getId().longValue();
            
            //Bug-2819: Performance issue due to participant drop down: Jitendra
            this.participantName = registration.getParticipant().getMessageLabel();
            //checkedButton = true;
        }
        this.participantClinicalStudyID = Utility.toString(registration.getClinicalStudyParticipantIdentifier());
        this.registrationDate = Utility.parseDateToString(registration.getRegistrationDate(),CommonServiceLocator.getInstance().getDatePattern());
       }
    /**
    * @return Returns the id assigned to form bean
    */
    public int getFormId()
    {
        return Constants.CLINICAL_STUDY_REGISTRATION_FORM_ID;
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
    public void setRegistrationDate(final String registrationDate)
    {
        this.registrationDate = registrationDate;
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
    public void setParticipantID(final long participantID) {
        this.participantID = participantID;
    }

    /**
    * @return returns praticipant Protocol ID
    */
    public String getParticipantClinicalStudyID() {
        return participantClinicalStudyID;
    }

    /**
     * @param participantProtocolID sets participant protocol ID
    */
    public void setParticipantClinicalStudyID(String participantClinicalStudyID) {
        this.participantClinicalStudyID = participantClinicalStudyID;
    }
    
    
    /**
   * Overrides the validate method of ActionForm.
   * */
   public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
   {
    
	   final ActionErrors errors = new ActionErrors();
	   final Validator validator = new Validator();
       try
       {
            setRedirectValue(validator);        
            //check if Protocol Title is empty.
            if (clinicalStudyId==-1)
            {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("collectionprotocolregistration.protocoltitle")));
            }           
            if (validator.isEmpty(participantClinicalStudyID) && (participantID == -1 || participantID == 0))
            {
            	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.collectionprotocolregistration.atleast"));
            }           
            final String errorKey = validator.validateDate(registrationDate,true ); 
            if(errorKey.trim().length() >0  )
            {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(errorKey,ApplicationProperties.getValue("collectionprotocolregistration.date")));
            }
            if (!validator.isValidOption(this.getActivityStatus()))
            {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("collectionprotocolregistration.activityStatus")));
            }
       }
       catch(Exception excp)
       {
    	   logger.error(excp.getMessage(),excp);
       }
       return errors;
    }
    
    /**
    * Resets the values of all the fields.
    * Is called by the overridden reset method defined in ActionForm.  
    * */
   protected void reset()
   {
//     this.collectionProtocolID = 0;
//     this.participantID = 0;
//     this.participantProtocolID = null;
//     this.registrationDate = null;
//     this.id = 0;
//     this.operation = null;
   }

    
//  /**
//   * @return returns boolean value of checkbox button
//   */
//  public boolean isCheckedButton() {
//      return checkedButton;
//  }
//
//  /**
//   * @param checkedButton sets value of checkeButton
//   */
//  public void setCheckedButton(boolean checkedButton) {
//      this.checkedButton = checkedButton;
//  }

    /**
     * This method sets Identifier of Objects inserted by AddNew activity in Form-Bean which initialized AddNew action
     * @param formBeanId - FormBean ID of the object inserted
     *  @param addObjectIdentifier - Identifier of the Object inserted 
     */
    public void setAddNewObjectIdentifier(String addNewFor, Long addObjectIdentifier)
     {
         if("clinicalStudyId".equals(addNewFor))
         {
             setClinicalStudyId(addObjectIdentifier.longValue());
         }
         else if("participantId".equals(addNewFor))
         {
             setParticipantID(addObjectIdentifier.longValue());
             //setCheckedButton(true);
         }
     }
    
    public String getParticipantName()
    {
        return participantName;
    }
    
    public void setParticipantName(final String participantName)
    {
        this.participantName = participantName;
    }
    

    
        
    public long getClinicalStudyId()
    {
        return clinicalStudyId;
    }

    
    public void setClinicalStudyId(final long clinicalStudyId)
    {
        this.clinicalStudyId = clinicalStudyId;
    }

}
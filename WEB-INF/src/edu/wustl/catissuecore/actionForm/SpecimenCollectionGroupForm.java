/**
 * <p>Title: SpecimenCollectionGroupForm Class>
 * <p>Description:  SpecimenCollectionGroupForm Class is used to encapsulate 
 * all the request parameters passed from New SpecimenCollectionGroup webpage. </p>
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

import edu.wustl.catissuecore.domain.ClinicalReport;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.DefaultValue;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * SpecimenCollectionGroupForm Class is used to encapsulate 
 * all the request parameters passed from New SpecimenCollectionGroup webpage.
 * @author ajay_sharma
 */
public class SpecimenCollectionGroupForm extends AbstractActionForm
{
	/**
     * Name : Virender Mehta
     * Reviewer: Sachin Lale
     * Bug ID: defaultValueConfiguration_BugID
     * Patch ID:defaultValueConfiguration_BugID_7
     * Description: Configuration for default value for clinicalDiagnosis and clinicalStatus
     *
     */
	private String clinicalDiagnosis = (String)DefaultValue.getDefaultValue(Constants.DEFAULT_CLINICAL_DIAGNOSIS);
    
	private String clinicalStatus = (String)DefaultValue.getDefaultValue(Constants.DEFAULT_CLINICAL_STATUS);
	
	private String surgicalPathologyNumber;
	
	private long participantsMedicalIdentifierId;
	
	/**
	 * An id which refers to the site of the container if it is parent container.
	 */
	private long siteId;
	
	private long  collectionProtocolId;
	
	private long collectionProtocolEventId;
		
	/**
     * Radio button to choose participantName/participantNumber.
     */
    private int checkedButton = 1;
    
	/**
     * unique name for Specimen Collection Group 
     */
    private String name ;
	
	private long participantId;
	
	private String protocolParticipantIdentifier;
	
	//For AddNew functionality
	private long collectionProtocolRegistrationId;
    
    
    /**
     * Comments given by user.
     */
    
    /**
     * Name: Shital Lawhale
     * Reviewer Name : Sachin Lale 
     * Bug ID: 3052
     * Patch ID: 3052_1_2
     * See also: 1_1 to 1_5
     * Description : A comment field at the Specimen Collection Group.
     */
    private String comment;
    
    
	/**
	 * Name : Ashish Gupta
	 * Reviewer Name : Sachin Lale 
	 * Bug ID: Multiple Specimen Bug
	 * Patch ID: Multiple Specimen Bug_3 
	 * See also: 1-8
	 * Description: number of specimens field on scg form
	*/
	private int numberOfSpecimens = 1;
	
	
	/**
	 * @return the numberOfSpecimens
	 */
	public int getNumberOfSpecimens()
	{
		return numberOfSpecimens;
	}


	
	/**
	 * @param numberOfSpecimens the numberOfSpecimens to set
	 */
	public void setNumberOfSpecimens(int numberOfSpecimens)
	{
		this.numberOfSpecimens = numberOfSpecimens;
	}


	/**
	 * No argument constructor for SpecimenCollectionGroupForm class 
	 */
	public SpecimenCollectionGroupForm()
	{
		reset();
	}
	
   
    /**
     * @return Returns the clinicalDiagnosis.
     */
    public String getClinicalDiagnosis()
    {
        return clinicalDiagnosis;
    }
    /**
     * @param clinicalDiagnosis The clinicalDiagnosis to set.
     */
    public void setClinicalDiagnosis(String cinicalDiagnosis)
    {
        this.clinicalDiagnosis = cinicalDiagnosis;
    }
           
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
    /**
     * @return Returns the surgicalPathologyNumber.
     */
    public String getSurgicalPathologyNumber()
    {
        return surgicalPathologyNumber;
    }
    /**
     * @param surgicalPathologyNumber The surgicalPathologyNumber to set.
     */
    public void setSurgicalPathologyNumber(String surgicalPathologyNumber)
    {
        this.surgicalPathologyNumber = surgicalPathologyNumber;
    }
  
		
	/**
	 * @return
	 */
	public long getParticipantId() 
	{
		return participantId;
	}

	/**
	 * @param participantId
	 */
	public void setParticipantId(long participantId) 
	{
		this.participantId = participantId;
	}

	/**
	 * For AddNew functionality
	 * @return
	 */
	public long getCollectionProtocolRegistrationId()
	{
	    return this.collectionProtocolRegistrationId;
	}
	public void setCollectionProtocolRegistrationId(long collectionProtocolRegistrationId)
	{
	    this.collectionProtocolRegistrationId = collectionProtocolRegistrationId;
	}
	
	/**
	 * @return Returns the checkedButton.
	 */
	public int getCheckedButton()
	{
		return checkedButton;
	}

	/**
	 * @param checkedButton The checkedButton to set.
	 */
	public void setCheckedButton(int checkedButton)
	{
			if(isMutable())
				this.checkedButton = checkedButton;
	}

	/**
	 * Resets the values of all the fields.
	 * Is called by the overridden reset method defined in ActionForm.  
	 * */
	protected void reset()
	{
//		this.clinicalDiagnosis = null;
//	    
//		this.clinicalStatus = null;;
//		
//		this.surgicalPathologyNumber = null;
//		
//		this.protocolParticipantIdentifier =  null;
//		checkedButton = 1;
	}
	/**
	   * This function Copies the data from an storage type object to a StorageTypeForm object.
	   * @param storageType A StorageType object containing the information about storage type of the container.  
	   */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
//		if(operation.equals("add" ) )
//			setMutable(true );
			
		SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) abstractDomain;
			
		id = specimenCollectionGroup.getId().longValue();
		name =  specimenCollectionGroup.getName();    
		Logger.out.debug("specimenCollectionGroup.getClinicalDiagnosis() "+specimenCollectionGroup.getClinicalDiagnosis());
		clinicalDiagnosis = Utility.toString(specimenCollectionGroup.getClinicalDiagnosis());
		clinicalStatus = Utility.toString(specimenCollectionGroup.getClinicalStatus());
		activityStatus = Utility.toString(specimenCollectionGroup.getActivityStatus());
        
         /**
         * Name: Shital Lawhale
         * Reviewer Name : Sachin Lale 
         * Bug ID: 3052
         * Patch ID: 3052_1_4
         * See also: 1_1 to 1_5
         * Description : Get comment field from database and set it to form bean.
         */  
        comment = Utility.toString(specimenCollectionGroup.getComment());
			
		ClinicalReport clinicalReport = specimenCollectionGroup.getClinicalReport();
		surgicalPathologyNumber = Utility.toString(clinicalReport.getSurgicalPathologyNumber());
		
		if(clinicalReport.getParticipantMedicalIdentifier()!=null)
			participantsMedicalIdentifierId = clinicalReport.getParticipantMedicalIdentifier().getId().longValue();
			
		collectionProtocolId = specimenCollectionGroup.getCollectionProtocolRegistration().getCollectionProtocol().getId().longValue();
		collectionProtocolEventId = specimenCollectionGroup.getCollectionProtocolEvent().getId().longValue();
		
		Participant participant = specimenCollectionGroup.getCollectionProtocolRegistration().getParticipant();
		Logger.out.debug("SCgForm --------- Participant : -- "+ participant.toString() );
		//if(participant!=null)
		String firstName = null;
		String lastName = null;
		String birthDate = null;
		String ssn = null;
		
		if(participant.getFirstName()==null)
			firstName ="";
		else
			firstName = participant.getFirstName();
		
		if(participant.getLastName()==null)
			lastName ="";
		else
			lastName = participant.getLastName();
		
		if(participant.getBirthDate()==null)
			birthDate ="";
		else
			birthDate = participant.getBirthDate().toString();
		
		if(participant.getSocialSecurityNumber()==null)
			ssn ="";
		else
			ssn = participant.getSocialSecurityNumber();
		
	
		if(firstName.length()>0 || lastName.length()>0 || birthDate.length()>0 || ssn.length()>0)
		{
				participantId = participant.getId().longValue();
				checkedButton = 1;
		}
		else
		{
			protocolParticipantIdentifier =  Utility.toString(specimenCollectionGroup.getCollectionProtocolRegistration().getProtocolParticipantIdentifier());
			checkedButton = 2;
		}
		
		Logger.out.debug("participantId.................................."+participantId);
		Logger.out.debug("protocolParticipantIdentifier........................."+protocolParticipantIdentifier);
		Logger.out.debug("SCgForm --------- checkButton : -- " + checkedButton );
		siteId = specimenCollectionGroup.getSite().getId().longValue();
	}
	
	  
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getFormId()
	 */
	public int getFormId() 
	{
		return Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID;

	}

	
	/**
	 * @return
	 */
	public long getSiteId() 
	{
		return siteId;
	}

	/**
	 * @param siteId
	 */
	public void setSiteId(long siteId) 
	{
		this.siteId = siteId;
	}

	/**
	 * @return
	 */
	public String getClinicalStatus() {
		return clinicalStatus;
	}

	/**
	 * @param clinicalStatus
	 */
	public void setClinicalStatus(String clinicalStatus) {
		this.clinicalStatus = clinicalStatus;
	}

	/**
	 * @return
	 */
	public long getCollectionProtocolEventId() {
		return collectionProtocolEventId;
	}

	/**
	 * @param collectionProtocolEventId
	 */
	public void setCollectionProtocolEventId(long collectionProtocolEventId) {
		this.collectionProtocolEventId = collectionProtocolEventId;
	}

	/**
	 * @return
	 */
	public long getCollectionProtocolId() {
		return collectionProtocolId;
	}

	/**
	 * @param collectionProtocolId
	 */
	public void setCollectionProtocolId(long collectionProtocolId) {
		this.collectionProtocolId = collectionProtocolId;
	}

	/**
	 * @return
	 */
	public long getParticipantsMedicalIdentifierId() {
		return participantsMedicalIdentifierId;
	}

	/**
	 * @param participantsMedicalIdentifierId
	 */
	public void setParticipantsMedicalIdentifierId(long participantsMedicalIdentifierId) {
		this.participantsMedicalIdentifierId = participantsMedicalIdentifierId;
	}

	/**
	 * @return
	 */
	public String getProtocolParticipantIdentifier() {
		return protocolParticipantIdentifier;
	}

	/**
	 * @param protocolParticipantIdentifier
	 */
	public void setProtocolParticipantIdentifier(String protocolParticipantIdentifier) {
		this.protocolParticipantIdentifier = protocolParticipantIdentifier;
	}


	/**
	 * Overrides the validate method of ActionForm.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		Validator validator = new Validator();
		try
		{
			setRedirectValue(validator  );
			if(this.collectionProtocolId == -1)
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",
								ApplicationProperties.getValue("specimenCollectionGroup.protocolTitle")));
			}
			
			if(this.siteId == -1)
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",
								ApplicationProperties.getValue("specimenCollectionGroup.site")));
			}
			
			
			// Check what user has selected Participant Name / Participant Number
			if(this.checkedButton == 1 )
			{   
				//if participant name field is checked.
				if(this.participantId == -1)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",
									ApplicationProperties.getValue("specimenCollectionGroup.collectedByParticipant")));
				}
			}
			else
			{
				if(!validator.isValidOption(this.protocolParticipantIdentifier))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",
									ApplicationProperties.getValue("specimenCollectionGroup.collectedByProtocolParticipantNumber")));
				}
			}
			if(this.name.equals(""))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
								ApplicationProperties.getValue("specimenCollectionGroup.groupName")));
			}
			
            // Mandatory Field : Study Calendar event point
			if(this.collectionProtocolEventId == -1)
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",
								ApplicationProperties.getValue("specimenCollectionGroup.studyCalendarEventPoint")));
			}
			
			// Mandatory Field : clinical Diagnosis
			if(!validator.isValidOption(this.clinicalDiagnosis))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",
								ApplicationProperties.getValue("specimenCollectionGroup.clinicalDiagnosis")));
			}
			
			// Mandatory Field : clinical Status
			if(!validator.isValidOption(clinicalStatus))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",
								ApplicationProperties.getValue("specimenCollectionGroup.clinicalStatus")));
			}
			
			//Condition for medical Record Number.
			if(this.checkedButton == 1 )
			{   
				//if participant name field is checked.
				// here medical record number field should be enabled and must have some value selected.
//					if(this.participantsMedicalIdentifierId == -1){
//						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",
//										ApplicationProperties.getValue("specimenCollectionGroup.medicalRecordNumber")));
//					}
							
			}
			else
			{
				// here this field will be alltogether disabled
				// No need of any condition.
			}
			
			//Added by Ashish for Multiple Specimens
			/**
 * Name : Ashish Gupta
 * Reviewer Name : Sachin Lale 
 * Bug ID: Multiple Specimen Bug
 * Patch ID: Multiple Specimen Bug_4 
 * See also: 1-8
 * Description: Remove the page on which number of multiple specimens are entered while going to multiple specimen page.
	*/
			String buttonName = request.getParameter("button");

			if(buttonName != null && !buttonName.equals(""))
			{
				if(numberOfSpecimens < 1)
		        {
		        	errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.multiplespecimen.minimumspecimen"));
		        }
			}
		}
		catch (Exception excp)
		{
	    	// use of logger as per bug 79
	    	Logger.out.error(excp.getMessage(),excp); 
			errors = new ActionErrors();
		}
		return errors;
	}
	
	/**
     * This method sets Identifier of Objects inserted by AddNew activity in Form-Bean which initialized AddNew action
     * @param formBeanId - FormBean ID of the object inserted
     *  @param addObjectIdentifier - Identifier of the Object inserted 
     */
	public void setAddNewObjectIdentifier(String addNewFor, Long addObjectIdentifier)
    {
        if(addNewFor.equals("collectionProtocol"))
        {
            setCollectionProtocolId(addObjectIdentifier.longValue());
        }
        else if(addNewFor.equals("site"))
        {
            setSiteId(addObjectIdentifier.longValue());
        }
        else if(addNewFor.equals("participant"))
        {
//            setParticipantId(addObjectIdentifier.longValue());
            setCollectionProtocolRegistrationId(addObjectIdentifier.longValue());
            setCheckedButton(1);
        }
        else if(addNewFor.equals("protocolParticipantIdentifier"))
        {
            setCollectionProtocolRegistrationId(addObjectIdentifier.longValue());
//            setProtocolParticipantIdentifier(addObjectIdentifier.toString());
            setCheckedButton(2);
        }
    }


    /**
     * Name: Shital Lawhale
     * Reviewer Name : Sachin Lale 
     * Bug ID: 3052
     * Patch ID: 3052_1_3
     * See also: 1_1 to 1_5
     * Description : A comment field at the Specimen Collection Group.
     */    
    
    /**
     * returns comment
     */
    public String getComment()
    {
        return comment;
    }

    /**
     * 
     * @param comment : user comment to set
     */
    
    public void setComment(String comment)
    {
        this.comment = comment;
    }
}
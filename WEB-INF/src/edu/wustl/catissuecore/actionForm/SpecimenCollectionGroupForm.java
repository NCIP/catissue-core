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

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.ClinicalReport;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.catissuecore.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * SpecimenCollectionGroupForm Class is used to encapsulate 
 * all the request parameters passed from New SpecimenCollectionGroup webpage.
 * @author ajay_sharma
 */
public class SpecimenCollectionGroupForm extends AbstractActionForm
{
	/**
	 * Represents the operation(Add/Edit) to be performed.
	 * */
	private String operation;
	
    private long systemIdentifier;
    
	private String clinicalDiagnosis;
    
	private String clinicalStatus;
	
	private String activityStatus = Constants.ACTIVITY_STATUS_ACTIVE;
	
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
	
	private long participantId;
	
	private String protocolParticipantIdentifier;
	
	/**
	 * No argument constructor for SpecimenCollectionGroupForm class 
	 */
	public SpecimenCollectionGroupForm()
	{
		reset();
	}
	
	/**
     * @return Returns the systemIdentifier.
     */
    public long getSystemIdentifier()
    {
	    return systemIdentifier;
    }
    
    /**
    * @param systemIdentifier The systemIdentifier to set.
    */
    public void setSystemIdentifier(long systemIdentifier)
    {
	    this.systemIdentifier = systemIdentifier;
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
//		this.activityStatus = null;
//		
//		this.surgicalPathologyNumber = null;
//		
//		this.protocolParticipantIdentifier =  null;
	}
	/**
	   * This function Copies the data from an storage type object to a StorageTypeForm object.
	   * @param storageType A StorageType object containing the information about storage type of the container.  
	   */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		try
		{
			SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) abstractDomain;
				
			systemIdentifier = specimenCollectionGroup.getSystemIdentifier().longValue();
			    
			Logger.out.debug("specimenCollectionGroup.getClinicalDiagnosis() "+specimenCollectionGroup.getClinicalDiagnosis());
			clinicalDiagnosis = Utility.toString(specimenCollectionGroup.getClinicalDiagnosis());
			clinicalStatus = Utility.toString(specimenCollectionGroup.getClinicalStatus());
			activityStatus = Utility.toString(specimenCollectionGroup.getActivityStatus());
				
			ClinicalReport clinicalReport = specimenCollectionGroup.getClinicalReport();
			surgicalPathologyNumber = Utility.toString(clinicalReport.getSurgicalPathologyNumber());
			
			if(clinicalReport.getParticipantMedicalIdentifier()!=null)
				participantsMedicalIdentifierId = clinicalReport.getParticipantMedicalIdentifier().getSystemIdentifier().longValue();
				
			collectionProtocolId = specimenCollectionGroup.getCollectionProtocolRegistration().getCollectionProtocol().getSystemIdentifier().longValue();
			collectionProtocolEventId = specimenCollectionGroup.getCollectionProtocolEvent().getSystemIdentifier().longValue();
			
			Participant participant = specimenCollectionGroup.getCollectionProtocolRegistration().getParticipant();
			if(participant!=null)
			{
				participantId = participant.getSystemIdentifier().longValue();
				checkedButton = 1;
			}
			else
			{
				protocolParticipantIdentifier =  Utility.toString(specimenCollectionGroup.getCollectionProtocolRegistration().getProtocolParticipantIdentifier());
				checkedButton = 2;
			}
			
			siteId = specimenCollectionGroup.getSite().getSystemIdentifier().longValue();
		}
		catch (Exception excp)
		{
	    	// use of logger as per bug 79
	    	Logger.out.error(excp.getMessage(),excp); 
		}
	}
	  
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getFormId()
	 */
	public int getFormId() 
	{
		return Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID;

	}
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#isAddOperation()
	 */
	public boolean isAddOperation() 
	{
		return getOperation().equals(Constants.ADD);
	}
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getActivityStatus()
	 */
	public String getActivityStatus() 
	{
		return this.activityStatus;
	}
	
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#setActivityStatus(java.lang.String)
	 */
	public void setActivityStatus(String activityStatus) 
	{
		this.activityStatus = activityStatus;
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
	 * @return
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * @param operation
	 */
	public void setOperation(String operation) {
		this.operation = operation;
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
		}
		catch (Exception excp)
		{
	    	// use of logger as per bug 79
	    	Logger.out.error(excp.getMessage(),excp); 
			errors = new ActionErrors();
		}
		return errors;
	}
}
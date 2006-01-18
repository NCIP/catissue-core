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
 
	private String clinicalDiagnosis;
    
	private String clinicalStatus;
	
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
			
		systemIdentifier = specimenCollectionGroup.getId().longValue();
		    
		Logger.out.debug("specimenCollectionGroup.getClinicalDiagnosis() "+specimenCollectionGroup.getClinicalDiagnosis());
		clinicalDiagnosis = Utility.toString(specimenCollectionGroup.getClinicalDiagnosis());
		clinicalStatus = Utility.toString(specimenCollectionGroup.getClinicalStatus());
		activityStatus = Utility.toString(specimenCollectionGroup.getActivityStatus());
			
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
	
	public void setAllVal(Object obj)
    {
	//		if(operation.equals("add" ) )
	//		setMutable(true );
			
	    edu.wustl.catissuecore.domainobject.SpecimenCollectionGroup specimenCollectionGroup = (edu.wustl.catissuecore.domainobject.SpecimenCollectionGroup) obj;
			
		systemIdentifier = specimenCollectionGroup.getId().longValue();
		    
		Logger.out.debug("specimenCollectionGroup.getClinicalDiagnosis() "+specimenCollectionGroup.getClinicalDiagnosis());
		clinicalDiagnosis = Utility.toString(specimenCollectionGroup.getClinicalDiagnosis());
		clinicalStatus = Utility.toString(specimenCollectionGroup.getClinicalStatus());
		activityStatus = Utility.toString(specimenCollectionGroup.getActivityStatus());
			
		edu.wustl.catissuecore.domainobject.ClinicalReport clinicalReport = specimenCollectionGroup.getClinicalReport();
		surgicalPathologyNumber = Utility.toString(clinicalReport.getSurgicalPathologyNumber());
		
		if(clinicalReport.getParticipantMedicalIdentifier()!=null)
			participantsMedicalIdentifierId = clinicalReport.getParticipantMedicalIdentifier().getId().longValue();
			
		collectionProtocolId = specimenCollectionGroup.getCollectionProtocolRegistration().getCollectionProtocol().getId().longValue();
		collectionProtocolEventId = specimenCollectionGroup.getCollectionProtocolEvent().getId().longValue();
		
		edu.wustl.catissuecore.domainobject.Participant participant = specimenCollectionGroup.getCollectionProtocolRegistration().getParticipant();
		Logger.out.debug("SCgForm --------- Participant : -- "+ participant.toString() );
		if(participant!=null)
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
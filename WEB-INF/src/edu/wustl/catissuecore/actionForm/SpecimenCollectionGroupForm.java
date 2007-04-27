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
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import edu.wustl.catissuecore.domain.ClinicalReport;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.DefaultValueManager;
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
	private String clinicalDiagnosis = (String)DefaultValueManager.getDefaultValue(Constants.DEFAULT_CLINICAL_DIAGNOSIS);
    
	private String clinicalStatus = (String)DefaultValueManager.getDefaultValue(Constants.DEFAULT_CLINICAL_STATUS);
	
	private String surgicalPathologyNumber;
	
	private long participantsMedicalIdentifierId;
	
	   /**
	 * Name : Ashish Gupta
	 * Reviewer Name : Sachin Lale 
	 * Bug ID: 2741
	 * Patch ID: 2741_9	 
	 * Description: Event Attributes
	*/
	private long collectionEventId;																											// Mandar : CollectionEvent 10-July-06
	private long collectionEventSpecimenId;
	private long collectionEventUserId;
	private String collectionEventdateOfEvent;
	private String collectionEventTimeInHours;
	private String collectionEventTimeInMinutes;
	private String collectionEventCollectionProcedure;
	private String collectionEventContainer;
	private String collectionEventComments;
	
	
	private long receivedEventId;
	private long receivedEventSpecimenId;
	private long receivedEventUserId;
	private String receivedEventDateOfEvent;
	private String receivedEventTimeInHours;
	private String receivedEventTimeInMinutes;
	private String receivedEventReceivedQuality;
	private String receivedEventComments;
	

	
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
	 * Name : Ashish Gupta
	 * Reviewer Name : Sachin Lale 
	 * Bug ID: 2741
	 * Patch ID: 2741_4	 
	 * Description: Attribute to set events in specimens associated with this scg
	*/
	private boolean applyEventsToSpecimens = false;
    
    
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
	
	//Patch ID: Bug#3184_28
	private String actualNumberOfSpecimen;
	
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
		
		/**
	 * Name : Ashish Gupta
	 * Reviewer Name : Sachin Lale 
	 * Bug ID: 2741
	 * Patch ID: 2741_10	 
	 * Description: Method to populate Events in SCG form
	*/
		//Populating the events
		setSCGEvents(specimenCollectionGroup);
	}
	/**
	 * @param specimenCollectionGroup
	 */
	private void setSCGEvents(SpecimenCollectionGroup specimenCollectionGroup)
	{
		Collection eventsParametersColl = specimenCollectionGroup.getSpecimenEventParametersCollection();
		if(eventsParametersColl != null && !eventsParametersColl.isEmpty())
		{
			Iterator iter = eventsParametersColl.iterator();
			while(iter.hasNext())
			{
				Object tempObj = iter.next();
				Calendar calender = Calendar.getInstance();
				if(tempObj instanceof CollectionEventParameters)
				{
					CollectionEventParameters collectionEventParameters = (CollectionEventParameters)tempObj;
					this.collectionEventId = collectionEventParameters.getId().longValue();																											// Mandar : CollectionEvent 10-July-06
					//this.collectionEventSpecimenId = collectionEventParameters.getSpecimen().getId().longValue();
					this.collectionEventUserId = collectionEventParameters.getUser().getId().longValue();					
				
				 	calender.setTime(collectionEventParameters.getTimestamp());
					this.collectionEventdateOfEvent = Utility.parseDateToString(collectionEventParameters.getTimestamp(),Constants.DATE_PATTERN_MM_DD_YYYY);
					this.collectionEventTimeInHours = Utility.toString(Integer.toString( calender.get(Calendar.HOUR_OF_DAY)));
					this.collectionEventTimeInMinutes  = Utility.toString(Integer.toString(calender.get(Calendar.MINUTE)));
					this.collectionEventCollectionProcedure = collectionEventParameters.getCollectionProcedure();
					this.collectionEventContainer = collectionEventParameters.getContainer();
					this.collectionEventComments = Utility.toString(collectionEventParameters.getComments());
				}
				else if(tempObj instanceof ReceivedEventParameters)
				{
					ReceivedEventParameters receivedEventParameters = (ReceivedEventParameters)tempObj;
					
					calender.setTime(receivedEventParameters.getTimestamp());
					this.receivedEventId = receivedEventParameters.getId().longValue();
				//	this.receivedEventSpecimenId = receivedEventParameters.getSpecimen().getId().longValue();
					this.receivedEventUserId = receivedEventParameters.getUser().getId().longValue();
					this.receivedEventDateOfEvent = Utility.parseDateToString(receivedEventParameters.getTimestamp(),Constants.DATE_PATTERN_MM_DD_YYYY);
					this.receivedEventTimeInHours = Utility.toString(Integer.toString( calender.get(Calendar.HOUR_OF_DAY)));
					this.receivedEventTimeInMinutes = Utility.toString(Integer.toString(calender.get(Calendar.MINUTE)));
					this.receivedEventReceivedQuality = receivedEventParameters.getReceivedQuality();
					this.receivedEventComments = Utility.toString(receivedEventParameters.getComments());
				}
			}
		}
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
			/**
	 * Name : Ashish Gupta
	 * Reviewer Name : Sachin Lale 
	 * Bug ID: 2741
	 * Patch ID: 2741_4	 
	 * Description: Methods for validation of events in scg
	*/
//			CollectionEvent validation.
    		EventsUtil.validateCollectionEvent(errors,validator,collectionEventUserId,collectionEventdateOfEvent,collectionEventCollectionProcedure);
    		//ReceivedEvent validation
    		EventsUtil.validateReceivedEvent(errors,validator,receivedEventUserId,receivedEventDateOfEvent,receivedEventReceivedQuality );
			
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
					setNumberOfSpecimens(1);
		        	errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.multiplespecimen.minimumspecimen"));		        	
		        }
			}
			request.getSession().setAttribute("scgForm", this);
			//For setting whether to set specimen 
			String applyToString = request.getParameter("applyToSpecimenValue");
			if(applyToString != null && applyToString.equals("true") )
			{
				applyEventsToSpecimens = true;
			}
			
			// Patch ID: 3184_29
			//Validate number of specimen entered against the number of actual number of specimen.
			validateNumberOfSpecimenField(errors);
		}
		catch (Exception excp)
		{
	    	// use of logger as per bug 79
	    	Logger.out.error(excp.getMessage(),excp); 
			errors = new ActionErrors();
		}
		return errors;
	}
	
	// Patch ID: 3184_30
	/**
	 * This method validates the number of specimen entered by the user against the actual number of specimen requirements 
	 * associated with the selected Study Calendar Event Point.
	 * @param errors
	 */
	private void validateNumberOfSpecimenField(ActionErrors errors) 
	{
		if(restrictSCGCheckbox != null)
		{
			if(restrictSCGCheckbox.equalsIgnoreCase(Constants.TRUE) && (numberOfSpecimens < Integer.parseInt(actualNumberOfSpecimen)))
			{
				errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.multiplespecimen.invalidNumberOfSpecimen"));
			}
		}
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
	 * @return the collectionEventCollectionProcedure
	 */
	public String getCollectionEventCollectionProcedure()
	{
		return collectionEventCollectionProcedure;
	}



	
	/**
	 * @param collectionEventCollectionProcedure the collectionEventCollectionProcedure to set
	 */
	public void setCollectionEventCollectionProcedure(String collectionEventCollectionProcedure)
	{
		this.collectionEventCollectionProcedure = collectionEventCollectionProcedure;
	}



	
	/**
	 * @return the collectionEventComments
	 */
	public String getCollectionEventComments()
	{
		return collectionEventComments;
	}



	
	/**
	 * @param collectionEventComments the collectionEventComments to set
	 */
	public void setCollectionEventComments(String collectionEventComments)
	{
		this.collectionEventComments = collectionEventComments;
	}



	
	/**
	 * @return the collectionEventContainer
	 */
	public String getCollectionEventContainer()
	{
		return collectionEventContainer;
	}



	
	/**
	 * @param collectionEventContainer the collectionEventContainer to set
	 */
	public void setCollectionEventContainer(String collectionEventContainer)
	{
		this.collectionEventContainer = collectionEventContainer;
	}



	
	/**
	 * @return the collectionEventdateOfEvent
	 */
	public String getCollectionEventdateOfEvent()
	{
		return collectionEventdateOfEvent;
	}



	
	/**
	 * @param collectionEventdateOfEvent the collectionEventdateOfEvent to set
	 */
	public void setCollectionEventdateOfEvent(String collectionEventdateOfEvent)
	{
		this.collectionEventdateOfEvent = collectionEventdateOfEvent;
	}



	
	/**
	 * @return the collectionEventId
	 */
	public long getCollectionEventId()
	{
		return collectionEventId;
	}



	
	/**
	 * @param collectionEventId the collectionEventId to set
	 */
	public void setCollectionEventId(long collectionEventId)
	{
		this.collectionEventId = collectionEventId;
	}



	
	/**
	 * @return the collectionEventSpecimenId
	 */
	public long getCollectionEventSpecimenId()
	{
		return collectionEventSpecimenId;
	}



	
	/**
	 * @param collectionEventSpecimenId the collectionEventSpecimenId to set
	 */
	public void setCollectionEventSpecimenId(long collectionEventSpecimenId)
	{
		this.collectionEventSpecimenId = collectionEventSpecimenId;
	}



	
	/**
	 * @return the collectionEventTimeInHours
	 */
	public String getCollectionEventTimeInHours()
	{
		return collectionEventTimeInHours;
	}



	
	/**
	 * @param collectionEventTimeInHours the collectionEventTimeInHours to set
	 */
	public void setCollectionEventTimeInHours(String collectionEventTimeInHours)
	{
		this.collectionEventTimeInHours = collectionEventTimeInHours;
	}



	
	/**
	 * @return the collectionEventTimeInMinutes
	 */
	public String getCollectionEventTimeInMinutes()
	{
		return collectionEventTimeInMinutes;
	}



	
	/**
	 * @param collectionEventTimeInMinutes the collectionEventTimeInMinutes to set
	 */
	public void setCollectionEventTimeInMinutes(String collectionEventTimeInMinutes)
	{
		this.collectionEventTimeInMinutes = collectionEventTimeInMinutes;
	}



	
	/**
	 * @return the collectionEventUserId
	 */
	public long getCollectionEventUserId()
	{
		return collectionEventUserId;
	}



	
	/**
	 * @param collectionEventUserId the collectionEventUserId to set
	 */
	public void setCollectionEventUserId(long collectionEventUserId)
	{
		this.collectionEventUserId = collectionEventUserId;
	}



	
	/**
	 * @return the receivedEventComments
	 */
	public String getReceivedEventComments()
	{
		return receivedEventComments;
	}



	
	/**
	 * @param receivedEventComments the receivedEventComments to set
	 */
	public void setReceivedEventComments(String receivedEventComments)
	{
		this.receivedEventComments = receivedEventComments;
	}



	
	/**
	 * @return the receivedEventDateOfEvent
	 */
	public String getReceivedEventDateOfEvent()
	{
		return receivedEventDateOfEvent;
	}



	
	/**
	 * @param receivedEventDateOfEvent the receivedEventDateOfEvent to set
	 */
	public void setReceivedEventDateOfEvent(String receivedEventDateOfEvent)
	{
		this.receivedEventDateOfEvent = receivedEventDateOfEvent;
	}



	
	/**
	 * @return the receivedEventId
	 */
	public long getReceivedEventId()
	{
		return receivedEventId;
	}



	
	/**
	 * @param receivedEventId the receivedEventId to set
	 */
	public void setReceivedEventId(long receivedEventId)
	{
		this.receivedEventId = receivedEventId;
	}



	
	/**
	 * @return the receivedEventReceivedQuality
	 */
	public String getReceivedEventReceivedQuality()
	{
		return receivedEventReceivedQuality;
	}



	
	/**
	 * @param receivedEventReceivedQuality the receivedEventReceivedQuality to set
	 */
	public void setReceivedEventReceivedQuality(String receivedEventReceivedQuality)
	{
		this.receivedEventReceivedQuality = receivedEventReceivedQuality;
	}



	
	/**
	 * @return the receivedEventSpecimenId
	 */
	public long getReceivedEventSpecimenId()
	{
		return receivedEventSpecimenId;
	}



	
	/**
	 * @param receivedEventSpecimenId the receivedEventSpecimenId to set
	 */
	public void setReceivedEventSpecimenId(long receivedEventSpecimenId)
	{
		this.receivedEventSpecimenId = receivedEventSpecimenId;
	}



	
	/**
	 * @return the receivedEventTimeInHours
	 */
	public String getReceivedEventTimeInHours()
	{
		return receivedEventTimeInHours;
	}



	
	/**
	 * @param receivedEventTimeInHours the receivedEventTimeInHours to set
	 */
	public void setReceivedEventTimeInHours(String receivedEventTimeInHours)
	{
		this.receivedEventTimeInHours = receivedEventTimeInHours;
	}



	
	/**
	 * @return the receivedEventTimeInMinutes
	 */
	public String getReceivedEventTimeInMinutes()
	{
		return receivedEventTimeInMinutes;
	}



	
	/**
	 * @param receivedEventTimeInMinutes the receivedEventTimeInMinutes to set
	 */
	public void setReceivedEventTimeInMinutes(String receivedEventTimeInMinutes)
	{
		this.receivedEventTimeInMinutes = receivedEventTimeInMinutes;
	}



	
	/**
	 * @return the receivedEventUserId
	 */
	public long getReceivedEventUserId()
	{
		return receivedEventUserId;
	}



	
	/**
	 * @param receivedEventUserId the receivedEventUserId to set
	 */
	public void setReceivedEventUserId(long receivedEventUserId)
	{
		this.receivedEventUserId = receivedEventUserId;
	}



	
	/**
	 * @return the applyEventsToSpecimens
	 */
	public boolean isApplyEventsToSpecimens()
	{
		return applyEventsToSpecimens;
	}



	
	/**
	 * @param applyEventsToSpecimens the applyEventsToSpecimens to set
	 */
	public void setApplyEventsToSpecimens(boolean applyEventsToSpecimens)
	{
		this.applyEventsToSpecimens = applyEventsToSpecimens;
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

	/**
	 * Patch ID: Bug#3184_7
	 */
    private String restrictSCGCheckbox;
	
	/**
	 * This method returns the value of the checkbox
	 * @return the restrictSCGCheckbox
	 */
	public String getRestrictSCGCheckbox() {
		return restrictSCGCheckbox;
	}

	/**
	 * This method sets the value of Checkbox
	 * @param restrictSCGCheckbox the restrictSCGCheckbox to set
	 */
	public void setRestrictSCGCheckbox(String restrictSCGCheckbox) {
		this.restrictSCGCheckbox = restrictSCGCheckbox;
	}


	// Patch ID: 3184_31
	/**
	 * @return the actualNumberOfSpecimen
	 */
	public String getActualNumberOfSpecimen() {
		return actualNumberOfSpecimen;
	}



	/**
	 * @param actualNumberOfSpecimen the actualNumberOfSpecimen to set
	 */
	public void setActualNumberOfSpecimen(String actualNumberOfSpecimen) {
		this.actualNumberOfSpecimen = actualNumberOfSpecimen;
	}
}
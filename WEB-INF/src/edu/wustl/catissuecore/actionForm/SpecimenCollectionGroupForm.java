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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.User;
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
public class SpecimenCollectionGroupForm extends AbstractActionForm implements ConsentTierData
{
 
	private String clinicalDiagnosis = Constants.NOTSPECIFIED;
    
	private String clinicalStatus = Constants.NOTSPECIFIED;
	
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
//Consent Tracking Module Virender Mehta
	/**
	 * Map for Storing responses for Consent Tiers.
	 */
	protected Map consentResponseForScgValues = new HashMap();
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
	protected String witnessName;

	/**
	 * Consent Date, Date on which Consent is Signed
	 */
	protected String consentDate="";
	
	/**
	 * This will be set in case of withdrawl popup
	 */
	protected String withdrawlButtonStatus = Constants.WITHDRAW_RESPONSE_NOACTION;
	/**
	 * This will be set in case if there is any change in response.
	 */
	protected String applyChangesTo= Constants.APPLY_NONE;
	/**
	 * If user changes the response after submiting response then this string will have 
	 * responseKeys for which response is changed .
	 */
	protected String stringOfResponseKeys="";
	
//Consent Tracking Module Virender Mehta 
 		   
	/**
     * unique name for Specimen Collection Group 
     */
    private String name ;
	
	private long participantId;
	
	private String protocolParticipantIdentifier;
	
	//For AddNew functionality
	private long collectionProtocolRegistrationId;
	
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
		siteId = specimenCollectionGroup.getSpecimenCollectionSite().getId().longValue();
		
		/**
	  	 * For Consent tracking setting UI attributes
	  	 */
			User witness= specimenCollectionGroup.getCollectionProtocolRegistration().getConsentWitness();
			if(witness==null||witness.getFirstName()==null)
			{
				this.witnessName="";
			}
			else
			{
				this.witnessName=Utility.toString(witness.getFirstName());
			}
			this.signedConsentUrl=Utility.toString(specimenCollectionGroup.getCollectionProtocolRegistration().getSignedConsentDocumentURL());
			this.consentDate=Utility.parseDateToString(specimenCollectionGroup.getCollectionProtocolRegistration().getConsentSignatureDate(), Constants.DATE_PATTERN_MM_DD_YYYY);
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
	public String getClinicalStatus() 
	{
		return clinicalStatus;
	}

	/**
	 * @param clinicalStatus
	 */
	public void setClinicalStatus(String clinicalStatus) 
	{
		this.clinicalStatus = clinicalStatus;
	}

	/**
	 * @return
	 */
	public long getCollectionProtocolEventId() 
	{
		return collectionProtocolEventId;
	}

	/**
	 * @param collectionProtocolEventId
	 */
	public void setCollectionProtocolEventId(long collectionProtocolEventId) 
	{
		this.collectionProtocolEventId = collectionProtocolEventId;
	}

	/**
	 * @return
	 */
	public long getCollectionProtocolId() 
	{
		return collectionProtocolId;
	}

	/**
	 * @param collectionProtocolId
	 */
	public void setCollectionProtocolId(long collectionProtocolId) 
	{
		this.collectionProtocolId = collectionProtocolId;
	}

	/**
	 * @return
	 */
	public long getParticipantsMedicalIdentifierId() 
	{
		return participantsMedicalIdentifierId;
	}

	/**
	 * @param participantsMedicalIdentifierId
	 */
	public void setParticipantsMedicalIdentifierId(long participantsMedicalIdentifierId) 
	{
		this.participantsMedicalIdentifierId = participantsMedicalIdentifierId;
	}

	/**
	 * @return
	 */
	public String getProtocolParticipantIdentifier() 
	{
		return protocolParticipantIdentifier;
	}

	/**
	 * @param protocolParticipantIdentifier
	 */
	public void setProtocolParticipantIdentifier(String protocolParticipantIdentifier) 
	{
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


//Consent Tracking Module Virender Mehta
	/**
	 * @return consentResponseForScgValues  The comments associated with Response at Specimen Collection Group level
	 */	
	public Map getConsentResponseForScgValues() 
	{
		return consentResponseForScgValues;
	}
	
	/**
	 * @param consentResponseForScgValues  The comments associated with Response at Specimen Collection Group level
	 */	
	public void setConsentResponseForScgValues(Map consentResponseForScgValues)
	{
		this.consentResponseForScgValues = consentResponseForScgValues;
	}
	
	/**
     * @param key Key prepared for saving data.
     * @param value Values correspponding to key
     */
    public void setConsentResponseForScgValue(String key, Object value) 
    {
   	 if (isMutable())
   		consentResponseForScgValues.put(key, value);
    }

    /**
     * @param key Key prepared for saving data.
     * @return consentResponseForScgValues.get(key)
     */
    public Object getConsentResponseForScgValue(String key) 
    {
        return consentResponseForScgValues.get(key);
    }
    
	/**
	 * @return values in map consentResponseForScgValues
	 */
	public Collection getAllConsentResponseForScgValue() 
	{
		return consentResponseForScgValues.values();
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
	 * @return witnessName The name of the witness to the consent Signature(PI or coordinator of the Collection Protocol)
	 */	
	public String getWitnessName()
	{
		return witnessName;
	}
	
	/**
	 * @param witnessName The name of the witness to the consent Signature(PI or coordinator of the Collection Protocol)
	 */	
	public void setWitnessName(String witnessName)
	{
		this.witnessName = witnessName;
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
	 * @return applyChangesTo
	 */
	public String getApplyChangesTo()
	{
		return applyChangesTo;
	}

	/**
	 * @param applyChangesTo 
	 */
	public void setApplyChangesTo(String applyChangesTo)
	{
		this.applyChangesTo = applyChangesTo;
	}
	
	/**
	 * 
	 * @return stringOfResponseKeys
	 */
	public String getStringOfResponseKeys()
	{
		return stringOfResponseKeys;
	}
	
	/**
	 * 
	 * @param stringOfResponseKeys
	 */
	public void setStringOfResponseKeys(String stringOfResponseKeys)
	{
		this.stringOfResponseKeys = stringOfResponseKeys;
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
			strArray = new String[6];
			strArray[0]="consentResponseForScgValues(ConsentBean:"+counter+"_consentTierID)";
			strArray[1]="consentResponseForScgValues(ConsentBean:"+counter+"_statement)";
			strArray[2]="consentResponseForScgValues(ConsentBean:"+counter+"_participantResponse)";
			strArray[3]="consentResponseForScgValues(ConsentBean:"+counter+"_participantResponseID)";
			strArray[4]="consentResponseForScgValues(ConsentBean:"+counter+"_specimenCollectionGroupLevelResponse)";
			strArray[5]="consentResponseForScgValues(ConsentBean:"+counter+"_specimenCollectionGroupLevelResponseID)";
			consentTiersList.add(strArray);
		}
		return consentTiersList;
	}

	/**
	 * This function returns the format of Key
	 * @return consentResponseForScgValues(ConsentBean:`_specimenCollectionGroupLevelResponse) 
	 */
	public String getConsentTierMap()
	{
		return "consentResponseForScgValues(ConsentBean:`_specimenCollectionGroupLevelResponse)";
	}
//Consent Tracking Module Virender Mehta	
}
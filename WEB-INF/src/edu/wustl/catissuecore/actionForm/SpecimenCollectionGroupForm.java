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
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.DefaultValueManager;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * SpecimenCollectionGroupForm Class is used to encapsulate 
 * all the request parameters passed from New SpecimenCollectionGroup webpage.
 * @author ajay_sharma
 */
public class SpecimenCollectionGroupForm extends AbstractActionForm
		implements
			ConsentTierData,
			IPrinterTypeLocation
{

	private static final long serialVersionUID = 1L;

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(SpecimenCollectionGroupForm.class);
	private String clinicalDiagnosis = (String) DefaultValueManager
			.getDefaultValue(Constants.DEFAULT_CLINICAL_DIAGNOSIS);

	private String clinicalStatus = (String) DefaultValueManager
			.getDefaultValue(Constants.DEFAULT_CLINICAL_STATUS);

	/**
	 * Specifies if the barcode is editable or not
	 */
	private String isBarcodeEditable = (String) DefaultValueManager
			.getDefaultValue(Constants.IS_BARCODE_EDITABLE);

	private String surgicalPathologyNumber;

	private long participantsMedicalIdentifierId;

	private String barcode;

	/**
	* Name : Ashish Gupta
	* Reviewer Name : Sachin Lale 
	* Bug ID: 2741
	* Patch ID: 2741_9	 
	* Description: Event Attributes
	*/
	private long collectionEventId; // Mandar : CollectionEvent 10-July-06
	private long collectionEventSpecimenId;
	private long collectionEventUserId;
	private String collectionEventdateOfEvent;
	private String collectionEventTimeInHours;
	private String collectionEventTimeInMinutes;
	private String collectionEventCollectionProcedure;
	private String collectionEventContainer;
	private String collectionEventComments = "";

	private long receivedEventId;
	private long receivedEventSpecimenId;
	private long receivedEventUserId;
	private String receivedEventDateOfEvent;
	private String receivedEventTimeInHours;
	private String receivedEventTimeInMinutes;
	private String receivedEventReceivedQuality;
	private String receivedEventComments = "";

	/**
	 * An id which refers to the site of the container if it is parent container.
	 */
	private long siteId;

	private long collectionProtocolId;

	private long collectionProtocolEventId;

	protected int offset = 0;
	/**
	 * Nmae: Vijay Pande
	 * Reviewer Name: Aarti Sharma
	 * Name of the variable changed from checkedButton to radionButton since this name was conflicting with the same name used on specimen page and creating problem (Wrong value was set) in CP based view
	 * Please check all the references of the variable radioButtonForParticipant
	 */
	/**
	 * Radio button to choose participantName/participantNumber.
	 */
	private int radioButtonForParticipant = 1;
	//Consent Tracking Module Virender Mehta
	/**
	 * Map for Storing responses for Consent Tiers.
	 */
	protected Map consentResponseForScgValues = new HashMap();
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
	protected String witnessName;

	/**
	 * Consent Date, Date on which Consent is Signed
	 */
	protected String consentDate = "";

	/**
	 * This will be set in case of withdrawl popup
	 */
	protected String withdrawlButtonStatus = Constants.WITHDRAW_RESPONSE_NOACTION;
	/**
	 * This will be set in case if there is any change in response.
	 */
	protected String applyChangesTo = Constants.APPLY_NONE;
	/**
	 * If user changes the response after submiting response then this string will have 
	 * responseKeys for which response is changed .
	 */
	protected String stringOfResponseKeys = "";

	//Consent Tracking Module Virender Mehta 

	/**
	 * unique name for Specimen Collection Group 
	 */
	private String name;

	/**For Migration Start**/
	/**
	 * participantName 
	 */
	private String participantName;

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
	 * Name : Falguni Sachde
	 *  
	 * 
	 * 	 
	 * Description: Attribute to set Collection Protocol Short name to associated with this scg
	*/
	private String collectionProtocolName;
	/**
	 * Name : Falguni Sachde
	 *  
	 * 
	 * 	 
	 * Description: Attribute to set Participant Name concatenated with Participant Identifier.
	*/
	private String participantNameWithProtocolId;

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
	private int numberOfSpecimens;

	/**
	 * Name: Chetan Patil
	 * Reviewer: Sachin Lale
	 * Bug ID: Bug#4227
	 * Patch ID: Bug#4227_1
	 * Description: buttonType stores the id of the button only if button for Add Multiple Specimen
	 * is clicked. If the value of this varaiable is null then the validation of number of specimen
	 * against actual number of specimen requirements is skipped. 
	 */
	private String buttonType;

	private String collectionStatus;

	private String printCheckbox;
	private String printerType;

	private String printerLocation;

	private String nextForwardTo;

	/**
	 * @return the buttonType
	 */
	public String getButtonType()
	{
		return this.buttonType;
	}

	/**
	 * @param buttonType the buttonType to set
	 */
	public void setButtonType(String buttonType)
	{
		this.buttonType = buttonType;
	}

	/**
	 * @return the numberOfSpecimens
	 */
	public int getNumberOfSpecimens()
	{
		return this.numberOfSpecimens;
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
		this.reset();
	}

	/**
	 * @return Returns the clinicalDiagnosis.
	 */
	public String getClinicalDiagnosis()
	{
		return this.clinicalDiagnosis;
	}

	/**
	 * @param cinicalDiagnosis The clinicalDiagnosis to set.
	 */
	public void setClinicalDiagnosis(String cinicalDiagnosis)
	{
		this.clinicalDiagnosis = cinicalDiagnosis;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return Returns the surgicalPathologyNumber.
	 */
	public String getSurgicalPathologyNumber()
	{
		return this.surgicalPathologyNumber;
	}

	/**
	 * @param surgicalPathologyNumber The surgicalPathologyNumber to set.
	 */
	public void setSurgicalPathologyNumber(String surgicalPathologyNumber)
	{
		this.surgicalPathologyNumber = surgicalPathologyNumber;
	}

	/**
	 * @return participantId
	 */
	public long getParticipantId()
	{
		return this.participantId;
	}

	/**
	 * @param participantId Setting participant id
	 */
	public void setParticipantId(long participantId)
	{
		this.participantId = participantId;
	}

	/**
	 * For AddNew functionality
	 * @return collectionProtocolRegistrationId
	 */
	public long getCollectionProtocolRegistrationId()
	{
		return this.collectionProtocolRegistrationId;
	}

	/**
	 * @param collectionProtocolRegistrationId Setting Collection Prot reg id
	 */
	public void setCollectionProtocolRegistrationId(long collectionProtocolRegistrationId)
	{
		this.collectionProtocolRegistrationId = collectionProtocolRegistrationId;
	}

	/**
	 * @return Returns the radioButtonForParticipant value.
	 */
	public int getRadioButtonForParticipant()
	{
		return this.radioButtonForParticipant;
	}

	/**
	 * @param radioButton The radioButtonForParticipant to set.
	 */
	public void setRadioButtonForParticipant(int radioButton)
	{
		if (this.isMutable())
		{
			this.radioButtonForParticipant = radioButton;
		}
	}

	/**
	 * Resets the values of all the fields.
	 * Is called by the overridden reset method defined in ActionForm.  
	 * */
	@Override
	protected void reset()
	{
		//		this.clinicalDiagnosis = null;
		//	    
		//		this.clinicalStatus = null;;
		//		
		//		this.surgicalPathologyNumber = null;
		//		
		//		this.protocolParticipantIdentifier =  null;
		//		radioButtonForParticipant = 1;
	}

	/**
	   * This function Copies the data from an storage type object to a StorageTypeForm object.
	   * @param abstractDomain A StorageType object containing the information about storage type of the container.  
	   */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		//		if(operation.equals("add" ) )
		//			setMutable(true );

		final SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) abstractDomain;

		this.setId(specimenCollectionGroup.getId().longValue());
		this.name = specimenCollectionGroup.getName();
		this.barcode = CommonUtilities.toString(specimenCollectionGroup.getBarcode());
		logger.debug("specimenCollectionGroup.getClinicalDiagnosis() "
				+ specimenCollectionGroup.getClinicalDiagnosis());
		this.clinicalDiagnosis = CommonUtilities.toString(specimenCollectionGroup
				.getClinicalDiagnosis());
		this.clinicalStatus = CommonUtilities.toString(specimenCollectionGroup.getClinicalStatus());
		this.setActivityStatus(CommonUtilities
				.toString(specimenCollectionGroup.getActivityStatus()));
		this.collectionStatus = CommonUtilities.toString(specimenCollectionGroup
				.getCollectionStatus());
		this.surgicalPathologyNumber = CommonUtilities.toString(specimenCollectionGroup
				.getSurgicalPathologyNumber());
		/**
		* Name: Shital Lawhale
		* Reviewer Name : Sachin Lale 
		* Bug ID: 3052
		* Patch ID: 3052_1_4
		* See also: 1_1 to 1_5
		* Description : Get comment field from database and set it to form bean.
		*/
		this.comment = CommonUtilities.toString(specimenCollectionGroup.getComment());

		////		ClinicalReport clinicalReport = specimenCollectionGroup.getClinicalReport();
		//		surgicalPathologyNumber = AppUtility.toString(clinicalReport.getSurgicalPathologyNumber());
		//		
		//		if(clinicalReport.getParticipantMedicalIdentifier()!=null)
		//		{
		//			participantsMedicalIdentifierId = clinicalReport.getParticipantMedicalIdentifier().getId().longValue();
		//		}

		this.collectionProtocolId = specimenCollectionGroup.getCollectionProtocolRegistration()
				.getCollectionProtocol().getId().longValue();
		this.collectionProtocolName = specimenCollectionGroup.getCollectionProtocolRegistration()
				.getCollectionProtocol().getShortTitle();
		this.collectionProtocolEventId = specimenCollectionGroup.getCollectionProtocolEvent()
				.getId().longValue();

		final Participant participant = specimenCollectionGroup.getCollectionProtocolRegistration()
				.getParticipant();
		/**For Migration Start**/

		this.participantId = participant.getId();
		/**For Migration End**/
		logger.debug("SCgForm --------- Participant : -- " + participant.toString());
		//if(participant!=null)
		String firstName = null;
		String lastName = null;
		String birthDate = null;
		String ssn = null;

		if (participant.getFirstName() == null)
		{
			firstName = "";
		}
		else
		{
			firstName = participant.getFirstName();
		}

		if (participant.getLastName() == null)
		{
			lastName = "";
		}
		else
		{
			lastName = participant.getLastName();
		}

		this.participantName = lastName + ", " + firstName;

		if (participant.getBirthDate() == null)
		{
			birthDate = "";
		}
		else
		{
			birthDate = participant.getBirthDate().toString();
		}

		if (participant.getSocialSecurityNumber() == null)
		{
			ssn = "";
		}
		else
		{
			ssn = participant.getSocialSecurityNumber();
		}

		//kalpana :bug #5761
		if (CommonUtilities.toString(specimenCollectionGroup.getCollectionProtocolRegistration()
				.getProtocolParticipantIdentifier()) != null)
		{
			this.protocolParticipantIdentifier = CommonUtilities.toString(specimenCollectionGroup
					.getCollectionProtocolRegistration().getProtocolParticipantIdentifier());

		}

		if (firstName.length() > 0 || lastName.length() > 0 || birthDate.length() > 0
				|| ssn.length() > 0)
		{
			this.participantId = participant.getId().longValue();
			this.radioButtonForParticipant = 1;
		}
		else
		{
			this.radioButtonForParticipant = 2;
		}

		logger.debug("participantId.................................." + this.participantId);
		logger.debug("protocolParticipantIdentifier........................."
				+ this.protocolParticipantIdentifier);
		logger.debug("SCgForm --------- checkButton : -- " + this.radioButtonForParticipant);

		//Abhishek Mehta If site is null
		final Site site = specimenCollectionGroup.getSpecimenCollectionSite();
		if (null != site)
		{
			this.siteId = site.getId().longValue();
		}

		/**
		 * For Consent tracking setting UI attributes
		 */
		final User witness = specimenCollectionGroup.getCollectionProtocolRegistration()
				.getConsentWitness();
		if (witness == null || witness.getFirstName() == null)
		{
			this.witnessName = "";
		}
		else
		{
			this.witnessName = CommonUtilities.toString(witness.getFirstName());
		}
		this.signedConsentUrl = CommonUtilities.toString(specimenCollectionGroup
				.getCollectionProtocolRegistration().getSignedConsentDocumentURL());
		this.consentDate = CommonUtilities.parseDateToString(specimenCollectionGroup
				.getCollectionProtocolRegistration().getConsentSignatureDate(),
				CommonServiceLocator.getInstance().getDatePattern());
		/**
		* Name : Ashish Gupta
		* Reviewer Name : Sachin Lale 
		* Bug ID: 2741
		* Patch ID: 2741_10	 
		* Description: Method to populate Events in SCG form
		*/
		//Populating the events
		this.setSCGEvents(specimenCollectionGroup);

		//For offset
		if (specimenCollectionGroup.getOffset() != null)
		{
			this.offset = specimenCollectionGroup.getOffset().intValue();
		}
	}

	/**
	 * @param specimenCollectionGroup Settign Sp Coll Group 
	 */
	private void setSCGEvents(SpecimenCollectionGroup specimenCollectionGroup)
	{
		final Collection eventsParametersColl = specimenCollectionGroup
				.getSpecimenEventParametersCollection();
		if (eventsParametersColl != null && !eventsParametersColl.isEmpty())
		{
			final Iterator iter = eventsParametersColl.iterator();
			while (iter.hasNext())
			{
				final Object tempObj = iter.next();
				final Calendar calender = Calendar.getInstance();
				if (tempObj instanceof CollectionEventParameters)
				{
					final CollectionEventParameters collectionEventParameters = (CollectionEventParameters) tempObj;
					this.collectionEventId = collectionEventParameters.getId().longValue(); // Mandar : CollectionEvent 10-July-06
					//this.collectionEventSpecimenId = collectionEventParameters.getSpecimen().getId().longValue();
					this.collectionEventUserId = collectionEventParameters.getUser().getId()
							.longValue();

					calender.setTime(collectionEventParameters.getTimestamp());
					this.collectionEventdateOfEvent = CommonUtilities.parseDateToString(
							collectionEventParameters.getTimestamp(), CommonServiceLocator
									.getInstance().getDatePattern());
					this.collectionEventTimeInHours = CommonUtilities.toString(Integer
							.toString(calender.get(Calendar.HOUR_OF_DAY)));
					this.collectionEventTimeInMinutes = CommonUtilities.toString(Integer
							.toString(calender.get(Calendar.MINUTE)));
					this.collectionEventCollectionProcedure = collectionEventParameters
							.getCollectionProcedure();
					this.collectionEventContainer = collectionEventParameters.getContainer();
					this.collectionEventComments = CommonUtilities
							.toString(collectionEventParameters.getComment());
				}
				else if (tempObj instanceof ReceivedEventParameters)
				{
					final ReceivedEventParameters receivedEventParameters = (ReceivedEventParameters) tempObj;

					calender.setTime(receivedEventParameters.getTimestamp());
					this.receivedEventId = receivedEventParameters.getId().longValue();
					//	this.receivedEventSpecimenId = receivedEventParameters.getSpecimen().getId().longValue();
					this.receivedEventUserId = receivedEventParameters.getUser().getId()
							.longValue();
					this.receivedEventDateOfEvent = CommonUtilities.parseDateToString(
							receivedEventParameters.getTimestamp(), CommonServiceLocator
									.getInstance().getDatePattern());
					this.receivedEventTimeInHours = CommonUtilities.toString(Integer
							.toString(calender.get(Calendar.HOUR_OF_DAY)));
					this.receivedEventTimeInMinutes = CommonUtilities.toString(Integer
							.toString(calender.get(Calendar.MINUTE)));
					this.receivedEventReceivedQuality = receivedEventParameters
							.getReceivedQuality();
					this.receivedEventComments = CommonUtilities.toString(receivedEventParameters
							.getComment());
				}
			}
		}
	}

	/**
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getFormId()
	 * @return SPECIMEN_COLLECTION_GROUP_FORM_ID
	 */
	@Override
	public int getFormId()
	{
		return Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID;
	}

	/**
	 * @return siteId
	 */
	public long getSiteId()
	{
		return this.siteId;
	}

	/**
	 * @param siteId Setting Site id
	 */
	public void setSiteId(long siteId)
	{
		this.siteId = siteId;
	}

	/**
	 * @return clinicalStatus
	 */
	public String getClinicalStatus()
	{
		return this.clinicalStatus;
	}

	/**
	 * @param clinicalStatus Settign clinicalStatus
	 */
	public void setClinicalStatus(String clinicalStatus)
	{
		this.clinicalStatus = clinicalStatus;
	}

	/**
	 * @return collectionProtocolEventId
	 */
	public long getCollectionProtocolEventId()
	{
		return this.collectionProtocolEventId;
	}

	/**
	 * @param collectionProtocolEventId Setting collectionProtocolEventId
	 */
	public void setCollectionProtocolEventId(long collectionProtocolEventId)
	{
		this.collectionProtocolEventId = collectionProtocolEventId;
	}

	/**
	 * @return collectionProtocolId
	 */
	public long getCollectionProtocolId()
	{
		return this.collectionProtocolId;
	}

	/**
	 * @param collectionProtocolId Setting collectionProtocolId
	 */
	public void setCollectionProtocolId(long collectionProtocolId)
	{
		this.collectionProtocolId = collectionProtocolId;
	}

	/**
	 * @return participantsMedicalIdentifierId
	 */
	public long getParticipantsMedicalIdentifierId()
	{
		return this.participantsMedicalIdentifierId;
	}

	/**
	 * @param participantsMedicalIdentifierId Setting  participantsMedicalIdentifierId
	 */
	public void setParticipantsMedicalIdentifierId(long participantsMedicalIdentifierId)
	{
		this.participantsMedicalIdentifierId = participantsMedicalIdentifierId;
	}

	/**
	 * @return protocolParticipantIdentifier
	 */
	public String getProtocolParticipantIdentifier()
	{
		return this.protocolParticipantIdentifier;
	}

	/**
	 * @param protocolParticipantIdentifier Setting protocolParticipantIdentifier
	 */
	public void setProtocolParticipantIdentifier(String protocolParticipantIdentifier)
	{
		this.protocolParticipantIdentifier = protocolParticipantIdentifier;
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
		ActionErrors errors = new ActionErrors();
		final Validator validator = new Validator();
		try
		{
			this.setRedirectValue(validator);

			if (this.getOperation().equals(Constants.EDIT))
			{
				if (this.collectionStatus.trim().length() <= 0)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
							ApplicationProperties.getValue("specimen.collectionStatus")));
				}
			}

			if (this.collectionProtocolId == -1)
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",
						ApplicationProperties.getValue("specimenCollectionGroup.protocolTitle")));
			}

			if (this.siteId == -1)
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",
						ApplicationProperties.getValue("specimenCollectionGroup.site")));
			}

			/**
			 * Name: Vijay Pande
			 * Reviewer Name: Aarti Sharma
			 * Validation for participant name and participantProtocolIdentifier added
			 */
			// Check what user has selected Participant Name / Participant Number
			if (this.radioButtonForParticipant == 1)
			{
				//if participant name field is checked.
				/**For Migration Start**/
				if (this.participantName == null || Validator.isEmpty(this.participantName)) // || AppUtility.this.participantName.trim().equals(""))
				{
					if (this.participantNameWithProtocolId == null
							|| Validator.isEmpty(this.participantNameWithProtocolId))
					{
						errors
								.add(
										ActionErrors.GLOBAL_ERROR,
										new ActionError(
												"errors.item.required",
												ApplicationProperties
														.getValue("specimenCollectionGroup.collectedByParticipant")));
					}
				}
				/**For Migration End**/
			}
			else
			{
				if (Validator.isEmpty(this.protocolParticipantIdentifier))
				{
					errors
							.add(
									ActionErrors.GLOBAL_ERROR,
									new ActionError(
											"errors.item.required",
											ApplicationProperties
													.getValue("specimenCollectionGroup.collectedByProtocolParticipantNumber")));
				}
			}
			if (!edu.wustl.catissuecore.util.global.Variables.isSpecimenCollGroupLabelGeneratorAvl
					&& this.name.equals(""))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("specimenCollectionGroup.groupName")));
			}

			// Mandatory Field : Study Calendar event point
			if (this.collectionProtocolEventId == -1)
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",
						ApplicationProperties
								.getValue("specimenCollectionGroup.studyCalendarEventPoint")));
			}

			// Mandatory Field : clinical Diagnosis
			if (!validator.isValidOption(this.clinicalDiagnosis))
			{
				errors.add(ActionErrors.GLOBAL_ERROR,
						new ActionError("errors.item.selected", ApplicationProperties
								.getValue("specimenCollectionGroup.clinicalDiagnosis")));
			}

			// Mandatory Field : clinical Status
			if (!validator.isValidOption(this.clinicalStatus))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",
						ApplicationProperties.getValue("specimenCollectionGroup.clinicalStatus")));
			}

			//Condition for medical Record Number.
			if (this.radioButtonForParticipant == 1)
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
			//Time validation
			final String collectionTime = this.collectionEventTimeInHours + ":"
					+ this.collectionEventTimeInMinutes + ":00";
			final String receivedTime = this.receivedEventTimeInHours + ":"
					+ this.receivedEventTimeInMinutes + ":00";
			//			CollectionEvent validation.
			EventsUtil.validateCollectionEvent(errors, validator, this.collectionEventUserId,
					this.collectionEventdateOfEvent, this.collectionEventCollectionProcedure,
					collectionTime);
			//ReceivedEvent validation
			EventsUtil.validateReceivedEvent(errors, validator, this.receivedEventUserId,
					this.receivedEventDateOfEvent, this.receivedEventReceivedQuality, receivedTime);

			//Added by Ashish for Multiple Specimens
			/**
			 * Name : Ashish Gupta
			 * Reviewer Name : Sachin Lale 
			 * Bug ID: Multiple Specimen Bug
			 * Patch ID: Multiple Specimen Bug_4 
			 * See also: 1-8
			 * Description: Remove the page on which number of multiple specimens are entered while going to multiple specimen page.
			*/
			final String buttonName = request.getParameter("button");

			if (buttonName != null && !buttonName.equals(""))
			{
				if (this.numberOfSpecimens < 1)
				{
					this.setNumberOfSpecimens(1);
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"errors.multiplespecimen.minimumspecimen"));
				}
			}
			request.getSession().setAttribute("scgForm", this);
			//For setting whether to set specimen 
			final String applyToString = request.getParameter("applyToSpecimenValue");
			if (applyToString != null && applyToString.equals("true"))
			{
				this.applyEventsToSpecimens = true;
			}

		}
		catch (final Exception excp)
		{
			// use of logger as per bug 79
			SpecimenCollectionGroupForm.logger.error(excp.getMessage(), excp);
			excp.printStackTrace(); 
			errors = new ActionErrors();
		}
		return errors;
	}

	/**
	 * This method sets Identifier of Objects inserted by AddNew activity in Form-Bean which initialized AddNew action
	 * @param addNewFor - FormBean ID of the object inserted
	 *  @param addObjectIdentifier - Identifier of the Object inserted 
	 */
	@Override
	public void setAddNewObjectIdentifier(String addNewFor, Long addObjectIdentifier)
	{
		if (addNewFor.equals("collectionProtocol"))
		{
			this.setCollectionProtocolId(addObjectIdentifier.longValue());
		}
		else if (addNewFor.equals("site"))
		{
			this.setSiteId(addObjectIdentifier.longValue());
		}
		else if (addNewFor.equals("participant"))
		{
			//            setParticipantId(addObjectIdentifier.longValue());
			this.setCollectionProtocolRegistrationId(addObjectIdentifier.longValue());
			this.setRadioButtonForParticipant(1);
		}
		else if (addNewFor.equals("protocolParticipantIdentifier"))
		{
			this.setCollectionProtocolRegistrationId(addObjectIdentifier.longValue());
			//            setProtocolParticipantIdentifier(addObjectIdentifier.toString());
			this.setRadioButtonForParticipant(2);
		}
	}

	//Consent Tracking Module Virender Mehta
	/**
	 * @return consentResponseForScgValues  The comments associated with Response at Specimen Collection Group level
	 */
	public Map getConsentResponseForScgValues()
	{
		return this.consentResponseForScgValues;
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
		if (this.isMutable())
		{
			this.consentResponseForScgValues.put(key, value);
		}
	}

	/**
	 * @param key Key prepared for saving data.
	 * @return consentResponseForScgValues.get(key)
	 */
	public Object getConsentResponseForScgValue(String key)
	{
		return this.consentResponseForScgValues.get(key);
	}

	/**
	 * @return values in map consentResponseForScgValues
	 */
	public Collection getAllConsentResponseForScgValue()
	{
		return this.consentResponseForScgValues.values();
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
	 * @return consentDate The Date on Which Consent is Signed
	 */
	public String getConsentDate()
	{
		return this.consentDate;
	}

	/**
	 * @param consentDate The Date on Which Consent is Signed
	 */
	public void setConsentDate(String consentDate)
	{
		this.consentDate = consentDate;
	}

	/**
	 * @return applyChangesTo
	 */
	public String getApplyChangesTo()
	{
		return this.applyChangesTo;
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
		return this.stringOfResponseKeys;
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
		final Collection consentTiersList = new ArrayList();
		String[] strArray = null;
		final int noOfConsents = this.getConsentTierCounter();
		for (int counter = 0; counter < noOfConsents; counter++)
		{
			strArray = new String[6];
			strArray[0] = "consentResponseForScgValues(ConsentBean:" + counter + "_consentTierID)";
			strArray[1] = "consentResponseForScgValues(ConsentBean:" + counter + "_statement)";
			strArray[2] = "consentResponseForScgValues(ConsentBean:" + counter
					+ "_participantResponse)";
			strArray[3] = "consentResponseForScgValues(ConsentBean:" + counter
					+ "_participantResponseID)";
			strArray[4] = "consentResponseForScgValues(ConsentBean:" + counter
					+ "_specimenCollectionGroupLevelResponse)";
			strArray[5] = "consentResponseForScgValues(ConsentBean:" + counter
					+ "_specimenCollectionGroupLevelResponseID)";
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
	/**
	 * @return the collectionEventCollectionProcedure
	 */
	public String getCollectionEventCollectionProcedure()
	{
		return this.collectionEventCollectionProcedure;
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
		return this.collectionEventComments;
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
		return this.collectionEventContainer;
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
		return this.collectionEventdateOfEvent;
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
		return this.collectionEventId;
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
		return this.collectionEventSpecimenId;
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
		return this.collectionEventTimeInHours;
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
		return this.collectionEventTimeInMinutes;
	}

	/**
	 * @param collectionEventTimeInMinutes the collectionEventTimeInMinutes to set
	 */
	public void setCollectionEventTimeInMinutes(String collectionEventTimeInMinutes)
	{
		this.collectionEventTimeInMinutes = collectionEventTimeInMinutes;
	}

	/**
	 * @return the receivedEventDateOfEvent
	 */
	public String getReceivedEventDateOfEvent()
	{
		return this.receivedEventDateOfEvent;
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
		return this.receivedEventId;
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
		return this.receivedEventReceivedQuality;
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
		return this.receivedEventSpecimenId;
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
		return this.receivedEventTimeInHours;
	}

	/**
	 * @param receivedEventTimeInHours the receivedEventTimeInHours to set
	 */
	public void setReceivedEventTimeInHours(String receivedEventTimeInHours)
	{
		this.receivedEventTimeInHours = receivedEventTimeInHours;
	}

	/**
	 * @return the collectionEventUserId
	 */
	public long getCollectionEventUserId()
	{
		return this.collectionEventUserId;
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
		return this.receivedEventComments;
	}

	/**
	 * @param receivedEventComments the receivedEventComments to set
	 */
	public void setReceivedEventComments(String receivedEventComments)
	{
		this.receivedEventComments = receivedEventComments;
	}

	/**
	 * @return the receivedEventTimeInMinutes
	 */
	public String getReceivedEventTimeInMinutes()
	{
		return this.receivedEventTimeInMinutes;
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
		return this.receivedEventUserId;
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
		return this.applyEventsToSpecimens;
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
	 * @returns comment
	 */
	public String getComment()
	{
		return this.comment;
	}

	/**
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
	public String getRestrictSCGCheckbox()
	{
		return this.restrictSCGCheckbox;
	}

	/**
	 * This method sets the value of Checkbox
	 * @param restrictSCGCheckbox the restrictSCGCheckbox to set
	 */
	public void setRestrictSCGCheckbox(String restrictSCGCheckbox)
	{
		this.restrictSCGCheckbox = restrictSCGCheckbox;
	}

	/**For Migration Start**/

	/**
	 * This method returns the value of the participantName
	 * @return the participantName
	 */
	public String getParticipantName()
	{
		return this.participantName;
	}

	/**
	 * This method sets the participantName
	 * @param participantName the participantName to set
	 */
	public void setParticipantName(String participantName)
	{
		this.participantName = participantName;
	}

	public String getCollectionProtocolName()
	{
		return this.collectionProtocolName;
	}

	public void setCollectionProtocolName(String collectionProtocolName)
	{
		this.collectionProtocolName = collectionProtocolName;
	}

	public String getParticipantNameWithProtocolId()
	{
		this.participantNameWithProtocolId = "";
		if (this.participantName != null)
		{
			this.participantNameWithProtocolId = this.participantName;
		}
		else
		{
			this.participantNameWithProtocolId = "N/A";
		}

		if (this.protocolParticipantIdentifier != null
				&& this.protocolParticipantIdentifier.length() > 0)
		{
			this.participantNameWithProtocolId = this.participantNameWithProtocolId + '('
					+ this.protocolParticipantIdentifier + ')';
		}
		else
		{
			this.participantNameWithProtocolId = this.participantNameWithProtocolId + '(' + "N/A"
					+ ')';
		}

		return this.participantNameWithProtocolId;
	}

	public void setParticipantNameWithProtocolId(String participantNameProtocolId)
	{
		this.participantNameWithProtocolId = participantNameProtocolId;
	}

	public String getCollectionStatus()
	{
		return this.collectionStatus;
	}

	public void setCollectionStatus(String collectionStatus)
	{
		this.collectionStatus = collectionStatus;
	}

	public String getNextForwardTo()
	{
		return this.nextForwardTo;
	}

	public void setNextForwardTo(String nextForwardTo)
	{
		this.nextForwardTo = nextForwardTo;
	}

	public String getPrintCheckbox()
	{
		return this.printCheckbox;
	}

	public void setPrintCheckbox(String printCheckbox)
	{
		this.printCheckbox = printCheckbox;
	}

	public int getOffset()
	{
		return this.offset;
	}

	public void setOffset(int offset)
	{
		this.offset = offset;
	}

	public String getBarcode()
	{
		return this.barcode;
	}

	public void setBarcode(String barcode)
	{
		this.barcode = barcode;
	}

	public String getPrinterLocation()
	{
		return this.printerLocation;
	}

	public void setPrinterLocation(String printerLocation)
	{
		this.printerLocation = printerLocation;
	}

	public String getPrinterType()
	{
		return this.printerType;
	}

	public void setPrinterType(String printerType)
	{
		this.printerType = printerType;
	}

	/**
	 * @return isBarcodeEditable
	 */
	public String getIsBarcodeEditable()
	{
		return this.isBarcodeEditable;
	}

	/** 
	 * @param isBarcodeEditable Setter method for isBarcodeEditable
	 */
	public void setIsBarcodeEditable(String isBarcodeEditable)
	{
		this.isBarcodeEditable = isBarcodeEditable;
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
	public void setSignedConsentUrl(String signedConsentUrl)
	{
		this.signedConsentUrl = signedConsentUrl;
	}

	/**
	 * @return witnessName The name of the witness to the consent Signature(PI or coordinator of the Collection Protocol)
	 */
	public String getWitnessName()
	{
		return this.witnessName;
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

}
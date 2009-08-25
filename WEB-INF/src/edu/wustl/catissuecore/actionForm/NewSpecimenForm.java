/**
 * <p>Title: NewSpecimenForm Class>
 * <p>Description:  NewSpecimenForm Class is used to encapsulate all the request parameters passed 
 * from New Specimen webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
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

import edu.wustl.catissuecore.domain.AbstractSpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.util.HibernateMetaData;

/**
 * NewSpecimenForm Class is used to encapsulate all the request parameters passed 
 * from New Specimen webpage.
 * @author aniruddha_phadnis
 */
public class NewSpecimenForm extends SpecimenForm implements ConsentTierData, IPrinterTypeLocation
{

	private static final long serialVersionUID = 1L;

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(NewSpecimenForm.class);
	//Consent Tracking Module (Virender Mehta)
	/**
	 * Map for Storing responses for Consent Tiers.
	 */
	protected Map consentResponseForSpecimenValues = new HashMap();
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
	//Consent Tracking Module (Virender Mehta)

	/**
	* Specimen Collection Group ID
	*/
	private String specimenCollectionGroupId = "0";

	/**
	 * Identifier of the Parent Speciemen if present.
	 */
	private String parentSpecimenId;

	/**
	 * If "True" then Parent is present else Parent is absent.
	 */
	private boolean parentPresent;

	/**
	 * Anatomic site from which the specimen was derived.
	 */
	private String tissueSite;

	/**
	 * For bilateral sites, left or right.
	 */
	private String tissueSide;

	/**
	 * Histopathological character of the specimen 
	 * e.g. Non-Malignant, Malignant, Non-Malignant Diseased, Pre-Malignant.
	 */
	private String pathologicalStatus;

	/**
	 * Type of the biohazard.
	 */
	private String biohazardType;

	/**
	 * Name of the biohazard.
	 */
	private String biohazardName;

	/**
	 * Number of biohazard rows.
	 */
	private int bhCounter = 1;

	private Map biohazard = new HashMap();

	private String specimenEventParameter;

	/**
	 * A number that tells how many aliquots to be created.
	 */
	private String noOfAliquots = null;

	/**
	 * Initial quantity per aliquot.
	 */
	private String quantityPerAliquot;

	/**
	 * Represents the weather participant Name is selected or not.
	 *
	 */
	private boolean checkedButton;
	/**
	 * When derived radio button is clicked
	 *
	 */
	private boolean derivedClicked;

	/**
	 * If true then this specimen is an aliquot else false.
	 */
	//private boolean aliquot;
	/**
	 * A historical information about the specimen i.e. whether the specimen is a new specimen
	 * or a derived specimen or an aliquot.
	 */
	private String lineage;

	//	-------------Mandar AutoEvents CollectionEvent parameters start
	private long collectionEventId; // Mandar : CollectionEvent 10-July-06
	private long collectionEventSpecimenId;
	private long collectionEventUserId;
	private String collectionEventdateOfEvent;
	private String collectionEventTimeInHours;
	private String collectionEventTimeInMinutes;
	private String collectionEventCollectionProcedure;
	private String collectionEventContainer;
	private String collectionEventComments;

	//-------------Mandar AutoEvents CollectionEvent parameters end

	//	-------------Mandar AutoEvents ReceivedEvent parameters start
	private long receivedEventId;
	private long receivedEventSpecimenId;
	private long receivedEventUserId;
	private String receivedEventDateOfEvent;
	private String receivedEventTimeInHours;
	private String receivedEventTimeInMinutes;
	private String receivedEventReceivedQuality;
	private String receivedEventComments;

	//	-------------Mandar AutoEvents ReceivedEvent parameters end

	// Patch ID: Bug#3184_5
	// Also See: Bug#3184_6
	private String specimenCollectionGroupName;

	private String parentSpecimenName;

	private String restrictSCGCheckbox;

	private int numberOfSpecimen;

	private String collectionStatus;

	private String printCheckbox;

	//added by printer properties
	private String printerType;

	private String printerLocation;

	private String createCpChildCheckBox;

	private String nextForwardTo;

	private String numberOfSpecimens = null;

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

	public String getCreateCpChildCheckBox()
	{
		return this.createCpChildCheckBox;
	}

	public void setCreateCpChildCheckBox(String createCpChildCheckBox)
	{
		this.createCpChildCheckBox = createCpChildCheckBox;
	}

	/**
	 * @return the restrictSCGCheckbox
	 */
	public String getRestrictSCGCheckbox()
	{
		return this.restrictSCGCheckbox;
	}

	/**
	 * @param restrictSCGCheckbox the restrictSCGCheckbox to set
	 */
	public void setRestrictSCGCheckbox(String restrictSCGCheckbox)
	{
		this.restrictSCGCheckbox = restrictSCGCheckbox;
	}

	/**
	 * Returns an identifier of the Parent Speciemen.
	 * @return String an identifier of the Parent Speciemen.
	 * @see #setParentSpecimenId(String)
	 * */
	public String getParentSpecimenId()
	{
		return this.parentSpecimenId;
	}

	/**
	 * Sets an identifier of the Parent Speciemen.
	 * @param parentSpecimenId an identifier of the Parent Speciemen.
	 * @see #getParentSpecimenId()
	 * */
	public void setParentSpecimenId(String parentSpecimenId)
	{
		this.parentSpecimenId = parentSpecimenId;
	}

	/**
	 * Associates the specified object with the specified key in the map.
	 * @param key the key to which the object is mapped.
	 * @param value the object which is mapped.
	 */
	public void setBiohazardValue(String key, Object value)
	{
		if (this.isMutable())
		{
			this.biohazard.put(key, value);
		}
	}

	/**
	 * Returns the object to which this map maps the specified key.
	 * @param key the required key.
	 * @return the object to which this map maps the specified key.
	 */
	public Object getBiohazardValue(String key)
	{
		return this.biohazard.get(key);
	}

	/**
	 * @return biohazard Returns the values.
	 */
	public Collection getAllBiohazards()
	{
		return this.biohazard.values();
	}

	/**
	 * @param biohazard Setting Biohazard
	 */
	public void setBiohazard(Map biohazard)
	{
		this.biohazard = biohazard;
	}

	/**
	 * @return biohazard 
	 */
	public Map getBiohazard()
	{
		return this.biohazard;
	}

	/**
	 * @return pathologicalStatus Returns the pathologicalStatus.
	 */
	public String getPathologicalStatus()
	{
		return this.pathologicalStatus;
	}

	/**
	 * @param pathologicalStatus The pathologicalStatus to set.
	 */
	public void setPathologicalStatus(String pathologicalStatus)
	{
		this.pathologicalStatus = pathologicalStatus;
	}

	/**
	 * @return Returns the specimenCollectionGroupId.
	 */
	public String getSpecimenCollectionGroupId()
	{
		return this.specimenCollectionGroupId;
	}

	/**
	 * @param specimenCollectionGroupId The specimenCollectionGroupId to set.
	 */
	public void setSpecimenCollectionGroupId(String specimenCollectionGroupId)
	{
		this.specimenCollectionGroupId = specimenCollectionGroupId;
	}

	/**
	 * @return tissueSide Returns the tissueSide.
	 */
	public String getTissueSide()
	{
		return this.tissueSide;
	}

	/**
	 * @param tissueSide The tissueSide to set.
	 */
	public void setTissueSide(String tissueSide)
	{
		this.tissueSide = tissueSide;
	}

	/**
	 * @return tissueSite Returns the tissueSite.
	 */
	public String getTissueSite()
	{
		return this.tissueSite;
	}

	/**
	 * @param tissueSite The tissueSite to set.
	 */
	public void setTissueSite(String tissueSite)
	{
		this.tissueSite = tissueSite;
	}

	/**
	 * Resets the values of all the fields.
	 */
	@Override
	protected void reset()
	{
		//        super.reset();
		//    	this.tissueSite = null;
		//        this.tissueSide = null;
		//        this.pathologicalStatus = null;
		//        this.biohazard = new HashMap();
		//   	this.parentPresent = false;
	}

	public String getNumberOfSpecimens()
	{
		return this.numberOfSpecimens;
	}

	public void setNumberOfSpecimens(String numberOfSpecimens)
	{
		this.numberOfSpecimens = numberOfSpecimens;
	}

	/**
	 * @return NEW_SPECIMEN_FORM_ID Returns the id assigned to form bean.
	 */
	@Override
	public int getFormId()
	{
		return Constants.NEW_SPECIMEN_FORM_ID;
	}

	/**
	 * This function Copies the data from an site object to a SiteForm object.
	 * @param abstractDomain An object containing the information about site.  
	 */
	@Override
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		super.setAllValues(abstractDomain);

		final Specimen specimen = (Specimen) abstractDomain;

		this.parentPresent = false;
		final AbstractSpecimenCollectionGroup absspecimenCollectionGroup = specimen
				.getSpecimenCollectionGroup();
		final SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) HibernateMetaData
				.getProxyObjectImpl(absspecimenCollectionGroup);

		if (specimenCollectionGroup != null)
		{
			this.specimenCollectionGroupId = CommonUtilities.toString(specimenCollectionGroup
					.getId());
			/**For Migration Start**/
			this.specimenCollectionGroupName = CommonUtilities.toString(specimenCollectionGroup
					.getName());
			/**For Migration End**/
		}
		if (specimen.getParentSpecimen() != null)
		{
			logger.debug("ParentSpecimen : -- " + specimen.getParentSpecimen());
			this.parentSpecimenId = String.valueOf(specimen.getParentSpecimen().getId());
			this.parentSpecimenName = CommonUtilities.toString(((Specimen) specimen
					.getParentSpecimen()).getLabel());
			this.parentPresent = true;
		}

		//    	this.aliquot = specimen.getIsAliquot().booleanValue();

		final SpecimenCharacteristics characteristic = specimen.getSpecimenCharacteristics();
		this.pathologicalStatus = specimen.getPathologicalStatus();
		this.tissueSide = characteristic.getTissueSide();
		this.tissueSite = characteristic.getTissueSite();
		this.lineage = specimen.getLineage();

		final Collection biohazardCollection = specimen.getBiohazardCollection();
		this.bhCounter = 1;

		if (biohazardCollection != null && biohazardCollection.size() != 0)
		{
			this.biohazard = new HashMap();

			int i = 1;

			final Iterator it = biohazardCollection.iterator();
			while (it.hasNext())
			{
				final String key1 = "Biohazard:" + i + "_type";
				final String key2 = "Biohazard:" + i + "_id";
				final String key3 = "Biohazard:" + i + "_persisted";

				final Biohazard hazard = (Biohazard) it.next();

				this.biohazard.put(key1, hazard.getType());
				this.biohazard.put(key2, hazard.getId());

				//boolean for showing persisted value
				this.biohazard.put(key3, "true");

				i++;
			}

			this.bhCounter = biohazardCollection.size();
		}
		//        
		//        if(abstractDomain instanceof AliquotSpecimen)
		//        {
		//        	AliquotSpecimen aliquotSpecimen = (AliquotSpecimen)abstractDomain;
		//        	
		//        }

		/**
		 * For Consent Tracking setting UI attributes (Virender Mehta)
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
		this.collectionStatus = CommonUtilities.toString(specimen.getCollectionStatus());
	}

	/**
	* @return biohazard Type Returns the biohazardType.
	*/
	/**
	public String getBiohazardType()
	{
		return biohazardType;
	}
	/**
	 * @param biohazardType The biohazardType to set.
	 */
	public void setBiohazardType(String biohazardType)
	{
		this.biohazardType = biohazardType;
	}

	/**
	 * @return biohazardName Returns the biohazardName.
	 */
	public String getBiohazardName()
	{
		return this.biohazardName;
	}

	/**
	 * @param biohazardName The biohazardName to set.
	 */
	public void setBiohazardName(String biohazardName)
	{
		this.biohazardName = biohazardName;
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
		final ActionErrors errors = super.validate(mapping, request);
		final Validator validator = new Validator();

		try
		{
			if (this.getOperation().equals(Constants.ADD)
					|| this.getOperation().equals(Constants.EDIT))
			{
				if (this.getOperation().equals(Constants.EDIT))
				{
					if (this.collectionStatus.trim().length() <= 0)
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"errors.item.required", ApplicationProperties
										.getValue("specimen.collectionStatus")));
					}
				}
				/**For Migration Start**/
				if (this.specimenCollectionGroupName != null
						&& this.specimenCollectionGroupName.trim().equals(""))
				{
					errors
							.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
									ApplicationProperties
											.getValue("specimen.specimenCollectionGroupName")));
				}
				//             	else if(specimenCollectionGroupName!=null && AppUtility.getSCGId(specimenCollectionGroupName.trim())==AppUtility.toString(null))
				//             	{
				//             		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.unknown", ApplicationProperties.getValue("specimen.specimenCollectionGroup")));
				//             	}
				/**For Migration End**/
				if (this.parentPresent && !validator.isValidOption(this.parentSpecimenName))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
							ApplicationProperties.getValue("createSpecimen.parent")));
				}

				if (this.tissueSite.equals("-1"))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
							ApplicationProperties.getValue("specimen.tissueSite")));
				}

				if (this.tissueSide.equals("-1"))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
							ApplicationProperties.getValue("specimen.tissueSide")));
				}

				if (this.pathologicalStatus.equals("-1"))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
							ApplicationProperties.getValue("specimen.pathologicalStatus")));
				}

				//Mandar 18-July-06: AutoEvents: 
				if (this.getOperation().equalsIgnoreCase(Constants.ADD))
				{
					//             		Time validation
					final String collectionTime = this.collectionEventTimeInHours + ":"
							+ this.collectionEventTimeInMinutes + ":00";
					final String receivedTime = this.receivedEventTimeInHours + ":"
							+ this.receivedEventTimeInMinutes + ":00";
					//CollectionEvent validation.
					EventsUtil.validateCollectionEvent(errors, validator,
							this.collectionEventUserId, this.collectionEventdateOfEvent,
							this.collectionEventCollectionProcedure, collectionTime);
					//ReceivedEvent validation
					EventsUtil.validateReceivedEvent(errors, validator, this.receivedEventUserId,
							this.receivedEventDateOfEvent, this.receivedEventReceivedQuality,
							receivedTime);
				}
				//Validation for aliquot quantity, resolved bug# 4040 (Virender)	
				if (this.checkedButton)
				{
					if (!validator.isNumeric(this.noOfAliquots, 1))
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
								ApplicationProperties.getValue("aliquots.noOfAliquots")));
					}

					if (this.quantityPerAliquot != null
							&& this.quantityPerAliquot.trim().length() != 0)
					{
						if (AppUtility.isQuantityDouble(this.className, this.type))
						{
							if (!validator.isDouble(this.quantityPerAliquot.trim(), true))
							{
								errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
										"errors.item.format", ApplicationProperties
												.getValue("aliquots.qtyPerAliquot")));
							}
						}
						else
						{
							if (!validator.isNumeric(this.quantityPerAliquot.trim(), 1))
							{
								errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
										"errors.item.format", ApplicationProperties
												.getValue("aliquots.qtyPerAliquot")));
							}
						}
					}
				}

				if (this.derivedClicked)
				{
					//For Derived Specimen             	
					if (!validator.isNumeric(this.numberOfSpecimens, 1))
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
								ApplicationProperties.getValue("aliquots.derivedCount")));
					}
				}

				//Validations for Biohazard Add-More Block
				final String className = "Biohazard:";
				final String key1 = "_type";
				final String key2 = "_" + Constants.SYSTEM_IDENTIFIER;
				final String key3 = "_persisted";
				int index = 1;

				while (true)
				{
					final String keyOne = className + index + key1;
					final String keyTwo = className + index + key2;
					final String keyThree = className + index + key3;

					final String value1 = (String) this.biohazard.get(keyOne);
					final String value2 = (String) this.biohazard.get(keyTwo);
					final String value3 = (String) this.biohazard.get(keyThree);

					if (value1 == null && value2 == null && value3 == null)
					{
						break;
					}
					else if (!validator.isValidOption(value1) && !validator.isValidOption(value2))
					{
						this.biohazard.remove(keyOne);
						this.biohazard.remove(keyTwo);
						this.biohazard.remove(keyThree);
					}
					else if ((validator.isValidOption(value1) && !validator.isValidOption(value2))
							|| (!validator.isValidOption(value1) && validator.isValidOption(value2)))
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"errors.newSpecimen.biohazard.missing", ApplicationProperties
										.getValue("newSpecimen.msg")));
						break;
					}
					index++;
				}
				if (this.getOperation().equals(Constants.ADD))
				{
					this.collectionStatus = Constants.COLLECTION_STATUS_COLLECTED;
				}
			}
		}
		catch (final Exception excp)
		{
			logger.error(excp.getMessage());
		}
		return errors;
	}

	/**
	 * @return bhCounter Returns the bhCounter.
	 */
	public int getBhCounter()
	{
		return this.bhCounter;
	}

	/**
	 * @param bhCounter The bhCounter to set.
	 */
	public void setBhCounter(int bhCounter)
	{
		this.bhCounter = bhCounter;
	}

	/**
	 * @return parentPresent  parentPresent Returns the parentPresent.
	 */
	public boolean isParentPresent()
	{
		return this.parentPresent;
	}

	/**
	 * @param parentPresent The parentPresent to set.
	 */
	public void setParentPresent(boolean parentPresent)
	{
		this.parentPresent = parentPresent;
	}

	/**
	 * @return specimenEventParameter Returns the specimenEventParameter.
	 */
	public String getSpecimenEventParameter()
	{
		return this.specimenEventParameter;
	}

	/**
	 * @param specimenEventParameter The specimenEventParameter to set.
	 */
	public void setSpecimenEventParameter(String specimenEventParameter)
	{
		this.specimenEventParameter = specimenEventParameter;
	}

	/**
	 * This method sets Identifier of Objects inserted by AddNew activity in Form-Bean which initialized AddNew action
	 * @param addNewFor - FormBean ID of the object inserted
	 * @param addObjectIdentifier - Identifier of the Object inserted 
	 */
	@Override
	public void setAddNewObjectIdentifier(String addNewFor, Long addObjectIdentifier)
	{
		if (addNewFor.equals("specimenCollectionGroupId"))
		{
			logger.debug("Setting SCG ID in NewSpecimenForm#####" + addObjectIdentifier);

			this.setSpecimenCollectionGroupId(addObjectIdentifier.toString());
		}
	}

	/**
	 * Returns the no. of aliquots to be created.
	 * @return noOfAliquots The no. of aliquots to be created.
	 * @see #setNoOfAliquots(String)
	 */
	public String getNoOfAliquots()
	{
		return this.noOfAliquots;
	}

	/**
	 * Sets the no. of aliquots to be created.
	 * @param noOfAliquots The no. of aliquots to be created.
	 * @see #getNoOfAliquots()
	 */
	public void setNoOfAliquots(String noOfAliquots)
	{
		this.noOfAliquots = noOfAliquots;
	}

	/**
	 * Returns the initial quantity per aliquot.
	 * @return quantityPerAliquot The initial quantity per aliquot.
	 * @see #setQuantityPerAliquot(String)
	 */
	public String getQuantityPerAliquot()
	{
		return this.quantityPerAliquot;
	}

	/**
	 * Sets the initial quantity per aliquot.
	 * @param quantityPerAliquot The initial quantity per aliquot.
	 * @see #getQuantityPerAliquot()
	 */
	public void setQuantityPerAliquot(String quantityPerAliquot)
	{
		this.quantityPerAliquot = quantityPerAliquot;
	}

	/**
	 * Tells whether the button is checked ot unchecked.
	 * @return True if button is checked else false.
	 * @see #setCheckedButton(boolean)
	 */
	public boolean isCheckedButton()
	{
		return this.checkedButton;
	}

	/**
	 * Sets/Resets the checked button.
	 * @param checkedButton The value of checked button.
	 * @see #isCheckedButton()
	 */
	public void setCheckedButton(boolean checkedButton)
	{
		this.checkedButton = checkedButton;
	}

	// Mandar: 10-july-06 AutoEvents : Collection Event start

	public boolean isDerivedClicked()
	{
		return this.derivedClicked;
	}

	public void setDerivedClicked(boolean derivedClicked)
	{
		this.derivedClicked = derivedClicked;
	}

	/**
	 * @return Returns the collectionEventCollectionProcedure.
	 */
	public String getCollectionEventCollectionProcedure()
	{
		return this.collectionEventCollectionProcedure;
	}

	/**
	 * @param collectionEventCollectionProcedure The collectionEventCollectionProcedure to set.
	 */
	public void setCollectionEventCollectionProcedure(String collectionEventCollectionProcedure)
	{
		this.collectionEventCollectionProcedure = collectionEventCollectionProcedure;
	}

	/**
	 * @return Returns the collectionEventComments.
	 */
	public String getCollectionEventComments()
	{
		return this.collectionEventComments;
	}

	/**
	 * @param collectionEventComments The collectionEventComments to set.
	 */
	public void setCollectionEventComments(String collectionEventComments)
	{
		this.collectionEventComments = collectionEventComments;
	}

	/**
	 * @return Returns the collectionEventContainer.
	 */
	public String getCollectionEventContainer()
	{
		return this.collectionEventContainer;
	}

	/**
	 * @param collectionEventContainer The collectionEventContainer to set.
	 */
	public void setCollectionEventContainer(String collectionEventContainer)
	{
		this.collectionEventContainer = collectionEventContainer;
	}

	/**
	 * @return Returns the collectionEventdateOfEvent.
	 */
	public String getCollectionEventdateOfEvent()
	{
		return this.collectionEventdateOfEvent;
	}

	/**
	 * @param collectionEventdateOfEvent The collectionEventdateOfEvent to set.
	 */
	public void setCollectionEventdateOfEvent(String collectionEventdateOfEvent)
	{
		this.collectionEventdateOfEvent = collectionEventdateOfEvent;
	}

	/**
	 * @return Returns the collectionEventSpecimenId.
	 */
	public long getCollectionEventSpecimenId()
	{
		return this.collectionEventSpecimenId;
	}

	/**
	 * @param collectionEventSpecimenId The collectionEventSpecimenId to set.
	 */
	public void setCollectionEventSpecimenId(long collectionEventSpecimenId)
	{
		this.collectionEventSpecimenId = collectionEventSpecimenId;
	}

	/**
	 * @return Returns the collectionEventId.
	 */
	public long getCollectionEventId()
	{
		return this.collectionEventId;
	}

	/**
	 * @param collectionEventId The collectionEventId to set.
	 */
	public void setCollectionEventId(long collectionEventId)
	{
		this.collectionEventId = collectionEventId;
	}

	/**
	 * @return Returns the collectionEventTimeInHours.
	 */
	public String getCollectionEventTimeInHours()
	{
		return this.collectionEventTimeInHours;
	}

	/**
	 * @param collectionEventTimeInHours The collectionEventTimeInHours to set.
	 */
	public void setCollectionEventTimeInHours(String collectionEventTimeInHours)
	{
		this.collectionEventTimeInHours = collectionEventTimeInHours;
	}

	/**
	 * @return Returns the collectionEventTimeInMinutes.
	 */
	public String getCollectionEventTimeInMinutes()
	{
		return this.collectionEventTimeInMinutes;
	}

	/**
	 * @param collectionEventTimeInMinutes The collectionEventTimeInMinutes to set.
	 */
	public void setCollectionEventTimeInMinutes(String collectionEventTimeInMinutes)
	{
		this.collectionEventTimeInMinutes = collectionEventTimeInMinutes;
	}

	/**
	 * @return Returns the collectionEventUserId.
	 */
	public long getCollectionEventUserId()
	{
		return this.collectionEventUserId;
	}

	/**
	 * @param collectionEventUserId The collectionEventUserId to set.
	 */
	public void setCollectionEventUserId(long collectionEventUserId)
	{
		this.collectionEventUserId = collectionEventUserId;
	}

	// Mandar: 10-july-06 AutoEvents : Collection Event end

	//Mandar : 11-July-06 AutoEvents : CollectionEvent validation

	//Mandar : 11-july-06 AutoEvents : ReceivedEvent start

	/**
	 * @return Returns the receivedEventComments.
	 */
	public String getReceivedEventComments()
	{
		return this.receivedEventComments;
	}

	/**
	 * @param receivedEventComments The receivedEventComments to set.
	 */
	public void setReceivedEventComments(String receivedEventComments)
	{
		this.receivedEventComments = receivedEventComments;
	}

	/**
	 * @return Returns the receivedEventDateOfEvent.
	 */
	public String getReceivedEventDateOfEvent()
	{
		return this.receivedEventDateOfEvent;
	}

	/**
	 * @param receivedEventDateOfEvent The receivedEventDateOfEvent to set.
	 */
	public void setReceivedEventDateOfEvent(String receivedEventDateOfEvent)
	{
		this.receivedEventDateOfEvent = receivedEventDateOfEvent;
	}

	/**
	 * @return Returns the receivedEventReceivedQuality.
	 */
	public String getReceivedEventReceivedQuality()
	{
		return this.receivedEventReceivedQuality;
	}

	/**
	 * @param receivedEventReceivedQuality The receivedEventReceivedQuality to set.
	 */
	public void setReceivedEventReceivedQuality(String receivedEventReceivedQuality)
	{
		this.receivedEventReceivedQuality = receivedEventReceivedQuality;
	}

	/**
	 * @return Returns the receivedEventSpecimenId.
	 */
	public long getReceivedEventSpecimenId()
	{
		return this.receivedEventSpecimenId;
	}

	/**
	 * @param receivedEventSpecimenId The receivedEventSpecimenId to set.
	 */
	public void setReceivedEventSpecimenId(long receivedEventSpecimenId)
	{
		this.receivedEventSpecimenId = receivedEventSpecimenId;
	}

	/**
	 * @return Returns the receivedEventId.
	 */
	public long getReceivedEventId()
	{
		return this.receivedEventId;
	}

	/**
	 * @param receivedEventId The receivedEventId to set.
	 */
	public void setReceivedEventId(long receivedEventId)
	{
		this.receivedEventId = receivedEventId;
	}

	/**
	 * @return Returns the receivedEventTimeInHours.
	 */
	public String getReceivedEventTimeInHours()
	{
		return this.receivedEventTimeInHours;
	}

	/**
	 * @param receivedEventTimeInHours The receivedEventTimeInHours to set.
	 */
	public void setReceivedEventTimeInHours(String receivedEventTimeInHours)
	{
		this.receivedEventTimeInHours = receivedEventTimeInHours;
	}

	/**
	 * @return Returns the receivedEventTimeInMinutes.
	 */
	public String getReceivedEventTimeInMinutes()
	{
		return this.receivedEventTimeInMinutes;
	}

	/**
	 * @param receivedEventTimeInMinutes The receivedEventTimeInMinutes to set.
	 */
	public void setReceivedEventTimeInMinutes(String receivedEventTimeInMinutes)
	{
		this.receivedEventTimeInMinutes = receivedEventTimeInMinutes;
	}

	/**
	 * @return Returns the receivedEventUserId.
	 */
	public long getReceivedEventUserId()
	{
		return this.receivedEventUserId;
	}

	/**
	 * @param receivedEventUserId The receivedEventUserId to set.
	 */
	public void setReceivedEventUserId(long receivedEventUserId)
	{
		this.receivedEventUserId = receivedEventUserId;
	}

	//Mandar : 18-July-06 ReceivedEvent validation

	//Mandar : 11-july-06 AutoEvents : ReceivedEvent end

	/**
	 * Returns the historical information about the specimen.
	 * @return lineage The historical information about the specimen.
	 * @see #setLineage(String)
	 */
	public String getLineage()
	{
		return this.lineage;
	}

	/**
	 * Sets the historical information about the specimen.
	 * @param lineage The historical information about the specimen.
	 * @see #getLineage()
	 */
	public void setLineage(String lineage)
	{
		this.lineage = lineage;
	}

	// Patch ID: Bug#3184_6
	/**
	 * This method returns the name of SpecimenCollectionGroup
	 * @return specimenCollectionGroupName the specimenCollectionGroupName
	 */
	public String getSpecimenCollectionGroupName()
	{
		return this.specimenCollectionGroupName;
	}

	/**
	 * This method sets the name of SpecimenCollectionGroup
	 * @param specimenCollectionGroupName the specimenCollectionGroupName to set
	 */
	public void setSpecimenCollectionGroupName(String specimenCollectionGroupName)
	{
		this.specimenCollectionGroupName = specimenCollectionGroupName;
	}

	/**
	 * @return numberOfSpecimen the numberOfSpecimen
	 */
	public int getNumberOfSpecimen()
	{
		return this.numberOfSpecimen;
	}

	/**
	 * @param numberOfSpecimen the numberOfSpecimen to set
	 */
	public void setNumberOfSpecimen(int numberOfSpecimen)
	{
		this.numberOfSpecimen = numberOfSpecimen;
	}

	/**
	 * Getting Parent Specimen Name
	 * @return parentSpecimenName
	 */
	public String getParentSpecimenName()
	{
		return this.parentSpecimenName;
	}

	/**
	 * @param parentSpecimenName setting Parent Specimen Name
	 */
	public void setParentSpecimenName(String parentSpecimenName)
	{
		this.parentSpecimenName = parentSpecimenName;
	}

	//Consent Tracking Module Virender mehta	
	/**
	 * @return consentResponseForSpecimenValues  The comments associated with Response at Specimen level.
	 */
	public Map getConsentResponseForSpecimenValues()
	{
		return this.consentResponseForSpecimenValues;
	}

	/**
	 * @param consentResponseForSpecimenValues  The comments associated with Response at Specimen level
	 */
	public void setConsentResponseForSpecimenValues(Map consentResponseForSpecimenValues)
	{
		this.consentResponseForSpecimenValues = consentResponseForSpecimenValues;
	}

	/**
	 *@param key Key prepared for saving data.
	 *@param value Values correspponding to key
	 */
	public void setConsentResponseForSpecimenValue(String key, Object value)
	{
		if (this.isMutable())
		{
			this.consentResponseForSpecimenValues.put(key, value);
		}
	}

	/**
	 * @param key Key prepared for saving data.
	 * @return consentResponseForSpecimenValues.get(key)
	 */
	public Object getConsentResponseForSpecimenValue(String key)
	{
		return this.consentResponseForSpecimenValues.get(key);
	}

	/**
	 * @return values in map consentResponseForSpecimenValues
	 */
	public Collection getAllConsentResponseForSpecimenValue()
	{
		return this.consentResponseForSpecimenValues.values();
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

	/**
	 * @param applyChangesTo 
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
	 * @return stringOfResponseKeys
	 */
	public String getStringOfResponseKeys()
	{
		return this.stringOfResponseKeys;
	}

	/**
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
			strArray[0] = "consentResponseForSpecimenValues(ConsentBean:" + counter
					+ "_consentTierID)";
			strArray[1] = "consentResponseForSpecimenValues(ConsentBean:" + counter + "_statement)";
			strArray[2] = "consentResponseForSpecimenValues(ConsentBean:" + counter
					+ "_participantResponse)";
			strArray[3] = "consentResponseForSpecimenValues(ConsentBean:" + counter
					+ "_participantResponseID)";
			strArray[4] = "consentResponseForSpecimenValues(ConsentBean:" + counter
					+ "_specimenLevelResponse)";
			strArray[5] = "consentResponseForSpecimenValues(ConsentBean:" + counter
					+ "_specimenLevelResponseID)";
			consentTiersList.add(strArray);
		}
		return consentTiersList;
	}

	/**
	 * This funtion returns the format of the response Key prepared. 
	 * @return consentResponseForSpecimenValues(ConsentBean:`_participantResponse)
	 */
	public String getConsentTierMap()
	{
		return "consentResponseForSpecimenValues(ConsentBean:`_specimenLevelResponse)";
	}

	//Consent Tracking Module Virender mehta

	public String getCollectionStatus()
	{
		return this.collectionStatus;
	}

	public void setCollectionStatus(String collectionStatus)
	{
		this.collectionStatus = collectionStatus;
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

}
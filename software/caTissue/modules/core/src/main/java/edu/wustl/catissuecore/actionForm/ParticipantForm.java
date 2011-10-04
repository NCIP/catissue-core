/**
' * <p>Title: ParticipantForm Class>
 * <p>Description:  ParticipantForm Class is used to encapsulate all the request parameters passed
 * from Participant Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Mar 3, 2005
 */

package edu.wustl.catissuecore.actionForm;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bean.ConsentBean;
import edu.wustl.catissuecore.bean.ConsentResponseBean;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Race;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.DefaultValueManager;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.participant.actionForm.IParticipantForm;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * ParticipantForm Class is used to encapsulate all the request parameters passed
 * from Participant Add/Edit webpage.
 * @author gautam_shetty
 */

public class ParticipantForm extends AbstractActionForm implements Serializable, IParticipantForm
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(ParticipantForm.class);
	private static final long serialVersionUID = 1234567890L;

	/**
	 * Last Name of the Participant.
	 */
	protected String lastName = "";

	/**
	 * First Name of the Participant.
	 */
	protected String firstName = "";

	/**
	 * Middle Name of the Participant.
	 */
	protected String middleName = "";

	/**
	 * Name : Virender Mehta
	 * Reviewer: Sachin Lale
	 * Bug ID: defaultValueConfiguration_BugID
	 * Patch ID:defaultValueConfiguration_BugID_6
	 * Description: Configuration for default value for Gender, Genotype, Race, Ethnicity, VitalStatus
	 */

	/**
	 * The gender of a participant.
	 */
	protected String gender = (String) DefaultValueManager
			.getDefaultValue(Constants.DEFAULT_GENDER);

	/**
	 * The genotype of a participant.
	 */
	protected String genotype = (String) DefaultValueManager
			.getDefaultValue(Constants.DEFAULT_GENOTYPE);

	/**
	 * The string to determine if barcode field is editable
	 */
	private String isBarcodeEditable = (String) DefaultValueManager
			.getDefaultValue(Constants.IS_BARCODE_EDITABLE);
	/**
	 * Social Security Number of the Participant.
	 */
	protected String socialSecurityNumberPartA = "";
	protected String socialSecurityNumberPartB = "";
	protected String socialSecurityNumberPartC = "";

	/**
	 * The Date of Birth of the Participant.
	 */
	protected String birthDate = "";

	/**
	 * The race to which the Participant belongs.
	 */
	protected String[] raceTypes = {(String) DefaultValueManager
			.getDefaultValue(Constants.DEFAULT_RACE)};

	/**
	 * Participant's ethnicity status.
	 */
	protected String ethnicity = (String) DefaultValueManager
			.getDefaultValue(Constants.DEFAULT_ETHNICITY);

	/**
	 * The Date of Death of the Participant.
	 */
	protected String deathDate = "";

	/**
	 * Vital status of the Participant.
	 */
	protected String vitalStatus = (String) DefaultValueManager
			.getDefaultValue(Constants.DEFAULT_VITAL_STATUS);

	/**
	 * Map to handle values of all the Participant Medical Identifiers
	 */
	protected Map<String, Object> values = new LinkedHashMap<String, Object>();

	/**
	 * Abhishek Mehta
	 */
	/**
	 * Map to handle values of registration of a Participant to a Collection Protocol.
	 */
	protected Map<String, Object> collectionProtocolRegistrationValues = new LinkedHashMap<String, Object>();

	/**
	 * Consent Response Collection for given collection protocols
	 */
	protected Collection<ConsentResponseBean> consentResponseBeanCollection;

	/**
	 * Consent Response hashtable entered by the user.
	 */
	protected Map<String, ConsentResponseBean> consentResponseHashTable;

	/**
	 * Counter that contains number of rows in the 'Add More' functionality for medical identifier.
	 */
	private int valueCounter;

	/**
	 * Counter that contains number of rows in the 'Add More' functionality for registration of a Participant to a Collection Protocol.
	 */
	private int collectionProtocolRegistrationValueCounter;

//	private String isConsentAvailable;

	private long cpId = -1;

	/**
	 * Initializes an empty ParticipantForm object.
	 */
	public ParticipantForm()
	{

	}

	/**
	 * @param ssnString Setting SSN number
	 */
	private void setSSN(String ssnString)
	{
		if (ssnString != null && !ssnString.equals(""))
		{
			try
			{
				final StringTokenizer tok = new StringTokenizer(ssnString, "-");
				this.socialSecurityNumberPartA = tok.nextToken();
				this.socialSecurityNumberPartB = tok.nextToken();
				this.socialSecurityNumberPartC = tok.nextToken();
			}
			catch (final Exception ex)
			{
				ParticipantForm.logger.error(ex.getMessage(), ex);
				ex.printStackTrace();
				this.socialSecurityNumberPartA = "";
				this.socialSecurityNumberPartB = "";
				this.socialSecurityNumberPartC = "";
			}
		}
	}

	/**
	 * Copies the data from an AbstractDomain object to a ParticipantForm object.
	 * @param abstractDomain An AbstractDomain object.
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		final Participant participant = (Participant) abstractDomain;
		this.setId(participant.getId().longValue());
		this.lastName = CommonUtilities.toString(participant.getLastName());
		this.firstName = CommonUtilities.toString(participant.getFirstName());
		this.middleName = CommonUtilities.toString(participant.getMiddleName());
		this.birthDate = CommonUtilities.parseDateToString(participant.getBirthDate(),
				CommonServiceLocator.getInstance().getDatePattern());
		this.gender = participant.getGender();
		this.genotype = participant.getSexGenotype();
		this.setSSN(participant.getSocialSecurityNumber());

		final Collection raceCollection = participant.getRaceCollection();
		if (raceCollection != null)
		{
			this.raceTypes = new String[raceCollection.size()];
			int i = 0;

			final Iterator it = raceCollection.iterator();
			while (it.hasNext())
			{
				final Race race = (Race) it.next();
				if (race != null)
				{
					//	String raceName = race.getRaceName();
					this.raceTypes[i] = race.getRaceName();
					i++;
				}
			}
		}

		//        this.race = participant.getRace();
		this.setActivityStatus(participant.getActivityStatus());
		this.ethnicity = participant.getEthnicity();
		this.deathDate = CommonUtilities.parseDateToString(participant.getDeathDate(),
				CommonServiceLocator.getInstance().getDatePattern());;
		this.vitalStatus = participant.getVitalStatus();

		//Populating the map with the participant medical identifiers data
		final Collection medicalIdentifierCollection = participant
				.getParticipantMedicalIdentifierCollection();

		if (medicalIdentifierCollection != null)
		{
			this.values = new LinkedHashMap<String, Object>();
			int i = 1;

			final Iterator it = medicalIdentifierCollection.iterator();
			while (it.hasNext())
			{
				final ParticipantMedicalIdentifier participantMedicalIdentifier = (ParticipantMedicalIdentifier) it
						.next();

				final String key1 = AppUtility.getParticipantMedicalIdentifierKeyFor(i,
						Constants.PARTICIPANT_MEDICAL_IDENTIFIER_SITE_ID); // "ParticipantMedicalIdentifier:" + i +"_Site_id";
				final String key2 = AppUtility.getParticipantMedicalIdentifierKeyFor(i,
						Constants.PARTICIPANT_MEDICAL_IDENTIFIER_MEDICAL_NUMBER); // "ParticipantMedicalIdentifier:" + i +"_medicalRecordNumber";
				final String key3 = AppUtility.getParticipantMedicalIdentifierKeyFor(i,
						Constants.PARTICIPANT_MEDICAL_IDENTIFIER_ID); // "ParticipantMedicalIdentifier:" + i +"_id";

				final Site site = participantMedicalIdentifier.getSite();

				if (site != null)
				{
					this.values.put(key1, CommonUtilities.toString(site.getId()));
				}
				else
				{
					this.values.put(key1, CommonUtilities.toString(Constants.SELECT_OPTION));
				}

				this.values.put(key2, CommonUtilities.toString(participantMedicalIdentifier
						.getMedicalRecordNumber()));
				this.values.put(key3, CommonUtilities
						.toString(participantMedicalIdentifier.getId()));

				i++;
			}
			this.valueCounter = medicalIdentifierCollection.size();
		}

		//Populating the map with the registrations of a Participant to a Collection Protocol.
		//(Abhishek Mehta)
		final Collection<CollectionProtocolRegistration> collectionProtocolRegistrationCollection = participant
				.getCollectionProtocolRegistrationCollection();

		if (collectionProtocolRegistrationCollection != null)
		{
			this.collectionProtocolRegistrationValues = new LinkedHashMap<String, Object>();
			if (this.consentResponseHashTable == null)
			{
				this.consentResponseHashTable = new LinkedHashMap<String, ConsentResponseBean>();
			}
			this.consentResponseBeanCollection = new LinkedHashSet<ConsentResponseBean>();
			int i = 1;

			final Iterator<CollectionProtocolRegistration> it = collectionProtocolRegistrationCollection.iterator();
			while (it.hasNext())
			{
				final CollectionProtocolRegistration collectionProtocolRegistration = it
						.next();
				if (collectionProtocolRegistration.getActivityStatus() != null
						&& !collectionProtocolRegistration.getActivityStatus().equalsIgnoreCase(
								Constants.DISABLED))
				{
					final String collectionProtocolId = "CollectionProtocolRegistration:" + i
							+ "_CollectionProtocol_id";
					final String collectionProtocolTitle = "CollectionProtocolRegistration:" + i
							+ "_CollectionProtocol_shortTitle";
					final String collectionProtocolParticipantId = "CollectionProtocolRegistration:"
							+ i + "_protocolParticipantIdentifier";
					final String barcode = "CollectionProtocolRegistration:" + i + "_barcode";
					final String collectionProtocolRegistrationDate = "CollectionProtocolRegistration:"
							+ i + "_registrationDate";
					final String collectionProtocolIdentifier = "CollectionProtocolRegistration:"
							+ i + "_id";
					/*final String isConsentAvailable = "CollectionProtocolRegistration:" + i
							+ "_isConsentAvailable";*/
					final String isActive = "CollectionProtocolRegistration:" + i
							+ "_activityStatus";

					final Collection consentTierCollection = collectionProtocolRegistration
							.getCollectionProtocol().getConsentTierCollection();
					/*if (consentTierCollection != null && consentTierCollection.isEmpty())
					{
//						this.setIsConsentAvailable(Constants.NO_CONSENTS_DEFINED);
						this.collectionProtocolRegistrationValues.put(isConsentAvailable,
								Constants.NO_CONSENTS_DEFINED);
					}
					else if (consentTierCollection != null && !consentTierCollection.isEmpty())
					{
//						this.setIsConsentAvailable(Constants.PARTICIPANT_CONSENT_ENTER_RESPONSE);
						this.collectionProtocolRegistrationValues.put(isConsentAvailable,
								Constants.PARTICIPANT_CONSENT_ENTER_RESPONSE);
					}*/

					final String date = CommonUtilities.parseDateToString(
							collectionProtocolRegistration.getRegistrationDate(),
							CommonServiceLocator.getInstance().getDatePattern());

					this.collectionProtocolRegistrationValues.put(collectionProtocolId,
							CommonUtilities.toString(collectionProtocolRegistration
									.getCollectionProtocol().getId()));
					this.collectionProtocolRegistrationValues.put(collectionProtocolTitle,
							CommonUtilities.toString(collectionProtocolRegistration
									.getCollectionProtocol().getShortTitle()));
					this.collectionProtocolRegistrationValues.put(collectionProtocolParticipantId,
							CommonUtilities.toString(collectionProtocolRegistration
									.getProtocolParticipantIdentifier()));
					this.collectionProtocolRegistrationValues.put(barcode, CommonUtilities
							.toString(collectionProtocolRegistration.getBarcode()));
					this.collectionProtocolRegistrationValues.put(
							collectionProtocolRegistrationDate, date);
					this.collectionProtocolRegistrationValues.put(collectionProtocolIdentifier,
							CommonUtilities.toString(collectionProtocolRegistration.getId()));
					this.collectionProtocolRegistrationValues.put(isActive, CommonUtilities
							.toString(collectionProtocolRegistration.getActivityStatus()));

					this.getConsentResponse(collectionProtocolRegistration);

					i++;
				}
			}
			this.collectionProtocolRegistrationValueCounter = (i - 1);
		}

		if (this.valueCounter == 0)
		{
			this.valueCounter = 1;
		}
	}

	/*
	 * Get the consent Response from the database.
	 * (Abhishek Mehta)
	 */
	private void getConsentResponse(CollectionProtocolRegistration collectionProtocolRegistration)
	{
		try
		{

			logger.debug(":::::::: collection protocol id  :"
					+ collectionProtocolRegistration.getCollectionProtocol().getId());
			logger.debug(":::::::: collection protocol registration  id :"
					+ collectionProtocolRegistration.getId());

			final long collectionProtocolID = collectionProtocolRegistration
					.getCollectionProtocol().getId();
			final String signedConsentURL = collectionProtocolRegistration
					.getSignedConsentDocumentURL();
			final User consentWitness = collectionProtocolRegistration.getConsentWitness();
			long witnessId = -1;
			if (consentWitness != null)
			{
				witnessId = consentWitness.getId();
			}

			final String consentSignatureDate = CommonUtilities.parseDateToString(
					collectionProtocolRegistration.getConsentSignatureDate(), CommonServiceLocator
							.getInstance().getDatePattern());
			Collection consentResponseCollection = collectionProtocolRegistration
					.getConsentTierResponseCollection();
			Collection<ConsentBean> consentResponse;

			if (consentResponseCollection == null || consentResponseCollection.isEmpty())
			{
				consentResponseCollection = collectionProtocolRegistration.getCollectionProtocol()
						.getConsentTierCollection();
				consentResponse = this.getConsentResponseCollection(consentResponseCollection,
						false);
			}
			else
			{
				consentResponse = this
						.getConsentResponseCollection(consentResponseCollection, true);
			}

			final ConsentResponseBean consentResponseBean = new ConsentResponseBean(
					collectionProtocolID, signedConsentURL, witnessId, consentSignatureDate,
					consentResponse, Constants.WITHDRAW_RESPONSE_NOACTION);

			final String consentResponseKey = Constants.CONSENT_RESPONSE_KEY + collectionProtocolID;
			if (this.consentResponseHashTable.containsKey(consentResponseKey))
			{
				throw AppUtility.getApplicationException(null,
						"errors.participant.duplicate.collectionProtocol", "ParticipantForm.java");
			}
			this.consentResponseHashTable.put(consentResponseKey, consentResponseBean);
			this.consentResponseBeanCollection.add(consentResponseBean);
		}
		catch (final Exception e)
		{
			ParticipantForm.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	/*
	 * Returns the consentBean collection from consent tier response for given protocol collection id
	 * (Abhishek Mehta)
	 */
	private Collection<ConsentBean> getConsentResponseCollection(Collection consentResponse,
			boolean isResponseExist)
	{
		final Collection<ConsentBean> consentBeanCollection = new HashSet<ConsentBean>();
		if (consentResponse != null)
		{
			final Iterator consentResponseIter = consentResponse.iterator();
			while (consentResponseIter.hasNext())
			{
				final ConsentBean consentBean = new ConsentBean();
				if (isResponseExist)
				{
					final ConsentTierResponse consentTierResponse = (ConsentTierResponse) consentResponseIter
							.next();
					final ConsentTier consentTier = consentTierResponse.getConsentTier();
					consentBean.setConsentTierID(CommonUtilities.toString(consentTier.getId()));
					consentBean.setStatement(consentTier.getStatement());
					consentBean.setParticipantResponse(consentTierResponse.getResponse());
					consentBean.setParticipantResponseID(CommonUtilities
							.toString(consentTierResponse.getId()));
					logger.debug("::::::: participant response :::"
							+ consentTierResponse.getResponse());
					logger.debug("::::::: participant response id :::"
							+ consentTierResponse.getId());
				}
				else
				{
					final ConsentTier consentTier = (ConsentTier) consentResponseIter.next();
					consentBean.setConsentTierID(CommonUtilities.toString(consentTier.getId()));
					consentBean.setStatement(consentTier.getStatement());
					consentBean.setParticipantResponse("");
					consentBean.setParticipantResponseID("");
				}
				consentBeanCollection.add(consentBean);
			}
		}
		return consentBeanCollection;
	}

	/**
	 * Returns the last name of the Participant.
	 * @return String the last name of the Participant.
	 * @see #setFirstName(String)
	 */
	public String getLastName()
	{
		return this.lastName;
	}

	/**
	 * Sets the last name of the Participant.
	 * @param lastName Last Name of the Participant.
	 * @see #getFirstName()
	 */
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	/**
	 * Returns the first name of the Participant.
	 * @return String the first name of the Participant.
	 * @see #setFirstName(String)
	 */
	public String getFirstName()
	{
		return this.firstName;
	}

	/**
	 * Sets the first name of the Participant.
	 * @param firstName String representing the first name of the Participant.
	 * @see #getFirstName()
	 */
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	/**
	 * Returns the middle name of the Participant.
	 * @return String the middle name of the Participant.
	 * @see #setMiddleName(String)
	 */
	public String getMiddleName()
	{
		return this.middleName;
	}

	/**
	 * Sets the middle name of the Participant.
	 * @param middleName String the middle name of the Participant.
	 * @see #getMiddleName()
	 */
	public void setMiddleName(String middleName)
	{
		this.middleName = middleName;
	}

	/**
	 * Returns the date of birth of the Participant.
	 * @return String the date of birth of the Participant.
	 * @see #setBirthDate(String)
	 */
	public String getBirthDate()
	{
		return this.birthDate;
	}

	/**
	 * Sets the date of birth of the Participant.
	 * @param dateOfBirth String the date of birth of the Participant.
	 * @see #getBirthDate()
	 */
	public void setBirthDate(String dateOfBirth)
	{
		this.birthDate = dateOfBirth;
	}

	/**
	 * Returns the genotype of the Participant.
	 * @return String the genotype of the Participant.
	 * @see #setGenotype(String)
	 */
	public String getGenotype()
	{
		return this.genotype;
	}

	/**
	 * Sets the genotype of the Participant.
	 * @param genotype String the genotype of the Participant.
	 * @see #getGenotype()
	 */
	public void setGenotype(String genotype)
	{
		this.genotype = genotype;
	}

	/**
	 * Returns the gender of the Participant.
	 * @return String the gender of the Participant.
	 * @see #setGender(String)
	 */
	public String getGender()
	{
		return this.gender;
	}

	/**
	 * Sets the gender of the Participant.
	 * @param gender String the gender of the Participant.
	 * @see #getGender()
	 */
	public void setGender(String gender)
	{
		this.gender = gender;
	}

	/**
	 * Returns the race of the Participant.
	 * @return String the race of the Participant.
	 * @see #setRace(String)
	 */
	public String[] getRaceTypes()
	{
		return this.raceTypes;
	}

	/**
	 * Sets the race of the Participant.
	 * @param raceTypes String the race of the Participant.
	 * @see #getRace()
	 */
	public void setRaceTypes(String[] raceTypes)
	{
		this.raceTypes = raceTypes;
	}

	/**
	 * Returns the ethnicity of the Participant.
	 * @return Ethnicity of the Participant.
	 * @see #setEthnicity(String)
	 */
	public String getEthnicity()
	{
		return this.ethnicity;
	}

	/**
	 * Sets the ethnicity of the Participant.
	 * @param ethnicity Ethnicity of the Participant.
	 * @see #getEthnicity()
	 */
	public void setEthnicity(String ethnicity)
	{
		this.ethnicity = ethnicity;
	}

	/**
	 * Returns the id assigned to form bean.
	 * @return the id assigned to form bean.
	 */
	@Override
	public int getFormId()
	{
		return Constants.PARTICIPANT_FORM_ID;
	}

	/**
	 * Resets the values of all the fields.
	 */
	@Override
	protected void reset()
	{
		//        this.id = -1;
		//        this.lastName = null;
		//        this.firstName = null;
		//        this.middleName = null;
		//        this.birthDate=null;
		//        this.genotype = null;
		//        this.socialSecurityNumber = null;
		//        this.race = null;
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
		final ActionErrors errors = new ActionErrors();
		final Validator validator = new Validator();
		//Abhishek Mehta
		// To get the consent response from session.
		final HttpSession session = request.getSession();

		try
		{
			this.setRedirectValue(validator);

			String errorKeyForBirthDate = "";
			String errorKeyForDeathDate = "";

			// Added by Geeta for DFCI
			if (!Variables.isLastNameNull)
			{
				if (Validator.isEmpty(this.lastName))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"error.participant.lastName"));
				}
			}

			if (!Validator.isEmpty(this.birthDate))
			{
				// date validation according to bug id  722 and 730
				errorKeyForBirthDate = validator.validateDate(this.birthDate, true);
				if (errorKeyForBirthDate.trim().length() > 0)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(errorKeyForBirthDate,
							ApplicationProperties.getValue("participant.birthDate")));
				}
			}

			if (!validator.isEmpty(this.deathDate))
			{
				errorKeyForDeathDate = validator.validateDate(this.deathDate, true);
				if (errorKeyForDeathDate.trim().length() > 0)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(errorKeyForDeathDate,
							ApplicationProperties.getValue("participant.deathDate")));
				}
			}

			if ((!validator.isEmpty(this.birthDate) && !validator.isEmpty(this.deathDate))
					&& (errorKeyForDeathDate.trim().length() == 0 && errorKeyForBirthDate.trim()
							.length() == 0))
			{
				final boolean errorKey1 = validator.compareDates(this.birthDate, this.deathDate);

				if (!errorKey1)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"participant.invaliddate", ApplicationProperties
									.getValue("participant.invaliddate")));
				}
			}

			final String socialSecurityNumber = this.socialSecurityNumberPartA + "-"
					+ this.socialSecurityNumberPartB + "-" + this.socialSecurityNumberPartC;
			if (!validator.isEmpty(this.socialSecurityNumberPartA + this.socialSecurityNumberPartB
					+ this.socialSecurityNumberPartC)
					&& !validator.isValidSSN(socialSecurityNumber))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.invalid",
						ApplicationProperties.getValue("participant.socialSecurityNumber")));
			}

			//Validation for Blank Participant
			if (validator.isEmpty(this.lastName)
					&& validator.isEmpty(this.firstName)
					&& validator.isEmpty(this.middleName)
					&& validator.isEmpty(this.birthDate)
					&& (validator.isEmpty(this.deathDate))
					&& !validator.isValidOption(this.gender)
					&& !validator.isValidOption(this.vitalStatus)
					&& !validator.isValidOption(this.genotype)
					&& this.ethnicity.equals("-1")
					&& validator.isEmpty(this.socialSecurityNumberPartA
							+ this.socialSecurityNumberPartB + this.socialSecurityNumberPartC))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
						"errors.participant.atLeastOneFieldRequired"));
			}

			//Validations for Add-More Block
			final String className = "ParticipantMedicalIdentifier:";
			final String key1 = "_Site_" + Constants.SYSTEM_IDENTIFIER;
			final String key2 = "_medicalRecordNumber";
			final String key3 = "_" + Constants.SYSTEM_IDENTIFIER;
			int index = 1;

			while (true)
			{
				final String keyOne = className + index + key1;
				final String keyTwo = className + index + key2;
				final String keyThree = className + index + key3;

				final String value1 = (String) this.values.get(keyOne);
				final String value2 = (String) this.values.get(keyTwo);

				if (value1 == null || value2 == null)
				{
					break;
				}
				else if (!validator.isValidOption(value1) && value2.trim().equals(""))
				{
					this.values.remove(keyOne);
					this.values.remove(keyTwo);
					this.values.remove(keyThree);
				}
				else if ((validator.isValidOption(value1) && value2.trim().equals(""))
						|| (!validator.isValidOption(value1) && !value2.trim().equals("")))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"errors.participant.missing", ApplicationProperties
									.getValue("participant.msg")));
					break;
				}
				index++;
			}

			//validation for add more block for collection protocol registration
			//(Abhishek Mehta)
			final String collectionProtocolClassName = "CollectionProtocolRegistration:";
			final String collectionProtocolId = "_CollectionProtocol_id";
			final String collectionProtocolParticipantId = "_protocolParticipantIdentifier";
			final String collectionProtocolRegistrationDate = "_registrationDate";
			final String collectionProtocolIdentifier = "_id";
			//final String isConsentAvailable = "_isConsentAvailable";
			final String isActive = "_activityStatus";
			final String collectionProtocolTitle = "_CollectionProtocol_shortTitle";

			index = 1;
			int count = 0;
			while (true)
			{
				final String keyOne = collectionProtocolClassName + index + collectionProtocolId;
				final String keyTwo = collectionProtocolClassName + index
						+ collectionProtocolParticipantId;
				final String keyThree = collectionProtocolClassName + index
						+ collectionProtocolRegistrationDate;
				final String keyFour = collectionProtocolClassName + index
						+ collectionProtocolIdentifier;
				//final String keyFive = collectionProtocolClassName + index + isConsentAvailable;
				final String keySix = collectionProtocolClassName + index + isActive;
				final String KeySeven = collectionProtocolClassName + index
						+ collectionProtocolTitle;

				final String value1 = (String) this.collectionProtocolRegistrationValues
						.get(keyOne);
				String value2 = (String) collectionProtocolRegistrationValues.get(keyTwo);
				final String value3 = (String) this.collectionProtocolRegistrationValues
						.get(keyThree);
				String value6 = (String) this.collectionProtocolRegistrationValues.get(keySix);

				if (value6 == null)
				{
					value6 = Status.ACTIVITY_STATUS_ACTIVE.toString();
				}
				if (value6.equalsIgnoreCase(Constants.DISABLED))
				{
					this.setForwardTo("blankPage");
					request.setAttribute(Constants.PAGE_OF, this.getPageOf());
					request.setAttribute("participantId", new Long(this.getId()).toString());
					request.setAttribute("cpId", value1);
				}

				final String errorKey = validator.validateDate(value3, true);
				if (value1 == null)
				{
					break;
				}
				else if (!validator.isValidOption(value1))
				{
					this.collectionProtocolRegistrationValues.remove(keyOne);
					this.collectionProtocolRegistrationValues.remove(keyTwo);
					this.collectionProtocolRegistrationValues.remove(keyThree);
					this.collectionProtocolRegistrationValues.remove(keyFour);
					//this.collectionProtocolRegistrationValues.remove(keyFive);
					this.collectionProtocolRegistrationValues.remove(keySix);
					this.collectionProtocolRegistrationValues.remove(KeySeven);
					count++;
				}

				else if ((validator.isValidOption(value1) && !validator.isValidOption(value6))
						|| (!validator.isValidOption(value1) && !validator.isValidOption(value6)))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"errors.participant.participantRegistration.missing",
							ApplicationProperties.getValue("participant.msg")));
					break;
				}

				if (errorKey.trim().length() > 0)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(errorKey,
							ApplicationProperties.getValue("participant.cpr.msg")));
					break;
				}

				index++;
			}

			this.collectionProtocolRegistrationValueCounter = this.collectionProtocolRegistrationValueCounter
					- count;

			//Getting ConsentRegistrationBean from the  session and creating consent response map.
			this.setConsentResponse(session);

		}
		catch (final Exception excp)
		{
			ParticipantForm.logger.error(excp.getMessage(),excp);
			excp.printStackTrace();
		}

		return errors;
	}

	/*
	 * This method will get the consent response from session and creates ConsentResponseBean Collection
	 *  Abhishek Mehta
	 */
	private void setConsentResponse(HttpSession session)
	{

		final Map consentResponsesHashTable = (Map) session
				.getAttribute(Constants.CONSENT_RESPONSE);
		if (consentResponsesHashTable != null)
		{
			final int size = consentResponsesHashTable.size();
			if (this.consentResponseBeanCollection == null)
			{
				this.consentResponseBeanCollection = new LinkedHashSet<ConsentResponseBean>();
			}

			for (int i = 1; i <= size; i++)
			{
				final String collectionProtocolID = (String) this.collectionProtocolRegistrationValues
						.get("CollectionProtocolRegistration:" + i + "_CollectionProtocol_id");
				final String consentResponseKey = Constants.CONSENT_RESPONSE_KEY
						+ collectionProtocolID;
				final ConsentResponseBean consentResponseBean = (ConsentResponseBean) consentResponsesHashTable
						.get(consentResponseKey);

				if (consentResponseBean != null)
				{
					this.consentResponseBeanCollection.add(consentResponseBean);
				}
			}
		}
	}

	/**
	 * Associates the specified object with the specified key in the map.
	 * @param key the key to which the object is mapped.
	 * @param value the object which is mapped.
	 */
	public void setValue(String key, Object value)
	{
		if (this.isMutable())
		{
			this.values.put(key, value);
		}
	}

	/**
	 * Returns the object to which this map maps the specified key.
	 * @param key the required key.
	 * @return the object to which this map maps the specified key.
	 */
	public Object getValue(String key)
	{
		return this.values.get(key);
	}

	/**
	 * @return Returns the values.
	 */
	public Collection<Object> getAllValues()
	{
		return this.values.values();
	}

	/**
	 * @param values The values to set.
	 */
	public void setValues(Map<String, Object> values)
	{
		this.values = values;
	}

	/**
	 * @param values The values to set.
	 */
	public Map<String, Object> getValues()
	{
		return this.values;
	}

	//Collection Protocol Registration Map
	//Abhishek Mehta
	/**
	 * Associates the specified object with the specified key in the map.
	 * @param key the key to which the object is mapped.
	 * @param value the object which is mapped.
	 */
	public void setCollectionProtocolRegistrationValue(String key, Object value)
	{
		if (this.isMutable())
		{
			this.collectionProtocolRegistrationValues.put(key, value);
		}
	}

	public void setDefaultCollectionProtocolRegistrationValue(String key, Object value)
	{
		this.collectionProtocolRegistrationValues.put(key, value);
	}

	/**
	 * Returns the object to which this map maps the specified key.
	 * @param key the required key.
	 * @return the object to which this map maps the specified key.
	 */
	public Object getCollectionProtocolRegistrationValue(String key)
	{
		return this.collectionProtocolRegistrationValues.get(key);
	}

	/**
	 * @return Returns the values.
	 */
	public Collection<Object> getAllCollectionProtocolRegistrationValues()
	{
		return this.collectionProtocolRegistrationValues.values();
	}

	public Map<String, Object> getCollectionProtocolRegistrationValues()
	{
		return this.collectionProtocolRegistrationValues;
	}

	public void setCollectionProtocolRegistrationValues(Map collectionProtocolRegistrationValues)
	{
		this.collectionProtocolRegistrationValues = collectionProtocolRegistrationValues;
	}

	/**
	 * Returns the counter.
	 * @return int the counter.
	 * @see #setCounter(int)
	 */
	public int getValueCounter()
	{
		return this.valueCounter;
	}

	/**
	 * Sets the counter.
	 * @param counter The counter.
	 * @see #getCounter()
	 */
	public void setValueCounter(int counter)
	{
		this.valueCounter = counter;
	}

	//Collection Protocol Registration Counter
	//Abhishek Mehta
	/**
	 * Returns the counter.
	 * @return int the counter.
	 * @see #setCounter(int)
	 */
	public int getCollectionProtocolRegistrationValueCounter()
	{
		return this.collectionProtocolRegistrationValueCounter;
	}

	/**
	 * Sets the counter.
	 * @param counter The counter.
	 * @see #getCounter()
	 */
	public void setCollectionProtocolRegistrationValueCounter(
			int collectionProtocolRegistrationValueCounter)
	{
		this.collectionProtocolRegistrationValueCounter = collectionProtocolRegistrationValueCounter;
	}

	/**
	 * Returns the first part of Social Security Number.
	 * @return String First part of Social Security Number.
	 * @see #setSocialSecurityNumberPartA(String)
	 */
	public String getSocialSecurityNumberPartA()
	{
		return this.socialSecurityNumberPartA;
	}

	/**
	 * Sets the first part of Social Security Number.
	 * @param socialSecurityNumberPartA First part of Social Security Number.
	 * @see #getSocialSecurityNumberPartA()
	 */
	public void setSocialSecurityNumberPartA(String socialSecurityNumberPartA)
	{
		this.socialSecurityNumberPartA = socialSecurityNumberPartA;
	}

	/**
	 * Returns the second part of Social Security Number.
	 * @return String Second part of Social Security Number.
	 * @see #setSocialSecurityNumberPartB(String)
	 */
	public String getSocialSecurityNumberPartB()
	{
		return this.socialSecurityNumberPartB;
	}

	/**
	 * Sets the second part of Social Security Number.
	 * @param socialSecurityNumberPartB Second part of Social Security Number.
	 * @see #getSocialSecurityNumberPartB()
	 */
	public void setSocialSecurityNumberPartB(String socialSecurityNumberPartB)
	{
		this.socialSecurityNumberPartB = socialSecurityNumberPartB;
	}

	/**
	 * Returns the third part of Social Security Number.
	 * @return String Third part of Social Security Number.
	 * @see #setSocialSecurityNumberPartC(String)
	 */
	public String getSocialSecurityNumberPartC()
	{
		return this.socialSecurityNumberPartC;
	}

	/**
	 * Sets the third part of Social Security Number.
	 * @param socialSecurityNumberPartC Third part of Social Security Number.
	 * @see #getSocialSecurityNumberPartC()
	 */
	public void setSocialSecurityNumberPartC(String socialSecurityNumberPartC)
	{
		this.socialSecurityNumberPartC = socialSecurityNumberPartC;
	}

	/**
	 * Returns the Death date of the Participant.
	 * @return Returns the deathDate.
	 */
	public String getDeathDate()
	{
		return this.deathDate;
	}

	/**
	 * Sets the Death date of the Participant.
	 * @param deathDate The deathDate to set.
	 */
	public void setDeathDate(String deathDate)
	{
		this.deathDate = deathDate;
	}

	/**
	 * Returns the Vital status of the Participant.
	 * @return Returns the vitalStatus.
	 */
	public String getVitalStatus()
	{
		return this.vitalStatus;
	}

	/**
	 * Sets the Vital status of the Participant.
	 * @param vitalStatus The vitalStatus to set.
	 */
	public void setVitalStatus(String vitalStatus)
	{
		this.vitalStatus = vitalStatus;
	}

	/**
	 * @return cpId
	 */
	public long getCpId()
	{
		return this.cpId;
	}

	/**
	 * @param cpId Set cpId
	 */
	public void setCpId(long cpId)
	{
		this.cpId = cpId;
	}

	/**
	 * Returns the Consent Response HashTable entered by the user.
	 * @return
	 */
	public Map<String, ConsentResponseBean> getConsentResponseHashTable()
	{
		return this.consentResponseHashTable;
	}

	/**
	 * Returns the Consent Response Collection entered by the user.
	 * @return
	 */
	public Collection<ConsentResponseBean> getConsentResponseBeanCollection()
	{
		return this.consentResponseBeanCollection;
	}

	/**
	 *
	 * @param consentResponseBeanCollection Consent Response Collection entered by the user
	 */
	public void setConsentResponseBeanCollection(
			Collection<ConsentResponseBean> consentResponseBeanCollection)
	{
		this.consentResponseBeanCollection = consentResponseBeanCollection;
	}

	/**
	 *
	 * @param consentResponseHashTable Consent Response HashTable entered by the user
	 */
	public void setConsentResponseHashTable(Map<String, ConsentResponseBean> consentResponseHashTable)
	{
		this.consentResponseHashTable = consentResponseHashTable;
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
	 * AddNewObjectIdentifier
	 */
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		throw new UnsupportedOperationException("Un-Implemented method");
	}

/*	public void setIsConsentAvailable(String isConsentAvailable)
	{
		this.isConsentAvailable = isConsentAvailable;
	}

	public String getIsConsentAvailable()
	{
		return isConsentAvailable;
	}*/
}

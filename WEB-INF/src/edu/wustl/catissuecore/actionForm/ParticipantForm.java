/**
 * <p>Title: ParticipantForm Class>
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
import java.util.Hashtable;
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
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.DefaultValueManager;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * ParticipantForm Class is used to encapsulate all the request parameters passed 
 * from Participant Add/Edit webpage.
 * @author gautam_shetty
 */

public class ParticipantForm extends AbstractActionForm implements Serializable
{


	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(ParticipantForm.class);
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
	protected String gender = (String) DefaultValueManager.getDefaultValue(Constants.DEFAULT_GENDER);

	/**
	 * The genotype of a participant.
	 */
	protected String genotype = (String) DefaultValueManager.getDefaultValue(Constants.DEFAULT_GENOTYPE);

	/**
	 * The string to determine if barcode field is editable
	 */
	private String isBarcodeEditable = (String) DefaultValueManager.getDefaultValue(Constants.IS_BARCODE_EDITABLE);
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
	protected String[] raceTypes = {(String) DefaultValueManager.getDefaultValue(Constants.DEFAULT_RACE)};

	/**
	 * Participant's ethnicity status.
	 */
	protected String ethnicity = (String) DefaultValueManager.getDefaultValue(Constants.DEFAULT_ETHNICITY);

	/**
	 * The Date of Death of the Participant.
	 */
	protected String deathDate = "";

	/**
	 * Vital status of the Participant.
	 */
	protected String vitalStatus = (String) DefaultValueManager.getDefaultValue(Constants.DEFAULT_VITAL_STATUS);

	/**
	 * Map to handle values of all the Participant Medical Identifiers
	 */
	protected Map values = new LinkedHashMap();

	/**
	 * Abhishek Mehta
	 */
	/**
	 * Map to handle values of registration of a Participant to a Collection Protocol. 
	 */
	protected Map collectionProtocolRegistrationValues = new LinkedHashMap();

	/**
	 * Consent Response Collection for given collection protocols 
	 */
	protected Collection<ConsentResponseBean> consentResponseBeanCollection;

	/**
	 * Consent Response hashtable entered by the user.
	 */
	protected Hashtable consentResponseHashTable;

	/**
	 * Counter that contains number of rows in the 'Add More' functionality for medical identifier.
	 */
	private int valueCounter;

	/**
	 * Counter that contains number of rows in the 'Add More' functionality for registration of a Participant to a Collection Protocol.
	 */
	private int collectionProtocolRegistrationValueCounter;

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
				StringTokenizer tok = new StringTokenizer(ssnString, "-");
				socialSecurityNumberPartA = tok.nextToken();
				socialSecurityNumberPartB = tok.nextToken();
				socialSecurityNumberPartC = tok.nextToken();
			}
			catch (Exception ex)
			{
				logger.debug(ex.getMessage(), ex);
				socialSecurityNumberPartA = "";
				socialSecurityNumberPartB = "";
				socialSecurityNumberPartC = "";
			}
		}
	}

	/**
	 * Copies the data from an AbstractDomain object to a ParticipantForm object.
	 * @param abstractDomain An AbstractDomain object.  
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		Participant participant = (Participant) abstractDomain;
		this.id = participant.getId().longValue();
		this.lastName = Utility.toString(participant.getLastName());
		this.firstName = Utility.toString(participant.getFirstName());
		this.middleName = Utility.toString(participant.getMiddleName());
		this.birthDate = Utility.parseDateToString(participant.getBirthDate(), Variables.dateFormat);
		this.gender = participant.getGender();
		this.genotype = participant.getSexGenotype();
		setSSN(participant.getSocialSecurityNumber());

		Collection raceCollection = participant.getRaceCollection();
		if (raceCollection != null)
		{
			this.raceTypes = new String[raceCollection.size()];
			int i = 0;

			Iterator it = raceCollection.iterator();
			while (it.hasNext())
			{
				Race race = (Race) it.next();
				if (race != null)
				{
					//	String raceName = race.getRaceName();
					this.raceTypes[i] = race.getRaceName();
					i++;
				}
			}
		}

		//        this.race = participant.getRace();
		this.activityStatus = participant.getActivityStatus();
		this.ethnicity = participant.getEthnicity();
		this.deathDate = Utility.parseDateToString(participant.getDeathDate(), Variables.dateFormat);;
		this.vitalStatus = participant.getVitalStatus();

		//Populating the map with the participant medical identifiers data 
		Collection medicalIdentifierCollection = participant.getParticipantMedicalIdentifierCollection();

		if (medicalIdentifierCollection != null)
		{
			values = new LinkedHashMap();
			int i = 1;

			Iterator it = medicalIdentifierCollection.iterator();
			while (it.hasNext())
			{
				ParticipantMedicalIdentifier participantMedicalIdentifier = (ParticipantMedicalIdentifier) it.next();

				String key1 = Utility.getParticipantMedicalIdentifierKeyFor(i, Constants.PARTICIPANT_MEDICAL_IDENTIFIER_SITE_ID); // "ParticipantMedicalIdentifier:" + i +"_Site_id";
				String key2 = Utility.getParticipantMedicalIdentifierKeyFor(i, Constants.PARTICIPANT_MEDICAL_IDENTIFIER_MEDICAL_NUMBER); // "ParticipantMedicalIdentifier:" + i +"_medicalRecordNumber";
				String key3 = Utility.getParticipantMedicalIdentifierKeyFor(i, Constants.PARTICIPANT_MEDICAL_IDENTIFIER_ID); // "ParticipantMedicalIdentifier:" + i +"_id";

				Site site = participantMedicalIdentifier.getSite();

				if (site != null)
				{
					values.put(key1, Utility.toString(site.getId()));
				}
				else
				{
					values.put(key1, Utility.toString(Constants.SELECT_OPTION));
				}

				values.put(key2, Utility.toString(participantMedicalIdentifier.getMedicalRecordNumber()));
				values.put(key3, Utility.toString(participantMedicalIdentifier.getId()));

				i++;
			}
			valueCounter = medicalIdentifierCollection.size();
		}

		//Populating the map with the registrations of a Participant to a Collection Protocol. 
		//(Abhishek Mehta)
		Collection collectionProtocolRegistrationCollection = participant.getCollectionProtocolRegistrationCollection();

		if (collectionProtocolRegistrationCollection != null)
		{
			collectionProtocolRegistrationValues = new LinkedHashMap();
			if (consentResponseHashTable == null)
			{
				consentResponseHashTable = new Hashtable();
			}
			consentResponseBeanCollection = new LinkedHashSet<ConsentResponseBean>();
			int i = 1;

			Iterator it = collectionProtocolRegistrationCollection.iterator();
			while (it.hasNext())
			{
				CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) it.next();
				if (collectionProtocolRegistration.getActivityStatus() != null
						&& !collectionProtocolRegistration.getActivityStatus().equalsIgnoreCase(Constants.DISABLED))
				{
					String collectionProtocolId = "CollectionProtocolRegistration:" + i + "_CollectionProtocol_id";
					String collectionProtocolTitle = "CollectionProtocolRegistration:" + i + "_CollectionProtocol_shortTitle";
					String collectionProtocolParticipantId = "CollectionProtocolRegistration:" + i + "_protocolParticipantIdentifier";
					String barcode = "CollectionProtocolRegistration:" + i + "_barcode";
					String collectionProtocolRegistrationDate = "CollectionProtocolRegistration:" + i + "_registrationDate";
					String collectionProtocolIdentifier = "CollectionProtocolRegistration:" + i + "_id";
					String isConsentAvailable = "CollectionProtocolRegistration:" + i + "_isConsentAvailable";
					String isActive = "CollectionProtocolRegistration:" + i + "_activityStatus";

					Collection consentTierCollection = collectionProtocolRegistration.getCollectionProtocol().getConsentTierCollection();
					if (consentTierCollection != null && consentTierCollection.isEmpty())
					{
						collectionProtocolRegistrationValues.put(isConsentAvailable, Constants.NO_CONSENTS_DEFINED);
					}
					else if (consentTierCollection != null && !consentTierCollection.isEmpty())
					{
						collectionProtocolRegistrationValues.put(isConsentAvailable, Constants.PARTICIPANT_CONSENT_ENTER_RESPONSE);
					}

					String date = Utility.parseDateToString(collectionProtocolRegistration.getRegistrationDate(), Variables.dateFormat);

					collectionProtocolRegistrationValues.put(collectionProtocolId, Utility.toString(collectionProtocolRegistration
							.getCollectionProtocol().getId()));
					collectionProtocolRegistrationValues.put(collectionProtocolTitle, Utility.toString(collectionProtocolRegistration
							.getCollectionProtocol().getShortTitle()));
					collectionProtocolRegistrationValues.put(collectionProtocolParticipantId, Utility.toString(collectionProtocolRegistration
							.getProtocolParticipantIdentifier()));
					collectionProtocolRegistrationValues.put(barcode, Utility.toString(collectionProtocolRegistration.getBarcode()));
					collectionProtocolRegistrationValues.put(collectionProtocolRegistrationDate, date);
					collectionProtocolRegistrationValues.put(collectionProtocolIdentifier, Utility.toString(collectionProtocolRegistration.getId()));
					collectionProtocolRegistrationValues.put(isActive, Utility.toString(collectionProtocolRegistration.getActivityStatus()));

					getConsentResponse(collectionProtocolRegistration);

					i++;
				}
			}
			collectionProtocolRegistrationValueCounter = (i - 1);
		}

		if (valueCounter == 0)
		{
			valueCounter = 1;
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

			long collectionProtocolID = collectionProtocolRegistration.getCollectionProtocol().getId();
			String signedConsentURL = collectionProtocolRegistration.getSignedConsentDocumentURL();
			User consentWitness = collectionProtocolRegistration.getConsentWitness();
			long witnessId = -1;
			if (consentWitness != null)
			{
				witnessId = consentWitness.getId();
			}

			String consentSignatureDate = Utility.parseDateToString(collectionProtocolRegistration.getConsentSignatureDate(), Variables.dateFormat);
			Collection consentResponseCollection = collectionProtocolRegistration.getConsentTierResponseCollection();
			Collection consentResponse;

			if (consentResponseCollection == null || consentResponseCollection.isEmpty())
			{
				consentResponseCollection = collectionProtocolRegistration.getCollectionProtocol().getConsentTierCollection();
				consentResponse = getConsentResponseCollection(consentResponseCollection, false);
			}
			else
			{
				consentResponse = getConsentResponseCollection(consentResponseCollection, true);
			}

			ConsentResponseBean consentResponseBean = new ConsentResponseBean(collectionProtocolID, signedConsentURL, witnessId,
					consentSignatureDate, consentResponse, Constants.WITHDRAW_RESPONSE_NOACTION);

			String consentResponseKey = Constants.CONSENT_RESPONSE_KEY + collectionProtocolID;
			if (consentResponseHashTable.containsKey(consentResponseKey))
			{
				throw new DAOException(ApplicationProperties.getValue("errors.participant.duplicate.collectionProtocol"));
			}
			consentResponseHashTable.put(consentResponseKey, consentResponseBean);
			consentResponseBeanCollection.add(consentResponseBean);
		}
		catch (Exception e)
		{
			logger.debug(e.getMessage(), e);
		}
	}

	/*
	 * Returns the consentBean collection from consent tier response for given protocol collection id
	 * (Abhishek Mehta)
	 */
	private Collection getConsentResponseCollection(Collection consentResponse, boolean isResponseExist)
	{
		Collection<ConsentBean> consentBeanCollection = new HashSet<ConsentBean>();
		if (consentResponse != null)
		{
			Iterator consentResponseIter = consentResponse.iterator();
			while (consentResponseIter.hasNext())
			{
				ConsentBean consentBean = new ConsentBean();
				if (isResponseExist)
				{
					ConsentTierResponse consentTierResponse = (ConsentTierResponse) consentResponseIter.next();
					ConsentTier consentTier = consentTierResponse.getConsentTier();
					consentBean.setConsentTierID(Utility.toString(consentTier.getId()));
					consentBean.setStatement(consentTier.getStatement());
					consentBean.setParticipantResponse(consentTierResponse.getResponse());
					consentBean.setParticipantResponseID(Utility.toString(consentTierResponse.getId()));
				}
				else
				{
					ConsentTier consentTier = (ConsentTier) consentResponseIter.next();
					consentBean.setConsentTierID(Utility.toString(consentTier.getId()));
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
		return lastName;
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
		return firstName;
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
		return middleName;
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
		return birthDate;
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
		return genotype;
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
		return gender;
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
		return raceTypes;
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
		return ethnicity;
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
	public int getFormId()
	{
		return Constants.PARTICIPANT_FORM_ID;
	}

	/**
	 * Resets the values of all the fields.
	 */
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
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		Validator validator = new Validator();
		//Abhishek Mehta 
		// To get the consent response from session.
		HttpSession session = request.getSession();

		try
		{
			setRedirectValue(validator);

			String errorKeyForBirthDate = "";
			String errorKeyForDeathDate = "";

			// Added by Geeta for DFCI
			if(!Variables.isLastNameNull){
				if(validator.isEmpty(lastName)){
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.participant.lastName"));
				   }
			}
			
			if (!validator.isEmpty(birthDate))
			{
				// date validation according to bug id  722 and 730
				errorKeyForBirthDate = validator.validateDate(birthDate, true);
				if (errorKeyForBirthDate.trim().length() > 0)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(errorKeyForBirthDate, ApplicationProperties
							.getValue("participant.birthDate")));
				}
			}

			if (!validator.isEmpty(deathDate))
			{
				errorKeyForDeathDate = validator.validateDate(deathDate, true);
				if (errorKeyForDeathDate.trim().length() > 0)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(errorKeyForDeathDate, ApplicationProperties
							.getValue("participant.deathDate")));
				}
			}

			if ((!validator.isEmpty(birthDate) && !validator.isEmpty(deathDate))
					&& (errorKeyForDeathDate.trim().length() == 0 && errorKeyForBirthDate.trim().length() == 0))
			{
				boolean errorKey1 = validator.compareDates(birthDate, deathDate);

				if (!errorKey1)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("participant.invaliddate", ApplicationProperties
							.getValue("participant.invaliddate")));
				}
			}

			String socialSecurityNumber = socialSecurityNumberPartA + "-" + socialSecurityNumberPartB + "-" + socialSecurityNumberPartC;
			if (!validator.isEmpty(socialSecurityNumberPartA + socialSecurityNumberPartB + socialSecurityNumberPartC)
					&& !validator.isValidSSN(socialSecurityNumber))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.invalid", ApplicationProperties
						.getValue("participant.socialSecurityNumber")));
			}

			//Validation for Blank Participant 
			if (validator.isEmpty(lastName) && validator.isEmpty(firstName) && validator.isEmpty(middleName) && validator.isEmpty(birthDate)
					&& (validator.isEmpty(deathDate)) && !validator.isValidOption(gender) && !validator.isValidOption(vitalStatus)
					&& !validator.isValidOption(genotype) && ethnicity.equals("-1")
					&& validator.isEmpty(socialSecurityNumberPartA + socialSecurityNumberPartB + socialSecurityNumberPartC))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.participant.atLeastOneFieldRequired"));
			}

			//Validations for Add-More Block
			String className = "ParticipantMedicalIdentifier:";
			String key1 = "_Site_" + Constants.SYSTEM_IDENTIFIER;
			String key2 = "_medicalRecordNumber";
			String key3 = "_" + Constants.SYSTEM_IDENTIFIER;
			int index = 1;

			while (true)
			{
				String keyOne = className + index + key1;
				String keyTwo = className + index + key2;
				String keyThree = className + index + key3;

				String value1 = (String) values.get(keyOne);
				String value2 = (String) values.get(keyTwo);

				if (value1 == null || value2 == null)
				{
					break;
				}
				else if (!validator.isValidOption(value1) && value2.trim().equals(""))
				{
					values.remove(keyOne);
					values.remove(keyTwo);
					values.remove(keyThree);
				}
				else if ((validator.isValidOption(value1) && value2.trim().equals(""))
						|| (!validator.isValidOption(value1) && !value2.trim().equals("")))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.participant.missing", ApplicationProperties
							.getValue("participant.msg")));
					break;
				}
				index++;
			}

			//validation for add more block for collection protocol registration
			//(Abhishek Mehta)
			String collectionProtocolClassName = "CollectionProtocolRegistration:";
			String collectionProtocolId = "_CollectionProtocol_id";
			String collectionProtocolParticipantId = "_protocolParticipantIdentifier";
			String collectionProtocolRegistrationDate = "_registrationDate";
			String collectionProtocolIdentifier = "_id";
			String isConsentAvailable = "_isConsentAvailable";
			String isActive = "_activityStatus";
			String collectionProtocolTitle = "_CollectionProtocol_shortTitle";

			index = 1;
			int count = 0;
			while (true)
			{
				String keyOne = collectionProtocolClassName + index + collectionProtocolId;
				String keyTwo = collectionProtocolClassName + index + collectionProtocolParticipantId;
				String keyThree = collectionProtocolClassName + index + collectionProtocolRegistrationDate;
				String keyFour = collectionProtocolClassName + index + collectionProtocolIdentifier;
				String keyFive = collectionProtocolClassName + index + isConsentAvailable;
				String keySix = collectionProtocolClassName + index + isActive;
				String KeySeven = collectionProtocolClassName + index + collectionProtocolTitle;

				String value1 = (String) collectionProtocolRegistrationValues.get(keyOne);
				String value2 = (String) collectionProtocolRegistrationValues.get(keyTwo);
				String value3 = (String) collectionProtocolRegistrationValues.get(keyThree);
				String value6 = (String) collectionProtocolRegistrationValues.get(keySix);

				if (value6 == null)
				{
					value6 = Constants.ACTIVITY_STATUS_ACTIVE;
				}
				if (value6.equalsIgnoreCase(Constants.DISABLED))
				{
					forwardTo = "blankPage";
					request.setAttribute(Constants.PAGEOF, pageOf);
					request.setAttribute("participantId", new Long(id).toString());
					request.setAttribute("cpId", value1);
				}

				String errorKey = validator.validateDate(value3, true);
				if (value1 == null)
				{
					break;
				}
				else if (!validator.isValidOption(value1))
				{
					collectionProtocolRegistrationValues.remove(keyOne);
					collectionProtocolRegistrationValues.remove(keyTwo);
					collectionProtocolRegistrationValues.remove(keyThree);
					collectionProtocolRegistrationValues.remove(keyFour);
					collectionProtocolRegistrationValues.remove(keyFive);
					collectionProtocolRegistrationValues.remove(keySix);
					collectionProtocolRegistrationValues.remove(KeySeven);
					count++;
				}

				else if ((validator.isValidOption(value1) && !validator.isValidOption(value6))
						|| (!validator.isValidOption(value1) && !validator.isValidOption(value6)))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.participant.participantRegistration.missing", ApplicationProperties
							.getValue("participant.msg")));
					break;
				}

				if (errorKey.trim().length() > 0)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(errorKey, ApplicationProperties.getValue("participant.cpr.msg")));
					break;
				}

				index++;
			}

			collectionProtocolRegistrationValueCounter = collectionProtocolRegistrationValueCounter - count;

			//Getting ConsentRegistrationBean from the  session and creating consent response map.
			setConsentResponse(session);

		}
		catch (Exception excp)
		{
			logger.error(excp.getMessage());
		}

		return errors;
	}

	/*
	 * This method will get the consent response from session and creates ConsentResponseBean Collection
	 *  Abhishek Mehta 
	 */
	private void setConsentResponse(HttpSession session)
	{

		Hashtable consentResponsesHashTable = (Hashtable) session.getAttribute(Constants.CONSENT_RESPONSE);
		if (consentResponsesHashTable != null)
		{
			int size = consentResponsesHashTable.size();
			if (this.consentResponseBeanCollection == null)
			{
				this.consentResponseBeanCollection = new LinkedHashSet<ConsentResponseBean>();
			}

			for (int i = 1; i <= size; i++)
			{
				String collectionProtocolID = (String) collectionProtocolRegistrationValues.get("CollectionProtocolRegistration:" + i
						+ "_CollectionProtocol_id");
				String consentResponseKey = Constants.CONSENT_RESPONSE_KEY + collectionProtocolID;
				ConsentResponseBean consentResponseBean = (ConsentResponseBean) consentResponsesHashTable.get(consentResponseKey);

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
		if (isMutable())
		{
			values.put(key, value);
		}
	}

	/**
	 * Returns the object to which this map maps the specified key.
	 * @param key the required key.
	 * @return the object to which this map maps the specified key.
	 */
	public Object getValue(String key)
	{
		return values.get(key);
	}

	/**
	 * @return Returns the values.
	 */
	public Collection getAllValues()
	{
		return values.values();
	}

	/**
	 * @param values The values to set.
	 */
	public void setValues(Map values)
	{
		this.values = values;
	}

	/**
	 * @param values The values to set.
	 */
	public Map getValues()
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
		if (isMutable())
		{
			collectionProtocolRegistrationValues.put(key, value);
		}
	}

	public void setDefaultCollectionProtocolRegistrationValue(String key, Object value)
	{
		collectionProtocolRegistrationValues.put(key, value);
	}

	/**
	 * Returns the object to which this map maps the specified key.
	 * @param key the required key.
	 * @return the object to which this map maps the specified key.
	 */
	public Object getCollectionProtocolRegistrationValue(String key)
	{
		return collectionProtocolRegistrationValues.get(key);
	}

	/**
	 * @return Returns the values.
	 */
	public Collection getAllCollectionProtocolRegistrationValues()
	{
		return collectionProtocolRegistrationValues.values();
	}

	public Map getCollectionProtocolRegistrationValues()
	{
		return collectionProtocolRegistrationValues;
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
		return valueCounter;
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
		return collectionProtocolRegistrationValueCounter;
	}

	/**
	 * Sets the counter.
	 * @param counter The counter.
	 * @see #getCounter()
	 */
	public void setCollectionProtocolRegistrationValueCounter(int collectionProtocolRegistrationValueCounter)
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
		return socialSecurityNumberPartA;
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
		return socialSecurityNumberPartB;
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
		return socialSecurityNumberPartC;
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
		return deathDate;
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
		return vitalStatus;
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
		return cpId;
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
	public Hashtable getConsentResponseHashTable()
	{
		return consentResponseHashTable;
	}

	/**
	 * Returns the Consent Response Collection entered by the user. 
	 * @return
	 */
	public Collection<ConsentResponseBean> getConsentResponseBeanCollection()
	{
		return consentResponseBeanCollection;
	}

	/**
	 * 
	 * @param consentResponseBeanCollection Consent Response Collection entered by the user
	 */
	public void setConsentResponseBeanCollection(Collection<ConsentResponseBean> consentResponseBeanCollection)
	{
		this.consentResponseBeanCollection = consentResponseBeanCollection;
	}

	/**
	 * 
	 * @param consentResponseHashTable Consent Response HashTable entered by the user
	 */
	public void setConsentResponseHashTable(Hashtable consentResponseHashTable)
	{
		this.consentResponseHashTable = consentResponseHashTable;
	}

	/**
	 * @return isBarcodeEditable
	 */
	public String getIsBarcodeEditable()
	{
		return isBarcodeEditable;
	}

	/** 
	 * @param isBarcodeEditable Setter method for isBarcodeEditable
	 */
	public void setIsBarcodeEditable(String isBarcodeEditable)
	{
		this.isBarcodeEditable = isBarcodeEditable;
	}

}
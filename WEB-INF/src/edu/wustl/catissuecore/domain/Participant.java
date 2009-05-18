/**
 * <p>Title: Participant Class>
 * <p>Description:  An individual from whom a specimen is collected. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 7, 2005
 */

package edu.wustl.catissuecore.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

import edu.wustl.catissuecore.actionForm.ParticipantForm;
import edu.wustl.catissuecore.bean.ConsentResponseBean;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
import edu.wustl.catissuecore.util.ConsentUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.bizlogic.IActivityStatus;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.exception.DAOException;

/**
 * An individual from whom a specimen is collected.
 * @hibernate.class table="CATISSUE_PARTICIPANT"
 * @author aniruddha_phadnis
 * @author gautam_shetty
 */
public class Participant extends AbstractDomainObject implements java.io.Serializable, IActivityStatus
{
	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(Participant.class);

	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * System generated unique id.
	 * */
	protected Long id;

	/**
	 * Last name of the participant.
	 */
	protected String lastName;

	/**
	 * First name of the participant.
	 */
	protected String firstName;

	/**
	 * Middle name of the participant.
	 */
	protected String middleName;

	/**
	 * Birth date of participant.
	 */
	protected Date birthDate;

	/**
	 * The gender of the participant.
	 */
	protected String gender;

	/**
	 * The genetic constitution of the individual.
	 */
	protected String sexGenotype;

	//	/**
	//     * Participant's racial origination.
	//     */
	//	protected String race;

	/**
	 * Participant's race origination.
	 */
	protected Collection raceCollection = new HashSet();

	/**
	 * Participant's ethnicity status.
	 */
	protected String ethnicity;

	/**
	 * Social Security Number of participant.
	 */
	protected String socialSecurityNumber;

	/**
	 * Defines whether this participant record can be queried (Active) or not queried (Inactive) by any actor.
	 */
	protected String activityStatus;

	/**
	 * Death date of participant.
	 */
	protected Date deathDate;

	/**
	 * Defines the vital status of the participant like 'Dead', 'Alive' or 'Unknown'.
	 */
	protected String vitalStatus;

	/**
	 * A collection of medical record identification number that refers to a Participant.
	 * */
	protected Collection participantMedicalIdentifierCollection = new LinkedHashSet();// = new HashSet();

	/**
	 * A collection of registration of a Participant to a Collection Protocol.
	 */
	protected Collection collectionProtocolRegistrationCollection = new HashSet();

	/**
	 * HashSet containing clinical Study Registration.
	 */
	protected Collection clinicalStudyRegistrationCollection = new HashSet();

	/**
	* Returns collection registrations of this participant.
	* @return collection of registrations of this participant.
	* @hibernate.set name="clinicalStudyRegistrationCollection" table="CATISSUE_CLINICAL_STUDY_REG"
	* @hibernate.collection-key column="PARTICIPANT_ID" lazy="true"
	* @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.ClinicalStudyRegistration"
	* @see setRegistrationCollection(Collection)
	*/
	public Collection getClinicalStudyRegistrationCollection()
	{
		return clinicalStudyRegistrationCollection;
	}

	/**
	 * Set ClinicalStudyRegistration Collection.
	 * @param clinicalStudyRegistrationCollection Collection.
	 */
	public void setClinicalStudyRegistrationCollection(Collection clinicalStudyRegistrationCollection)
	{
		this.clinicalStudyRegistrationCollection = clinicalStudyRegistrationCollection;
	}

	/**
	 * Default Constructor.
	 */
	public Participant()
	{
		super();
	}

	/**
	 * Parameterized Constructor.
	 * @param form AbstractActionForm.
	 */
	public Participant(AbstractActionForm form)
	{
		super();
		setAllValues(form);
	}

	/**
	 * Copy Constructor.
	 * @param participant Participant object
	 */
	public Participant(Participant participant)
	{
		super();
		this.id = Long.valueOf(participant.getId().longValue());
		this.lastName = participant.getLastName();
		this.firstName = participant.getFirstName();
		this.middleName = participant.getMiddleName();
		this.birthDate = participant.getBirthDate();
		this.gender = participant.getGender();
		this.sexGenotype = participant.getSexGenotype();
		this.ethnicity = participant.getEthnicity();
		this.socialSecurityNumber = participant.getSocialSecurityNumber();
		this.activityStatus = participant.getActivityStatus();
		this.deathDate = participant.getDeathDate();
		this.vitalStatus = participant.getVitalStatus();
		this.collectionProtocolRegistrationCollection = null;
		this.clinicalStudyRegistrationCollection =null;
		this.collectionProtocolRegistrationCollection = null;
		this.clinicalStudyRegistrationCollection =null;
		Collection<Race> raceCollection = new ArrayList<Race>();
		Iterator<Race> raceItr = participant.getRaceCollection().iterator();
		while (raceItr.hasNext())
		{
			Race race = new Race(raceItr.next());
			race.setParticipant(this);
			raceCollection.add(race);
		}
		this.raceCollection = raceCollection;

		Collection<ParticipantMedicalIdentifier> pmiCollection =
			new ArrayList<ParticipantMedicalIdentifier>();
		if (participant.getParticipantMedicalIdentifierCollection() != null)
		{
			Iterator<ParticipantMedicalIdentifier> pmiItr =
				participant.getParticipantMedicalIdentifierCollection().iterator();
			while (pmiItr.hasNext())
			{
				ParticipantMedicalIdentifier pmi = new ParticipantMedicalIdentifier(pmiItr.next());
				pmi.setParticipant(this);
				pmiCollection.add(pmi);
			}
			this.participantMedicalIdentifierCollection = pmiCollection;
		}
	}

	/**
	 * Returns System generated unique id.
	 * @return Long System generated unique id.
	 * @see #setId(Long)
	 * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_PARTICIPANT_SEQ"
	 */
	@Override
	public Long getId()
	{
		return id;
	}

	/**
	 * Sets system generated unique id.
	 * @param identifier System generated unique id.
	 * @see #getId()
	 * */
	@Override
	public void setId(Long identifier)
	{
		this.id = identifier;
	}

	/**
	 * Returns the last name of the Participant.
	 * @return String representing the last name of the Participant.
	 * @see #setLastName(String)
	 * @hibernate.property name="lastName" type="string"
	 * column="LAST_NAME" length="255"
	 */
	public String getLastName()
	{
		return lastName;
	}

	/**
	 * Sets the last name of the Participant.
	 * @param lastName Last Name of the Participant.
	 * @see #getLastName()
	 */
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	/**
	 * Returns the first name of the Participant.
	 * @return String representing the first name of the Participant.
	 * @see #setFirstName(String)
	 * @hibernate.property name="firstName" type="string"
	 * column="FIRST_NAME" length="255"
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
	 * @return String representing the middle name of the Participant.
	 * @see #setMiddleName(String)
	 * @hibernate.property name="middleName" type="string"
	 * column="MIDDLE_NAME" length="255"
	 */
	public String getMiddleName()
	{
		return middleName;
	}

	/**
	 * Sets the middle name of the Participant.
	 * @param middleName String representing the middle name of the Participant.
	 * @see #getMiddleName()
	 */
	public void setMiddleName(String middleName)
	{
		this.middleName = middleName;
	}

	/**
	 * Returns the date of birth of the Participant.
	 * @return String representing the middle name of the Participant.
	 * @see #setBirthDate(String)
	 * @hibernate.property name="birthDate" column="BIRTH_DATE" type="date"
	 */
	public Date getBirthDate()
	{
		return birthDate;
	}

	/**
	 * Sets the date of birth of the Participant.
	 * @param birthDate String representing the date of birth of the Participant.
	 * @see #getDateOfBirth()
	 */
	public void setBirthDate(Date birthDate)
	{
		this.birthDate = birthDate;
	}

	/**
	 * Returns the gender of a participant.
	 * @return String representing the gender of a participant.
	 * @see #setGender(String)
	 * @hibernate.property name="gender" type="string"
	 * column="GENDER" length="20"
	 */
	public String getGender()
	{
		return gender;
	}

	/**
	 * Sets the gender of a participant.
	 * @param gender the gender of a participant.
	 * @see #getGender()
	 */
	public void setGender(String gender)
	{
		this.gender = gender;
	}

	/**
	 * Returns the genotype of a participant.
	 * @return String representing the genotype of a participant.
	 * @see #setSexGenotype(String)
	 * @hibernate.property name="sexGenotype" type="string"
	 * column="GENOTYPE" length="50"
	 */
	public String getSexGenotype()
	{
		return sexGenotype;
	}

	/**
	 * Sets the genotype of a participant.
	 * @param sexGenotype the genotype of a participant.
	 * @see #getSexGenotype()
	 */
	public void setSexGenotype(String sexGenotype)
	{
		this.sexGenotype = sexGenotype;
	}

	//	/**
	//     * Returns the race of the Participant.
	//     * @return String representing the race of the Participant.
	//     * @see #setRace(String)
	//     * @hibernate.property name="race" type="string"
	//     * column="RACE" length="50"
	//     */
	//	public String getRace()
	//	{
	//		return race;
	//	}
	//
	//	/**
	//     * Sets the race of the Participant.
	//     * @param race String representing the race of the Participant.
	//     * @see #getRace()
	//     */
	//	public void setRace(String race)
	//	{
	//		this.race = race;
	//	}

	/**
	 * @return Returns the raceCollection.
	 * @hibernate.set name="raceCollection" table="CATISSUE_RACE"
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="PARTICIPANT_ID"
	 * @hibernate.element type="string" column="NAME" length="30"
	 */
	public Collection getRaceCollection()
	{
		return raceCollection;
	}

	/**
	 * @param raceCollection The raceCollection to set.
	 */
	public void setRaceCollection(Collection raceCollection)
	{
		this.raceCollection = raceCollection;
	}

	/**
	 * Returns the ethnicity of the Participant.
	 * @return Ethnicity of the Participant.
	 * @see #setEthnicity(String)
	 * @hibernate.property name="ethnicity" type="string"
	 * column="ETHNICITY" length="50"
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
	 * Returns the Social Security Number of the Participant.
	 * @return String representing the Social Security Number of the Participant.
	 * @see #setSocialSecurityNumber(String)
	 * @hibernate.property name="socialSecurityNumber" type="string"
	 * column="SOCIAL_SECURITY_NUMBER" length="50" unique="true"
	 */
	public String getSocialSecurityNumber()
	{
		return socialSecurityNumber;
	}

	/**
	 * Sets the Social Security Number of the Participant.
	 * @param socialSecurityNumber - String representing the Social Security Number of the Participant.
	 * @see #getSocialSecurityNumber()
	 */
	public void setSocialSecurityNumber(String socialSecurityNumber)
	{
		this.socialSecurityNumber = socialSecurityNumber;
	}

	/**
	 * Returns the activity status of the participant.
	 * @hibernate.property name="activityStatus" type="string"
	 * column="ACTIVITY_STATUS" length="50"
	 * @return Returns the activity status of the participant.
	 * @see #setActivityStatus(String)
	 */
	public String getActivityStatus()
	{
		return activityStatus;
	}

	/**
	 * Sets the activity status of the participant.
	 * @param activityStatus activity status of the participant.
	 * @see #getActivityStatus()
	 */
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}

	/**
	 * Returns the date of death of the Participant.
	 * @return Date representing the death date of the Participant.
	 * @see #setDeathDate(Date)
	 * @hibernate.property name="deathDate" column="DEATH_DATE" type="date"
	 */
	public Date getDeathDate()
	{
		return deathDate;
	}

	/**
	 * Sets the date of birth of the Participant.
	 * @param deathDate The deathDate to set.
	 */
	public void setDeathDate(Date deathDate)
	{
		this.deathDate = deathDate;
	}

	/**
	 * Returns the vital status of the participant.
	 * @return Returns the vital status of the participant.
	 * @see #setVitalStatus(String)
	 * @hibernate.property name="vitalStatus" type="string"
	 * column="VITAL_STATUS" length="50"
	 */
	public String getVitalStatus()
	{
		return vitalStatus;
	}

	/**
	 * Sets the vital status of the Participant.
	 * @param vitalStatus The vitalStatus to set.
	 */
	public void setVitalStatus(String vitalStatus)
	{
		this.vitalStatus = vitalStatus;
	}

	/**
	 * Returns collection of medical identifiers associated with this participant.
	 * @return collection of medical identifiers of this participant.
	 * @hibernate.set name="participantMedicalIdentifierCollection" table="CATISSUE_PART_MEDICAL_ID"
	 * cascade="none" inverse="true" lazy="false"
	 * @hibernate.collection-key column="PARTICIPANT_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier"
	 * @see setParticipantMedicalIdentifierCollection(Collection)
	 */
	public Collection getParticipantMedicalIdentifierCollection()
	{
		return participantMedicalIdentifierCollection;
	}

	/**
	 * Sets the collection of medical identifiers of this participant.
	 * @param participantMedicalIdentifierCollection collection of medical identifiers of this participant.
	 * @see #getParticipantMedicalIdentifierCollection()
	 */
	public void setParticipantMedicalIdentifierCollection(Collection participantMedicalIdentifierCollection)
	{
		this.participantMedicalIdentifierCollection = participantMedicalIdentifierCollection;
	}

	/**
	 * Returns collection of collection protocol registrations of this participant.
	 * @return collection of collection protocol registrations of this participant.
	 * @hibernate.set name="collectionProtocolRegistrationCollection" table="CATISSUE_COLL_PROT_REG"
	 * @hibernate.collection-key column="PARTICIPANT_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.CollectionProtocolRegistration"
	 * @see setCollectionProtocolRegistrationCollection(Collection)
	 */
	public Collection getCollectionProtocolRegistrationCollection()
	{
		return collectionProtocolRegistrationCollection;
	}

	/**
	 * Sets the collection protocol registrations of this participant.
	 * @param collectionProtocolRegistrationCollection - Collection of collection
	 * protocol registrations of this participant.
	 * @see #getCollectionProtocolRegistrationCollection()
	 */
	public void setCollectionProtocolRegistrationCollection(Collection
			collectionProtocolRegistrationCollection)
	{
		this.collectionProtocolRegistrationCollection = collectionProtocolRegistrationCollection;
	}

	/**
	 * This function Copies the data from a StorageTypeForm object to a StorageType object.
	 * @param abstractForm - A StorageTypeForm object containing the information about the StorageType.
	 * */
	@Override
	public void setAllValues(IValueObject abstractForm)
	{
		String nullString = null;
		try
		{
			ParticipantForm form = (ParticipantForm) abstractForm;
			Validator validator = new Validator();

			this.activityStatus = form.getActivityStatus();
			this.firstName = form.getFirstName();
			this.middleName = form.getMiddleName();
			this.lastName = form.getLastName();

			if (validator.isValidOption(form.getGender()))
			{
				this.gender = form.getGender();
			}
			else
			{
				this.gender = nullString;
			}

			if (validator.isValidOption(form.getGenotype()))
			{
				this.sexGenotype = form.getGenotype();
			}
			else
			{
				this.sexGenotype = nullString;
			}

			if (validator.isValidOption(form.getEthnicity()))
			{
				this.ethnicity = form.getEthnicity();
			}
			else
			{
				this.ethnicity = nullString;
			}

			//	        if(validator.isValidOption(form.getRace()) )
			//	        	this.race = form.getRace();
			//	        else
			//	        	this.race = null;
			raceCollection.clear();
			String[] raceTypes = form.getRaceTypes();
			if (raceTypes != null)
			{
				for (int i = 0; i < raceTypes.length; i++)
				{
					if (!raceTypes[i].equals("-1"))
					{
						Race race = new Race();
						race.setRaceName(raceTypes[i]);
						race.setParticipant(this);
						raceCollection.add(race);
					}

				}
			}

			String socialSecurityNumberTemp = form.getSocialSecurityNumberPartA() +
			"-" + form.getSocialSecurityNumberPartB() + "-"	+ form.getSocialSecurityNumberPartC();

			if (!validator.isEmpty(socialSecurityNumberTemp) &&
					validator.isValidSSN(socialSecurityNumberTemp))
			{
				this.socialSecurityNumber = socialSecurityNumberTemp;
			}
			else
			{
				this.socialSecurityNumber = nullString;
			}

			this.birthDate = Utility.parseDate(form.getBirthDate(),
					Utility.datePattern(form.getBirthDate()));

			this.deathDate = Utility.parseDate(form.getDeathDate(),
					Utility.datePattern(form.getDeathDate()));

			if (validator.isValidOption(form.getVitalStatus()))
			{
				this.vitalStatus = form.getVitalStatus();
			}
			else
			{
				this.vitalStatus = nullString;
			}

			this.participantMedicalIdentifierCollection.clear();
			Map map = form.getValues();
			logger.debug("Map " + map);
			MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.domain");
			this.participantMedicalIdentifierCollection = parser.generateData(map);

			//Collection Protocol Registration of the participant
			//(Abhishek Mehta)
			this.collectionProtocolRegistrationCollection.clear();
			Map mapCollectionProtocolRegistrationCollection =
				form.getCollectionProtocolRegistrationValues();
			logger.debug("Map " + map);
			MapDataParser parserCollectionProtocolRegistrationCollection =
				new MapDataParser("edu.wustl.catissuecore.domain");
			this.collectionProtocolRegistrationCollection =
				parserCollectionProtocolRegistrationCollection
					.generateData(mapCollectionProtocolRegistrationCollection);
			logger.debug("ParticipantMedicalIdentifierCollection " +
					participantMedicalIdentifierCollection);

			setConsentsResponseToCollectionProtocolRegistration(form);
		}
		catch (Exception excp)
		{
			// use of logger as per bug 79
			logger.error(excp.getMessage(), excp);
		}
	}

	/**
	 * Setting Consent Response for the collection protocol.
	 * @param form ParticipantForm.
	 */
	private void setConsentsResponseToCollectionProtocolRegistration(ParticipantForm form) throws Exception
	{
			logger.debug(":: participant id  :"+form.getId());
			Collection<ConsentResponseBean> consentResponseBeanCollection =
				form.getConsentResponseBeanCollection();
			Iterator itr = this.collectionProtocolRegistrationCollection.iterator();
			while (itr.hasNext())
			{
				CollectionProtocolRegistration collectionProtocolRegistration =
					(CollectionProtocolRegistration) itr.next();
				setConsentResponse(collectionProtocolRegistration, consentResponseBeanCollection);
			}
	}

	/**
	 * Set Consent Response for given collection protocol.
	 * @param collectionProtocolRegistration CollectionProtocolRegistration.
	 * @param consentResponseBeanCollection Collection.
	 */
	private void setConsentResponse(CollectionProtocolRegistration collectionProtocolRegistration, Collection consentResponseBeanCollection) throws Exception
	{
		if(consentResponseBeanCollection!= null && !consentResponseBeanCollection.isEmpty())
		{
			Iterator itr = consentResponseBeanCollection.iterator();
			while(itr.hasNext())
	        {
				ConsentResponseBean consentResponseBean = (ConsentResponseBean)itr.next();
				long cpIDcollectionProtocolRegistration = collectionProtocolRegistration.getCollectionProtocol().getId().longValue();
				long cpIDconsentRegistrationBean =  consentResponseBean.getCollectionProtocolID();
				if(cpIDcollectionProtocolRegistration == cpIDconsentRegistrationBean){
					
					logger.debug(":: collection protocol id :"+cpIDcollectionProtocolRegistration);
					logger.debug(":: collection protocol Registration id  :"+collectionProtocolRegistration.getId());
					
					String signedConsentUrl = consentResponseBean.getSignedConsentUrl();
					long witnessId = consentResponseBean.getWitnessId();
					String consentDate = consentResponseBean.getConsentDate();
					Collection consentTierResponseCollection = prepareConsentTierResponseCollection(consentResponseBean.getConsentResponse(), true);
					 
					collectionProtocolRegistration.setSignedConsentDocumentURL(signedConsentUrl);
					if(witnessId>0)
					{
						User consentWitness = new User();
						consentWitness.setId(new Long(witnessId));
						collectionProtocolRegistration.setConsentWitness(consentWitness);
					}
					
					collectionProtocolRegistration.setConsentSignatureDate(Utility.parseDate(consentDate));
					collectionProtocolRegistration.setConsentTierResponseCollection(consentTierResponseCollection);
					collectionProtocolRegistration.setConsentWithdrawalOption(consentResponseBean.getConsentWithdrawalOption());
					break;
				}
	        }
		}
		else // Setting default response to collection protocol
		{
			if(collectionProtocolRegistration.getCollectionProtocol()!=null)
			{
			String cpIDcollectionProtocolRegistration = collectionProtocolRegistration.getCollectionProtocol().getId().toString();
			Collection consentTierCollection = getConsentList(cpIDcollectionProtocolRegistration);
			
			Collection consentTierResponseCollection = prepareConsentTierResponseCollection(consentTierCollection,false);
			collectionProtocolRegistration.setConsentTierResponseCollection(consentTierResponseCollection);
			}
		}

	}

	/**
	 * Preparing consent response collection from entered response.
	 * @param consentResponse Collection.
	 * @param isResponse boolean.
	 * @return Collection.
	 */
	private Collection prepareConsentTierResponseCollection(Collection consentResponse, boolean isResponse)
	{
		Collection consentTierResponseCollection = new HashSet();
		if (consentResponse != null && !consentResponse.isEmpty())
		{
			if (isResponse)
			{
				Iterator iter = consentResponse.iterator();
				ConsentUtil.createConsentResponseColl(consentTierResponseCollection, iter);
			}
			else
			{
				Iterator iter = consentResponse.iterator();
				while (iter.hasNext())
				{
					ConsentTier consentTier = (ConsentTier) iter.next();
					ConsentTierResponse consentTierResponse = new ConsentTierResponse();
					consentTierResponse.setResponse(Constants.NOT_SPECIFIED);
					consentTierResponse.setConsentTier(consentTier);
					consentTierResponseCollection.add(consentTierResponse);
				}
			}
		}
		return consentTierResponseCollection;
	}

	/**
	 * Consent List for given collection protocol.
	 * @param collectionProtocolID String.
	 * @return Collection.
	 * @throws BizLogicException 
	 * @throws NumberFormatException 
	 * @throws DAOException DAOException.
	 */
	private Collection getConsentList(String collectionProtocolID) throws NumberFormatException, BizLogicException
	{
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		CollectionProtocolBizLogic collectionProtocolBizLogic =
			(CollectionProtocolBizLogic) factory.getBizLogic(
				Constants.COLLECTION_PROTOCOL_FORM_ID);
		Collection consentTierCollection = (Collection) collectionProtocolBizLogic.
			retrieveAttribute(CollectionProtocol.class.getName(), Long
				.valueOf(collectionProtocolID), "elements(consentTierCollection)");
		return consentTierCollection;
	}

	/**
	 * Returns message label to display on success add or edit.
	 * @return String.
	 */
	public String getMessageLabel()
	{
		return AppUtility.getlLabel(this.lastName, this.firstName);
	}
}
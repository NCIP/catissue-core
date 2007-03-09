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

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import edu.wustl.catissuecore.actionForm.ParticipantForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * An individual from whom a specimen is collected.
 * @hibernate.class table="CATISSUE_PARTICIPANT"
 * @author aniruddha_phadnis
 * @author gautam_shetty
 */
public class Participant extends AbstractDomainObject implements java.io.Serializable
{
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
	 * Defines whether this participant record can be queried (Active) or not queried (Inactive) by any actor
	 * */
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
	 * Defines the Marital status of the participant like 'Single', 'Married' or 'Unknown'.
	 */
	protected String maritalStatus;
	
	/**
     * A collection of medical record identification number that refers to a Participant. 
     * */
	protected Collection participantMedicalIdentifierCollection;// = new HashSet();
	
	/**
     * A collection of registration of a Participant to a Collection Protocol. 
     * */
	protected Collection collectionProtocolRegistrationCollection = new HashSet();
	
	//Default Constructor
	public Participant()
	{		
	}
	
	public Participant(AbstractActionForm form)
	{
		setAllValues(form);
	}
	
	
	public Participant(Long id2, String lastName2, String firstName2, String middleName2, Date birthDate2, String gender2, String sexGenotype2, Collection raceCollection2, String ethnicity2, String socialSecurityNumber2, String activityStatus2, Date deathDate2, String vitalStatus2)
	{
		this.id = id2;
		this.lastName = lastName2;
		this.firstName = firstName2;
		this.middleName = middleName2;
		this.birthDate = birthDate2;
		this.gender = gender2;
		this.sexGenotype = sexGenotype2;
		this.raceCollection = raceCollection2;
		this.ethnicity = ethnicity2;
		this.socialSecurityNumber = socialSecurityNumber2;
		this.activityStatus = activityStatus2;
		this.deathDate = deathDate2;
		this.vitalStatus = vitalStatus2;
		
	}

	/**
     * Returns System generated unique id.
     * @return Long System generated unique id.
     * @see #setId(Long)
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native" 
     * @hibernate.generator-param name="sequence" value="CATISSUE_PARTICIPANT_SEQ"
     */
	public Long getId()
	{
		return id;
	}

	/**
     * Sets system generated unique id.
     * @param id System generated unique id.
     * @see #getId()
     * */
	public void setId(Long id)
	{
		this.id = id;
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
     * @param birthDate String representing the Social Security Number of the Participant.
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
	public void setDeathDate(Date deathDate) {
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
	 * @param protocolRegistrationCollection collection of collection protocol registrations of this participant.
	 * @see #getCollectionProtocolRegistrationCollection()
	 */
	public void setCollectionProtocolRegistrationCollection(Collection collectionProtocolRegistrationCollection)
	{
		this.collectionProtocolRegistrationCollection = collectionProtocolRegistrationCollection;
	}
	
	/**
	 * This function Copies the data from a StorageTypeForm object to a StorageType object.
	 * @param storageTypeForm A StorageTypeForm object containing the information about the StorageType.  
	 * */
	public void setAllValues(AbstractActionForm abstractForm)
	{
	    try
	    {
	        ParticipantForm form = (ParticipantForm) abstractForm;
	        Validator validator = new Validator();
	        
	        this.activityStatus = form.getActivityStatus();
	        this.firstName = form.getFirstName();
	        this.middleName = form.getMiddleName();
	        this.lastName = form.getLastName();

	        if(validator.isValidOption(form.getGender()))
	        	this.gender = form.getGender();
	        else
	        	this.gender = null;
	        
	        if(validator.isValidOption(form.getGenotype()) )
	        	this.sexGenotype = form.getGenotype();
	        else
	        	this.sexGenotype = null;

	        if(validator.isValidOption(form.getEthnicity()) )
	        	this.ethnicity = form.getEthnicity();
	       	else
	       		this.ethnicity = null;
	        
//	        if(validator.isValidOption(form.getRace()) )
//	        	this.race = form.getRace();
//	        else
//	        	this.race = null;
	        raceCollection.clear();
        	String [] raceTypes = form.getRaceTypes();
        	if(raceTypes!=null)
        	{
	        	for (int i = 0; i < raceTypes.length; i++)
				{
	        		if(!raceTypes[i].equals("-1"))
	        			raceCollection.add(raceTypes[i]);
	        		
				}
        	}
	        
	        String socialSecurityNumberTemp = form.getSocialSecurityNumberPartA()+"-"+form.getSocialSecurityNumberPartB()+"-"+form.getSocialSecurityNumberPartC();
	        
	        if(!validator.isEmpty(socialSecurityNumberTemp) && validator.isValidSSN(socialSecurityNumberTemp))
	        	this.socialSecurityNumber = socialSecurityNumberTemp;
	        else
	        	this.socialSecurityNumber = null;
	        
	        this.birthDate = Utility.parseDate(form.getBirthDate(),Utility.datePattern(form.getBirthDate()));
	        
	        this.deathDate = Utility.parseDate(form.getDeathDate(),Utility.datePattern(form.getDeathDate()));
	        
	        if(validator.isValidOption(form.getVitalStatus()) )
	        	this.vitalStatus = form.getVitalStatus();
	        else
	        	this.vitalStatus = null;
	        
	        Map map = form.getValues();
	        Logger.out.debug("Map "+map);
	        MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.domain");
	        participantMedicalIdentifierCollection = parser.generateData(map);
	        Logger.out.debug("ParticipantMedicalIdentifierCollection "+participantMedicalIdentifierCollection);
	    }
	    catch(Exception excp)
	    {
	    	// use of logger as per bug 79
	    	Logger.out.error(excp.getMessage(),excp); 
	    }
	}
	
	 /**
     * Returns message label to display on success add or edit
     * @return String
     */
	public String getMessageLabel() {		
		
		if (this.lastName!= null && !this.lastName.equals("") && this.firstName != null && !this.firstName.equals("")) 
		{
			return this.lastName + "," + this.firstName;
		} 
		else if(this.lastName!= null && !this.lastName.equals(""))
		{
			return this.lastName;
		}
		else if(this.firstName!= null && !this.firstName.equals(""))
		{
			return this.firstName;
		}		
		return null; 
	}
	/**
     * Returns the Marital Status of the Participant.
     * @return String representing the Marital Status of the Participant.
     * @see #setMaritalStatus(String)
     * @hibernate.property name="maritalStatus" type="string"
     * column="MARITAL_STATUS" length="50"
     */
	public String getMaritalStatus() {
		return maritalStatus;
	}
	/**
     * Sets the Marital Status of a participant.
     * @param maritalStatus the gender of a participant.
     * @see #getMaritalStatus()
     */
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
}
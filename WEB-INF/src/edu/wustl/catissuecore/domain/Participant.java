/**
 * <p>Title: Participant Class>
 * <p>Description:  An individual from whom a specimen is collected. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 7, 2005
 */
package edu.wustl.catissuecore.domain;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

/**
 * An individual from whom a specimen is collected.
 * @hibernate.class table="CATISSUE_PARTICIPANT"
 * @author gautam_shetty
 */
public class Participant implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;
	
	/**
     * System generated unique identifier.
     * */
	protected Long systemIdentifier;
	
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
     * The gender of a participant as defined by their genotype.
     */
	protected String genotypicGender;
	
	/**
     * Participant's racial origination.
     */
	protected String race;
	
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
     * A collection of medical record identification number that refers to a Participant. 
     * */
	protected Collection participantMedicalIdentifierCollection = new HashSet();
	
	/**
     * A collection of registration of a Participant to a Collection Protocol. 
     * */
	protected Collection collectionProtocolRegistrationCollection = new HashSet();
	
	
	public Participant()
	{
		
	}
	
	/**
     * Returns System generated unique identifier.
     * @return Long System generated unique identifier.
     * @see #setSystemIdentifier(Long)
     * @hibernate.id name="systemIdentifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native" 
     */
	public Long getSystemIdentifier()
	{
		return systemIdentifier;
	}

	/**
     * Sets system generated unique identifier.
     * @param systemIdentifier System generated unique identifier.
     * @see #getSystemIdentifier()
     * */
	public void setSystemIdentifier(Long systemIdentifier)
	{
		this.systemIdentifier = systemIdentifier;
	}

	/**
     * Returns the last name of the Participant. 
     * @return String representing the last name of the Participant.
     * @see #setLastName(String)
     * @hibernate.property name="lastName" type="string" 
     * column="LAST_NAME" length="50"
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
     * column="FIRST_NAME" length="50"
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
     * column="MIDDLE_NAME" length="50"
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
     * Returns the gender of a participant as defined by their genotype.
     * @return String representing the gender of a participant as defined by their genotype.
     * @see #setGenotypicGender(String)
     * @hibernate.property name="genotypicGender" type="string" 
     * column="GENDER" length="20"
     */
	public String getGenotypicGender()
	{
		return genotypicGender;
	}

	/**
     * Sets the gender of a participant as defined by their genotype.
     * @param genotypicGender the gender of a participant as defined by their genotype.
     * @see #getGenotypicGender()
     */
	public void setGenotypicGender(String genotypicGender)
	{
		this.genotypicGender = genotypicGender;
	}

	/**
     * Returns the race of the Participant.
     * @return String representing the race of the Participant.
     * @see #setRace(String)
     * @hibernate.property name="race" type="string" 
     * column="RACE" length="50"
     */
	public String getRace()
	{
		return race;
	}

	/**
     * Sets the race of the Participant.
     * @param race String representing the race of the Participant.
     * @see #getRace()
     */
	public void setRace(String race)
	{
		this.race = race;
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
     * @param dateOfBirth String representing the Social Security Number of the Participant.
     * @see #getSocialSecurityNumber()
     */
	public void setSocialSecurityNumber(String socialSecurityNumber)
	{
		this.socialSecurityNumber = socialSecurityNumber;
	}

	/**
	 * Returns the activity status of the participant.
	 * @hibernate.property name="activityStatus" type="string"
	 * column="ACTIVITY_STATUS" length="20"
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
	 * Returns collection of medical identifiers associated with this participant.
	 * @return collection of medical identifiers of this participant.
	 * @hibernate.set name="participantMedicalIdentifierCollection" table="CATISSUE_PARTICIPANT_MEDICAL_IDENTIFIER"
	 * cascade="save-update" inverse="true" lazy="false"
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
	 * @hibernate.set name="collectionProtocolRegistrationCollection" table="CATISSUE_COLLECTION_PROTOCOL_REGISTRATION"
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
}
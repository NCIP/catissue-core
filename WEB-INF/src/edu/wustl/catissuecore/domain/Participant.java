/**
 * <p>Title: Participant Class>
 * <p>Description:  Models the Participant information. </p>
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

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.actionForm.ParticipantForm;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.util.logger.Logger;


/**
 * Models the Participant information.
 * @hibernate.class table="CATISSUE_PARTICIPANT"
 * @author gautam_shetty
 */
public class Participant extends AbstractDomainObject
{
    /**
     * identifier is a unique id assigned to each Participant.
     * */
    protected Long identifier;

    /**
     * A string containing the Last Name of the Participant.
     */
    protected String lastName = "";

    /**
     * A string containing the First Name of the Participant.
     */
    protected String firstName = "";

    /**
     * A string containing the Middle Name of the Participant.
     */
    protected String middleName = "";

    /**
     * A string containing the gender of the Participant.
     */
    protected String gender = "";

    /**
     * A string containing the Social Security Number of the Participant.
     */
    protected String socialSecurityNumber = "";

    /**
     * A string containing the Unique Medical Record Number of the Participant.
     */
    protected String uniqueMedicalRecordNumber = "";

    /**
     * A string containing the Date of Birth of the Participant.
     */
    protected Date dateOfBirth = new Date();

    /**
     * A string containing the race of the Participant.
     */
    protected String race = "";

    /**
	 * Activity Status of user, it could be CLOSED, ACTIVE, DISABLED
	 * */
	protected ActivityStatus activityStatus;
    
    /**
     * Collection of accessions for perform on this participant. 
     * */
    private Collection accessionCollection = new HashSet();
    
    /**
	 * Initialize a new Participant instance. 
	 * Note: Hibernate invokes this constructor through reflection API.  
	 */
    public Participant()
    {
        
    }
    /**
	 * This Constructor Copies the data from an ParticipantForm object to a Participant object.
	 * @param participantForm An ParticipantForm object containing the information about the Participant.  
	 */
    public Participant(ParticipantForm participantForm)
    {
        setAllValues(participantForm);
    }
    
    /**
     * Returns the identifier assigned to Participant.
     * @return Long representing the id assigned to User.
     * @see #setIdentifier(Long)
     * @hibernate.id name="identifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * */
    public Long getIdentifier()
    {
        return identifier;
    }
    
    /**
     * Sets an id for the Participant.
     * @param identifier id to be assigned to the Participant.
     * @see #getIdentifier()
     * */
    public void setIdentifier(Long identifier)
    {
        this.identifier = identifier;
    }
    
    /**
     * Returns the last name of the Participant. 
     * @return String representing the last name of the Participant.
     * @see #setFirstName(String)
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
     * @see #getFirstName()
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
     * @see #setDateOfBirth(String)
     * @hibernate.property name="dateOfBirth" column="DATE_OF_BIRTH" type="date"
     */
    public Date getDateOfBirth()
    {
        return dateOfBirth;
    }
    
    /**
     * Sets the date of birth of the Participant.
     * @param dateOfBirth String representing the date of birth of the Participant.
     * @see #getDateOfBirth()
     */
    public void setDateOfBirth(Date dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Returns the gender of the Participant.
     * @return String representing the gender of the Participant.
     * @see #setGender(String)
     * @hibernate.property name="gender" type="string" 
     * column="GENDER" length="20"
     */
    public String getGender()
    {
        return gender;
    }

    /**
     * Sets the gender of the Participant.
     * @param dateOfBirth String representing the gender of the Participant.
     * @see #getGender()
     */
    public void setGender(String gender)
    {
        this.gender = gender;
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
     * Returns the Unique Medical Record Number of the Participant.
     * @return String representing the Unique Medical Record Number of the Participant.
     * @see #setUniqueMedicalRecordNumber(String)
	 * @hibernate.property name="uniqueMedicalRecordNumber" type="string"
     * column="UNIQUE_MEDICAL_RECORD_NUMBER" length="50" unique="true" 
     */
    public String getUniqueMedicalRecordNumber()
    {
        return uniqueMedicalRecordNumber;
    }

    /**
     * Sets the Unique Medical Record Number of the Participant.
     * @param dateOfBirth String representing the Unique Medical Record Number of the Participant.
     * @see #getUniqueMedicalRecordNumber()
     */
    public void setUniqueMedicalRecordNumber(String uniqueMedicalRecordNumber)
    {
        this.uniqueMedicalRecordNumber = uniqueMedicalRecordNumber;
    }

    /**
     * Returns the race of the Participant.
     * @return String representing the race of the Participant.
     * @see #setRace(String)
     * @hibernate.property name="race" type="string" 
     * column="RACE" length="20"
     */
    public String getRace()
    {
        return race;
    }

    /**
     * Sets the race of the Participant.
     * @param dateOfBirth String representing the race of the Participant.
     * @see #getRace()
     */
    public void setRace(String race)
    {
        this.race = race;
    }

    /**
	 * Returns the activity status of the participant.
	 * @hibernate.many-to-one column="ACTIVITY_STATUS_ID" 
	 * class="edu.wustl.catissuecore.domain.ActivityStatus" constrained="true"
	 * @return Returns the activity status of the participant.
	 * @see #setActivityStatus(ActivityStatus)
	 */
	public ActivityStatus getActivityStatus()
	{
		return activityStatus;
	}
	/**
	 * Sets the activity status of the participant.
	 * @param activityStatus activity status of the participant.
	 * @see #getActivityStatus()
	 */
	public void setActivityStatus(ActivityStatus activityStatus)
	{
		this.activityStatus = activityStatus;
	}
	
	/**
	 * Returns collection of accessions performed on this participant.
	 * @return Returns collection of accessions performed on this participant.
	 * @hibernate.set name="accessionCollection" table="CATISSUE_ACCESSION" cascade="save-update" inverse="true" lazy="false"
	 * @hibernate.collection-key column="PARTICIPANT_IDENTIFIER"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.Accession"
	 * @see #setAccessionCollection(Collection)
	 */
	public Collection getAccessionCollection()	
	{
		return accessionCollection;
	}
	
	/**
	 * Sets the collection of accessions performed on this participant.
	 * @param accessionCollection collection of accessions performed on this participant.
	 * @see #getAccessionCollection()
	 */
	public void setAccessionCollection(Collection accessionCollection)
	{
		this.accessionCollection = accessionCollection;
	}
	
	/**
	 * This function Copies the data from an ParticipantForm object to a Participant object.
	 * @param participantForm An ParticipantForm object containing the information about the Participant.  
	 * */
	public void setAllValues(AbstractActionForm abstractForm)
	{
	    try
	    {
	        ParticipantForm participantForm = (ParticipantForm) abstractForm;
	        
	        this.identifier = new Long(participantForm.getIdentifier());
	        this.lastName = participantForm.getLastName();
	        this.firstName = participantForm.getFirstName();
	        this.middleName = participantForm.getMiddleName();
	        this.dateOfBirth = Utility.parseDate(participantForm.getDateOfBirth());
	        this.gender = participantForm.getGender();
	        this.socialSecurityNumber = participantForm.getSocialSecurityNumber();
	        this.uniqueMedicalRecordNumber = participantForm.getUniqueMedicalRecordNumber();
	        this.race = participantForm.getRace();
	    }
	    catch(Exception excp)
	    {
	        Logger.out.error(excp.getMessage());
	    }
	}
}
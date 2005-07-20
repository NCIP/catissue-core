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
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.catissuecore.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * ParticipantForm Class is used to encapsulate all the request parameters passed 
 * from Participant Add/Edit webpage.
 * @author gautam_shetty
 */

public class ParticipantForm extends AbstractActionForm implements Serializable
{
    
    private static final long serialVersionUID = 1234567890L;

    /**
     * systemIdentifier is a unique id assigned to each Participant.
     * */
    protected long systemIdentifier;

    /**
     * The operation(Add/Edit) to be performed.
     * */
    private String operation = "";
    
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
     * The genotypicGender of a participant as defined by their genotype.
     */
    protected String genotypicGender = "";

    /**
     * Social Security Number of the Participant.
     */
    protected String socialSecurityNumber = "";

    /**
     * The Date of Birth of the Participant.
     */
    protected String birthDate ="";

    /**
     * The race to which the Participant belongs.
     */
    protected String race = "";
    
    /**
     * Participant's ethnicity status.
     */
	protected String ethnicity;

    /**
     * The activity status of the Participant.
     */
    protected String activityStatus = "";

    private Map participantMedicalRecordSources = new HashMap();
    
    private Map participantMedicalRecordNumbers = new HashMap();
    
    /**
     * Initializes an empty ParticipantForm object. 
     */
    public ParticipantForm()
    {
        
    }

    /**
     * Copies the data from an AbstractDomain object to a ParticipantForm object.
     * @param abstractDomain An AbstractDomain object.  
     */
    public void setAllValues(AbstractDomainObject abstractDomain)
    {
//    	Participant participant = (Participant) abstractDomain;
//        this.systemIdentifier = participant.getSystemIdentifier().longValue();
//        this.lastName = participant.getLastName();
//        this.firstName = participant.getFirstName();
//        this.middleName = participant.getMiddleName();
//        this.birthDate = Utility.parseDateToString(participant.getBirthDate()); 
//        this.genotypicGender = participant.getGenotypicGender();
//        this.socialSecurityNumber = participant.getSocialSecurityNumber();
//        this.race = participant.getRace();
   }
    
    /**
     * Returns the systemIdentifier assigned to the Participant.
     * @return int the id assigned to User.
     * @see #setSystemIdentifier(int)
     * */
    public long getSystemIdentifier()
    {
        return systemIdentifier;
    }
    
    /**
     * Sets an id for the Participant.
     * @param systemIdentifier id to be assigned to the Participant.
     * @see #getSystemIdentifier()
     * */
    public void setSystemIdentifier(long identifier)
    {
        this.systemIdentifier = identifier;
    }
    
    /**
     * Returns the operation(Add/Edit) to be performed.
     * @return Returns the operation.
     */
    public String getOperation()
    {
        return operation;
    }
    
    /**
     * Sets the operation to be performed.
     * @param operation The operation to set.
     */
    public void setOperation(String operation)
    {
        this.operation = operation;
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
     * @param birthDate String the date of birth of the Participant.
     * @see #getBirthDate()
     */
    public void setBirthDate(String dateOfBirth)
    {
        this.birthDate = dateOfBirth;
    }

    /**
     * Returns the genotypicGender of the Participant.
     * @return String the genotypicGender of the Participant.
     * @see #setGenotypicGender(String)
     */
    public String getGenotypicGender()
    {
        return genotypicGender;
    }

    /**
     * Sets the genotypicGender of the Participant.
     * @param birthDate String the genotypicGender of the Participant.
     * @see #getGenotypicGender()
     */
    public void setGenotypicGender(String gender)
    {
        this.genotypicGender = gender;
    }

    /**
     * Returns the Social Security Number of the Participant.
     * @return String the Social Security Number of the Participant.
     * @see #setSocialSecurityNumber(String)
     */
    public String getSocialSecurityNumber()
    {
        return socialSecurityNumber;
    }

    /**
     * Sets the Social Security Number of the Participant.
     * @param birthDate String the Social Security Number of the Participant.
     * @see #getSocialSecurityNumber()
     */
    public void setSocialSecurityNumber(String socialSecurityNumber)
    {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    /**
     * Returns the race of the Participant.
     * @return String the race of the Participant.
     * @see #setRace(String)
     */
    public String getRace()
    {
        return race;
    }

    /**
     * Sets the race of the Participant.
     * @param birthDate String the race of the Participant.
     * @see #getRace()
     */
    public void setRace(String race)
    {
        this.race = race;
    }

    /**
     * Returns the Activity Status of the Participant.
     * @return String the Activity Status of the Participant.
     * @see #setActivityStatus(String)
     */
    public String getActivityStatus()
    {
        return activityStatus;
    }

    /**
     * Sets the Activity Status of the Participant.
     * @param activityStatus String the Activity Status of the Participant.
     * @see #getActivityStatus()
     */
    public void setActivityStatus(String activityStatus)
    {
        this.activityStatus = activityStatus;
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
    
    public void setParticipantMedicalRecordSource(String key, Object value)
    {
        participantMedicalRecordSources.put(key, value);
    }
    
    public Object getParticipantMedicalRecordSource(String key)
    {
        return participantMedicalRecordSources.get(key);
    }
    
    public void setParticipantMedicalRecordNumber(String key, Object value)
    {
        participantMedicalRecordNumbers.put(key,value);
    }
    
    public Object getParticipantMedicalRecordNumber(String key)
    {
        return participantMedicalRecordNumbers.get(key);
    }
    
    /**
     * Checks the operation to be performed is add operation or not.
     * @return Returns true if operation is equal to "add", else it returns false
     * */
    public boolean isAddOperation()
    {
        return(getOperation().equals(Constants.ADD));
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
    private void reset()
    {
        this.systemIdentifier = -1;
        this.lastName = null;
        this.firstName = null;
        this.middleName = null;
        this.birthDate=null;
        this.genotypicGender = null;
        this.socialSecurityNumber = null;
        this.race = null;
    }
    
    /**
     * Resets the values of all the fields.
     * This method defined in ActionForm is overridden in this class.
     */
    public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        reset();
    }
    
    /**
     * Overrides the validate method of ActionForm.
     * */
     public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
     {
         ActionErrors errors = new ActionErrors();
         Validator validator = new Validator(); 

         try
         {
             if (operation.equals(Constants.SEARCH))
             {
                 checkValidNumber(new Long(systemIdentifier).toString(),"user.systemIdentifier",errors,validator);
             }
             if (operation.equals(Constants.ADD) || operation.equals(Constants.EDIT))
             {
                 checkValidNumber(socialSecurityNumber,"participant.socialSecurityNumber",errors,validator);
             }
         }
         catch(Exception excp)
         {
             Logger.out.error(excp.getMessage());
         }
        return errors;
     }
 }

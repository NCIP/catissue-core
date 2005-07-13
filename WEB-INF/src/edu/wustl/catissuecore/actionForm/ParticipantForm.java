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
     * identifier is a unique id assigned to each Participant.
     * */
    protected long identifier;

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
     * Gender of the Participant.
     */
    protected String gender = "";

    /**
     * Social Security Number of the Participant.
     */
    protected String socialSecurityNumber = "";

    /**
     * Unique Medical Record Number of the Participant.
     */
    protected String uniqueMedicalRecordNumber = "";

    /**
     * The Date of Birth of the Participant.
     */
    protected String dateOfBirth ="";

    /**
     * The race to which the Participant belongs.
     */
    protected String race = "";

    /**
     * The activity status of the Participant.
     */
    protected String activityStatus = "";

    
    private Collection accessionCollection = new HashSet();
    
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
        Participant participant = (Participant) abstractDomain;
        this.identifier = participant.getIdentifier().longValue();
        this.lastName = participant.getLastName();
        this.firstName = participant.getFirstName();
        this.middleName = participant.getMiddleName();
        this.dateOfBirth = Utility.parseDateToString(participant.getDateOfBirth()); 
        this.gender = participant.getGender();
        this.socialSecurityNumber = participant.getSocialSecurityNumber();
        this.uniqueMedicalRecordNumber = participant.getUniqueMedicalRecordNumber();
        this.race = participant.getRace();
    }
    
    /**
     * Returns the identifier assigned to the Participant.
     * @return int the id assigned to User.
     * @see #setIdentifier(int)
     * */
    public long getIdentifier()
    {
        return identifier;
    }
    
    /**
     * Sets an id for the Participant.
     * @param identifier id to be assigned to the Participant.
     * @see #getIdentifier()
     * */
    public void setIdentifier(long identifier)
    {
        this.identifier = identifier;
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
     * @see #setDateOfBirth(String)
     */
    public String getDateOfBirth()
    {
        return dateOfBirth;
    }
    
    /**
     * Sets the date of birth of the Participant.
     * @param dateOfBirth String the date of birth of the Participant.
     * @see #getDateOfBirth()
     */
    public void setDateOfBirth(String dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
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
     * @param dateOfBirth String the gender of the Participant.
     * @see #getGender()
     */
    public void setGender(String gender)
    {
        this.gender = gender;
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
     * @param dateOfBirth String the Social Security Number of the Participant.
     * @see #getSocialSecurityNumber()
     */
    public void setSocialSecurityNumber(String socialSecurityNumber)
    {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    /**
     * Returns the Unique Medical Record Number of the Participant.
     * @return String the Unique Medical Record Number of the Participant.
     * @see #setUniqueMedicalRecordNumber(String)
     */
    public String getUniqueMedicalRecordNumber()
    {
        return uniqueMedicalRecordNumber;
    }

    /**
     * Sets the Unique Medical Record Number of the Participant.
     * @param dateOfBirth String the Unique Medical Record Number of the Participant.
     * @see #getUniqueMedicalRecordNumber()
     */
    public void setUniqueMedicalRecordNumber(String uniqueMedicalRecordNumber)
    {
        this.uniqueMedicalRecordNumber = uniqueMedicalRecordNumber;
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
     * @param dateOfBirth String the race of the Participant.
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
     * Returns the collection of Accession form the Participant.
     * @return A Collection object containing the Accesion objects for the Participant.
     */
    public Collection getAccessionCollection()
    {

        return accessionCollection;
    }

    /**
     * Sets the collection of Accession of the Participant.
     * @param accessionCollection A Collection object containing the Accesion objects for the Participant.
     */
    public void setAccessionCollection(Collection accessionCollection)
    {
        this.accessionCollection = accessionCollection;
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
        this.identifier = -1;
        this.lastName = null;
        this.firstName = null;
        this.middleName = null;
        this.dateOfBirth=null;
        this.gender = null;
        this.socialSecurityNumber = null;
        this.uniqueMedicalRecordNumber = null;
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
                 checkValidNumber(new Long(identifier).toString(),"user.identifier",errors,validator);
             }
             if (operation.equals(Constants.ADD) || operation.equals(Constants.EDIT))
             {
//                 checkValidString(lastName,"user.lastName",errors,validator);
//                 checkValidString(firstName,"user.firstName",errors,validator);
//                 checkValidString(middleName,"participant.middleName",errors,validator);
                 checkValidNumber(socialSecurityNumber,"participant.socialSecurityNumber",errors,validator);
                 checkValidNumber(uniqueMedicalRecordNumber,"participant.uniqueMedicalRecordNumber",errors,validator);
             }
         }
         catch(Exception excp)
         {
             Logger.out.error(excp.getMessage());
         }
        return errors;
     }
 }

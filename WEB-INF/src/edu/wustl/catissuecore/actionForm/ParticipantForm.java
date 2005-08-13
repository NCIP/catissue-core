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
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
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
     * The gender of a participant.
     */
    protected String gender;
    
    /**
     * The genotype of a participant.
     */
    protected String genotype;

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

    /**
	 * Map to handle values of all the CollectionProtocol Events
	 */
	protected Map values = new HashMap();
    
	/**
	 * Counter that contains number of rows in the 'Add More' functionality.
	 */
	private int counter=1;
	
	
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
        this.systemIdentifier = participant.getSystemIdentifier().longValue();
        this.lastName = participant.getLastName();
        this.firstName = participant.getFirstName();
        this.middleName = participant.getMiddleName();
        this.birthDate = Utility.parseDateToString(participant.getBirthDate(),Constants.DATE_PATTERN_MM_DD_YYYY);
        this.gender = participant.getGender();
        this.genotype = participant.getGenotype();
        this.socialSecurityNumber = participant.getSocialSecurityNumber();
        this.race = participant.getRace();
        this.activityStatus = participant.getActivityStatus();
        this.ethnicity = participant.getEthnicity();
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
    protected void reset()
    {
        this.systemIdentifier = -1;
        this.lastName = null;
        this.firstName = null;
        this.middleName = null;
        this.birthDate=null;
        this.genotype = null;
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
             	if (validator.isEmpty(birthDate))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("participant.birthDate")));
                }
             	
             	if(gender.equals(Constants.SELECT_OPTION))
                {
                	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("participant.gender")));
                }
             	
             	if(genotype.equals(Constants.SELECT_OPTION))
                {
                	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("participant.genotype")));
                }
             	
             	if(race.equals(Constants.SELECT_OPTION))
                {
                	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("participant.race")));
                }
             	
             	if(ethnicity.equals(Constants.SELECT_OPTION))
                {
                	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("participant.ethnicity")));
                }
             	
                checkValidNumber(socialSecurityNumber,"participant.socialSecurityNumber",errors,validator);
              
                //Validations for Add-More Block
                String className = "ParticipantMedicalIdentifier:";
                String key1 = "_Site_systemIdentifier";
                String key2 = "_medicalRecordNumber";
                int index = 1;
                boolean isError = false;
                
                while(true)
                {
                	String keyOne = className + index + key1;
					String keyTwo = className + index + key2;
                	String value1 = (String)values.get(keyOne);
                	String value2 = (String)values.get(keyTwo);
                	
                	if(value1 == null || value2 == null)
                	{
                		break;
                	}
                	else if(value1.equals("-1") && value2.equals(""))
                	{
                		values.remove(keyOne);
                		values.remove(keyTwo);
                	}
                	else if((!value1.equals("-1") && value2.equals("")) || (value1.equals("-1") && !value2.equals("")))
                	{
                		isError = true;
                		break;
                	}
                	index++;
                }
                
                if(isError)
                {
                	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.participant.missing",ApplicationProperties.getValue("particiapnt.msg")));
                }
             }
         }
         catch(Exception excp)
         {
             Logger.out.error(excp.getMessage());
         }
        return errors;
     }
     
     
     /**
      * Associates the specified object with the specified key in the map.
      * @param key the key to which the object is mapped.
      * @param value the object which is mapped.
      */
     public void setValue(String key, Object value) 
     {
             values.put(key, value);
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
 	 * @param values
 	 * The values to set.
 	 */
 	public void setValues(Map values)
 	{
 		this.values = values;
 	}
 	
 	/**
 	 * @param values
 	 * The values to set.
 	 */
 	public Map getValues()
 	{
 		return this.values;
 	}
 	
	/**
	 * @return Returns the counter.
	 */
	public int getCounter()
	{
		return counter;
	}
	
	/**
	 * @param counter The counter to set.
	 */
	public void setCounter(int counter)
	{
		this.counter = counter;
	}
 }

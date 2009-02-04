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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
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
     * The gender of a participant.
     */
    protected String gender = Constants.UNSPECIFIED;
    
    /**
     * The genotype of a participant.
     */
    protected String genotype = Constants.UNKNOWN;

    /**
     * Social Security Number of the Participant.
     */
    protected String socialSecurityNumberPartA = "";
    protected String socialSecurityNumberPartB = "";
    protected String socialSecurityNumberPartC = "";

    /**
     * The Date of Birth of the Participant.
     */
    protected String birthDate ="";

    /**
     * The race to which the Participant belongs.
     */
    protected String[] raceTypes = {Constants.NOTSPECIFIED};
    
    /**
     * Participant's ethnicity status.
     */
	protected String ethnicity = Constants.NOTSPECIFIED;
	
	/**
	 * The Date of Death of the Participant.
	 */
	protected String deathDate = "";
	
	/**
	 * Vital status of the Participant.
	 */
	protected String vitalStatus = Constants.UNKNOWN;
	
    /**
	 * Map to handle values of all the Participant Medical Identifiers
	 */
	protected Map values = new HashMap();
    
	/**
	 * Counter that contains number of rows in the 'Add More' functionality.
	 */
	private int counter=1;
	/**
	 * List to handle values of site
	 */
	private List siteList=null;
	/**
	 * String that contains participant id
	 */
	private String participantId="";
	/**
	 * List to handle values of gender
	 */
	private List genderList=null;
	/**
	 * List to handle values of ethnicity
	 */
	private List ethnicityList=null;
	/**
	 * List to handle values of race
	 */
	private List raceList=null;
	/**
	 * List to handle values of sex genotype
	 */
	private List genotypeList=null;
	/**
	 * List to handle values of vital status
	 */
	private List vitalStatusList=null;
	/**
	 * String array to handle values of activity status
	 */
	private String[] activityStatusList=null;
	
	
	private long cpId = -1; 
    /**
     * Initializes an empty ParticipantForm object. 
     */
    public ParticipantForm()
    {
        
    }
    
    private void setSSN(String ssnString)
    {
    	if(ssnString!=null && !ssnString.equals(""))
    	{
    		try
			{
    			StringTokenizer tok = new StringTokenizer(ssnString,"-");
    			socialSecurityNumberPartA = tok.nextToken();
    			socialSecurityNumberPartB = tok.nextToken();
    			socialSecurityNumberPartC = tok.nextToken();
			}
    		catch(Exception ex)
			{
    			Logger.out.debug(ex.getMessage(), ex);
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
        this.lastName = Utility.toString( participant.getLastName());
        this.firstName =  Utility.toString( participant.getFirstName());
        this.middleName = Utility.toString( participant.getMiddleName());
        this.birthDate = Utility.parseDateToString(participant.getBirthDate(),Constants.DATE_PATTERN_MM_DD_YYYY);
        this.gender = participant.getGender();
        this.genotype = participant.getSexGenotype();
        setSSN(participant.getSocialSecurityNumber());
        
        Collection raceCollection=participant.getRaceCollection();
        if (raceCollection != null)
		{
			this.raceTypes = new String[raceCollection.size()];
			int i = 0;

			Iterator it = raceCollection.iterator();
			while (it.hasNext())
			{
			    String race=(String)it.next();
				this.raceTypes[i]=race;
				i++;
			}
		}
        
        
//        this.race = participant.getRace();
        this.activityStatus = participant.getActivityStatus();
        this.ethnicity = participant.getEthnicity();
        
        this.deathDate = Utility.parseDateToString(participant.getDeathDate(),Constants.DATE_PATTERN_MM_DD_YYYY);;
        this.vitalStatus = participant.getVitalStatus();
        
        //Populating the map with the participant medical identifiers data 
        Collection medicalIdentifierCollection = participant.getParticipantMedicalIdentifierCollection();
        
        if(medicalIdentifierCollection != null)
        {
        	values = new HashMap();
        	int i = 1;
        	
        	Iterator it = medicalIdentifierCollection.iterator();
        	while(it.hasNext())
        	{
        		ParticipantMedicalIdentifier participantMedicalIdentifier = (ParticipantMedicalIdentifier)it.next();
        		
        		String key1 = "ParticipantMedicalIdentifier:" + i +"_Site_id";
				String key2 = "ParticipantMedicalIdentifier:" + i +"_medicalRecordNumber";
				String key3 = "ParticipantMedicalIdentifier:" + i +"_id";

				Site site = participantMedicalIdentifier.getSite();
				
				if(site!=null)
				{
					values.put(key1,Utility.toString(site.getId()));
				}
				else
				{
					values.put(key1,Utility.toString(Constants.SELECT_OPTION));
				}
				
				values.put(key2,Utility.toString(participantMedicalIdentifier.getMedicalRecordNumber()));
				values.put(key3,Utility.toString(participantMedicalIdentifier.getId()));
				
				i++;
        	}
        	counter = medicalIdentifierCollection.size();
        }
        
        //At least one row should be displayed in ADD MORE therefore
		if(counter == 0)
			counter = 1;
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
     * @param birthDate String the race of the Participant.
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
     * */
     public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
     {
         ActionErrors errors = new ActionErrors();
         Validator validator = new Validator();
         try
         {
         	setRedirectValue(validator);
         	
         	String errorKeyForBirthDate = "";
         	String errorKeyForDeathDate = "";
         	
         	if (!validator.isEmpty(birthDate) )
			{
	         	// date validation according to bug id  722 and 730
	    		errorKeyForBirthDate = validator.validateDate(birthDate,true );
	    		if(errorKeyForBirthDate.trim().length() > 0)
	    		{
	    			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(errorKeyForBirthDate,ApplicationProperties.getValue("participant.birthDate")));
	    		}
			}
         	
         	if (!validator.isEmpty(deathDate) )
			{
	    		errorKeyForDeathDate = validator.validateDate(deathDate,true );
	    		if(errorKeyForDeathDate.trim().length() > 0)
	    		{
	    			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(errorKeyForDeathDate,ApplicationProperties.getValue("participant.deathDate")));
	    		}	    		
			}
         	
         	if( (!validator.isEmpty(birthDate) &&  !validator.isEmpty(deathDate)) && (errorKeyForDeathDate.trim().length() == 0 && errorKeyForBirthDate.trim().length() == 0) )
         	{
         		boolean errorKey1 = validator.compareDates(birthDate,deathDate);
	    		
	    		if(!errorKey1)
	    		{
	    			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("participant.invaliddate",ApplicationProperties.getValue("participant.invaliddate")));
	    		}
         	}
         	
         	String socialSecurityNumber = socialSecurityNumberPartA+"-"+socialSecurityNumberPartB+"-"+socialSecurityNumberPartC; 
         	if(!validator.isEmpty(socialSecurityNumberPartA+socialSecurityNumberPartB+socialSecurityNumberPartC) && !validator.isValidSSN(socialSecurityNumber ) )
         	{
         		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.invalid",ApplicationProperties.getValue("participant.socialSecurityNumber")));
         	}
			
         	//Validation for Blank Participant 
         	if(validator.isEmpty(lastName) && validator.isEmpty(firstName) && validator.isEmpty(middleName) && validator.isEmpty(birthDate) && (validator.isEmpty(deathDate))&& !validator.isValidOption(gender) && !validator.isValidOption(vitalStatus) && !validator.isValidOption(genotype) && ethnicity.equals("-1") && validator.isEmpty(socialSecurityNumberPartA+socialSecurityNumberPartB+socialSecurityNumberPartC))
         	{
         	   errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.participant.atLeastOneFieldRequired"));
         	}
         	
			//Validations for Add-More Block
			String className = "ParticipantMedicalIdentifier:";
			String key1 = "_Site_" + Constants.SYSTEM_IDENTIFIER;
			String key2 = "_medicalRecordNumber";
			String key3 = "_" + Constants.SYSTEM_IDENTIFIER;
			int index = 1;

			while(true)
			{
				String keyOne = className + index + key1;
				String keyTwo = className + index + key2;
				String keyThree = className + index + key3;
				
				String value1 = (String)values.get(keyOne);
				String value2 = (String)values.get(keyTwo);
				
				if(value1 == null || value2 == null)
				{
					break;
				}
				else if(!validator.isValidOption(value1) && value2.trim().equals(""))
				{
					values.remove(keyOne);
					values.remove(keyTwo);
					values.remove(keyThree);
				}
				else if((validator.isValidOption(value1) && value2.trim().equals("")) || (!validator.isValidOption(value1) && !value2.trim().equals("")))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.participant.missing",ApplicationProperties.getValue("participant.msg")));
					break;
				}
				index++;
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
    	 if (isMutable())
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
     * Returns the counter.
     * @return int the counter.
     * @see #setCounter(int)
     */
	public int getCounter()
	{
		return counter;
	}
	
	/**
     * Sets the counter.
     * @param counter The counter.
     * @see #getCounter()
     */
	public void setCounter(int counter)
	{
		this.counter = counter;
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
	public String getDeathDate() {
		return deathDate;
	}
	
	/**
	 * Sets the Death date of the Participant.
	 * @param deathDate The deathDate to set.
	 */
	public void setDeathDate(String deathDate) {
		this.deathDate = deathDate;
	}
	
	/**
	 * Returns the Vital status of the Participant.
	 * @return Returns the vitalStatus.
	 */
	public String getVitalStatus() {
		return vitalStatus;
	}
	
	/**
	 * Sets the Vital status of the Participant.
	 * @param vitalStatus The vitalStatus to set.
	 */
	public void setVitalStatus(String vitalStatus) {
		this.vitalStatus = vitalStatus;
	}

	public long getCpId()
	{
		return cpId;
	}

	public void setCpId(long cpId)
	{
		this.cpId = cpId;
	}

	
	/**
	 * Returns the site list of participant
	 * @return siteList
	 */
	public List getSiteList()
	{
		return siteList;
	}

	
	/**
	 * Sets the site list of participant
	 * @param siteList
	 */
	public void setSiteList(List siteList)
	{
		this.siteList = siteList;
	}

	
	/**
	 * Returns the Particioant id
	 * @return participantId
	 */
	public String getParticipantId()
	{
		return participantId;
	}

	
	/**
	 * Sets the Participant Id
	 * @param participantId
	 */
	public void setParticipantId(String participantId)
	{
		this.participantId = participantId;
	}

	
	/**
	 * Returns the gender list of participant
	 * @return genderList
	 */
	public List getGenderList()
	{
		return genderList;
	}

	
	/**
	 * Sets the gender list of participant
	 * @param genderList
	 */
	public void setGenderList(List genderList)
	{
		this.genderList = genderList;
	}

	
	/**
	 * Returnd ethnicity list of participant
	 * @return ethnicityList
	 */
	public List getEthnicityList()
	{
		return ethnicityList;
	}

	
	/**
	 * Sets the ethnicity list of participant
	 * @param ethnicityList
	 */
	public void setEthnicityList(List ethnicityList)
	{
		this.ethnicityList = ethnicityList;
	}

	
	/**
	 * Returns the sex genotype list of participant
	 * @return genotypeList
	 */
	public List getGenotypeList()
	{
		return genotypeList;
	}

	
	/**
	 * Sets the sex genotype list of participant
	 * @param genotypeList
	 */
	public void setGenotypeList(List genotypeList)
	{
		this.genotypeList = genotypeList;
	}

	
	/**
	 * Returns the sex race list of participant
	 * @return raceList
	 */
	public List getRaceList()
	{
		return raceList;
	}

	
	/**
	 * Sets the race list of participant
	 * @param raceList
	 */
	public void setRaceList(List raceList)
	{
		this.raceList = raceList;
	}

	
	/**
	 * Returns the activity status list of participant
	 * @return activityStatusList
	 */
	public String[] getActivityStatusList()
	{
		return activityStatusList;
	}

	
	/**
	 * Sets the activity status list of participant
	 * @param activityStatusList
	 */
	public void setActivityStatusList(String[] activityStatusList)
	{
		this.activityStatusList = activityStatusList;
	}

	
	/**
	 * Returns the vital status list of participant
	 * @return vitalStatusList
	 */
	public List getVitalStatusList()
	{
		return vitalStatusList;
	}

	
	/**
	 * Sets the vital status list of participant
	 * @param vitalStatusList
	 */
	public void setVitalStatusList(List vitalStatusList)
	{
		this.vitalStatusList = vitalStatusList;
	}
}
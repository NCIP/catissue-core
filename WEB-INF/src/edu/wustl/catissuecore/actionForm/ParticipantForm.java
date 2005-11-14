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
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
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
    protected String race = "";
    
    /**
     * Participant's ethnicity status.
     */
	protected String ethnicity;

 
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
        this.systemIdentifier = participant.getSystemIdentifier().longValue();
        this.lastName = Utility.toString( participant.getLastName());
        this.firstName =  Utility.toString( participant.getFirstName());
        this.middleName = Utility.toString( participant.getMiddleName());
        this.birthDate = Utility.parseDateToString(participant.getBirthDate(),Constants.DATE_PATTERN_MM_DD_YYYY);
        this.gender = participant.getGender();
        this.genotype = participant.getGenotype();
        //this.socialSecurityNumber = Utility.toString( participant.getSocialSecurityNumber());
        setSSN(participant.getSocialSecurityNumber());
        this.race = participant.getRace();
        this.activityStatus = participant.getActivityStatus();
        this.ethnicity = participant.getEthnicity();
        
        Collection medicalIdentifierCollection = participant.getParticipantMedicalIdentifierCollection();
        
        if(medicalIdentifierCollection != null)
        {
        	values = new HashMap();
        	int i = 1;
        	
        	Iterator it = medicalIdentifierCollection.iterator();
        	while(it.hasNext())
        	{
        		ParticipantMedicalIdentifier participantMedicalIdentifier = (ParticipantMedicalIdentifier)it.next();
        		
        		String key1 = "ParticipantMedicalIdentifier:" + i +"_Site_systemIdentifier";
				String key2 = "ParticipantMedicalIdentifier:" + i +"_medicalRecordNumber";
				String key3 = "ParticipantMedicalIdentifier:" + i +"_systemIdentifier";

				Site site = participantMedicalIdentifier.getSite();
				if(site!=null)
					values.put(key1,Utility.toString(site.getSystemIdentifier()));
				else
					values.put(key1,Utility.toString(Constants.SELECT_OPTION));
				
				values.put(key2,Utility.toString(participantMedicalIdentifier.getMedicalRecordNumber()));
				values.put(key3,Utility.toString(participantMedicalIdentifier.getSystemIdentifier()));
				
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

//    /**
//     * Returns the Social Security Number of the Participant.
//     * @return String the Social Security Number of the Participant.
//     * @see #setSocialSecurityNumber(String)
//     */
//    public String getSocialSecurityNumber()
//    {
//        return socialSecurityNumber;
//    }
//
//    /**
//     * Sets the Social Security Number of the Participant.
//     * @param birthDate String the Social Security Number of the Participant.
//     * @see #getSocialSecurityNumber()
//     */
//    public void setSocialSecurityNumber(String socialSecurityNumber)
//    {
//        this.socialSecurityNumber = socialSecurityNumber;
//    }

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
//        this.systemIdentifier = -1;
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
//         	String cf = getRedirectTo();
//         	if(validator.isEmpty(cf ) )
//         		cf = "";
//         	setRedirectTo(cf );
//          -----------------------------------------------------   
         	
         	
//            if (validator.isEmpty(lastName))
// 			{
// 			    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("user.lastName")));
// 			}
//             
//			if (validator.isEmpty(firstName))
//			{
//			    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("user.firstName")));
//			}
			
//			if (validator.isEmpty(middleName))
//			{
//			    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("participant.middleName")));
//			}

         	if (!validator.isEmpty(birthDate) )
			{
	         	//  date validation according to bug id  722 and 730
	    		String errorKey = validator.validateDate(birthDate,true );
	    		if(errorKey.trim().length() >0  )
	    		{
	    			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(errorKey,ApplicationProperties.getValue("participant.birthDate")));
	    		}
			}
			
//			if(!validator.isValidOption(gender))
//			{
//				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("participant.gender")));
//			}
//			
//			if(!validator.isValidOption(genotype))
//			{
//				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("participant.genotype")));
//			}
//			
//			if(!validator.isValidOption(race))
//			{
//				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("participant.race")));
//			}
//			
//			if(!validator.isValidOption(ethnicity))
//			{
//				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("participant.ethnicity")));
//			}
//			
//			if (!validator.isValidOption(activityStatus))
//			{
//			    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("participant.activityStatus")));
//			}

			String socialSecurityNumber = socialSecurityNumberPartA+"-"+socialSecurityNumberPartB+"-"+socialSecurityNumberPartC; 
         	if(!validator.isEmpty(socialSecurityNumberPartA+socialSecurityNumberPartB+socialSecurityNumberPartC) && !validator.isValidSSN(socialSecurityNumber ) )
         	{
         		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.invalid",ApplicationProperties.getValue("participant.socialSecurityNumber")));
         	}
			  
			//Validations for Add-More Block
			String className = "ParticipantMedicalIdentifier:";
			String key1 = "_Site_systemIdentifier";
			String key2 = "_medicalRecordNumber";
			String key3 = "_systemIdentifier";
			int index = 1;
			boolean isError = false;
			
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
				else if(value1.equals("-1") && value2.equals(""))
				{
					values.remove(keyOne);
					values.remove(keyTwo);
					values.remove(keyThree);
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
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.participant.missing",ApplicationProperties.getValue("participant.msg")));
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
	public String getSocialSecurityNumberPartA()
	{
		return socialSecurityNumberPartA;
	}
	public void setSocialSecurityNumberPartA(String socialSecurityNumberPartA)
	{
		this.socialSecurityNumberPartA = socialSecurityNumberPartA;
	}
	public String getSocialSecurityNumberPartB()
	{
		return socialSecurityNumberPartB;
	}
	public void setSocialSecurityNumberPartB(String socialSecurityNumberPartB)
	{
		this.socialSecurityNumberPartB = socialSecurityNumberPartB;
	}
	public String getSocialSecurityNumberPartC()
	{
		return socialSecurityNumberPartC;
	}
	public void setSocialSecurityNumberPartC(String socialSecurityNumberPartC)
	{
		this.socialSecurityNumberPartC = socialSecurityNumberPartC;
	}
}
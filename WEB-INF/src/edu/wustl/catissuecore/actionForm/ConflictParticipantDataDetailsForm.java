/**
 * <p>Title: ConflictParticipantDataDetailsForm Class>
 * <p>Description:	This class contains attributes to display on ConflictParticipantDataDetailsView.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.00
 * @author kalpana_thakur
 * Created on Feb 27,2007
 */


package edu.wustl.catissuecore.actionForm;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Race;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;


public class ConflictParticipantDataDetailsForm extends AbstractActionForm
{
	
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
		

	/**
	 * String for first name of participant
	 */
	protected String participantName;
	/**
	 * String for vital status of participant
	 */
	protected String vitalStatus;
	/**
	 * String for birth date of participant
	 */
	protected String birthDate;
	/**
	 * String for death date of participant
	 */
	protected String deathDate;
	/**
	 * String for ethinicity of participant
	 */
	protected String ethinicity;
	/**
	 * Collection for race of participant
	 */
	protected String race;
	/**
	 * String for gender of participant
	 */
	protected String gender;
	/**
	 * String for sexgenotype of participant
	 */
	protected String sexGenotype;
	/**
	 * String for social security number of participant
	 */
	protected String socialSecurityNumber;
	/**
	 * Collection for medical identifier numbers of participant
	 */
	protected Collection medicalIdentifierNumbers=new HashSet();
	
	/**
	 * Map for medical identifier numbers
	 */
	protected Map values = new HashMap();
	/**
	 * int counter for map size
	 */
	private int counter=0;
	
	/**
	 * This is the method to get date of birth of particicpant
	 * @return birthDate Date of Birth of participant
	 */
	public String getBirthDate()
	{
		return birthDate;
	}
	
	/**
	 * This is the method to set date of birth of particicpant
	 * @param dateOfBirth Date of Birth of participant
	 */
	public void setBirthDate(String dateOfBirth)
	{
		this.birthDate = dateOfBirth;
	}
	
	/**
	 * This is the method to get death date of particicpant
	 * @return deathDate Death date of participant
	 */
	public String getDeathDate()
	{
		return deathDate;
	}
	
	/**
	 * This is the method to set death date of particicpant
	 * @param dateOfDeath Death date of participant
	 */
	public void setDeathDate(String dateOfDeath)
	{
		this.deathDate = dateOfDeath;
	}
	
	/**
	 * This is the method to get ethinicity of particicpant
	 * @return ethinicity Ethinicity participant
	 */
	public String getEthinicity()
	{
		return ethinicity;
	}
	
	/**
	 * This is the method to set ethinicity of particicpant
	 * @param ethinicity Ethinicity participant
	 */
	public void setEthinicity(String ethinicity)
	{
		this.ethinicity = ethinicity;
	}
	
	/**
	 * This is the method to get name of particicpant
	 * @return firstName First name of participant
	 */
	public String getParticipantName()
	{
		return participantName;
	}
	
	/**
	 * This is the method to set name of particicpant
	 * @param firstName First name of participant
	 */
	public void setParticipantName(String firstName)
	{
		this.participantName = firstName;
	}
	
	/**
	 * This is the method to get gender of particicpant
	 * @return gender Gender of particicpant
	 */
	public String getGender()
	{
		return gender;
	}
	
	/**
	 * This is the method to set gender of particicpant
	 * @param gender Gender of particicpant
	 */
	public void setGender(String gender)
	{
		this.gender = gender;
	}
	
	/**
	 * This is the method to get vital status of particicpant
	 * @return lastName Last name of participant
	 */
	public String getVitalStatus()
	{
		return vitalStatus;
	}
	
	/**
	 * This is the method to set vital status of particicpant
	 * @param lastName Last name of participant
	 */
	public void setVitalStatus(String vitalStatus)
	{
		this.vitalStatus = vitalStatus;
	}
	
	/**
	 * This is the method to get collection of MedicalIdentifierNumbers of particicpant
	 * @return medicalIdentifierNumbers Medical Identifier Number of Participant
	 */
	public Collection getMedicalIdentifierNumbers()
	{
		return medicalIdentifierNumbers;
	}
	
	/**
	 * This is the method to set collection of MedicalIdentifierNumbers of particicpant
	 * @param medicalIdentifierNumbers Medical Identifier Number of Participant
	 */
	public void setMedicalIdentifierNumbers(Collection medicalIdentifierNumbers)
	{
		this.medicalIdentifierNumbers = medicalIdentifierNumbers;
	}
	
	/**
	 * This is the method to get collection of race of particicpant
	 * @return race Race of participant
	 */
	public String getRace()
	{
		return race;
	}
	
	/**
	 * This is the method to set collection of race of particicpant
	 * @param race Race of participant
	 */
	public void setRace(String race)
	{
		this.race = race;
	}
	
	/**
	 * This is the method to get sex genotype of particicpant
	 * @return sexGenotype Sex genotype of particicpant
	 */
	public String getSexGenotype()
	{
		return sexGenotype;
	}
	
	/**
	 * This is the method to set sex genotype of particicpant
	 * @param sexGenotype Sex genotype of particicpant
	 */
	public void setSexGenotype(String sexGenotype)
	{
		this.sexGenotype = sexGenotype;
	}
	
	/**
	 * This is the method to get Social Security Number of particicpant
	 * @return socialSecurityNumber Social Security Number (SSN) of participant
	 */
	public String getSocialSecurityNumber()
	{
		return socialSecurityNumber;
	}

	/**
	 * This is the method to set Social Security Number of particicpant
	 * @param socialsecurityNumber Social Security Number (SSN) of participant
	 */
	public void setSocialSecurityNumber(String socialsecurityNumber)
	{
		this.socialSecurityNumber = socialsecurityNumber;
	}
	
	
	/**
	 * Default Constructor of the class
	 */
	public ConflictParticipantDataDetailsForm()
	{
		reset();
	}

	/** 
	 * Reset all values of form
	 * @see edu.wustl.common.actionForm.AbstractActionForm#reset()
	 * 
	 */
	protected void reset() 
	{
		
	}



	/**
	 *  set values of participant related variables
	 * @param participant Participant Object 
	 */
	public void setAllValues(AbstractDomainObject domainObj)
	{
		Participant participant =(Participant)domainObj;
		try
		{
			this.participantName=participant.getLastName()+","+participant.getFirstName();
			this.vitalStatus=participant.getVitalStatus();
			if(participant.getBirthDate()!=null)
			{
				this.birthDate=Utility.parseDateToString(participant.getBirthDate(), Constants.DATE_PATTERN_MM_DD_YYYY);
			}
			if(participant.getDeathDate()!=null)
			{
				this.deathDate=Utility.parseDateToString(participant.getDeathDate(), Constants.DATE_PATTERN_MM_DD_YYYY);
			}
			this.ethinicity=participant.getEthnicity();
			Collection tempRaceColl=participant.getRaceCollection();
			Iterator raceIter=tempRaceColl.iterator();
			this.race="";
			StringBuffer tempStr=new StringBuffer();
			Race raceObj=null;
			while(raceIter.hasNext())
			{
				raceObj =(Race) raceIter.next();
				tempStr.append(raceObj.getRaceName()+", ");
			}
			this.race=tempStr.toString();
			this.gender=participant.getGender();
			this.sexGenotype=participant.getSexGenotype();
			this.socialSecurityNumber=participant.getSocialSecurityNumber();
			this.medicalIdentifierNumbers=participant.getParticipantMedicalIdentifierCollection();
			
			if(medicalIdentifierNumbers != null)
	        {
	        	values=edu.wustl.catissuecore.caties.util.ViewSPRUtil.getMedicalIdentifierNumbers(medicalIdentifierNumbers);
	        	counter = medicalIdentifierNumbers.size();
	        }
		}
		catch(Exception ex)
		{
			Logger.out.error("viewSPR:Participant information is null");
		}
	}
	
	/** 
	 * On the basis of Request for submitting comments different form ID will be returned to save two different kind of comments
	 * review comment and quarantine comments
	 * @see edu.wustl.common.actionForm.AbstractActionForm#getFormId()
	 * @return identifier of Form
	 */
	public int getFormId()
	{	
		if(this.submittedFor.equalsIgnoreCase("quarantine"))
		{
			return Constants.QUARANTINE_EVENT_PARAMETER_FORM_ID;
		}
		return Constants.PATHOLOGY_REPORT_REVIEW_FORM_ID;
	}
	
	
	
	/**
	 * This is the method to get counter that is size of map for medical identifier numbers
	 * @return counter Counter for the number of elemets in MedicalIdentifierMap
	 */
	public int getCounter()
	{
		return counter;
	}
	
	/**
	 * This is the method to set counter that is size of map for medical identifier numbers
	 * @param counter Counter for the number of elemets in MedicalIdentifierMap
	 */
	public void setCounter(int counter)
	{
		this.counter = counter;
	}
	
	/**
	 * This is the method to add a key value pair to map for medical identifier numbers
	 * @param key key to set value in map
	 * @param value object to be set as value in map
	 */
	public void setValue(String key, Object value) 
    {
		if (isMutable())values.put(key, value);
    }

	/**
	 * This is the method to retrieve a value for given key from map for medical identifier numbers
	 * @param key key to retrieve object from map
	 * @return object retrieved object from map
	 */
	public Object getValue(String key) 
    {
		return values.get(key);
    }
      
 	/**
 	 * This is the method to retrieve all values for all keys from map for medical identifier numbers
 	 * @return collection Returns all the objects stored in map
 	 */
 	public Collection getAllValues() 
 	{
 		return values.values();
 	}

 	/**
 	 * This is the method to add multiple key value pair to map for medical identifier numbers
 	 * @param values map of object
 	 */
 	public void setValues(Map values)
 	{
 		this.values = values;
 	}
 
 	/**
 	 * This is the method to retrieve multiple values from map for medical identifier numbers
 	 * @return map map of object
 	 */
 	public Map getValues()
 	{
 		return this.values;
 	}

	/** 
	 * This is the overriden method to get the id of the domain object of the page from which this age is accessed 
	 * @see edu.wustl.common.actionForm.AbstractActionForm#getId()
	 * @return id identifier of current domain object
	 */
	public long getId()
	{
		return id;
	}
	
	/** 
	 * This is the overriden method to set the id of the domain object of the page from which this age is accessed 
	 * @see edu.wustl.common.actionForm.AbstractActionForm#setId(long)
	 * @param id identifier of current domain object
	 */
	public void setId(long id)
	{
		this.id = id;
	}

	
	
}
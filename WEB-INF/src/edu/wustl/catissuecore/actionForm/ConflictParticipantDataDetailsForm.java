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
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;


public class ConflictParticipantDataDetailsForm extends AbstractActionForm
{
	
	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(ClinicalStudyRegistrationForm.class);

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
	public void setBirthDate(final String dateOfBirth)
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
	public void setDeathDate(final String dateOfDeath)
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
	public void setEthinicity(final String ethinicity)
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
	public void setParticipantName(final String firstName)
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
	public void setGender(final String gender)
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
	public void setVitalStatus(final String vitalStatus)
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
	public void setRace(final String race)
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
	public void setSexGenotype(final String sexGenotype)
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
//		reset();
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
		String dtePattern =CommonServiceLocator.getInstance().getDatePattern();
		final Participant participant =(Participant)domainObj;
		try
		{
			this.participantName=participant.getLastName()+","+participant.getFirstName();
			this.vitalStatus=participant.getVitalStatus();
			if(participant.getBirthDate()!=null)
			{
				this.birthDate= Utility.parseDateToString(participant.getBirthDate(), dtePattern);
			}
			if(participant.getDeathDate()!=null)
			{
				this.deathDate= Utility.parseDateToString(participant.getDeathDate(), dtePattern);
			}
			this.ethinicity=participant.getEthnicity();
			final Collection tempRaceColl=participant.getRaceCollection();
			final Iterator raceIter=tempRaceColl.iterator();
			this.race="";
			final StringBuffer tempStr=new StringBuffer();
			Race raceObj=null;
			while(raceIter.hasNext())
			{
				raceObj =(Race) raceIter.next();
				tempStr.append(raceObj.getRaceName());
				tempStr.append(", ");
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
			logger.error("viewSPR:Participant information is null");
		}
	}
	
	/** 
	 * On the basis of Request for submitting comments different form ID will be returned to save two different kind of comments
	 * review comment and quarantine comments
	 * @see edu.wustl.common.actionForm.AbstractActionForm#getFormId()
	 * @return identifier of Form
	 */
	public int getFormId()
	{	int result = Constants.PATHOLOGY_REPORT_REVIEW_FORM_ID; 
		if(this.getSubmittedFor().equalsIgnoreCase("quarantine"))
		{
			result = Constants.QUARANTINE_EVENT_PARAMETER_FORM_ID;
		}
		return result;
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
	public void setCounter(final int counter)
	{
		this.counter = counter;
	}
	
	/**
	 * This is the method to add a key value pair to map for medical identifier numbers
	 * @param key key to set value in map
	 * @param value object to be set as value in map
	 */
	public void setValue(final String key, final Object value) 
    {
		if (isMutable()){values.put(key, value);}
    }

	/**
	 * This is the method to retrieve a value for given key from map for medical identifier numbers
	 * @param key key to retrieve object from map
	 * @return object retrieved object from map
	 */
	public Object getValue(final String key) 
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
 	public void setValues(final Map values)
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
		return super.getId();
	}
	
	/** 
	 * This is the overriden method to set the id of the domain object of the page from which this age is accessed 
	 * @see edu.wustl.common.actionForm.AbstractActionForm#setId(long)
	 * @param id identifier of current domain object
	 */
	public void setId(final long id)
	{
		super.setId(id);
	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub
		
	}

	
	
}
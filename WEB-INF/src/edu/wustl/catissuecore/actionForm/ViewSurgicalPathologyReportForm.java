package edu.wustl.catissuecore.actionForm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;

/**
 * @author vijay_pande
 * ActionForm bean class for viewSurgicalPathologyReport.jsp
 */
public class ViewSurgicalPathologyReportForm extends AbstractActionForm
{
	
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * String for first name of participant
	 */
	protected String firstName;
	/**
	 * String for last name of participant
	 */
	protected String lastName;
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
	protected Collection race=new HashSet();
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
	 * String for accession number of Identified Report
	 */
	protected String identifiedReportAccessionNumber;
	/**
	 * String for site of Identified Report
	 */
	protected String identifiedReportSite;
	/**
	 * long for id of Identified Report*
	 */
	private String identifiedReportId = "";
	/**
	 * long for report Identifier
	 */
	private long requestIdentifier=-1;
	/**
	 * This variable is to identify if the request is quarantined or Dequarantined 
	 */
	private long acceptReject;
	/**
	 * String for report text of Identified Report
	 */
	protected String identifiedReportTextContent;
	/**
	 * String for accession number of Deidentified Report
	 */
	protected String deIdentifiedReportAccessionNumber;
	/**
	 * String for site of Deidentified Report
	 */
	protected String deIdentifiedReportSite;
	/**
	 * long for id of Deidentified Report
	 */
	private long deIdentifiedReportId;
	/**
	 * String for report text of Deidentified Report
	 */
	protected String deIdentifiedReportTextContent;
	/**
	 * String for comments of event parameters
	 */
	protected String comments;
	/**
	 * String for comments of User
	 */
	protected String userComments;
	/**
	 * String for User name
	 */
	protected String userName;
	
	/**
	 * Map for medical identifier numbers
	 */
	protected Map values = new HashMap();
	/**
	 * int counter for map size
	 */
	private int counter=0;
	/**
	 * long id of domain object of the page from which this page is accessed
	 */
	private long id;
	/**
	 * List to handle values of report Id if the domain object is Participant
	 */
	private List reportIdList=new ArrayList();
	
	/**
	 * String to handle values of current report id from report id list
	 */
	private String reportId="";
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
	 * This is the method to get first name of particicpant
	 * @return firstName First name of participant
	 */
	public String getFirstName()
	{
		return firstName;
	}
	
	/**
	 * This is the method to set first name of particicpant
	 * @param firstName First name of participant
	 */
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
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
	 * This is the method to get last name of particicpant
	 * @return lastName Last name of participant
	 */
	public String getLastName()
	{
		return lastName;
	}
	
	/**
	 * This is the method to set last name of particicpant
	 * @param lastName Last name of participant
	 */
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
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
	public Collection getRace()
	{
		return race;
	}
	
	/**
	 * This is the method to set collection of race of particicpant
	 * @param race Race of participant
	 */
	public void setRace(Collection race)
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
	 * This is the method to get Accession Number of Identified Report
	 * @return identifiedReportAccessionNumber Accession Number of identified report
	 */
	public String getIdentifiedReportAccessionNumber()
	{
		return identifiedReportAccessionNumber;
	}
	
	/**
	 * This is the method to set Accession Number of Identified Report
	 * @param accessionNumber Accession Number of identified report
	 */
	public void setIdentifiedReportAccessionNumber(String accessionNumber)
	{
		this.identifiedReportAccessionNumber = accessionNumber;
	}
	
	/**
	 * This is the method to get comments of Evevnt Parameter (Either review or quarantine event parameter)
	 * @return comments Comments text
	 */
	public String getComments()
	{
		return comments;
	}
	
	/**
	 * This is the method to set comments of Evevnt Parameter (Either review or quarantine event parameter)
	 * @param comments Comments text
	 */
	public void setComments(String comments)
	{
		this.comments = comments;
	}
	
	/**
	 * This is the method to get site of Identified Report
	 * @return site of Identified Report
	 */
	public String getIdentifiedReportSite()
	{
		return identifiedReportSite;
	}
	
	/**
	 * This is the method to set site of Identified Report
	 * @param site of Identified Report
	 */
	public void setIdentifiedReportSite(String site)
	{
		this.identifiedReportSite = site;
	}
	
	/**
	 * Default Constructor of the class
	 */
	public ViewSurgicalPathologyReportForm()
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
	 * Set values of all variables of form
	 * @see edu.wustl.common.actionForm.AbstractActionForm#setAllValues(edu.wustl.common.domain.AbstractDomainObject)
	 * @param abstractDomain Object of AbstractDomainObject which is IdentifiedSurgicalPathologyReport object in this case
	 */
	public void setAllValues(AbstractDomainObject abstractDomain) 
	{
		IdentifiedSurgicalPathologyReport ispr=(IdentifiedSurgicalPathologyReport)abstractDomain;
		setIdentifiedReport(ispr);
		if(ispr!=null && ispr.getSpecimenCollectionGroup()!=null)
		{
			setDeIdentifiedReport(ispr.getDeidentifiedSurgicalPathologyReport());
			setParticipant(ispr.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getParticipant());
		}
		else
		{
			setDeIdentifiedReport(null);
		}
		this.comments=null;
	}
	
	/**
	 * set values of De-Identified report related variables
	 * @param deidentifiedSurgicalPathologyReport DeidentifiedSurgicalPathologyReport Object
	 */
	public void setDeIdentifiedReport(DeidentifiedSurgicalPathologyReport deidentifiedSurgicalPathologyReport)
	{
		try
		{
			this.deIdentifiedReportId=deidentifiedSurgicalPathologyReport.getId();
			this.deIdentifiedReportAccessionNumber=deidentifiedSurgicalPathologyReport.getSpecimenCollectionGroup().getSurgicalPathologyNumber();
			this.deIdentifiedReportTextContent=deidentifiedSurgicalPathologyReport.getTextContent().getData();
			this.deIdentifiedReportSite=deidentifiedSurgicalPathologyReport.getSource().getName();
		}
		catch(NullPointerException ex)
		{
			Logger.out.error("viewSPR:De-identified Report information is null");
			if(this.deIdentifiedReportTextContent==null)
			{
				this.deIdentifiedReportTextContent="";//De-Identified Report Not Found !
			}
		}
	}

	/**
	 * set values of Identified report related variables
	 * @param ispr IdentifiedSurgicalPathologyReport Object
	 *  
	 */
	public void setIdentifiedReport(IdentifiedSurgicalPathologyReport ispr)
	{
//		if(ispr.getId() != null)
//		{
			try
		{
			this.identifiedReportId=ispr.getId().toString();
			this.identifiedReportAccessionNumber=ispr.getSpecimenCollectionGroup().getSurgicalPathologyNumber();
			this.identifiedReportTextContent=ispr.getTextContent().getData();	
			this.identifiedReportSite=ispr.getSource().getName();
		}
		catch(NullPointerException ex)
		{
			Logger.out.error("viewSPR:Identified Report information is null");
			if(this.identifiedReportTextContent==null)
			{
				this.identifiedReportTextContent="Identified Report Not Found !";
			}
		}
//		}
//		else
//		{
//			this.identifiedReportId=Constants.HASHED_OUT;
//			this.identifiedReportAccessionNumber=Constants.HASHED_OUT;
//			this.identifiedReportTextContent=Constants.HASHED_OUT;	
//			this.identifiedReportSite=Constants.HASHED_OUT;
//		}
	}

	/**
	 *  set values of participant related variables
	 * @param participant Participant Object 
	 */
	public void setParticipant(final Participant participant)
	{
//		if(participant.getId() != null)
//		{
		try
		{
			this.firstName=participant.getFirstName();
			this.lastName=participant.getLastName();
			if(participant.getBirthDate()!=null)
			{
				this.birthDate=Utility.parseDateToString(participant.getBirthDate(), Constants.DATE_PATTERN_MM_DD_YYYY);
			}
			if(participant.getDeathDate()!=null)
			{
				this.deathDate=Utility.parseDateToString(participant.getDeathDate(), Constants.DATE_PATTERN_MM_DD_YYYY);
			}
			this.ethinicity=participant.getEthnicity();
			this.race=participant.getRaceCollection();
			this.gender=participant.getGender();
			this.sexGenotype=participant.getSexGenotype();
			this.socialSecurityNumber=participant.getSocialSecurityNumber();
			this.medicalIdentifierNumbers=participant.getParticipantMedicalIdentifierCollection();
			
			if(medicalIdentifierNumbers != null)
	        {
	        	values = new HashMap();
	        	int i = 1;
	        	
	        	Iterator it = medicalIdentifierNumbers.iterator();
	        	while(it.hasNext())
	        	{
	        		ParticipantMedicalIdentifier participantMedicalIdentifier = (ParticipantMedicalIdentifier)it.next();
	        		
	        		String key1 = "ParticipantMedicalIdentifier:" + i +"_Site_id";
					String key2 = "ParticipantMedicalIdentifier:" + i +"_medicalRecordNumber";
					String key3 = "ParticipantMedicalIdentifier:" + i  +"_id";
	
					Site site = participantMedicalIdentifier.getSite();
					
					if(site!=null)
					{
						values.put(key1,Utility.toString(site.getName()));
					}
					else
					{
						values.put(key1,Utility.toString(Constants.SELECT_OPTION));
					}
					
					values.put(key2,Utility.toString(participantMedicalIdentifier.getMedicalRecordNumber()));
					values.put(key3,Utility.toString(participantMedicalIdentifier.getId()));
					
					i++;
	        	}
	        	counter = medicalIdentifierNumbers.size();
	        }
		}
		catch(NullPointerException ex)
		{
			Logger.out.error("viewSPR:Participant information is null");
		}
//		}
//		else
//		{
//			this.firstName=Constants.HASHED_OUT;
//			this.lastName=Constants.HASHED_OUT;
//			this.birthDate = Constants.HASHED_OUT;
//			this.deathDate = Constants.HASHED_OUT;
//			this.ethinicity=Constants.HASHED_OUT;
//			Collection raceColl = new HashSet();
//			raceColl.add(Constants.HASHED_OUT);
//			this.race=raceColl;
//			this.gender=Constants.HASHED_OUT;
//			this.sexGenotype=Constants.HASHED_OUT;
//			this.socialSecurityNumber=Constants.HASHED_OUT;
//			
//			Collection medicalIdentifierColl = participant.getParticipantMedicalIdentifierCollection();
//			Collection tempColl = new HashSet();
//			if(medicalIdentifierColl != null)
//			{
//				Iterator iter = medicalIdentifierColl.iterator();
//				
//				while(iter.hasNext())
//				{
//					ParticipantMedicalIdentifier participantMedicalIdentifier = (ParticipantMedicalIdentifier)iter.next();
//					ParticipantMedicalIdentifier tempParticipantMedicalIdentifier = participantMedicalIdentifier;
//					tempParticipantMedicalIdentifier.getSite().setName(Constants.HASHED_OUT);
//					tempParticipantMedicalIdentifier.setMedicalRecordNumber(Constants.HASHED_OUT);
//					
//					tempColl.add(tempParticipantMedicalIdentifier);					
//				}
//			}
//			this.medicalIdentifierNumbers=tempColl;
//			makeMapForMedicalIdentifier();
//		}
	}
//	private void makeMapForMedicalIdentifier()
//	{		
//		if(medicalIdentifierNumbers != null)
//        {
//        	values = new HashMap();
//        	int i = 1;
//        	
//        	Iterator it = medicalIdentifierNumbers.iterator();
//        	while(it.hasNext())
//        	{
//        		ParticipantMedicalIdentifier participantMedicalIdentifier = (ParticipantMedicalIdentifier)it.next();
//        		
//        		String key1 = "ParticipantMedicalIdentifier:" + i +"_Site_id";
//				String key2 = "ParticipantMedicalIdentifier:" + i +"_medicalRecordNumber";
//				String key3 = "ParticipantMedicalIdentifier:" + i  +"_id";
//
//				Site site = participantMedicalIdentifier.getSite();
//				
//				if(site!=null)
//				{
//					values.put(key1,Utility.toString(site.getName()));
//				}
//				else
//				{
//					values.put(key1,Utility.toString(Constants.SELECT_OPTION));
//				}
//				
//				values.put(key2,Utility.toString(participantMedicalIdentifier.getMedicalRecordNumber()));
//				values.put(key3,Utility.toString(participantMedicalIdentifier.getId()));
//				
//				i++;
//        	}
//        	counter = medicalIdentifierNumbers.size();
//        }
//	}
	
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
	 * This is the method to get report text content of Deidentified Report
	 * @return deIdentifiedReportTextContent text of textContent for deIdentifiedReport
	 */
	public String getDeIdentifiedReportTextContent()
	{
		return deIdentifiedReportTextContent;
	}
	
	/**
	 * This is the method to set report text content of Deidentified Report
	 * @param deIdentifiedReportTextContent text of textContent for deIdentifiedReport
	 */
	public void setDeIdentifiedReportTextContent(String deIdentifiedReportTextContent)
	{
		this.deIdentifiedReportTextContent = deIdentifiedReportTextContent;
	}
	
	/**
	 * This is the method to get report text content of Identified Report
	 * @return identifiedReportTextContent text of textContent for identifiedReport
	 */
	public String getIdentifiedReportTextContent()
	{
		return identifiedReportTextContent;
	}
	
	/**
	 * This is the method to set report text content of Identified Report
	 * @param identifiedReportTextContent text of textContent for identifiedReport
	 */
	public void setIdentifiedReportTextContent(String identifiedReportTextContent)
	{
		this.identifiedReportTextContent = identifiedReportTextContent;
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

	/**
	 * This is the method to get report id of Identified Report
	 * @return identifiedReportId id of the identifiedReport
	 */
	public String getIdentifiedReportId()
	{
		return identifiedReportId;
	}
	
	/**
	 * This is the method to set report id of Identified Report
	 * @param identifiedReportId id of the identifiedReport
	 */
	public void setIdentifiedReportId(String identifiedReportId)
	{
		this.identifiedReportId = identifiedReportId;
	}
	
	/**
	 * This is the method to get accession number of Deidentified Report
	 * @return deIdentifiedReportAccessionNumber accession number of deidentifiedReport
	 */
	public String getDeIdentifiedReportAccessionNumber()
	{
		return deIdentifiedReportAccessionNumber;
	}
	
	/**
	 * This is the method to set accession number of Deidentified Report
	 * @param deIdentifiedReportAccessionNumber accession number of deidentifiedReport
	 */
	public void setDeIdentifiedReportAccessionNumber(String deIdentifiedReportAccessionNumber)
	{
		this.deIdentifiedReportAccessionNumber = deIdentifiedReportAccessionNumber;
	}

	/**
	 * This is the method to get report id of Deidentified Report
	 * @return deIdentifiedReportId id of deidentifiedReport
	 */
	public long getDeIdentifiedReportId()
	{
		return deIdentifiedReportId;
	}
	
	/**
	 *  This is the method to set report id of Deidentified Report
	 * @param deIdentifiedReportId id of deidentifiedReport
	 */
	public void setDeIdentifiedReportId(long deIdentifiedReportId)
	{
		this.deIdentifiedReportId = deIdentifiedReportId;
	}
	
	/**
	 * This is the method to get site Deidentified Report
	 * @return site of de-identified report
	 */
	public String getDeIdentifiedReportSite()
	{
		return deIdentifiedReportSite;
	}
	
	/**
	 * This is the method to get site Deidentified Report
	 * @param deIdentifiedReportSite site of de-identified report
	 */
	public void setDeIdentifiedReportSite(String deIdentifiedReportSite)
	{
		this.deIdentifiedReportSite = deIdentifiedReportSite;
	}

	
	/**
	 * Returns the report id list
	 * @return reportIdList list of report ids
	 */
	public List getReportIdList()
	{
		return reportIdList;
	}

	
	/**
	 * Sets the report id list
	 * @param reportIdList list of report ids
	 */
	public void setReportIdList(List reportIdList)
	{
		this.reportIdList = reportIdList;
	}

	/**
	 * Returns the report id 
	 * @return reportId current report id
	 */
	public String getReportId()
	{
		return reportId;
	}

	/**
	 * Sets the report id 
	 * @param reportId current report id
	 */
	public void setReportId(String reportId)
	{
		this.reportId = reportId;
	}

	/**
	 * This is the method to set comments of Users is the status is Pending (Either review or quarantine event parameter)
	 * @return userComments user comments text
	 */
	public String getUserComments()
	{
		return userComments;
	}

	/**
	 * This is the method to set comments of Users is the status is Pending (Either review or quarantine event parameter)
	 * @param userComments user comments text
	 */
	public void setUserComments(String userComments)
	{
		this.userComments = userComments;
	}

	/**
	 * Unique Identifier for a report
	 * @return requestIdentifier Unique Identifier for a report
	 */
	public long getRequestIdentifier()
	{
		return requestIdentifier;
	}

	/**
	 * Unique Identifier for a report
	 * @param requestIdentifier Unique Identifier for a report
	 */
	public void setRequestIdentifier(long requestIdentifier)
	{
		this.requestIdentifier = requestIdentifier;
	}
	
	/**
	 * This variable is to identify if the request is quarantined or Dequarantined
	 * @return  acceptReject If acceptReject=1 then request for accept if acceptReject =2 then req for dequarantine
	 */
	public long getAcceptReject()
	{
		return acceptReject;
	}
	
	/**
	 * This variable is to identify if the request is quarantined or Dequarantined
	 * @param acceptReject If acceptReject=1 then request for accept if acceptReject =2 then req for dequarantine
	 */
	public void setAcceptReject(long acceptReject)
	{
		this.acceptReject = acceptReject;
	}

	/**
	 * String for User name
	 * @return userName Full name of user
	 */
	public String getUserName()
	{
		return userName;
	}

	/**
	 * String for User name
	 * @param userName Full name of the User
	 */
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
}
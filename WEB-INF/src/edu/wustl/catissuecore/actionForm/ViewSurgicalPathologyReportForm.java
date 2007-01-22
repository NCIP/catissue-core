package edu.wustl.catissuecore.actionForm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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
	protected String firstName;
	protected String lastName;
	protected String birthDate;
	protected String deathDate;
	protected String ethinicity;
	protected Collection race;
	protected String gender;
	protected String sexGenotype;
	protected String socialSecurityNumber;
	protected Collection medicalIdentifierNumbers;
		
	protected String identifiedReportAccessionNumber;
	protected String identifiedReportSite;
	private long identifiedReportId;
	protected String identifiedReportTextContent;
	
	protected String deIdentifiedReportAccessionNumber;
	protected String deIdentifiedReportSite;
	private long deIdentifiedReportId;
	protected String deIdentifiedReportTextContent;
	protected String comments;
	
	protected Map values = new HashMap();
	private int counter=1;
	private long id;
	
	/**
	 * @return date of birth of particicpant
	 */
	public String getBirthDate()
	{
		return birthDate;
	}
	
	/**
	 * @param dateOfBirth date of birth
	 */
	public void setBirthDate(String dateOfBirth)
	{
		this.birthDate = dateOfBirth;
	}
	
	/**
	 * @return date Of Death of particicpant
	 */
	public String getDeathDate()
	{
		return deathDate;
	}
	
	/**
	 * @param dateOfDeath date Of Death
	 */
	public void setDeathDate(String dateOfDeath)
	{
		this.deathDate = dateOfDeath;
	}
	
	/**
	 * @return ethinicity of particicpant
	 */
	public String getEthinicity()
	{
		return ethinicity;
	}
	
	/**
	 * @param ethinicity 
	 */
	public void setEthinicity(String ethinicity)
	{
		this.ethinicity = ethinicity;
	}
	
	/**
	 * @return first name of particicpant
	 */
	public String getFirstName()
	{
		return firstName;
	}
	
	/**
	 * @param firstName 
	 */
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	
	/**
	 * @return gender of particicpant
	 */
	public String getGender()
	{
		return gender;
	}
	
	/**
	 * @param gender
	 */
	public void setGender(String gender)
	{
		this.gender = gender;
	}
	
	/**
	 * @return last name of particicpant
	 */
	public String getLastName()
	{
		return lastName;
	}
	
	/**
	 * @param lastName
	 */
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
	
	/**
	 * @return collection of MedicalIdentifierNumbers of particicpant
	 */
	public Collection getMedicalIdentifierNumbers()
	{
		return medicalIdentifierNumbers;
	}
	
	/**
	 * @param medicalIdentifierNumbers
	 */
	public void setMedicalIdentifierNumbers(Collection medicalIdentifierNumbers)
	{
		this.medicalIdentifierNumbers = medicalIdentifierNumbers;
	}
	
	/**
	 * @return collection of race of particicpant
	 */
	public Collection getRace()
	{
		return race;
	}
	
	/**
	 * @param race
	 */
	public void setRace(Collection race)
	{
		this.race = race;
	}
	
	/**
	 * @return sex genotype of particicpant
	 */
	public String getSexGenotype()
	{
		return sexGenotype;
	}
	
	/**
	 * @param sexGenotype
	 */
	public void setSexGenotype(String sexGenotype)
	{
		this.sexGenotype = sexGenotype;
	}
	
	/**
	 * @return Social Security Number of particicpant
	 */
	public String getSocialSecurityNumber()
	{
		return socialSecurityNumber;
	}

	/**
	 * @param socialsecurityNumber
	 */
	public void setSocialSecurityNumber(String socialsecurityNumber)
	{
		this.socialSecurityNumber = socialsecurityNumber;
	}
	
	/**
	 * @return Accession Number of Identified Report
	 */
	public String getIdentifiedReportAccessionNumber()
	{
		return identifiedReportAccessionNumber;
	}
	
	/**
	 * @param accessionNumber
	 */
	public void setIdentifiedReportAccessionNumber(String accessionNumber)
	{
		this.identifiedReportAccessionNumber = accessionNumber;
	}
	
	/**
	 * @return comments for review or quarantine
	 */
	public String getComments()
	{
		return comments;
	}
	
	/**
	 * @param comments
	 */
	public void setComments(String comments)
	{
		this.comments = comments;
	}
	
	/**
	 * @return site of Identified Report
	 */
	public String getIdentifiedReportSite()
	{
		return identifiedReportSite;
	}
	
	/**
	 * @param site
	 */
	public void setIdentifiedReportSite(String site)
	{
		this.identifiedReportSite = site;
	}
	
	/**
	 * Constructor
	 */
	public ViewSurgicalPathologyReportForm()
	{
		reset();
	}

	/** (non-Javadoc)
	 * @see edu.wustl.common.actionForm.AbstractActionForm#reset()
	 * Reset all values of form
	 */
	protected void reset() 
	{
/*		this.firstName=null;
		this.lastName=null;
		this.birthDate=null;
		this.deathDate=null;
		this.ethinicity=null;
		this.race=null;
		this.gender=null;
		this.sexGenotype=null;
		this.socialSecurityNumber=null;
		this.medicalIdentifierNumbers=null;
			
		this.reportId=null;
		this.identifiedReportAccessionNumber=null;
		this.identifiedReportSite=null;
		this.identifiedReportId=0;
		this.identifiedReportTextContent=null;
		
		this.deIdentifiedReportAccessionNumber=null;
		this.deIdentifiedReportSite=null;
		this.deIdentifiedReportId=0;
		this.deIdentifiedReportTextContent=null;
		this.values=null;
		this.counter=0;
		this.comments=null;
		this.id=0;	
		
*/
	}

	/** (non-Javadoc)
	 * @see edu.wustl.common.actionForm.AbstractActionForm#setAllValues(edu.wustl.common.domain.AbstractDomainObject)
	 * Set values of all variables of form
	 */
	public void setAllValues(AbstractDomainObject abstractDomain) 
	{
		IdentifiedSurgicalPathologyReport ispr=(IdentifiedSurgicalPathologyReport)abstractDomain;
		setIdentifiedReport(ispr);
		if(ispr!=null && ispr.getSpecimenCollectionGroup()!=null)
		{
			setDeIdentifiedReport(ispr.getDeidentifiedSurgicalPathologyReport());
			setParticipant(ispr.getSpecimenCollectionGroup().getParticipant());
		}
		else
		{
			setDeIdentifiedReport(null);
		}
		this.comments=null;
	}
	
	/**
	 * @param deidentifiedSurgicalPathologyReport
	 * set values of De-Identified report related variables
	 */
	public void setDeIdentifiedReport(DeidentifiedSurgicalPathologyReport deidentifiedSurgicalPathologyReport)
	{
		try
		{
			this.deIdentifiedReportId=deidentifiedSurgicalPathologyReport.getId();
			this.deIdentifiedReportAccessionNumber=deidentifiedSurgicalPathologyReport.getAccessionNumber();
			this.deIdentifiedReportTextContent=deidentifiedSurgicalPathologyReport.getTextContent().getData();
			this.deIdentifiedReportSite=deidentifiedSurgicalPathologyReport.getSource().getName();
		}
		catch(NullPointerException ex)
		{
			Logger.out.error("viewSPR:De-identified Report information is null");
			if(this.deIdentifiedReportTextContent==null)
			{
				this.deIdentifiedReportTextContent=new String("De-Identified Report Not Found !");
			}
		}
	}

	/**
	 * @param ispr identified Surgical pathology report
	 *  set values of Identified report related variables
	 */
	public void setIdentifiedReport(IdentifiedSurgicalPathologyReport ispr)
	{
		try
		{
			this.identifiedReportId=ispr.getId();
			this.identifiedReportAccessionNumber=ispr.getAccessionNumber();
			this.identifiedReportTextContent=ispr.getTextContent().getData();	
			this.identifiedReportSite=ispr.getSource().getName();
		}
		catch(NullPointerException ex)
		{
			Logger.out.error("viewSPR:Identified Report information is null");
			if(this.identifiedReportTextContent==null)
			{
				this.identifiedReportTextContent=new String("Identified Report Not Found !");
			}
		}
	}

	/**
	 * @param p participant
	 *  set values of participant related variables
	 */
	public void setParticipant(final Participant p)
	{
		try
		{
		this.firstName=p.getFirstName();
		this.lastName=p.getLastName();
		if(p.getBirthDate()!=null)
		{
			this.birthDate=Utility.parseDateToString(p.getBirthDate(), Constants.DATE_PATTERN_MM_DD_YYYY);
		}
		if(p.getDeathDate()!=null)
		{
			this.deathDate=Utility.parseDateToString(p.getDeathDate(), Constants.DATE_PATTERN_MM_DD_YYYY);
		}
		this.ethinicity=p.getEthnicity();
		this.race=p.getRaceCollection();
		this.gender=p.getGender();
		this.sexGenotype=p.getSexGenotype();
		this.socialSecurityNumber=p.getSocialSecurityNumber();
		this.medicalIdentifierNumbers=p.getParticipantMedicalIdentifierCollection();
		
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
				String key3 = "ParticipantMedicalIdentifier:" + i +"_id";

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
	}
	
	/** (non-Javadoc)
	 * @see edu.wustl.common.actionForm.AbstractActionForm#getFormId()
	 * On the basis of Request for submitting comments different form ID will be returned to save two different kind of comments
	 * review comment and quarantine comments
	 */
	public int getFormId()
	{	
		if(this.submittedFor.equalsIgnoreCase("quarantine"))
		{
			return Constants.DEIDENTIFIED_SURGICAL_PATHOLOGY_REPORT_FORM_ID;
		}
		return Constants.SURGICAL_PATHOLOGY_REPORT_FORM_ID;
	}
	
	/**
	 * @return text content of deidentified report
	 */
	public String getDeIdentifiedReportTextContent()
	{
		return deIdentifiedReportTextContent;
	}
	
	/**
	 * @param deIdentifiedReportTextContent
	 */
	public void setDeIdentifiedReportTextContent(String deIdentifiedReportTextContent)
	{
		this.deIdentifiedReportTextContent = deIdentifiedReportTextContent;
	}
	
	/**
	 * @return text content of identified report
	 */
	public String getIdentifiedReportTextContent()
	{
		return identifiedReportTextContent;
	}
	
	/**
	 * @param identifiedReportTextContent
	 */
	public void setIdentifiedReportTextContent(String identifiedReportTextContent)
	{
		this.identifiedReportTextContent = identifiedReportTextContent;
	}
	
	/**
	 * @return counter
	 */
	public int getCounter()
	{
		return counter;
	}
	
	/**
	 * @param counter
	 */
	public void setCounter(int counter)
	{
		this.counter = counter;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setValue(String key, Object value) 
    {
		if (isMutable())values.put(key, value);
    }

	/**
	 * @param key
	 * @return
	 */
	public Object getValue(String key) 
    {
		return values.get(key);
    }
      
 	/**
 	 * @return
 	 */
 	public Collection getAllValues() 
 	{
 		return values.values();
 	}

 	/**
 	 * @param values
 	 */
 	public void setValues(Map values)
 	{
 		this.values = values;
 	}
 
 	/**
 	 * @return
 	 */
 	public Map getValues()
 	{
 		return this.values;
 	}

	/** (non-Javadoc)
	 * @see edu.wustl.common.actionForm.AbstractActionForm#getId()
	 * @return id identifier of current domain object
	 */
	public long getId()
	{
		return id;
	}
	
	/** (non-Javadoc)
	 * @see edu.wustl.common.actionForm.AbstractActionForm#setId(long)
	 */
	public void setId(long id)
	{
		this.id = id;
	}

	/**
	 * @return report id of Identified report
	 */
	public long getIdentifiedReportId()
	{
		return identifiedReportId;
	}
	
	/**
	 * @param currentReportId
	 */
	public void setIdentifiedReportId(long currentReportId)
	{
		this.identifiedReportId = currentReportId;
	}
	
	/**
	 * @return accession number of de-identified report
	 */
	public String getDeIdentifiedReportAccessionNumber()
	{
		return deIdentifiedReportAccessionNumber;
	}
	
	/**
	 * @param deIdentifiedReportAccessionNumber
	 */
	public void setDeIdentifiedReportAccessionNumber(String deIdentifiedReportAccessionNumber)
	{
		this.deIdentifiedReportAccessionNumber = deIdentifiedReportAccessionNumber;
	}

	/**
	 * @return report id of de-identified report
	 */
	public long getDeIdentifiedReportId()
	{
		return deIdentifiedReportId;
	}
	
	/**
	 * @param deIdentifiedReportId
	 */
	public void setDeIdentifiedReportId(long deIdentifiedReportId)
	{
		this.deIdentifiedReportId = deIdentifiedReportId;
	}
	
	/**
	 * @return site of de-identified report
	 */
	public String getDeIdentifiedReportSite()
	{
		return deIdentifiedReportSite;
	}
	
	/**
	 * @param deIdentifiedReportSite
	 */
	public void setDeIdentifiedReportSite(String deIdentifiedReportSite)
	{
		this.deIdentifiedReportSite = deIdentifiedReportSite;
	}
}
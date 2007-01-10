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
	protected Collection reportId;
	
	public String getBirthDate()
	{
		return birthDate;
	}
	
	public void setBirthDate(String dateOfBirth)
	{
		this.birthDate = dateOfBirth;
	}
	
	public String getDeathDate()
	{
		return deathDate;
	}
	
	public void setDeathDate(String dateOfDeath)
	{
		this.deathDate = dateOfDeath;
	}
	
	public String getEthinicity()
	{
		return ethinicity;
	}
	
	public void setEthinicity(String ethinicity)
	{
		this.ethinicity = ethinicity;
	}
	
	public String getFirstName()
	{
		return firstName;
	}
	
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	
	public String getGender()
	{
		return gender;
	}
	
	public void setGender(String gender)
	{
		this.gender = gender;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
	
	public Collection getMedicalIdentifierNumbers()
	{
		return medicalIdentifierNumbers;
	}
	
	public void setMedicalIdentifierNumbers(Collection medicalIdentifierNumbers)
	{
		this.medicalIdentifierNumbers = medicalIdentifierNumbers;
	}
	
	public Collection getRace()
	{
		return race;
	}
	
	public void setRace(Collection race)
	{
		this.race = race;
	}
	
	public String getSexGenotype()
	{
		return sexGenotype;
	}
	
	public void setSexGenotype(String sexGenotype)
	{
		this.sexGenotype = sexGenotype;
	}
	
	public String getSocialSecurityNumber()
	{
		return socialSecurityNumber;
	}

	public void setSocialSecurityNumber(String socialsecurityNumber)
	{
		this.socialSecurityNumber = socialsecurityNumber;
	}
	
	public String getIdentifiedReportAccessionNumber()
	{
		return identifiedReportAccessionNumber;
	}
	
	public void setIdentifiedReportAccessionNumber(String accessionNumber)
	{
		this.identifiedReportAccessionNumber = accessionNumber;
	}
	
	public String getComments()
	{
		return comments;
	}
	
	public void setComments(String comments)
	{
		this.comments = comments;
	}
	
	public String getIdentifiedReportSite()
	{
		return identifiedReportSite;
	}
	
	public void setIdentifiedReportSite(String site)
	{
		this.identifiedReportSite = site;
	}
	
	public ViewSurgicalPathologyReportForm()
	{
		reset();
	}

	protected void reset() 
	{
		this.firstName=null;
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
	}

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
			this.deIdentifiedReportTextContent=new String("De-Identified Report Not Found !");
		}
	}

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
			this.identifiedReportTextContent=new String("Identified Report Not Found !");
		}
	}

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
	
	public int getFormId()
	{	
		if(this.submittedFor.equalsIgnoreCase("quarantine"))
		{
			return Constants.DEIDENTIFIED_SURGICAL_PATHOLOGY_REPORT_FORM_ID;
		}
		return Constants.SURGICAL_PATHOLOGY_REPORT_FORM_ID;
	}
	
	public String getDeIdentifiedReportTextContent()
	{
		return deIdentifiedReportTextContent;
	}
	
	public void setDeIdentifiedReportTextContent(String deIdentifiedReportTextContent)
	{
		this.deIdentifiedReportTextContent = deIdentifiedReportTextContent;
	}
	
	public String getIdentifiedReportTextContent()
	{
		return identifiedReportTextContent;
	}
	
	public void setIdentifiedReportTextContent(String identifiedReportTextContent)
	{
		this.identifiedReportTextContent = identifiedReportTextContent;
	}
	
	public int getCounter()
	{
		return counter;
	}
	
	public void setCounter(int counter)
	{
		this.counter = counter;
	}
	
	public void setValue(String key, Object value) 
    {
		if (isMutable())values.put(key, value);
    }

	public Object getValue(String key) 
    {
		return values.get(key);
    }
      
 	public Collection getAllValues() 
 	{
 		return values.values();
 	}

 	public void setValues(Map values)
 	{
 		this.values = values;
 	}
 
 	public Map getValues()
 	{
 		return this.values;
 	}

	public long getId()
	{
		return id;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}

	public Collection getReportId()
	{
		return reportId;
	}

	public void setReportId(Collection reportId)
	{
		this.reportId = reportId;
	}

	public long getIdentifiedReportId()
	{
		return identifiedReportId;
	}
	
	public void setIdentifiedReportId(long currentReportId)
	{
		this.identifiedReportId = currentReportId;
	}
	
	public String getDeIdentifiedReportAccessionNumber()
	{
		return deIdentifiedReportAccessionNumber;
	}
	
	public void setDeIdentifiedReportAccessionNumber(String deIdentifiedReportAccessionNumber)
	{
		this.deIdentifiedReportAccessionNumber = deIdentifiedReportAccessionNumber;
	}

	public long getDeIdentifiedReportId()
	{
		return deIdentifiedReportId;
	}
	
	public void setDeIdentifiedReportId(long deIdentifiedReportId)
	{
		this.deIdentifiedReportId = deIdentifiedReportId;
	}
	
	public String getDeIdentifiedReportSite()
	{
		return deIdentifiedReportSite;
	}
	
	public void setDeIdentifiedReportSite(String deIdentifiedReportSite)
	{
		this.deIdentifiedReportSite = deIdentifiedReportSite;
	}
}
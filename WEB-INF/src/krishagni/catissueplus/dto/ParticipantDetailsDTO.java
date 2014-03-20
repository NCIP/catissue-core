package krishagni.catissueplus.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;


public class ParticipantDetailsDTO
{
    private Long id;
    private String lastName;
    private String firstName;
    private String middleName;
    private Date birthDate;
    private String gender;
    private String sexGenotype;
    private List<String> raceCollection = new ArrayList<String>();
    private String ethnicity;
    private String socialSecurityNumber;
    private String activityStatus;
    private Date deathDate;
    private String vitalStatus;
    private String empiIdStatus;
    private List<MedicalIdentifierDTO> medicalIdentifierList = new ArrayList<MedicalIdentifierDTO>();
    private List<CollectionProtocolRegistrationDTO> collectionProtocolRegistrationDTOList = new ArrayList<CollectionProtocolRegistrationDTO>();
    private String metaPhoneCode;
    private String empiId;
    private String lookUpMRNNumber;
    private String lookUpSiteName;
    private String ppidsString;//This is comma separated List
    private String cpTitles;//This are the comma separated List
    
    
    
    public String getPpidsString()
    {
        return ppidsString;
    }


    
    public void setPpidsString(String ppidsString)
    {
        this.ppidsString = ppidsString;
    }


    
    public String getCpTitles()
    {
        return cpTitles;
    }


    
    public void setCpTitles(String cpTitles)
    {
        this.cpTitles = cpTitles;
    }


    public String getLookUpMRNNumber()
    {
        return lookUpMRNNumber;
    }

    
    public void setLookUpMRNNumber(String lookUpMRNNumber)
    {
        this.lookUpMRNNumber = lookUpMRNNumber;
    }

    
    public String getLookUpSiteName()
    {
        return lookUpSiteName;
    }

    
    public void setLookUpSiteName(String lookUpSiteName)
    {
        this.lookUpSiteName = lookUpSiteName;
    }

    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getLastName()
    {
        return lastName;
    }
    
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }
    
    public String getFirstName()
    {
        return firstName;
    }
    
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }
    
    public String getMiddleName()
    {
        return middleName;
    }
    
    public void setMiddleName(String middleName)
    {
        this.middleName = middleName;
    }
    
    public Date getBirthDate()
    {
        return birthDate;
    }
    
    public void setBirthDate(Date birthDate)
    {
        this.birthDate = birthDate;
    }
    
    public String getGender()
    {
        return gender;
    }
    
    public void setGender(String gender)
    {
        this.gender = gender;
    }
    
    public String getSexGenotype()
    {
        return sexGenotype;
    }
    
    public void setSexGenotype(String sexGenotype)
    {
        this.sexGenotype = sexGenotype;
    }
    
    public List<String> getRaceCollection()
    {
        return raceCollection;
    }
    
    public void setRaceCollection(List<String> raceCollection)
    {
        this.raceCollection = raceCollection;
    }
    
    public String getEthnicity()
    {
        return ethnicity;
    }
    
    public void setEthnicity(String ethnicity)
    {
        this.ethnicity = ethnicity;
    }
    
    public String getSocialSecurityNumber()
    {
        return socialSecurityNumber;
    }
    
    public void setSocialSecurityNumber(String socialSecurityNumber)
    {
        this.socialSecurityNumber = socialSecurityNumber;
    }
    
    public String getActivityStatus()
    {
        return activityStatus;
    }
    
    public void setActivityStatus(String activityStatus)
    {
        this.activityStatus = activityStatus;
    }
    
    public Date getDeathDate()
    {
        return deathDate;
    }
    
    public void setDeathDate(Date deathDate)
    {
        this.deathDate = deathDate;
    }
    
    public String getVitalStatus()
    {
        return vitalStatus;
    }
    
    public void setVitalStatus(String vitalStatus)
    {
        this.vitalStatus = vitalStatus;
    }
    
    public String getEmpiIdStatus()
    {
        return empiIdStatus;
    }
    
    public void setEmpiIdStatus(String empiIdStatus)
    {
        this.empiIdStatus = empiIdStatus;
    }
    
    public List<MedicalIdentifierDTO> getMedicalIdentifierList()
    {
        return medicalIdentifierList;
    }
    
    public void setMedicalIdentifierList(List<MedicalIdentifierDTO> medicalIdentifierList)
    {
        this.medicalIdentifierList = medicalIdentifierList;
    }
    
    public List<CollectionProtocolRegistrationDTO> getCollectionProtocolRegistrationDTOList()
    {
        return collectionProtocolRegistrationDTOList;
    }
    
    public void setCollectionProtocolRegistrationDTOList(
            List<CollectionProtocolRegistrationDTO> collectionProtocolRegistrationDTOList)
    {
        this.collectionProtocolRegistrationDTOList = collectionProtocolRegistrationDTOList;
    }
    
    public String getMetaPhoneCode()
    {
        return metaPhoneCode;
    }
    
    public void setMetaPhoneCode(String metaPhoneCode)
    {
        this.metaPhoneCode = metaPhoneCode;
    }
    
    public String getEmpiId()
    {
        return empiId;
    }
    
    public void setEmpiId(String empiId)
    {
        this.empiId = empiId;
    }
    
    

}

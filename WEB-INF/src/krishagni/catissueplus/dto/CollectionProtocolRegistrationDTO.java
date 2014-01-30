package krishagni.catissueplus.dto;

import java.util.ArrayList;
import java.util.Date;

import edu.wustl.catissuecore.domain.User;


public class CollectionProtocolRegistrationDTO
{
    private Long cprId;
    private String protocolParticipantIdentifier;
    private Date registrationDate;
    private Long participantId;
    private Long cpId;
    private String activityStatus;
    private String signedConsentDocumentURL;
    private Date consentSignatureDate;
    private String consentWitnessName;
    private ArrayList<ConsentResponseDTO> consentResponseList = new ArrayList<ConsentResponseDTO>();
    private String consentWithdrawalOption;
    private String isConsentAvailable;
    private String barcode;
    private String irbID;
    
    
    
    
    public String getIrbID()
    {
        return irbID;
    }


    
    public void setIrbID(String irbID)
    {
        this.irbID = irbID;
    }


    public String getConsentWitnessName()
    {
        return consentWitnessName;
    }

    
    public void setConsentWitnessName(String consentWitnessName)
    {
        this.consentWitnessName = consentWitnessName;
    }

    public Long getCprId()
    {
        return cprId;
    }
    
    public void setCprId(Long cprId)
    {
        this.cprId = cprId;
    }
    
    public String getProtocolParticipantIdentifier()
    {
        return protocolParticipantIdentifier;
    }
    
    public void setProtocolParticipantIdentifier(String protocolParticipantIdentifier)
    {
        this.protocolParticipantIdentifier = protocolParticipantIdentifier;
    }
    
    public Date getRegistrationDate()
    {
        return registrationDate;
    }
    
    public void setRegistrationDate(Date registrationDate)
    {
        this.registrationDate = registrationDate;
    }
    
    public Long getParticipantId()
    {
        return participantId;
    }
    
    public void setParticipantId(Long participantId)
    {
        this.participantId = participantId;
    }
    
    public Long getCpId()
    {
        return cpId;
    }
    
    public void setCpId(Long cpId)
    {
        this.cpId = cpId;
    }
    
    public String getActivityStatus()
    {
        return activityStatus;
    }
    
    public void setActivityStatus(String activityStatus)
    {
        this.activityStatus = activityStatus;
    }
    
    public String getSignedConsentDocumentURL()
    {
        return signedConsentDocumentURL;
    }
    
    public void setSignedConsentDocumentURL(String signedConsentDocumentURL)
    {
        this.signedConsentDocumentURL = signedConsentDocumentURL;
    }
    
    public Date getConsentSignatureDate()
    {
        return consentSignatureDate;
    }
    
    public void setConsentSignatureDate(Date consentSignatureDate)
    {
        this.consentSignatureDate = consentSignatureDate;
    }
    
    
    public ArrayList<ConsentResponseDTO> getConsentResponseDTOList()
    {
        return consentResponseList;
    }
    
    public void setConsentResponseDTOList(ArrayList<ConsentResponseDTO> consentTierList)
    {
        this.consentResponseList = consentTierList;
    }
    
    public String getConsentWithdrawalOption()
    {
        return consentWithdrawalOption;
    }
    
    public void setConsentWithdrawalOption(String consentWithdrawalOption)
    {
        this.consentWithdrawalOption = consentWithdrawalOption;
    }
    
    public String getIsConsentAvailable()
    {
        return isConsentAvailable;
    }
    
    public void setIsConsentAvailable(String isConsentAvailable)
    {
        this.isConsentAvailable = isConsentAvailable;
    }
    
    public String getBarcode()
    {
        return barcode;
    }
    
    public void setBarcode(String barcode)
    {
        this.barcode = barcode;
    }
    
    
}

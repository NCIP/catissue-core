package krishagni.catissueplus.dto;


public class ConsentTierDTO
{
    private Long id;
    private String consentStatment;
    
    public Long getId()
    {
        return id;
        
    }
    
    public void setId(Long consentId)
    {
        this.id = consentId;
    }
    
    public String getConsentStatment()
    {
        return consentStatment;
    }
    
    public void setConsentStatment(String consentStatement)
    {
        this.consentStatment = consentStatement;
    }
    
    
}

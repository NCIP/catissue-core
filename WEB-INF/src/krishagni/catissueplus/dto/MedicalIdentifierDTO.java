package krishagni.catissueplus.dto;


public class MedicalIdentifierDTO
{
    private String mrnValue;
    private Long siteId;
    private String siteName;
    
    public String getMrnValue()
    {
        return mrnValue;
    }
    
    public void setMrnValue(String mrnValue)
    {
        this.mrnValue = mrnValue;
    }

    
    public Long getSiteId()
    {
        return siteId;
    }

    
    public void setSiteId(Long siteId)
    {
        this.siteId = siteId;
    }

    
    public String getSiteName()
    {
        return siteName;
    }

    
    public void setSiteName(String siteName)
    {
        this.siteName = siteName;
    }

    
      
       

}

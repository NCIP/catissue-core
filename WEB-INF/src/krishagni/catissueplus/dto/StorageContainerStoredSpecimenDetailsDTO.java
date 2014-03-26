
package krishagni.catissueplus.dto;

import java.util.Date;

public class StorageContainerStoredSpecimenDetailsDTO
{

    private Date dateOfSpecimenCount;
    private Long specimenCount;
    private Long percentUtilization;
    private Long capacity;
    private Long siteId;
    private String siteName;
    
    
    
    public String getSiteName()
    {
        return siteName;
    }


    
    public void setSiteName(String siteName)
    {
        this.siteName = siteName;
    }


    public Long getSiteId()
    {
        return siteId;
    }

    
    public void setSiteId(Long siteId)
    {
        this.siteId = siteId;
    }

    
    public Long getSpecimenCount()
    {
        return specimenCount;
    }



    public Long getCapacity()
    {
        return capacity;
    }


    
    public void setCapacity(Long capacity)
    {
        this.capacity = capacity;
    }


    
    public void setSpecimenCount(Long specimenCount)
    {
        this.specimenCount = specimenCount;
    }


    
    public void setPercentUtilization(Long percentUtilization)
    {
        this.percentUtilization = percentUtilization;
    }


    public Long getPercentUtilization()
    {
        return percentUtilization;
    }

    
  

    /**
     * @return the dateOfSpecimenCount
     */
    public Date getDateOfSpecimenCount()
    {
        return dateOfSpecimenCount;
    }

    /**
     * @param dateOfSpecimenCount the dateOfSpecimenCount to set
     */
    public void setDateOfSpecimenCount(Date dateOfSpecimenCount)
    {
        this.dateOfSpecimenCount = dateOfSpecimenCount;
    }



}

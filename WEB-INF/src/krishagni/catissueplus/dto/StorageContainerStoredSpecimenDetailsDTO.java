
package krishagni.catissueplus.dto;

import java.util.Date;

public class StorageContainerStoredSpecimenDetailsDTO
{

    private Date dateOfSpecimenCount;
    private Integer specimenCount;
    private long percentUtilization;

    
    public long getPercentUtilization()
    {
        return percentUtilization;
    }

    
    public void setPercentUtilization(long percentUtilization)
    {
        this.percentUtilization = percentUtilization;
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

    /**
     * @return the specimenCount
     */
    public Integer getSpecimenCount()
    {
        return specimenCount;
    }

    /**
     * @param specimenCount the specimenCount to set
     */
    public void setSpecimenCount(Integer specimenCount)
    {
        this.specimenCount = specimenCount;
    }


}


package krishagni.catissueplus.dto;

import java.util.ArrayList;

public class StorageContainerUtilizationDetailsDTO
{

    private String containerName;
    private ArrayList<StorageContainerStoredSpecimenDetailsDTO> StorageContainerStoredSpecimenDetailsDTOList = new ArrayList<StorageContainerStoredSpecimenDetailsDTO>();

    /**
     * @return the containerName
     */
    public String getContainerName()
    {
        return containerName;
    }

    /**
     * @param containerName the containerName to set
     */
    public void setContainerName(String containerName)
    {
        this.containerName = containerName;
    }

    /**
     * @return the storageContainerStoredSpecimenDetailsDTOList
     */
    public ArrayList<StorageContainerStoredSpecimenDetailsDTO> getStorageContainerStoredSpecimenDetailsDTOList()
    {
        return StorageContainerStoredSpecimenDetailsDTOList;
    }

    /**
     * @param storageContainerStoredSpecimenDetailsDTOList the storageContainerStoredSpecimenDetailsDTOList to set
     */
    public void setStorageContainerStoredSpecimenDetailsDTOList(
            ArrayList<StorageContainerStoredSpecimenDetailsDTO> storageContainerStoredSpecimenDetailsDTOList)
    {
        StorageContainerStoredSpecimenDetailsDTOList = storageContainerStoredSpecimenDetailsDTOList;
    }

}


package krishagni.catissueplus.bizlogic;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import krishagni.catissueplus.dao.StorageContainerGraphDAO;
import krishagni.catissueplus.dto.StorageContainerStoredSpecimenDetailsDTO;
import krishagni.catissueplus.dto.StorageContainerUtilizationDTO;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;

public class StorageContainerGraphBizlogic
{
    //global variable to keep count of stored specimens
    private static Integer totalCount = new Integer(0);
    //global variable to keep count of container capacity
    private static Integer totalCapacity = new Integer(0);

    /**
     * Recursively gets all children of container and sets count of specimens and capacity
     * @param containerId
     * @param dao
     * @return
     * @throws ApplicationException
     */
    private void updateContainerSpecimenAndCapacityCounts( StorageContainerUtilizationDTO storageContainerUtilizationDTO,Long containerId, HibernateDAO dao)
            throws ApplicationException
    {
        StorageContainerGraphDAO storageContainerGraphDAO = new StorageContainerGraphDAO();
        Long specCount = storageContainerGraphDAO.getNumberOfSpecimensAssigned(containerId, dao);
        if (specCount != null)
        {
            specCount = specCount + storageContainerUtilizationDTO.getNumberOfSpecimensAssigned();
            storageContainerUtilizationDTO.setNumberOfSpecimensAssigned(specCount);
        }

        Integer capacity = storageContainerGraphDAO.getStorageTypeCapacityCountFromContainerId(containerId, dao);
        if (capacity != null)
        {
           Long totalCapacity = storageContainerUtilizationDTO.getTotalStorageCapacity()+capacity;
            storageContainerUtilizationDTO.setTotalStorageCapacity(totalCapacity);
        }

        List<Long> childContainerList = new StorageContainerGraphDAO().getListOfChildContainerId(containerId, dao);

        for (Long childId : childContainerList)
        {
            updateContainerSpecimenAndCapacityCounts(storageContainerUtilizationDTO,childId, dao);//recursive call to find children of child container
        }
    }

    public List<StorageContainerStoredSpecimenDetailsDTO> getSiteUtilizationDTO(HibernateDAO hibernateDAO) throws ApplicationException, SQLException{
        StorageContainerGraphDAO storageContainerDAO = new StorageContainerGraphDAO();
        List<StorageContainerStoredSpecimenDetailsDTO> storageContainerStoredSpecimenDetailsDTO = storageContainerDAO.getSiteUtilizationList(hibernateDAO);
       return storageContainerStoredSpecimenDetailsDTO;
    }
   
    /**
    * Returns map containing specimen counts, capacity and percentage of utilization of a container
    * @param containerId
    * @param hibernateDAO
    * @return
    * @throws ApplicationException 
    */
    public StorageContainerUtilizationDTO getStorageContainerUtilizationDTO(Long containerId, HibernateDAO hibernateDAO)
            throws ApplicationException
    {
        //recursive call to find all children and set specimen count and total capacity count
        StorageContainerUtilizationDTO storageContainerUtilizationDTO = new StorageContainerUtilizationDTO();
        storageContainerUtilizationDTO.setContainerId(containerId);
        updateContainerSpecimenAndCapacityCounts(storageContainerUtilizationDTO,containerId, hibernateDAO);
        storageContainerUtilizationDTO.setSiteId(new StorageContainerGraphDAO().getSiteIdFromContainerName(hibernateDAO, containerId));

        return storageContainerUtilizationDTO;
    }

    /**
     * Inserts number of specimens in top most parent container to catissue_stor_cont_spec_counts table.
     * @param hibernateDAO
     * @throws DAOException
     * @throws ApplicationException
     * @throws SQLException 
     */
    public void saveSpecimenCountsOfParentContainer(HibernateDAO hibernateDAO) throws DAOException,
            ApplicationException, SQLException
    {
        StorageContainerGraphDAO storageContainerDAO = new StorageContainerGraphDAO();
        //get id list of all top most parent storage containers
        List<Long> parentStorageContainerIdList = storageContainerDAO.getIdOfAllContainers(hibernateDAO);

        for (Long containerId : parentStorageContainerIdList)
        {
            //get utilization DTO for the container
            StorageContainerUtilizationDTO storageContainerUtilizationDTO = getStorageContainerUtilizationDTO(containerId, hibernateDAO);
            storageContainerDAO.insertIntoStorContSpecCountsTable(storageContainerUtilizationDTO, hibernateDAO);
        }
    }

   
}

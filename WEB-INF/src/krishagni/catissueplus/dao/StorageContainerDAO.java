
package krishagni.catissueplus.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import krishagni.catissueplus.dto.StorageContainerStoredSpecimenDetailsDTO;
import krishagni.catissueplus.dto.StorageContainerUtilizationDTO;
import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;

public class StorageContainerDAO
{

    /**
     * Returns list of child container id.
     * @param containerId
     * @param dao
     * @return
     * @throws DAOException
     */
    public List<Long> getListOfChildContainerId(Long containerId, HibernateDAO dao) throws DAOException
    {
        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("0", new NamedQueryParam(DBTypes.LONG, containerId));

        //get child storage container ids
        List<Long> childContainerList = dao.executeNamedQuery("getChildContainerIds", params);
        return childContainerList;
    }

    /**
     * Returns number of specimens stored in container
     * @param containerId
     * @param dao
     * @return
     * @throws DAOException
     */
    public Integer getNumberOfSpecimensAssigned(Long containerId, HibernateDAO dao) throws DAOException
    {
        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("0", new NamedQueryParam(DBTypes.LONG, containerId));

        //get count of specimens stored in the container
        List<Integer> allocatedList = dao.executeNamedQuery("getAssignedSpecimenCount", params);
        if (allocatedList == null || allocatedList.isEmpty())
        {
            return null;
        }
        return allocatedList.get(0);
    }

    /**
     * Returns capacity of storage type of container
     * @param containerId
     * @param dao
     * @return
     * @throws DAOException
     */
    public Integer getStorageTypeCapacityCountFromContainerId(Long containerId, HibernateDAO dao) throws DAOException
    {
        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("0", new NamedQueryParam(DBTypes.LONG, containerId));

        //get capacity of storage type which can hold specimens from container id
        List<Capacity> containerList = dao.executeNamedQuery("getStorageTypeCapacityFromContainerId", params);

        if (containerList == null || containerList.isEmpty())
        {
            return null;
        }

        Capacity capacity = containerList.get(0);

        return capacity.getOneDimensionCapacity() * capacity.getTwoDimensionCapacity();
    }

    /**
     * Returns Id List of all top most parent storage container
     * @param dao
     * @return
     * @throws DAOException
     */
    public List<Long> getIdOfAllParentStorageContainers(HibernateDAO dao) throws DAOException
    {
        //get capacity of storage type which can hold specimens from container id
        List<Long> parentContList = dao.executeNamedQuery("getIdOfParentStorageContainers", null);

        return parentContList;
    }

    /**
     * Inserts into catissue_stor_cont_spec_counts table with named SQL query
     * @param storageContainerUtilizationDTO
     * @param hibernateDAO
     * @throws DAOException
     * @throws SQLException
     */
    public void insertIntoStorContSpecCountsTable(StorageContainerUtilizationDTO storageContainerUtilizationDTO,
            HibernateDAO hibernateDAO) throws DAOException, SQLException
    {
        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("1", new NamedQueryParam(DBTypes.LONG, storageContainerUtilizationDTO.getContainerId()));
        params.put("2",
                new NamedQueryParam(DBTypes.INTEGER, storageContainerUtilizationDTO.getNumberOfSpecimensAssigned()));
        params.put("3", new NamedQueryParam(DBTypes.DATE, new java.sql.Date(new Date().getTime())));

        Long utilizationPercentage = new Long(0);
        if (storageContainerUtilizationDTO.getTotalStorageCapacity() > 0)
        {
            utilizationPercentage = Math.round(((storageContainerUtilizationDTO.getNumberOfSpecimensAssigned()
                    .doubleValue() / storageContainerUtilizationDTO.getTotalStorageCapacity().doubleValue()) * 100));
        }

        params.put("4", new NamedQueryParam(DBTypes.INTEGER, utilizationPercentage));

        hibernateDAO.executeUpdateWithNamedSQLQuery("insertIntoStorContSpecCountsTable", params);
    }

    /**
     * Returns container name
     * @param hibernateDAO 
     * @param contId
     * @return
     * @throws DAOException 
     */
    public String getContainerNameById(HibernateDAO hibernateDAO, Long containerId) throws DAOException
    {
        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("0", new NamedQueryParam(DBTypes.LONG, containerId));

        List<String> list = hibernateDAO.executeNamedQuery("getContainerNameById", params);

        if (list == null || list.isEmpty())
        {
            return null;
        }

        return list.get(0);
    }

    /**
     * Returns StorageContainerStoredSpecimenDetailsDTOList by STORAGE_CONTAINER_ID
     * @param hibernateDAO
     * @param containerId
     * @return
     * @throws DAOException
     * @throws SQLException
     */
    public ArrayList<StorageContainerStoredSpecimenDetailsDTO> getStorageContainerStoredSpecimenDetailsDTOById(
            HibernateDAO hibernateDAO, Long containerId) throws DAOException, SQLException
    {
        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("1", new NamedQueryParam(DBTypes.LONG, containerId));

        ArrayList<StorageContainerStoredSpecimenDetailsDTO> storageContainerStoredSpecimenDetailsDTOList = new ArrayList<StorageContainerStoredSpecimenDetailsDTO>();

        ResultSet resultSet = hibernateDAO.executeNamedSQLQuery("getStorContSpecCountDataById", params);

        while (resultSet.next())
        {
            //populate StorageContainerStoredSpecDateCountDTO
            StorageContainerStoredSpecimenDetailsDTO storageContainerStoredSpecimenDetailsDTO = new StorageContainerStoredSpecimenDetailsDTO();
            storageContainerStoredSpecimenDetailsDTO.setDateOfSpecimenCount(resultSet.getDate(1));
            storageContainerStoredSpecimenDetailsDTO.setSpecimenCount(resultSet.getInt(2));
            storageContainerStoredSpecimenDetailsDTO.setPercentUtilization(resultSet.getInt(3));
            storageContainerStoredSpecimenDetailsDTOList.add(storageContainerStoredSpecimenDetailsDTO);
        }
        return storageContainerStoredSpecimenDetailsDTOList;

    }

    /**
     * Returns StorageContainerStoredSpecimenDetailsDTOList by STORAGE_CONTAINER_ID
     * @param hibernateDAO
     * @param containerId
     * @return
     * @throws DAOException
     * @throws SQLException
     */
    public StorageContainerStoredSpecimenDetailsDTO getUtilizationCountsOfContainerById(HibernateDAO hibernateDAO,
            Long containerId) throws DAOException, SQLException
    {
        StorageContainerStoredSpecimenDetailsDTO storageContainerStoredSpecimenDetailsDTO = new StorageContainerStoredSpecimenDetailsDTO();
        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("1", new NamedQueryParam(DBTypes.LONG, containerId));
        params.put("2", new NamedQueryParam(DBTypes.LONG, containerId));

        ResultSet resultSet = hibernateDAO.executeNamedSQLQuery("getUtilizationCountsOfContainerById", params);

        while (resultSet.next())
        {
            storageContainerStoredSpecimenDetailsDTO.setPercentUtilization(resultSet.getLong(1));
            storageContainerStoredSpecimenDetailsDTO.setSpecimenCount(resultSet.getInt(2));

        }

        return storageContainerStoredSpecimenDetailsDTO;

    }

    /**
     * Returns List of ids of top most parent storage containers of site
     * @param hibernateDAO
     * @param siteName
     * @return
     * @throws DAOException
     */
    public List<Long> getIdOfAllParentStorageContainersBySiteName(HibernateDAO hibernateDAO, String siteName)
            throws DAOException
    {
        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("0", new NamedQueryParam(DBTypes.STRING, siteName));

        List<Long> parentContList = hibernateDAO.executeNamedQuery("getIdOfParentStorageContainersBySiteName", params);

        return parentContList;
    }
}

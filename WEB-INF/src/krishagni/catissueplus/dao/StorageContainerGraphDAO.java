
package krishagni.catissueplus.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import krishagni.catissueplus.dto.StorageContainerStoredSpecimenDetailsDTO;
import krishagni.catissueplus.dto.StorageContainerUtilizationDTO;
import krishagni.catissueplus.dto.StorageContainerUtilizationDetailsDTO;
import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.MySQLDAOImpl;
import edu.wustl.dao.OracleDAOImpl;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;

public class StorageContainerGraphDAO
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
     * @param hibernateDAO
     * @return
     * @throws DAOException
     */
    public Long getNumberOfSpecimensAssigned(Long containerId, HibernateDAO hibernateDAO) throws DAOException
    {
        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("0", new NamedQueryParam(DBTypes.LONG, containerId));

        //get count of specimens stored in the container
        List<Integer> allocatedList = hibernateDAO.executeNamedQuery("getAssignedSpecimenCount", params);
        if (allocatedList == null || allocatedList.isEmpty())
        {
            return null;
        }
        return  allocatedList.get(0).longValue();
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
     * Returns Id List of all top most parent storage container
     * @param dao
     * @return
     * @throws DAOException
     */
    public List<Long> getIdOfAllContainers(HibernateDAO dao) throws DAOException
    {
        //get capacity of storage type which can hold specimens from container id
        List<Long> parentContList = dao.executeNamedQuery("getIdOfAllContainers", null);

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
                new NamedQueryParam(DBTypes.LONG, storageContainerUtilizationDTO.getNumberOfSpecimensAssigned()));
        params.put("3", new NamedQueryParam(DBTypes.DATE, new java.sql.Date(new Date().getTime())));
        

        Long utilizationPercentage = new Long(0);
        if (storageContainerUtilizationDTO.getTotalStorageCapacity() > 0)
        {
            utilizationPercentage = Math.round(((storageContainerUtilizationDTO.getNumberOfSpecimensAssigned()
                    .doubleValue() / storageContainerUtilizationDTO.getTotalStorageCapacity().doubleValue()) * 100));
        }

        params.put("4", new NamedQueryParam(DBTypes.LONG, utilizationPercentage));
        params.put("5", new NamedQueryParam(DBTypes.LONG, storageContainerUtilizationDTO.getTotalStorageCapacity()));
        params.put("6", new NamedQueryParam(DBTypes.LONG, storageContainerUtilizationDTO.getSiteId()!=null?storageContainerUtilizationDTO.getSiteId():0l));
      
        try
        {
            hibernateDAO.executeUpdateWithNamedSQLQuery("insertIntoStorContSpecCountsTable", params);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
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
    public List<StorageContainerStoredSpecimenDetailsDTO> getStorageContainerStoredSpecimenDetailsDTOById(
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
            storageContainerStoredSpecimenDetailsDTO.setSpecimenCount(resultSet.getLong(2));
            storageContainerStoredSpecimenDetailsDTO.setPercentUtilization(resultSet.getLong(3));
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
    public StorageContainerStoredSpecimenDetailsDTO getStrotedSpecimenDetailsDTOByContainerId(HibernateDAO hibernateDAO,
            Long containerId) throws DAOException, SQLException
    {
        StorageContainerStoredSpecimenDetailsDTO storageContainerStoredSpecimenDetailsDTO = new StorageContainerStoredSpecimenDetailsDTO();
        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("1", new NamedQueryParam(DBTypes.LONG, containerId));
        params.put("2", new NamedQueryParam(DBTypes.LONG, containerId));

        ResultSet resultSet = hibernateDAO.executeNamedSQLQuery("getUtilizationCountsOfContainerById", params);

        if (resultSet.next())
        {
            storageContainerStoredSpecimenDetailsDTO.setPercentUtilization(resultSet.getLong("UTILIZATION_PERCENTAGE"));
            storageContainerStoredSpecimenDetailsDTO.setSpecimenCount(resultSet.getLong("STORED_SPECIMEN_COUNT"));
            storageContainerStoredSpecimenDetailsDTO.setCapacity(resultSet.getLong("total_capacity"));
            

        }
        
        return storageContainerStoredSpecimenDetailsDTO;

    }

    /**
     * Returns List of ids of top most parent storage containers of site
     * @param hibernateDAO
     * @param siteName
     * @return
     * @throws DAOException
     * @throws SQLException 
     */
    public Map<String,List<StorageContainerStoredSpecimenDetailsDTO>>  getContainerUtilizationDTOMap(HibernateDAO hibernateDAO, String siteName)
            throws DAOException, SQLException
    {
        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("0", new NamedQueryParam(DBTypes.STRING, siteName));
       
        Collection<?> containerDetailsList =  hibernateDAO.executeNamedQuery("getParentStorageContainersDetailsBySiteName", params);
        Iterator<?> cprDetailIterator = containerDetailsList.iterator();
        Map<String,List<StorageContainerStoredSpecimenDetailsDTO>> containerStoredSpecimenDetailsMap = new HashMap<String,List<StorageContainerStoredSpecimenDetailsDTO>>();
        while(cprDetailIterator.hasNext()){
            Object[] valArr = (Object[]) cprDetailIterator.next();
            Long containerId = (Long)valArr[0];
            String containerName = valArr[1].toString();
            containerStoredSpecimenDetailsMap.put(containerName, getStorageContainerStoredSpecimenDetailsDTOById(hibernateDAO,containerId));
        }

        return containerStoredSpecimenDetailsMap ;
    }
    
    public List<StorageContainerStoredSpecimenDetailsDTO> getSiteUtilizationList(HibernateDAO hibernateDAO) throws DAOException, SQLException{
        List<StorageContainerStoredSpecimenDetailsDTO> storageContainerStoredSpecimenDetailsDTOList = new ArrayList<StorageContainerStoredSpecimenDetailsDTO>();
        int noSiteToDispaly = 10;
        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("1", new NamedQueryParam(DBTypes.INTEGER, noSiteToDispaly));
        ResultSet resultSet = null;
        
        if(DAOConfigFactory.getInstance().getDAOFactory(
                Constants.APPLICATION_NAME).getDataBaseType().equals("MYSQL")){
            resultSet =   hibernateDAO.executeNamedSQLQuery("getSiteUtilizationMysql",params);
            
        }else if(hibernateDAO instanceof OracleDAOImpl){
            resultSet =   hibernateDAO.executeNamedSQLQuery("getSiteUtilizationOracle",params);
        }
        while (resultSet.next())
        {
            StorageContainerStoredSpecimenDetailsDTO storageContainerStoredSpecimenDetailsDTO = new StorageContainerStoredSpecimenDetailsDTO();
            storageContainerStoredSpecimenDetailsDTO.setSpecimenCount(resultSet.getLong("total_count"));
            storageContainerStoredSpecimenDetailsDTO.setCapacity(resultSet.getLong("total_capacity"));
            storageContainerStoredSpecimenDetailsDTO.setSiteName(resultSet.getString("site_name"));
            storageContainerStoredSpecimenDetailsDTOList.add(storageContainerStoredSpecimenDetailsDTO);
        }
        return storageContainerStoredSpecimenDetailsDTOList;
    }
    
    public Long getSiteIdFromContainerName(HibernateDAO hibernateDAO,Long containerId) throws DAOException{
        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("0", new NamedQueryParam(DBTypes.LONG, containerId));
        List<Long> siteIdList = hibernateDAO.executeNamedQuery("getSiteIdFromContainerName",params);
        return siteIdList!=null && !siteIdList.isEmpty() ? siteIdList.get(0):null;
      
    }
}

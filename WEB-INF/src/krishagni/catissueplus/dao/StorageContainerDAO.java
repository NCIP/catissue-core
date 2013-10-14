
package krishagni.catissueplus.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import krishagni.catissueplus.bizlogic.SpecimenBizLogic;

import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;

public class StorageContainerDAO
{

	private static final Logger LOGGER = Logger.getCommonLogger(StorageContainerDAO.class);
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
    
    public Boolean isContainerCanHoldSpecimen(HibernateDAO hibernateDAO, String specimenClass, String specimenType, Long cpId, Long containerId) throws BizLogicException
    {
    	String sqlForClassType="select cont.identifier from catissue_storage_container cont,catissue_stor_cont_spec_class contc," +
    			" catissue_stor_cont_spec_type contt where cont.identifier="+containerId+" and contc.STORAGE_CONTAINER_ID=cont.identifier and " +
    			"contt.STORAGE_CONTAINER_ID=cont.identifier and contc.SPECIMEN_CLASS like '"+specimenClass+"' and contt.SPECIMEN_TYPE like '"+specimenType+"'";
    	try {
			List result = AppUtility.executeSQLQuery(sqlForClassType);
			if(result != null && result.size() >= 1 && isContainerCanHoldCp(hibernateDAO, cpId, containerId))
			{
				return Boolean.TRUE;
			}
		} catch (ApplicationException e) 
		{
			LOGGER.error(e);
			throw new BizLogicException(null, e, "Error while DB operations.");
		}
    	
    	return Boolean.FALSE;
    }

    public Boolean isContainerCanHoldCp(HibernateDAO hibernateDAO, Long cpId,Long containerId) throws DAOException
    {
    	Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("0", new NamedQueryParam(DBTypes.LONG, containerId));

        List cpIds = hibernateDAO.executeNamedQuery("getCPIdsbyContainerID", params);
        if(cpIds == null || cpIds.isEmpty() || cpIds.contains(null) || cpIds.contains(cpId))
        {
        	return Boolean.TRUE;
        }
        	
        return Boolean.FALSE;
    }
}

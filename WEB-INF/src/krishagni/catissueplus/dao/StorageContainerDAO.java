
package krishagni.catissueplus.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public List<Long> getListOfChildContainerId(Long containerId,
	        HibernateDAO dao) throws DAOException
	{
		Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
		params.put("0", new NamedQueryParam(DBTypes.LONG, containerId));

		//get child storage container ids
		List<Long> childContainerList = dao.executeNamedQuery(
		        "getChildContainerIds", params);
		return childContainerList;
	}

	/**
	 * Returns number of specimens stored in container
	 * @param containerId
	 * @param dao
	 * @return
	 * @throws DAOException
	 */
	public Integer getNumberOfSpecimensAssigned(Long containerId,
	        HibernateDAO dao) throws DAOException
	{
		Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
		params.put("0", new NamedQueryParam(DBTypes.LONG, containerId));

		//get count of specimens stored in the container
		List<Integer> allocatedList = dao.executeNamedQuery(
		        "getAssignedSpecimenCount", params);
		if (allocatedList.isEmpty() || allocatedList == null)
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
	public Integer getStorageTypeCapacityCountFromContainerId(Long containerId,
	        HibernateDAO dao) throws DAOException
	{
		Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
		params.put("0", new NamedQueryParam(DBTypes.LONG, containerId));

		//get capacity of storage type which can hold specimens from container id
		List<Capacity> containerList = dao.executeNamedQuery(
		        "getStorageTypeCapacityFromContainerId", params);

		if (containerList.isEmpty() || containerList == null)
		{
			return null;
		}

		Capacity capacity = containerList.get(0);

		return capacity.getOneDimensionCapacity()
		        * capacity.getTwoDimensionCapacity();
	}

	/**
	 * Returns Id List of all top most parent storage container
	 * @param dao
	 * @return
	 * @throws DAOException
	 */
	public List<Long> getIdOfAllParentStorageContainers(HibernateDAO dao)
	        throws DAOException
	{
		//get capacity of storage type which can hold specimens from container id
		List<Long> parentContList = dao.executeNamedQuery(
		        "getIdOfParentStorageContainers", null);

		return parentContList;
	}

    /**
     * Inserts into catissue_stor_cont_spec_counts table with named SQL query
     * @param dto
     * @param hibernateDAO
     * @throws DAOException
     * @throws SQLException
     */
    public void insertIntoStorContSpecCountsTable(StorageContainerUtilizationDTO dto, HibernateDAO hibernateDAO)
            throws DAOException, SQLException
    {
        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("1", new NamedQueryParam(DBTypes.LONG, dto.getContainerId()));
        params.put("2", new NamedQueryParam(DBTypes.INTEGER, dto.getNumberOfSpecimensAssigned()));
        params.put("3", new NamedQueryParam(DBTypes.DATE, new java.sql.Date(new Date().getTime())));

        hibernateDAO.executeUpdateWithNamedSQLQuery("insertIntoStorContSpecCountsTable", params);
    }
}

package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.NameValueBeanRelevanceComparator;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.exception.DAOException;

public abstract class AbstractSCSelectionBizLogic
{
	/**
	 * Logger object.
	 */
	private static final transient Logger logger = Logger.getCommonLogger(AbstractSCSelectionBizLogic.class);
	private static final int CONTAINERS_MAX_LIMIT = Integer.parseInt(XMLPropertyHandler
			.getValue(Constants.CONTAINERS_MAX_LIMIT));
	/**
	 * Returns a map of allocated containers versus their respective
	 * position maps, for the given containers
	 * 
	 * @return Returns a map of allocated containers versus their respective free
	 *         locations.
	 * @throws DAOException
	 */
	protected Map<NameValueBean, Map<NameValueBean, List<NameValueBean>>>
	getAllocDetailsForContainers(final List<?> containerList, final DAO dao) throws BizLogicException
	{
		logger.info("No of containers:" + containerList.size());
		final Map<NameValueBean, Map<NameValueBean, List<NameValueBean>>> containerMap = 
			new TreeMap<NameValueBean, Map<NameValueBean, List<NameValueBean>>>(new NameValueBeanRelevanceComparator());
		long relevance = 1;
		for (final Iterator<?> itr = containerList.listIterator(); itr.hasNext(); relevance++)
		{
			final ArrayList<?> container = (ArrayList<?>) itr.next();
			final Map<NameValueBean, List<NameValueBean>> positionMap = 
				StorageContainerUtil.getAvailablePositionMapForContainer(String
					.valueOf(container.get(0)), 0, Integer.parseInt(String
					.valueOf(container.get(2))),
					Integer.parseInt(String.valueOf(container.get(3))), dao);
			if (!positionMap.isEmpty())
			{
				final NameValueBean nvb = new NameValueBean(container.get(1), container.get(0),
						relevance);
				containerMap.put(nvb, positionMap);
			}
		}
		return containerMap;
	}
	/**
	 * Returns a list of storage containers. Each index corresponds to the entry:<br>
	 * 		[id, name, one_dimension_capacity, two_dimension_capacity ...]
	 * @param holdsType - The type that the containers can 
	 * hold (Specimen/SpecimenArray/Container)
	 * @param cpId - Collection Protocol Id
	 * @param spClass - Specimen Class
	 * @param aliquotCount - Number of aliquotes that the fetched containers 
	 * should minimally support. A value of <b>0</b> specifies that there's
	 * no such restriction
	 * @param containerTypeId  containerTypeId
	 * @param specimenArrayTypeId specimenArrayTypeId
	 * @param exceedingLimit exceedingLimit value
	 * @param storageType Auto, Manual, Virtual
	 * @return a list of storage containers
	 * @throws ApplicationException 
	 */
	protected List<?> getStorageContainerList(final String storageType, final String[] queries) throws ApplicationException
	{
		final JDBCDAO dao = AppUtility.openJDBCSession();
		final List containers = new ArrayList();
		try
		{
			int remainingContainersNeeded = CONTAINERS_MAX_LIMIT;
			for (int i = 0; i < queries.length; i++)
			{
				logger.debug(String.format("Firing query: query%d", i));
				logger.debug(queries[i]);
				final List resultList = getContainerListByQuery(storageType, dao, queries[i],
				remainingContainersNeeded); 
				//dao.executeQuery(queries[i]);
				if (resultList == null || resultList.size() == 0)
				{
					continue;
				}
				if(!Constants.STORAGE_TYPE_POSITION_MANUAL.equals(storageType) 
				&& resultList.size() >= remainingContainersNeeded)
				{
					List subListOfCont = resultList.subList(0, remainingContainersNeeded);
					if(!containers.containsAll( subListOfCont ))
					{
					  containers.addAll(subListOfCont);
					  break;
					}
				}
				if(!containers.containsAll( resultList ))
				{
				   containers.addAll(resultList);
				}
				remainingContainersNeeded = remainingContainersNeeded - resultList.size();
			}
		}
		finally
		{
			AppUtility.closeJDBCSession(dao);
		}
		logger.debug(String.format("%s:%s:%d", this.getClass().getSimpleName(),
				"getStorageContainers() number of containers fetched", containers.size()));
		return containers;
	}
	/**
	 * @param storageType 
	 * @param dao A dao object to be used to execute query.
	 * @param query to be executed to get containers.
	 * @param maxRecords maximum records to be retrieved by query.
	 * @return containers as query result in the form of List
	 * @throws DAOException DAO Exception 
	 */
	private List getContainerListByQuery(String storageType, final JDBCDAO dao,
			final String query, int maxRecords) throws DAOException
	{
		final List resultList;
		if(!"Manual".equals(storageType))
		{
			resultList= dao.executeQuery(query,0,maxRecords,null );
		}
		else
		{
			resultList= dao.executeQuery(query);
		}
		return resultList;
	}
}

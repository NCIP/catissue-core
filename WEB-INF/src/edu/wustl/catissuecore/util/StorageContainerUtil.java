
package edu.wustl.catissuecore.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import edu.wustl.catissuecore.actionForm.SpecimenArrayForm;
import edu.wustl.catissuecore.bizlogic.SpecimenArrayBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.domain.Container;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.exception.DAOException;

public final class StorageContainerUtil
{

	private static Logger logger = Logger.getCommonLogger(StorageContainerUtil.class);
	/**
	 * creates a singleton object
	 */
	private static StorageContainerUtil storcontUtil = new StorageContainerUtil();

	/**
	 * Private constructor
	 */
	private StorageContainerUtil()
	{

	}

	/**
	 * returns single object
	 */

	public static StorageContainerUtil getInstance()
	{
		return storcontUtil;
	}

	/**
	 * This functions returns a list of disabled containers from the cache.
	 * @return Returns a list of disabled.
	 * @throws DAOException
	 */
	public static List getListOfDisabledContainersFromCache() throws ApplicationException
	{
		// TODO if map is null
		// TODO move all code to common utility

		// getting instance of catissueCoreCacheManager and getting participantMap from cache
		CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager.getInstance();
		List disabledconts = (ArrayList) catissueCoreCacheManager
				.getObjectFromCache(Constants.MAP_OF_DISABLED_CONTAINERS);
		return disabledconts;
	}

	/**
	 * This functions returns a map of available rows vs. available columns.
	 * @param container The container.
	 * @return Returns a map of available rows vs. available columns.
	 * @throws DAOException
	 */

	public static Map getAvailablePositionMapForNewContainer(StorageContainer container)
			throws DAOException
	{
		Map map = new TreeMap();

		if (!container.isFull().booleanValue())
		{
			final int dimX = container.getCapacity().getOneDimensionCapacity().intValue() + 1;
			final int dimY = container.getCapacity().getTwoDimensionCapacity().intValue() + 1;

			for (int x = 1; x < dimX; x++)
			{

				List list = new ArrayList();

				for (int y = 1; y < dimY; y++)
				{
					list.add(new NameValueBean(Integer.valueOf(y), Integer.valueOf(y)));

				}

				if (!list.isEmpty())
				{
					Integer xObj = Integer.valueOf(x);
					NameValueBean nvb = new NameValueBean(xObj, xObj);
					map.put(nvb, list);

				}
			}
		}

		return map;
	}

	public static Position getFirstAvailablePositionsInContainer(StorageContainer storageContainer,
			HashSet<String> allocatedPositions) throws ApplicationException
	{
		StorageContainerBizLogic scBizLogic = new StorageContainerBizLogic();
		Position position = null;
		boolean positionFound = false;
		while (!positionFound)
		{
			position = scBizLogic.getFirstAvailablePositionInContainer(storageContainer);
			if (position != null)
			{
				String containerValue = null;
				Long containerId = storageContainer.getId();
				if (storageContainer.getName() != null)
				{
					containerValue = StorageContainerUtil.getStorageValueKey(storageContainer
							.getName(), null, position.getXPos(), position.getYPos());
				}
				else
				{
					containerValue = StorageContainerUtil.getStorageValueKey(null, containerId
							.toString(), position.getXPos(), position.getYPos());
				}
				if (!allocatedPositions.contains(containerValue))
				{
					positionFound = true;
					break;
				}
			}
			else
			{
				throw AppUtility.getApplicationException(null, "storage.full", "");
			}
		}
		return position;
	}

	/**
	 *  This method gives first valid storage position to a specimen if it is not given. 
	 *  If storage position is given it validates the storage position
	 * @param specimen
	 * @throws ApplicationException 
	 */
	public static void validateStorageLocationForSpecimen(Specimen specimen, DAO dao,
			HashSet<String> allocatedPositions) throws ApplicationException
	{
		Integer xPos = null;
		Integer yPos = null;
		if (specimen.getSpecimenPosition() != null
				&& specimen.getSpecimenPosition().getStorageContainer() != null)
		{
			StorageContainer storageContainer = specimen.getSpecimenPosition()
					.getStorageContainer();
			String containerValue = null;
			if (specimen.getSpecimenPosition() != null)
			{
				xPos = specimen.getSpecimenPosition().getPositionDimensionOne();
				yPos = specimen.getSpecimenPosition().getPositionDimensionTwo();
			}
			/**
			 *  Following code is added to set the x and y dimension in case only storage container is given 
			 *  and x and y positions are not given 
			 */

			if (xPos == null || yPos == null)
			{
				
				StorageContainerBizLogic scBizLogic = new StorageContainerBizLogic();
				Position position = scBizLogic.getFirstAvailablePositionInContainer(storageContainer);
					if(position!=null)
					{
						xPos = position.getXPos();
						yPos = position.getYPos();
						Long containerId = storageContainer.getId();
						if (containerId != null)
						{
							containerValue = StorageContainerUtil.getStorageValueKey(null, containerId
									.toString(), xPos, yPos);
						}
						else
						{
							containerValue = StorageContainerUtil.getStorageValueKey(storageContainer
									.getName(), null, xPos, yPos);
						}
						if (!allocatedPositions.contains(containerValue))
						{
							SpecimenPosition specPos = specimen.getSpecimenPosition();
							specPos.setPositionDimensionOne(xPos);
							specPos.setPositionDimensionTwo(yPos);
							specPos.setSpecimen(specimen);
							allocatedPositions.add(containerValue);
							//	break;
						}
					}
					else
					{
						throw AppUtility.getApplicationException(null, "storage.full", "");
					}
			}
			if (containerValue == null)
			{
				if (storageContainer.getId() == null)
				{
					containerValue = StorageContainerUtil.getStorageValueKey(storageContainer
							.getName(), null, xPos, yPos);
				}
				else
				{
					containerValue = StorageContainerUtil.getStorageValueKey(null, storageContainer
							.getId().toString(), xPos, yPos);
				}
				if (!allocatedPositions.contains(containerValue))
				{
					allocatedPositions.add(containerValue);
				}
				else
				{
					throw AppUtility.getApplicationException(null,
							"errors.storageContainer.Multiple.inUse", "StorageContainerUtil.java");
				}
			}

			if (xPos == null || yPos == null || xPos.intValue() < 0 || yPos.intValue() < 0)
			{
				throw AppUtility.getApplicationException(null, "errors.item.format",
						"StorageContainerUtil.java :"
								+ ApplicationProperties
										.getValue("specimen.positionInStorageContainer"));
			}
		}

	}

		/**
	 * @param dao
	 * @param similarContainerMap
	 * @param containerPrefixKey
	 * @param positionsToBeAllocatedList
	 * @param usedPositionsList
	 * @param iCount
	 * @throws ApplicationException 
	 */
	public static void prepareContainerMap(DAO dao, Map similarContainerMap,
			String containerPrefixKey, List positionsToBeAllocatedList, List usedPositionsList,
			int iCount, String contId) throws ApplicationException
	{
		String radioButonKey = "radio_" + iCount;
		String containerIdKey = containerPrefixKey + iCount + contId;
		String containerNameKey = containerPrefixKey + iCount + "_StorageContainer_name";
		String posDim1Key = containerPrefixKey + iCount + "_positionDimensionOne";
		String posDim2Key = containerPrefixKey + iCount + "_positionDimensionTwo";

		String containerName = null;
		String containerId = null;
		String posDim1 = null;
		String posDim2 = null;
		//get the container values based on user selection from dropdown or map
		if (similarContainerMap.get(radioButonKey) != null
				&& similarContainerMap.get(radioButonKey).equals("1"))
		{
			containerId = (String) similarContainerMap.get(containerIdKey);
			posDim1 = (String) similarContainerMap.get(posDim1Key);
			posDim2 = (String) similarContainerMap.get(posDim2Key);
			usedPositionsList.add(containerId + Constants.STORAGE_LOCATION_SAPERATOR + posDim1
					+ Constants.STORAGE_LOCATION_SAPERATOR + posDim2);

		}
		else if (similarContainerMap.get(radioButonKey) != null
				&& similarContainerMap.get(radioButonKey).equals("2"))
		{
			containerName = (String) similarContainerMap.get(containerNameKey + "_fromMap");

			String sourceObjectName = StorageContainer.class.getName();
			String[] selectColumnName = {"id"};
			String[] whereColumnName = {"name"};
			String[] whereColumnCondition = {"="};
			Object[] whereColumnValue = {containerName};
			String joinCondition = null;

			QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			queryWhereClause.addCondition(new EqualClause("name", containerName));
			List list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);

			if (!list.isEmpty())
			{
				containerId = list.get(0).toString();
			}
			else
			{
				String message = ApplicationProperties.getValue("specimen.storageContainer");
				throw AppUtility.getApplicationException( null,"errors.invalid",
						"StorageContainerUtil.java :" + message);
			}

			posDim1 = (String) similarContainerMap.get(posDim1Key + "_fromMap");
			posDim2 = (String) similarContainerMap.get(posDim2Key + "_fromMap");

			if (posDim1 == null || posDim1.trim().equals("") || posDim2 == null
					|| posDim2.trim().equals(""))
			{
				positionsToBeAllocatedList.add(Integer.valueOf(iCount));
			}
			else
			{

				usedPositionsList.add(containerId + Constants.STORAGE_LOCATION_SAPERATOR + posDim1
						+ Constants.STORAGE_LOCATION_SAPERATOR + posDim2);
				similarContainerMap.put(containerIdKey, containerId);
				similarContainerMap.put(posDim1Key, posDim1);
				similarContainerMap.put(posDim2Key, posDim2);
				similarContainerMap.remove(containerIdKey + "_fromMap");
				similarContainerMap.remove(posDim1Key + "_fromMap");
				similarContainerMap.remove(posDim2Key + "_fromMap");
			}

		}
	}

	/**
	 * check for initial values for storage container.
	 * @param containerMap container map
	 * @return list of initial values
	 */
	public static List checkForInitialValues(Map containerMap)
	{
		List initialValues = null;

		if((containerMap!=null)&&(containerMap.size() > 0))
		{
			String[] startingPoints = new String[3];

			Set keySet = containerMap.keySet();
			Iterator itr = keySet.iterator();
			NameValueBean nvb = (NameValueBean) itr.next();
			startingPoints[0] = nvb.getValue();

			Map map1 = (Map) containerMap.get(nvb);
			keySet = map1.keySet();
			itr = keySet.iterator();
			nvb = (NameValueBean) itr.next();
			startingPoints[1] = nvb.getValue();

			List list = (List) map1.get(nvb);
			nvb = (NameValueBean) list.get(0);
			startingPoints[2] = nvb.getValue();

			logger.info("Starting points[0]" + startingPoints[0]);
			logger.info("Starting points[1]" + startingPoints[1]);
			logger.info("Starting points[2]" + startingPoints[2]);
			initialValues = new ArrayList();
			initialValues.add(startingPoints);
		}
		return initialValues;
	}

	/**
	 * @param containerMap
	 * @param aliquotCount
	 * @param counter
	 * @return
	 */
	public static int checkForLocation(Map containerMap, int aliquotCount, int counter)
	{
		Object[] containerId = containerMap.keySet().toArray();
		for (int i = 0; i < containerId.length; i++)
		{
			Map xDimMap = (Map) containerMap.get(containerId[i]);
			if (!xDimMap.isEmpty())
			{
				Object[] xDim = xDimMap.keySet().toArray();
				for (int j = 0; j < xDim.length; j++)
				{
					List yDimList = (List) xDimMap.get(xDim[j]);
					counter = counter + yDimList.size();
					if (counter >= aliquotCount)
					{
						i = containerId.length;
						break;
					}
				}
			}
		}
		return counter;
	}

	/**
	 * @param form
	 * @param containerMap
	 * @param aliquotMap
	 */

	public static void populateAliquotMap(String objectKey, Map containerMap, Map aliquotMap,
			String noOfAliquots)
	{
		int counter = 1;
		if (!containerMap.isEmpty())
		{
			Object[] containerId = containerMap.keySet().toArray();
			for (int i = 0; i < containerId.length; i++)
			{
				Map xDimMap = (Map) containerMap.get(containerId[i]);
				counter = setAliquotMap(objectKey, xDimMap, containerId, noOfAliquots, counter,
						aliquotMap, i);
				if (counter <= Integer.parseInt(noOfAliquots))
				{
					i = containerId.length;
				}
			}
		}
	}

	public static int setAliquotMap(String objectKey, Map xDimMap, Object[] containerId,
			String noOfAliquots, int counter, Map aliquotMap, int i)
	{
		objectKey = objectKey + ":";

		if (!xDimMap.isEmpty())
		{
			Object[] xDim = xDimMap.keySet().toArray();
			for (int j = 0; j < xDim.length; j++)
			{
				List yDimList = (List) xDimMap.get(xDim[j]);
				for (int k = 0; k < yDimList.size(); k++)
				{
					if (counter <= Integer.parseInt(noOfAliquots))
					{
						String containerKey = objectKey + counter + "_StorageContainer_id";
						String pos1Key = objectKey + counter + "_positionDimensionOne";
						String pos2Key = objectKey + counter + "_positionDimensionTwo";

						aliquotMap.put(containerKey, ((NameValueBean) containerId[i]).getValue());
						aliquotMap.put(pos1Key, ((NameValueBean) xDim[j]).getValue());
						aliquotMap.put(pos2Key, ((NameValueBean) yDimList.get(k)).getValue());

						counter++;
					}
					else
					{
						j = xDim.length;
						//i = containerId.length;
						break;
					}
				}
			}
		}
		//counter = new Integer(intCounter);
		return counter;
	}

	/**
	 * @param specimenArrayBizLogic
	 * @param specimenArrayForm
	 * @param containerMap
	 * @return
	 * @throws DAOException
	 */
	public static List setInitialValue(SpecimenArrayBizLogic specimenArrayBizLogic,
			SpecimenArrayForm specimenArrayForm, TreeMap containerMap) throws ApplicationException
	{
		List initialValues = null;
		String[] startingPoints = new String[]{"-1", "-1", "-1"};
		String containerName = null;
		if (specimenArrayForm.getStorageContainer() != null
				&& !specimenArrayForm.getStorageContainer().equals("-1"))
		{
			startingPoints[0] = specimenArrayForm.getStorageContainer();
			String[] selectColumnName = {"name"};
			String[] whereColumnName = {Constants.SYSTEM_IDENTIFIER};
			String[] whereColumnCondition = {"="};
			Object[] whereColumnValue = {Long.valueOf(startingPoints[0])};
			String joinCondition = Constants.AND_JOIN_CONDITION;
			List containerList = specimenArrayBizLogic.retrieve(StorageContainer.class.getName(),
					selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue,
					joinCondition);
			if ((containerList != null) && (!containerList.isEmpty()))
			{
				containerName = (String) containerList.get(0);
			}
		}
		if (specimenArrayForm.getPositionDimensionOne() != -1)
		{
			startingPoints[1] = String.valueOf(specimenArrayForm.getPositionDimensionOne());
		}

		if (specimenArrayForm.getPositionDimensionTwo() != -1)
		{
			startingPoints[2] = String.valueOf(specimenArrayForm.getPositionDimensionTwo());
		}
		initialValues = new ArrayList();
		initialValues.add(startingPoints);

		return initialValues;
	}

	
	/**
	 *  This method allocates available position to single specimen
	 * @param object
	 * @param aliquotMap
	 * @param usedPositionsList
	 * @throws ApplicationException 
	 */
	public static void allocatePositionToSingleContainerOrSpecimen(Object object, Map aliquotMap,
			List usedPositionsList, String spKey, String scId) throws ApplicationException
	{
		int specimenNumber = ((Integer) object).intValue();
		String specimenKey = spKey;
		String containerIDKey = specimenKey + specimenNumber
				+ "_StorageContainer_id";
		String containerIdKey = specimenKey + specimenNumber + scId;
		String posDim1Key = specimenKey + specimenNumber
				+ "_positionDimensionOne";
		String posDim2Key = specimenKey + specimenNumber
				+ "_positionDimensionTwo";

		String containerID = (String) aliquotMap.get(containerIDKey);
		Container container = new Container();
		container.setId(Long.valueOf(containerID));
		StorageContainerBizLogic scBizLogic = new StorageContainerBizLogic();
		Position position = scBizLogic.getFirstAvailablePositionInContainer(container);
		if (position != null) {
			aliquotMap.put(containerIdKey, containerID);
			aliquotMap.put(posDim1Key, position.getXPos());
			aliquotMap.put(posDim2Key, position.getYPos());
			aliquotMap.remove(containerIdKey + "_fromMap");
			aliquotMap.remove(posDim1Key + "_fromMap");
			aliquotMap.remove(posDim2Key + "_fromMap");
		} else {
			throw AppUtility.getApplicationException(null,
					"storage.container.has.nt.enough.space", Long.valueOf(
							specimenNumber).toString());
		}
	}

	/**
	 * @return
	 */
	public static boolean checkPos1AndPos2(String pos1, String pos2)
	{
		boolean flag = false;
		if (pos1 != null && !pos1.trim().equals(""))
		{
			long l = 1;
			try
			{
				l = Long.parseLong(pos1);
			}
			catch (Exception e)
			{
				logger.debug(e.getMessage(), e);
				flag = true;

			}
			if (l <= 0)
			{
				flag = true;
			}
		}
		if (pos2 != null && !pos2.trim().equals(""))
		{
			long l = 1;
			try
			{
				l = Long.parseLong(pos2);
			}
			catch (Exception e)
			{
				logger.debug(e.getMessage(), e);
				flag = true;

			}
			if (l <= 0)
			{
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * @param dao
	 * @param containerId
	 * @return
	 * @throws DAOException
	 * @throws ClassNotFoundException 
	 */
	public static Collection getChildren(DAO dao, Long containerId) throws ApplicationException
	{
		String hql = "select cntPos.occupiedContainer from ContainerPosition cntPos, StorageContainer container where cntPos.occupiedContainer.id=container.id and cntPos.parentContainer.id ="
				+ containerId;
		List childrenColl = new ArrayList();
		childrenColl = dao.executeQuery(hql);
		return childrenColl;
	}

	/**
	* @param children
	* @param dao
	* @param containerId
	* @throws DAOException
	*/
	public static void setChildren(Collection children, DAO dao, Long containerId)
			throws ApplicationException
	{
		if (children != null)
		{
			getChildren(dao, containerId).addAll(children);
		}
	}

	/**
	 * Description: This method is used to create storage loaction key value.
	 * Used while updating or inserting Specimen
	 * @param containerName - storage container name
	 * @param containerID - storage container id
	 * @param containerPos1 - storage container Position 1
	 * @param containerPos2 - storage container Position 2
	 * @return storageValue : container name or container id:container Position 1,container Position 2
	 */
	//bug 8294
	public static String getStorageValueKey(String containerName, String containerID,
			Integer containerPos1, Integer containerPos2)
	{
		StringBuffer storageValue = new StringBuffer();
		if (containerName != null)
		{
			storageValue.append(containerName);
			storageValue.append(":");
			storageValue.append(containerPos1);
			storageValue.append(" ,");
			storageValue.append(containerPos2);
		}
		else if (containerID != null)
		{
			storageValue.append(containerID);
			storageValue.append(":");
			storageValue.append(containerPos1);
			storageValue.append(" ,");
			storageValue.append(containerPos2);
		}
		return storageValue.toString();
	}
}
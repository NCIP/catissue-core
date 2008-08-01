
package edu.wustl.catissuecore.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import net.sf.ehcache.CacheException;
import edu.wustl.catissuecore.actionForm.SpecimenArrayForm;
import edu.wustl.catissuecore.bizlogic.SpecimenArrayBizLogic;
import edu.wustl.catissuecore.domain.Container;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;

public class StorageContainerUtil
{

	/**
	 * This functions updates the storage container map when a new position is to be added in map
	 * @param storageContainer - Storage container whose position is freed
	 * @param continerMap - map of storage container
	 * @param x - x position which is updated
	 * @param y - y position which is updated
	 */
	public static synchronized void insertSinglePositionInContainerMap(Container storageContainer, Map containerMap, int x, int y)
	{
		NameValueBean storageContainerId = new NameValueBean(storageContainer.getName(), (storageContainer.getId()));
		TreeMap storageContainerMap = (TreeMap) containerMap.get(storageContainerId);
		Integer xObj = new Integer(x);

		NameValueBean nvb = new NameValueBean(xObj, xObj);
		if (storageContainerMap == null)
		{
			storageContainerMap = new TreeMap();
			containerMap.put(storageContainerId, storageContainerMap);

		}
		List yPosList = (List) storageContainerMap.get(nvb);
		if (yPosList == null || yPosList.size() == 0)
		{
			yPosList = new ArrayList();
			yPosList.add(new NameValueBean(new Integer(y), new Integer(y)));
		}
		else
		{
			Collections.sort(yPosList);
			int size = yPosList.size();
			boolean insertFlag = true;
			for (int i = 0; i < size; i++)
			{
				NameValueBean yPosNvb = (NameValueBean) yPosList.get(i);
				if (Integer.parseInt(yPosNvb.getName()) == y)
				{
					insertFlag = false;
					break;
				}
			}
			if (insertFlag)
			{
				yPosList.add(new NameValueBean(new Integer(y), new Integer(y)));
				Collections.sort(yPosList);
			}

		}
		storageContainerMap.put(nvb, yPosList);
	}

	/**
	 * This functions updates the storage container map when a new position is to be deleted in map
	 * @param storageContainer - Storage container whose position is occupied
	 * @param continerMap - map of storage container
	 * @param x - x position which is updated
	 * @param y - y position which is updated
	 */
	public static synchronized void deleteSinglePositionInContainerMap(Container storageContainer, Map continersMap, int x, int y)
	{
		NameValueBean storageContainerId = new NameValueBean(storageContainer.getName(), storageContainer.getId());
		TreeMap storageContainerMap = (TreeMap) continersMap.get(storageContainerId);

		Integer xObj = new Integer(x);

		NameValueBean nvb = new NameValueBean(xObj, xObj);

		List yPosList = (List) storageContainerMap.get(nvb);
		if (yPosList != null)
		{

			//Logger.out.debug("deleteSinglePositionInContainerMap method :----yPosList :" + yPosList);
			for (int i = 0; i < yPosList.size(); i++)
			{
				NameValueBean yPosnvb = (NameValueBean) yPosList.get(i);
				if (yPosnvb.getValue().equals("" + y))
				{
					//Logger.out.debug("Removing value:" + y);
					yPosList.remove(i);
					break;
				}

				//yPosList.remove(new NameValueBean(new Integer(y), new Integer(y)));

			}
		}
		//Logger.out.debug("deleteSinglePositionInContainerMap method after deleting :----yPosList :" + yPosList);

		if (yPosList == null || yPosList.size() == 0)
		{
			storageContainerMap.remove(nvb);
		}
		if (storageContainerMap == null || storageContainerMap.size() == 0)
		{
			continersMap.remove(storageContainerId);

		}

	}

	/**
	 * This functions updates the storage container map when a new container is added
	 * @param storageContainer - Storage container which is added
	 * @param continerMap - map of storage container
	 * @param x - x position of parent storage container
	 * @param y - y position of parent storage container
	 */
	public static synchronized void addStorageContainerInContainerMap(StorageContainer storageContainer, Map continersMap) throws DAOException
	{
		NameValueBean storageContainerId = new NameValueBean(storageContainer.getName(), storageContainer.getId());
		TreeMap availabelPositionsMap = (TreeMap) getAvailablePositionMapForNewContainer(storageContainer);
		if (availabelPositionsMap != null && availabelPositionsMap.size() != 0)
		{
			continersMap.put(storageContainerId, availabelPositionsMap);
		}

		if (storageContainer.getLocatedAtPosition() != null && storageContainer.getLocatedAtPosition().getParentContainer() != null)
		{
			Container parentContainer = storageContainer.getLocatedAtPosition().getParentContainer();
			int x = storageContainer.getLocatedAtPosition().getPositionDimensionOne().intValue();
			int y = storageContainer.getLocatedAtPosition().getPositionDimensionTwo().intValue();
			deleteSinglePositionInContainerMap((StorageContainer) parentContainer, continersMap, x, y);
		}

	}

	/**
	 * This functions updates the storage container map when a new container is deleted or disabled
	 * @param storageContainer - Storage container which is deleted or disabled
	 * @param continerMap - map of storage container
	 * @param x - x position of parent storage container
	 * @param y - y position of parent storage container
	 */
	public static synchronized void removeStorageContainerInContainerMap(StorageContainer storageContainer, Map continersMap) throws DAOException
	{
		NameValueBean storageContainerId = new NameValueBean(storageContainer.getName(), storageContainer.getId());
		continersMap.remove(storageContainerId);

		if (storageContainer.getLocatedAtPosition() != null && storageContainer.getLocatedAtPosition().getParentContainer() != null)
		{
			Container parentContainer = storageContainer.getLocatedAtPosition().getParentContainer();
			int x = storageContainer.getLocatedAtPosition().getPositionDimensionOne().intValue();
			int y = storageContainer.getLocatedAtPosition().getPositionDimensionTwo().intValue();
			insertSinglePositionInContainerMap((StorageContainer) parentContainer, continersMap, x, y);
		}

	}

	/**
	 * This functions returns a map of containers from the cache.
	 * @return Returns a map of allocated containers vs. their respective free locations.
	 * @throws DAOException
	 */
	public static Map getContainerMapFromCache() throws CacheException
	{
		// TODO if map is null
		// TODO move all code to common utility

		// getting instance of catissueCoreCacheManager and getting participantMap from cache
		CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager.getInstance();
		Map containerMap = (TreeMap) catissueCoreCacheManager.getObjectFromCache(Constants.MAP_OF_STORAGE_CONTAINERS);
		return containerMap;
	}

	/**
	 * This functions returns a list of disabled containers from the cache.
	 * @return Returns a list of disabled.
	 * @throws DAOException
	 */
	public static List getListOfDisabledContainersFromCache() throws Exception
	{
		// TODO if map is null
		// TODO move all code to common utility

		// getting instance of catissueCoreCacheManager and getting participantMap from cache
		CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager.getInstance();
		List disabledconts = (ArrayList) catissueCoreCacheManager.getObjectFromCache(Constants.MAP_OF_DISABLED_CONTAINERS);
		return disabledconts;
	}

	/**
	 * This functions returns a map of available rows vs. available columns.
	 * @param container The container.
	 * @return Returns a map of available rows vs. available columns.
	 * @throws DAOException
	 */

	public static Map getAvailablePositionMapForNewContainer(StorageContainer container) throws DAOException
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
					list.add(new NameValueBean(new Integer(y), new Integer(y)));

				}

				if (!list.isEmpty())
				{
					Integer xObj = new Integer(x);
					NameValueBean nvb = new NameValueBean(xObj, xObj);
					map.put(nvb, list);

				}
			}
		}

		return map;
	}
	public static LinkedList<Integer> getFirstAvailablePositionsInContainer(
			StorageContainer storageContainer, Map continersMap, HashSet<String> allocatedPositions) throws DAOException
	{
		//kalpana bug#6001
		NameValueBean storageContainerId = new NameValueBean(storageContainer.getName(), storageContainer.getId());
		TreeMap storageContainerMap = (TreeMap) continersMap.get(storageContainerId);

		Integer xpos= null;
		Integer ypos=null;
		String containerName = storageContainer.getName();
		if (storageContainerMap == null || storageContainerMap.isEmpty())
		{
			throw new DAOException("Storagecontainer information not found!");
		}
		
		Iterator containerPosIterator = storageContainerMap.keySet().iterator();
		while (containerPosIterator.hasNext())
		{
			
			NameValueBean nvb = (NameValueBean) containerPosIterator.next();
			xpos = new Integer(nvb.getValue());
			List yposValues = (List) storageContainerMap.get(nvb);
			Iterator yposIterator = yposValues.iterator();
			
			while(yposIterator.hasNext())
			{
				nvb =(NameValueBean) yposIterator.next();
				ypos= new Integer(nvb.getValue());
				//bug 8294
				String containerValue = null;
				Long containerId = storageContainer.getId();
				if(containerId!=null)
				{
					containerValue = StorageContainerUtil.getStorageValueKey(null, containerId.toString(), xpos, ypos);
				}
				else 
				{
					containerValue = StorageContainerUtil.getStorageValueKey(containerName,null,xpos, ypos);
				}
				if (!allocatedPositions.contains(containerValue))
				{
					LinkedList<Integer> positions = new LinkedList<Integer>();
					positions.add(xpos);
					positions.add(ypos);
					return positions;
				}
			}			
		}
		throw new DAOException("Either Storagecontainer is full! or it cannot accomodate all the specimens.");
	}
	public static synchronized void updateStoragePositions(Map containerMap, StorageContainer currentContainer, StorageContainer oldContainer)
	{
		int xOld = oldContainer.getCapacity().getOneDimensionCapacity().intValue();
		int xNew = currentContainer.getCapacity().getOneDimensionCapacity().intValue();
		int yOld = oldContainer.getCapacity().getTwoDimensionCapacity().intValue();
		int yNew = currentContainer.getCapacity().getTwoDimensionCapacity().intValue();
		NameValueBean storageContainerId = new NameValueBean(currentContainer.getName(), (currentContainer.getId()));
		TreeMap storageContainerMap = (TreeMap) containerMap.get(storageContainerId);
		if (storageContainerMap == null)
		{
			storageContainerMap = new TreeMap();
			containerMap.put(storageContainerId, storageContainerMap);
		}

		if (xNew > xOld)
		{
			for (int i = xOld + 1; i <= xNew; i++)
			{
				NameValueBean xNvb = new NameValueBean(new Integer(i), new Integer(i));

				List yPosList = new ArrayList();
				for (int j = 1; j <= yOld; j++)
				{
					NameValueBean yNvb = new NameValueBean(new Integer(j), new Integer(j));
					yPosList.add(yNvb);

				}
				if (yPosList.size() > 0)
				{
					storageContainerMap.put(xNvb, yPosList);
				}

			}
		}
		if (yNew > yOld)
		{
			for (int i = 1; i <= xNew; i++)
			{
				NameValueBean xNvb = new NameValueBean(new Integer(i), new Integer(i));
				List yPosList = new ArrayList();
				for (int j = yOld + 1; j <= yNew; j++)
				{
					NameValueBean yNvb = new NameValueBean(new Integer(j), new Integer(j));

					yPosList.add(yNvb);
				}
				List yList = (ArrayList) storageContainerMap.get(xNvb);
				if (yList == null)
				{
					storageContainerMap.put(xNvb, yPosList);
				}
				else
				{
					yList.addAll(yPosList);
				}

			}
		}
		
		if (xNew < xOld)
		{
			for (int i = xNew + 1; i <= xOld; i++)
			{
				NameValueBean xNvb = new NameValueBean(new Integer(i), new Integer(i));
				storageContainerMap.remove(xNvb);
			}
		}
		
		if (yNew < yOld)
		{
			for (int i = 1; i <= xNew; i++)
			{
				NameValueBean xNvb = new NameValueBean(new Integer(i), new Integer(i));
				List yPosList = new ArrayList();
				for (int j = yNew + 1; j <= yOld; j++)
				{
					NameValueBean yNvb = new NameValueBean(new Integer(j), new Integer(j));

					yPosList.add(yNvb);
				}
				List yList = (ArrayList) storageContainerMap.get(xNvb);
				if (yList != null)
				{
					yList.removeAll(yPosList);
				}

			}
		}

	}

	public static synchronized void updateNameInCache(Map containerMap, StorageContainer currentContainer, StorageContainer oldContainer)
	{
		//Using treeMap , so can't directly update the key contents.
		Map positionMap = new TreeMap();
		boolean keyRemoved = false;
		Set keySet = containerMap.keySet();
		Iterator itr = keySet.iterator();
		while (itr.hasNext())
		{
			NameValueBean nvb = (NameValueBean) itr.next();
			if (nvb.getValue().equals(oldContainer.getId().toString()) && nvb.getName().equals(oldContainer.getName().toString()))
			{
				positionMap = (Map) containerMap.get(nvb);
				containerMap.remove(nvb);
				keyRemoved = true;
				break;
			}
		}
		if (keyRemoved)
		{
			NameValueBean nvbUpdated = new NameValueBean(currentContainer.getName(), currentContainer.getId());
			containerMap.put(nvbUpdated, positionMap);
		}

	}

	public static boolean chkContainerFull(String storageContainerId, String storageContainerName) throws Exception
	{
		Map containerMap = getContainerMapFromCache();

		NameValueBean nvb = new NameValueBean(storageContainerName, storageContainerId);
		if (containerMap.containsKey(nvb))
			return false;
		else
			return true;

	}
	public static boolean isPostionAvaialble(String storageContainerId , String storageContainerName , String x , String y) throws CacheException
	{
		Map containerMap = getContainerMapFromCache();
		NameValueBean nvb = new NameValueBean(storageContainerName, storageContainerId);
		Map positionMap = (Map)containerMap.get(nvb);
		
		if(positionMap != null && !positionMap.isEmpty())
		{
			NameValueBean xNvb = new NameValueBean(new Integer(x),new Integer(x));
			List yList = (List) positionMap.get(xNvb);
			if(yList != null && !yList.isEmpty())
			{
				NameValueBean yNvb = new NameValueBean(y ,y);
				if(!yList.contains(yNvb))
					return false;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
		return true;
		
	}

	/**
	 *  This method gives first valid storage position to a specimen if it is not given. 
	 *  If storage position is given it validates the storage position
	 * @param specimen
	 * @throws DAOException
	 */
	public static void validateStorageLocationForSpecimen(Specimen specimen) throws DAOException
	{
		if (specimen.getSpecimenPosition() != null && specimen.getSpecimenPosition().getStorageContainer() != null)
		{
			//Long storageContainerId = specimen.getStorageContainer().getId();
			Integer xPos = null;
			Integer yPos = null;
			if(specimen.getSpecimenPosition() != null)
			{
				xPos = specimen.getSpecimenPosition().getPositionDimensionOne();
				yPos = specimen.getSpecimenPosition().getPositionDimensionTwo();
			}
			boolean isContainerFull = false;
			/**
			 *  Following code is added to set the x and y dimension in case only storage container is given 
			 *  and x and y positions are not given 
			 */
			
			if (xPos == null || yPos == null)
			{
				isContainerFull = true;
				Map containerMapFromCache = null;
				try
				{
					containerMapFromCache = (TreeMap) StorageContainerUtil.getContainerMapFromCache();
				}
				catch (CacheException e)
				{
					e.printStackTrace();
				}
			
				if (containerMapFromCache != null)
				{
					Iterator itr = containerMapFromCache.keySet().iterator();
					
					while (itr.hasNext())
					{
						NameValueBean nvb = (NameValueBean) itr.next();
						if(nvb.getValue().toString().equals(specimen.getSpecimenPosition().getStorageContainer().getId().toString()))
						{
						
							Map tempMap = (Map) containerMapFromCache.get(nvb);
							Iterator tempIterator = tempMap.keySet().iterator();;
							NameValueBean nvb1 = (NameValueBean) tempIterator.next();
							
							List list = (List) tempMap.get(nvb1);
							NameValueBean nvb2 = (NameValueBean) list.get(0);
									
							SpecimenPosition specPos = specimen.getSpecimenPosition();							
							specPos.setPositionDimensionOne(new Integer(nvb1.getValue()));
						    specPos.setPositionDimensionTwo(new Integer(nvb2.getValue()));
						  
						    specPos.setSpecimen(specimen);
						    
						    isContainerFull = false;
						    break;
						}
						
					}
				}
				if(specimen.getSpecimenPosition() != null)
				{
					xPos = specimen.getSpecimenPosition().getPositionDimensionOne();
					yPos = specimen.getSpecimenPosition().getPositionDimensionTwo();
				}
			}

			if(isContainerFull)
			{
				throw new DAOException("The Storage Container you specified is full");
			}
			else if (xPos == null || yPos == null || xPos.intValue() < 0 || yPos.intValue() < 0)
			{
				throw new DAOException(ApplicationProperties.getValue("errors.item.format", ApplicationProperties
						.getValue("specimen.positionInStorageContainer")));
			}
		}
		
	}
	
	/**
	 * 
	 * Method which checks whether capacity of a container can be reduced
	 * @param oldContainer
	 * @param container
	 * @return
	 */
		
		public static boolean checkCanReduceDimension(StorageContainer oldContainer, StorageContainer container) {
			Integer oldContainerDimOne = oldContainer.getCapacity().getOneDimensionCapacity();
			Integer oldContainerDimTwo = oldContainer.getCapacity().getTwoDimensionCapacity();
			Integer newContainerDimOne = container.getCapacity().getOneDimensionCapacity();
			Integer newContainerDimTwo = container.getCapacity().getTwoDimensionCapacity();
			
			List deletedPositions = new ArrayList();
			if(newContainerDimOne < oldContainerDimOne)
			{
				for(int i=newContainerDimOne+1;i<oldContainerDimOne+1;i++)
				{
					for(int j=1;j<oldContainerDimTwo+1;j++)
					{
						deletedPositions.add(i+"@"+j);
					}
				}
			}
			if(newContainerDimTwo < oldContainerDimTwo)
			{
				for(int i=newContainerDimTwo+1;i<oldContainerDimTwo+1;i++)
				{
					for(int j=1;j<oldContainerDimOne+1;j++)
					{
						if(!deletedPositions.contains(j+"@"+i))
						{
						   deletedPositions.add(j+"@"+i);
						}
					}
				}
			}
			
			
			int count = 0;
			Map containerMapFromCache = null;
			try
			{
				containerMapFromCache = (TreeMap) StorageContainerUtil.getContainerMapFromCache();
			}
			catch (CacheException e)
			{
				e.printStackTrace();
			}

			if (containerMapFromCache != null)
			{
				Iterator itr = containerMapFromCache.keySet().iterator();
				while (itr.hasNext())
				{
					NameValueBean nvb = (NameValueBean) itr.next();
					if (nvb.getValue().toString().equals(container.getId().toString()))
					{

						Map tempMap = (Map) containerMapFromCache.get(nvb);
						Iterator tempIterator = tempMap.keySet().iterator();
						while(tempIterator.hasNext())
						{
							
						NameValueBean nvb1 = (NameValueBean) tempIterator.next();
						List list = (List) tempMap.get(nvb1);
						for(int i=0;i<list.size();i++)
						{
						  NameValueBean nvb2 = (NameValueBean) list.get(i);
						  String formatedPosition = nvb1.getValue() + "@" + nvb2.getValue();
						  if(deletedPositions.contains(formatedPosition))
						  {
							  count++;
						  }
						}
								
						}
					}

				}
			}
			
			if(count!=deletedPositions.size())
			{
		    	return false;
			}
			return true;
		}
			
		/**
		 * @param dao
		 * @param similarContainerMap
		 * @param containerPrefixKey
		 * @param positionsToBeAllocatedList
		 * @param usedPositionsList
		 * @param iCount
		 * @throws DAOException
		 */
		public static void prepareContainerMap(DAO dao, Map similarContainerMap, String containerPrefixKey,
				List positionsToBeAllocatedList, List usedPositionsList, int iCount, String contId) throws DAOException
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
			if (similarContainerMap.get(radioButonKey)!=null && similarContainerMap.get(radioButonKey).equals("1"))
			{
				containerId = (String) similarContainerMap.get(containerIdKey);
				posDim1 = (String) similarContainerMap.get(posDim1Key);
				posDim2 = (String) similarContainerMap.get(posDim2Key);
				usedPositionsList.add(containerId + Constants.STORAGE_LOCATION_SAPERATOR + posDim1 + Constants.STORAGE_LOCATION_SAPERATOR + posDim2);

			}
			else if (similarContainerMap.get(radioButonKey)!=null && similarContainerMap.get(radioButonKey).equals("2"))
			{
				containerName = (String) similarContainerMap.get(containerNameKey + "_fromMap");

				String sourceObjectName = StorageContainer.class.getName();
				String[] selectColumnName = {"id"};
				String[] whereColumnName = {"name"};
				String[] whereColumnCondition = {"="};
				Object[] whereColumnValue = {containerName};
				String joinCondition = null;

				List list = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);

				if (!list.isEmpty())
				{
					containerId = list.get(0).toString();
				}
				else
				{
					String message = ApplicationProperties.getValue("specimen.storageContainer");
					throw new DAOException(ApplicationProperties.getValue("errors.invalid", message));
				}

				posDim1 = (String) similarContainerMap.get(posDim1Key + "_fromMap");
				posDim2 = (String) similarContainerMap.get(posDim2Key + "_fromMap");

				if (posDim1 == null || posDim1.trim().equals("") || posDim2 == null || posDim2.trim().equals(""))
				{
					positionsToBeAllocatedList.add(new Integer(iCount));
				}
				else
				{

					usedPositionsList.add(containerId + Constants.STORAGE_LOCATION_SAPERATOR + posDim1 + Constants.STORAGE_LOCATION_SAPERATOR
							+ posDim2); 
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

			if (containerMap.size() > 0)
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

				Logger.out.info("Starting points[0]" + startingPoints[0]);
				Logger.out.info("Starting points[1]" + startingPoints[1]);
				Logger.out.info("Starting points[2]" + startingPoints[2]);
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
	
		public static void populateAliquotMap(String objectKey, Map containerMap, Map aliquotMap, String noOfAliquots)
		{
			int counter = 1;
			if (!containerMap.isEmpty())
			{
				Object[] containerId = containerMap.keySet().toArray();
				for (int i = 0; i < containerId.length; i++)
				{
					Map xDimMap = (Map) containerMap.get(containerId[i]);
					counter = setAliquotMap(objectKey, xDimMap,containerId, noOfAliquots,counter,aliquotMap, i);
					if (counter <= Integer.parseInt(noOfAliquots))
					{
						i=containerId.length;
					}
				}
			}
		}
		public static int setAliquotMap(String objectKey,Map xDimMap, Object[] containerId, String noOfAliquots, int counter, Map aliquotMap, int i)
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
				SpecimenArrayForm specimenArrayForm, TreeMap containerMap) throws DAOException
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
				List containerList = specimenArrayBizLogic.retrieve(StorageContainer.class.getName(),selectColumnName,whereColumnName,whereColumnCondition,whereColumnValue,joinCondition);
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
			// if not null
			if (containerName != null)
			{
				addPostions(containerMap,Long.valueOf(startingPoints[0]),containerName,Integer.valueOf(startingPoints[1]),Integer.valueOf(startingPoints[2]));
			}
			return initialValues;
		}
		
		/**
		 * add positions while in edit mode
		 * @param containerMap 
		 * @param id
		 * @param containerName
		 * @param pos1
		 * @param pos2
		 */
		public static void addPostions(Map containerMap, Long id, String containerName, Integer pos1, Integer pos2)
		{
			int flag = 0;
			NameValueBean xpos = new NameValueBean(pos1, pos1);
			NameValueBean ypos = new NameValueBean(pos2, pos2);
			NameValueBean parentId = new NameValueBean(containerName, id);

			Set keySet = containerMap.keySet();
			Iterator itr = keySet.iterator();
			while (itr.hasNext())
			{
				NameValueBean nvb = (NameValueBean) itr.next();
				if (nvb.getValue().equals(id.toString()))
				{
					Map pos1Map = (Map) containerMap.get(nvb);
					Set keySet1 = pos1Map.keySet();
					Iterator itr1 = keySet1.iterator();
					while (itr1.hasNext())
					{
						NameValueBean nvb1 = (NameValueBean) itr1.next();
						if (nvb1.getValue().equals(pos1.toString()))
						{
							List pos2List = (List) pos1Map.get(nvb1);
							pos2List.add(ypos);
							flag = 1;
							break;
						}
					}
					if (flag != 1)
					{
						List pos2List = new ArrayList();
						pos2List.add(ypos);
						pos1Map.put(xpos, pos2List);
						flag = 1;
					}
				}
			}
			if (flag != 1)
			{
				List pos2List = new ArrayList();
				pos2List.add(ypos);

				Map pos1Map = new TreeMap();
				pos1Map.put(xpos, pos2List);
				containerMap.put(parentId, pos1Map);

			}
		}
		/**
		 *  This method allocates available position to single specimen
		 * @param object
		 * @param aliquotMap
		 * @param usedPositionsList
		 * @throws DAOException
		 */
		public static void allocatePositionToSingleContainerOrSpecimen(Object object, Map aliquotMap, 
				List usedPositionsList,String spKey,String scId) throws DAOException
		{
			int specimenNumber = ((Integer) object).intValue();
			String specimenKey = spKey;
			String containerNameKey = specimenKey + specimenNumber + "_StorageContainer_name";
			String containerIdKey = specimenKey + specimenNumber + scId;
			String posDim1Key = specimenKey + specimenNumber + "_positionDimensionOne";
			String posDim2Key = specimenKey + specimenNumber + "_positionDimensionTwo";
			String containerName = (String) aliquotMap.get(containerNameKey + "_fromMap");

			boolean isContainerFull = false;
			Map containerMapFromCache = null;
			try
			{
				containerMapFromCache = (TreeMap) StorageContainerUtil.getContainerMapFromCache();
			}
			catch (CacheException e)
			{
				e.printStackTrace();
			}

			if (containerMapFromCache != null)
			{
				Iterator itr = containerMapFromCache.keySet().iterator();
				while (itr.hasNext())
				{
					NameValueBean nvb = (NameValueBean) itr.next();
					String containerNameFromCacheName = nvb.getName().toString();
				
					// TODO
					if (containerNameFromCacheName.equalsIgnoreCase(containerName.trim()))
					{
						String containerId = nvb.getValue();
						Map tempMap = (Map) containerMapFromCache.get(nvb);
						Iterator tempIterator = tempMap.keySet().iterator();

						while (tempIterator.hasNext())
						{
							NameValueBean nvb1 = (NameValueBean) tempIterator.next();
							List yPosList = (List) tempMap.get(nvb1);
							for (int i = 0; i < yPosList.size(); i++)
							{
								NameValueBean nvb2 = (NameValueBean) yPosList.get(i);
								String availaleStoragePosition = containerId + Constants.STORAGE_LOCATION_SAPERATOR + nvb1.getValue()
										+ Constants.STORAGE_LOCATION_SAPERATOR + nvb2.getValue();
								int j = 0;
								
								for (; j < usedPositionsList.size(); j++)
								{
									if (usedPositionsList.get(j).toString().equals(availaleStoragePosition))
										break;
								}
								if (j==usedPositionsList.size())
								{
				            		 usedPositionsList.add(availaleStoragePosition);
									 aliquotMap.put(containerIdKey,containerId);
									 aliquotMap.put(posDim1Key, nvb1.getValue());
									 aliquotMap.put(posDim2Key, nvb2.getValue());
									 aliquotMap.remove(containerIdKey+"_fromMap");
									 aliquotMap.remove(posDim1Key+"_fromMap");
									 aliquotMap.remove(posDim2Key+"_fromMap");
	                                 return;
								}

							}
						}
					}
				}
			}
			
			throw new DAOException("The container you specified does not have enough space to allocate storage position for Container Number " + specimenNumber);
		}
		
		/**
		 * @return
		 */
		public static boolean checkPos1AndPos2(String pos1, String pos2)
		{
			boolean flag = false;
			if(pos1!=null&&!pos1.trim().equals(""))
			{
				long l = 1;
			      try 
				  {
			        	l = Long.parseLong(pos1);
				  }
				 catch(Exception e)
				 {
				 	flag = true;
					
				 }
				 if(l<=0)
				 {
				 	flag = true;
				 }
			}
			if(pos2!=null&&!pos2.trim().equals(""))
			{
				long l = 1;
			      try 
				  {
			        	l = Long.parseLong(pos2);
				  }
				 catch(Exception e)
				 {
				 	flag = true;
					
				 }
				 if(l<=0)
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
		public static Collection getChildren(DAO dao,Long containerId) throws DAOException
		{
			String hql = "select cntPos.occupiedContainer from ContainerPosition cntPos, StorageContainer container where cntPos.occupiedContainer.id=container.id and cntPos.parentContainer.id ="+containerId;
			List childrenColl = new ArrayList();
			try
			{
				childrenColl = dao.executeQuery(hql, null, false, null);
			}
			catch (ClassNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		      return childrenColl;
		}
		  
		  /**
		 * @param children
		 * @param dao
		 * @param containerId
		 * @throws DAOException
		 */
		public static void setChildren(Collection children, DAO dao, Long containerId) throws DAOException
		{
			  if(children != null)
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
		public static String getStorageValueKey(String containerName,String containerID,Integer containerPos1,Integer containerPos2)
		{
			StringBuffer storageValue = new StringBuffer();
			if(containerName!=null)
			{
				storageValue.append(containerName);
				storageValue.append(":");
				storageValue.append(containerPos1);
				storageValue.append(" ,");
				storageValue.append(containerPos2);				
			}
			else if(containerID!=null)
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

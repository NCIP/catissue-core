package edu.wustl.catissuecore.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * This class exposes the functionality to set storage containers
 * automatically for multiple specimens.
 * @author abhijit_naik
 * 
 */
public class SpecimenAutoStorageContainer {

	private HashMap<String, ArrayList<GenericSpecimen>> specimenMap = new HashMap<String, ArrayList<GenericSpecimen>> ();
	private Long cpId = null;
	private HashMap<Long, HashMap<String, ArrayList<GenericSpecimen>>> collectionProtocolSpecimenMap = 
						new HashMap<Long, HashMap<String,ArrayList<GenericSpecimen>>> ();
	private ArrayList<String> storageContainerIds = new ArrayList<String>();
	
	public void setCollectionProtocol(Long cpId)
	{
		
		this.cpId = cpId;
	}
	
	public void addSpecimen(GenericSpecimen specimen, String className)
	{
		addToMap(specimen, className, specimenMap);
	}
	 
	public void addSpecimen(GenericSpecimen specimen, String className, Long collectionProtocolId)
	{ 
		if (collectionProtocolSpecimenMap.get(collectionProtocolId) == null)
		{ 
			collectionProtocolSpecimenMap.put(collectionProtocolId, new HashMap<String, ArrayList<GenericSpecimen>> ());
		}
		HashMap<String, ArrayList<GenericSpecimen>> targetMap = collectionProtocolSpecimenMap.get(collectionProtocolId);
		addToMap(specimen, className, targetMap);
	}
	
	private void addToMap (GenericSpecimen specimen, String className, HashMap<String, ArrayList<GenericSpecimen>> targetMap)
	{
		if( targetMap.get(className) == null)
		{
			targetMap.put(className, new ArrayList<GenericSpecimen>());
		}
		ArrayList<GenericSpecimen> specimenList = targetMap.get(className);
		specimenList.add(specimen);		
	}

	public void setSpecimenStoragePositions(SessionDataBean sessionDataBean) throws DAOException
	{

		storageContainerIds.clear();
		setAutoStoragePositions(specimenMap, sessionDataBean, cpId);
		
	}
	public void setCollectionProtocolSpecimenStoragePositions(
			SessionDataBean sessionDataBean) throws DAOException
	{
 
		storageContainerIds.clear();
		Set<Long> keySet = collectionProtocolSpecimenMap.keySet();
		Iterator<Long> keySetIterator = keySet.iterator();
		
		while(keySetIterator.hasNext())
		{
			Long collectionProtocolId = keySetIterator.next();
			
			HashMap<String, ArrayList<GenericSpecimen>> autoSpecimenMap =
				collectionProtocolSpecimenMap.get(collectionProtocolId);
			
			setAutoStoragePositions(autoSpecimenMap, sessionDataBean,
					collectionProtocolId );
		}	
	}

	/**
	 * @param sessionDataBean
	 * @throws DAOException
	 */
	private void setAutoStoragePositions(
			HashMap<String, ArrayList<GenericSpecimen>> autoSpecimenMap, 
			SessionDataBean sessionDataBean, Long collectionProtocolId)
			throws DAOException {
		
		Set<String> keySet = autoSpecimenMap.keySet();
		if (!keySet.isEmpty())
		{
			Iterator<String> keySetIterator = keySet.iterator();

			while(keySetIterator.hasNext())
			{
				String key = keySetIterator.next();
				ArrayList<GenericSpecimen> specimenList =
					autoSpecimenMap.get(key);
				setSpecimenStorageDetails(specimenList,key, sessionDataBean, collectionProtocolId);
			}
		}
	}
	
	protected void setSpecimenStorageDetails(List<GenericSpecimen> specimenDataBeanList, 
			String className, SessionDataBean bean, Long collectionProtocolId ) throws DAOException
	{
 
		StorageContainerBizLogic bizLogic = (StorageContainerBizLogic) BizLogicFactory.getInstance()
		.getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
		
		TreeMap containerMap = bizLogic.getAllocatedContaienrMapForSpecimen(
				collectionProtocolId.longValue(), className, 0, "false", bean, true);
	
		populateStorageLocations(specimenDataBeanList, containerMap);
	}
	
	protected void populateStorageLocations(List specimenDataBeanList, Map containerMap)
	{
		
		int counter = 0;

		if (!containerMap.isEmpty())
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

						for (int k = 0; k < yDimList.size(); k++)
						{
							if(counter < specimenDataBeanList.size())
							{
								GenericSpecimen specimenDataBean = (GenericSpecimen)specimenDataBeanList.get(counter);
								String stName = ((NameValueBean) containerId[i]).getName();
								String posOne = ((NameValueBean) xDim[j]).getValue();
								String posTwo = ((NameValueBean) yDimList.get(k)).getValue();
								String storageValue = stName+":"+posOne+" ,"+posTwo; 
								if(!storageContainerIds.contains(storageValue))
								{													
									specimenDataBean.setContainerId(((NameValueBean) containerId[i]).getValue());
									specimenDataBean.setSelectedContainerName(stName);
									specimenDataBean.setPositionDimensionOne(posOne);
									specimenDataBean.setPositionDimensionTwo(posTwo);
									storageContainerIds.add(storageValue);
									counter++;									
								}
							}
							else
							{
								break;
							}
						}
					}
				}
			}
		}

	}

}

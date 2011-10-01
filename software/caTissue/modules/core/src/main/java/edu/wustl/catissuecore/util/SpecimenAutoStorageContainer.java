package edu.wustl.catissuecore.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.bizlogic.StorageContainerForSpecimenBizLogic;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.exception.SMException;

/**
 * This class exposes the functionality to set storage containers
 * automatically for multiple specimens.
 * @author abhijit_naik
 *
 */
public class SpecimenAutoStorageContainer {

	private transient Logger logger = Logger.getCommonLogger(SpecimenAutoStorageContainer.class);
	private LinkedHashMap<String, LinkedList<GenericSpecimen>> specimenMap =
		new LinkedHashMap<String, LinkedList<GenericSpecimen>> ();
	private Long cpId = null;
	private LinkedHashMap<Long, LinkedHashMap<String, LinkedList<GenericSpecimen>>> collectionProtocolSpecimenMap =
						new LinkedHashMap<Long, LinkedHashMap<String,LinkedList<GenericSpecimen>>> ();
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
			collectionProtocolSpecimenMap.put(collectionProtocolId, new LinkedHashMap<String, LinkedList<GenericSpecimen>> ());
		}
		LinkedHashMap<String, LinkedList<GenericSpecimen>> targetMap = collectionProtocolSpecimenMap.get(collectionProtocolId);
		addToMap(specimen, className, targetMap);
	}

	private void addToMap (GenericSpecimen specimen, String className, LinkedHashMap<String, LinkedList<GenericSpecimen>> targetMap)
	{
		if( targetMap.get(className) == null)
		{
			targetMap.put(className, new LinkedList<GenericSpecimen>());
		}
		LinkedList<GenericSpecimen> specimenList = targetMap.get(className);
		specimenList.add(specimen);
	}

	public void setSpecimenStoragePositions(SessionDataBean sessionDataBean) throws ApplicationException
	{

		storageContainerIds.clear();
		setAutoStoragePositions(specimenMap, sessionDataBean, cpId);

	}
	public void setCollectionProtocolSpecimenStoragePositions(
			SessionDataBean sessionDataBean) throws ApplicationException
	{


		storageContainerIds.clear();
		Set<Long> keySet = collectionProtocolSpecimenMap.keySet();
		Iterator<Long> keySetIterator = keySet.iterator();

		while(keySetIterator.hasNext())
		{
			Long collectionProtocolId = keySetIterator.next();

			LinkedHashMap<String, LinkedList<GenericSpecimen>> autoSpecimenMap =
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
			LinkedHashMap<String, LinkedList<GenericSpecimen>> autoSpecimenMap,
			SessionDataBean sessionDataBean, Long cpId)
			throws ApplicationException
{

		DAO dao = null;
		try
		{
			dao = AppUtility.openDAOSession(sessionDataBean);

			Set<String> keySet = autoSpecimenMap.keySet();
			if (!keySet.isEmpty())
			{
				Iterator<String> keySetIterator = keySet.iterator();

				while(keySetIterator.hasNext())
				{
					String key = keySetIterator.next();
					LinkedList<GenericSpecimen> specimenList =
						autoSpecimenMap.get(key);
					setSpecimenStorageDetails(specimenList,key,sessionDataBean, cpId,dao);
				}
			}
		}
		catch(DAOException daoException)
		{
			this.logger.error(daoException.getMessage(),daoException);
			daoException.printStackTrace();
			throw AppUtility.getApplicationException(daoException, daoException.getErrorKeyName(), daoException.getMsgValues());
		}
		finally
		{
			AppUtility.closeDAOSession(dao);
		}
	}

	protected void setSpecimenStorageDetails(LinkedList<GenericSpecimen> specimenDataBeanList,
			String className, SessionDataBean bean, Long cpId ,DAO dao) throws ApplicationException
	{
		try
		{
				StorageContainerForSpecimenBizLogic bizLogic =
					new StorageContainerForSpecimenBizLogic();
				Iterator<GenericSpecimen> itr = specimenDataBeanList.iterator();
				Map<String, LinkedList<GenericSpecimen>> spTypeMap =
					new LinkedHashMap<String, LinkedList<GenericSpecimen>>();
				String specimenType = null;
				while(itr.hasNext())
				{
					GenericSpecimen specimenDataBean =
						(GenericSpecimen)itr.next();
					specimenType = specimenDataBean.getType();
					if(!spTypeMap.keySet().contains(specimenType))
					{
						spTypeMap.put(specimenType,new LinkedList<GenericSpecimen>());
					}
					spTypeMap.get(specimenType).add(specimenDataBean);
				}
				Iterator<String> spTIterator = spTypeMap.keySet().iterator();
				Map containerMap = null;
				while(spTIterator.hasNext())
				{
					specimenType = spTIterator.next();
					containerMap = bizLogic.getAllocatedContainerMapForSpecimen(
						AppUtility.setparameterList(cpId.longValue(),className,0,
						specimenType), bean, dao);
					populateStorageLocations(spTypeMap.get(specimenType),
						cpId.longValue(), containerMap, bean, className);
				}
				spTypeMap.clear();
			}
			catch (ApplicationException exception)
			{
				this.logger.error(exception.getMessage(), exception);
				exception.printStackTrace();
				throw AppUtility.getApplicationException( exception,exception.getErrorKeyName(),
						exception.getMsgValues());
			}

	}

	protected void populateStorageLocations(LinkedList specimenDataBeanList,
							Long collectionProtocolId, Map containerMap,
							SessionDataBean bean, String classType)
			throws SMException,DAOException
	{

		int counter = 0;

		if (containerMap.isEmpty())
		{
			return;
		}
		Object[] containerId = containerMap.keySet().toArray();

		for (int i = 0; i < containerId.length; i++)
		{
			if(counter >= specimenDataBeanList.size())
			{
				break;
			}

			String storageId = ((NameValueBean) containerId[i]).getValue();
			InstanceFactory<StorageContainer> scInstFact = DomainInstanceFactory.getInstanceFactory(StorageContainer.class);
			StorageContainer storageCont = scInstFact.createObject();//new StorageContainer();
			storageCont.setId(Long.valueOf(storageId));
			storageCont.setName(((NameValueBean) containerId[i]).getName());


			Map xDimMap = (Map) containerMap.get(containerId[i]);
			if (!xDimMap.isEmpty())
			{
				counter = populateStoragePositions(specimenDataBeanList,  counter,
						 storageCont, xDimMap);
			}
		}

	}


	/**
	 * @param specimenDataBeanList
	 * @param counter
	 * @param storageContainer
	 * @param xDimMap
	 * @return
	 */
	private int populateStoragePositions(LinkedList specimenDataBeanList, int counter,
			StorageContainer storageContainer, Map xDimMap)
	{

		Object[] xDim = xDimMap.keySet().toArray();

		for (int j = 0; j < xDim.length; j++)
		{
			List yDimList = (List) xDimMap.get(xDim[j]);
			if(counter >= specimenDataBeanList.size())
			{
				break;
			}

			for (int k = 0; k < yDimList.size(); k++)
			{
				if(counter >= specimenDataBeanList.size())
				{
					break;
				}
				GenericSpecimen specimenDataBean =
					(GenericSpecimen)specimenDataBeanList.get(counter);
				String stName = storageContainer.getName();
				String posOne = ((NameValueBean) xDim[j]).getValue();
				String posTwo = ((NameValueBean) yDimList.get(k)).getValue();
				String storageValue = stName+":"+posOne+" ,"+posTwo;

				if(specimenDataBean.getReadOnly())
				{
					storageValue = specimenDataBean.getSelectedContainerName()+":"+specimenDataBean.getPositionDimensionOne()+" ,"+specimenDataBean.getPositionDimensionTwo();
					k--;
					counter++;
				}
				else
				{
					if(!storageContainerIds.contains(storageValue))
					{
						specimenDataBean.setContainerId(String.valueOf(storageContainer.getId()));
						specimenDataBean.setSelectedContainerName(stName);
						specimenDataBean.setPositionDimensionOne(posOne);
						specimenDataBean.setPositionDimensionTwo(posTwo);
						storageContainerIds.add(storageValue);
						counter++;
					}
					else
					{
						continue;
					}
				}
			}
		}
		return counter;
	}

	public void fillAllocatedPositionSet(Set asignedPositonSet)
	{
		Iterator keyItr = specimenMap.keySet().iterator();
		while(keyItr.hasNext())
		{
			String key = (String)keyItr.next();
			LinkedList speciList = (LinkedList)specimenMap.get(key);
			Iterator speciListItr = speciList.iterator();
			while(speciListItr.hasNext())
			{
				GenericSpecimen specimen = (GenericSpecimen)speciListItr.next();
				//Mandar : 19Aug08 ---start
				if(specimen.getSelectedContainerName() != null)
				{
					String allocatedPos = specimen.getSelectedContainerName() + "#"+ specimen.getContainerId()+"#"+specimen.getPositionDimensionOne()+"#"+specimen.getPositionDimensionTwo();
					asignedPositonSet.add(allocatedPos);
				}
				//Mandar : 19Aug08 ---end
			}
		}
	}
}

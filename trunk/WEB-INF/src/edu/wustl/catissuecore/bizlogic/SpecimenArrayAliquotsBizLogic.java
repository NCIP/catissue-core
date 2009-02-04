/*
 * Created on Sep 22, 2006
 */
package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.ehcache.CacheException;

import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Quantity;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayContent;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;


/**
 * SpecimenArrayAliquotsBizLogic class is used to create SpecimenArray aliquots from the parent SpecimenArray
 * and to inserts all the aliquotes into the database 
 * @author jitendra_agrawal
 */
public class SpecimenArrayAliquotsBizLogic extends DefaultBizLogic
{
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
	throws DAOException, UserNotAuthorizedException
	{
		SpecimenArray specimenArray = (SpecimenArray) obj;
		
		String specimenKey = "SpecimenArray:";
		Map aliquotMap = specimenArray.getAliqoutMap();
//		Retrieving the parent specimenArray of the aliquot
		SpecimenArray parentSpecimenArray = (SpecimenArray) dao.retrieve(SpecimenArray.class.getName(), specimenArray.getId());
		
		SpecimenArrayBizLogic specimenArrayBizLogic = (SpecimenArrayBizLogic) BizLogicFactory
		.getInstance().getBizLogic(Constants.SPECIMEN_ARRAY_FORM_ID);

		List positionsToBeAllocatedList = new ArrayList();
		List usedPositionsList = new ArrayList();

		for (int i = 1; i <= specimenArray.getAliquotCount(); i++)
		{
			String radioButonKey = "radio_" + i;
			String containerIdKey = specimenKey + i + "_StorageContainer_id";
			String containerNameKey = specimenKey + i + "_StorageContainer_name";
			String posDim1Key = specimenKey + i + "_positionDimensionOne";
			String posDim2Key = specimenKey + i + "_positionDimensionTwo";

			String containerName = null;
			String containerId = null;
			String posDim1 = null;
			String posDim2 = null;
			//get the container values based on user selection from dropdown or map
			if (aliquotMap.get(radioButonKey).equals("1"))
			{
				containerId = (String) aliquotMap.get(containerIdKey);
				posDim1 = (String) aliquotMap.get(posDim1Key);
				posDim2 = (String) aliquotMap.get(posDim2Key);
				usedPositionsList.add(containerId + Constants.STORAGE_LOCATION_SAPERATOR + posDim1 + Constants.STORAGE_LOCATION_SAPERATOR + posDim2);

			}
			else if (aliquotMap.get(radioButonKey).equals("2"))
			{
				containerName = (String) aliquotMap.get(containerNameKey + "_fromMap");

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

				posDim1 = (String) aliquotMap.get(posDim1Key + "_fromMap");
				posDim2 = (String) aliquotMap.get(posDim2Key + "_fromMap");

				if (posDim1 == null || posDim1.trim().equals("") || posDim2 == null || posDim2.trim().equals(""))
				{
					positionsToBeAllocatedList.add(new Integer(i));
				}
				else
				{

					usedPositionsList.add(containerId + Constants.STORAGE_LOCATION_SAPERATOR + posDim1 + Constants.STORAGE_LOCATION_SAPERATOR
							+ posDim2);
					aliquotMap.put(containerIdKey, containerId);
					aliquotMap.put(posDim1Key, posDim1);
					aliquotMap.put(posDim2Key, posDim2);
					aliquotMap.remove(containerIdKey + "_fromMap");
					aliquotMap.remove(posDim1Key + "_fromMap");
					aliquotMap.remove(posDim2Key + "_fromMap");
				}

			}

		}

		for (int i = 0; i < positionsToBeAllocatedList.size(); i++)
		{
			allocatePositionToSingleSpecimen(positionsToBeAllocatedList.get(i), aliquotMap, usedPositionsList);
		}
		
		

		for (int i = 1; i <= specimenArray.getAliquotCount(); i++)
		{
			//Preparing the map keys			
			String radioButonKey = "radio_"+i;
			String labelKey = specimenKey + i + "_label";
			String barcodeKey = specimenKey + i + "_barcode";
			String containerIdKey = specimenKey + i + "_StorageContainer_id";
			String containerNameKey = specimenKey + i + "_StorageContainer_name";
			String posDim1Key = specimenKey + i + "_positionDimensionOne";
			String posDim2Key = specimenKey + i + "_positionDimensionTwo";
			String storageContainerNameKey = specimenKey + i + "_StorageContainer_name";
									
			//Retrieving the quantity, barcode & location values for each aliquot
			String label = (String) aliquotMap.get(labelKey);
			String barcode = (String) aliquotMap.get(barcodeKey);
					
			String containerId = (String) aliquotMap.get(containerIdKey);
			String posDim1 = (String) aliquotMap.get(posDim1Key);
			String posDim2 = (String) aliquotMap.get(posDim2Key);
			//Create an object of Specimen Subclass
			SpecimenArray aliquotSpecimenArray = new SpecimenArray();
			
			/**
			 * Start: Change for API Search   --- Jitendra 06/10/2006
			 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
			 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
			 * So we removed default class level initialization on domain object and are initializing in method
			 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
			 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
			 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
			 */
			ApiSearchUtil.setSpecimenArrayDefault(aliquotSpecimenArray);
	    	
	    	//End: Change for API Search
			

			if (parentSpecimenArray != null)
			{
				//check for closed ParentSpecimenArray
				checkStatus(dao, parentSpecimenArray, "Parent SpecimenArray");
				aliquotSpecimenArray.setParent(parentSpecimenArray);
				aliquotSpecimenArray.setSpecimenArrayType(parentSpecimenArray.getSpecimenArrayType());				
				aliquotSpecimenArray.setCreatedBy(parentSpecimenArray.getCreatedBy());
				aliquotSpecimenArray.setCapacity(parentSpecimenArray.getCapacity());
				Collection specimenArrayContentCollection = PopulateSpecimenArrayContentCollectionForAliquot(parentSpecimenArray,aliquotSpecimenArray,specimenArray.getAliquotCount());
				aliquotSpecimenArray.setSpecimenArrayContentCollection(specimenArrayContentCollection);
			}
			
			aliquotSpecimenArray.setAliquot(true);
			aliquotSpecimenArray.setFull(Boolean.valueOf(false));
			
			//Explicity set barcode to null if it is empty as its a unique key in the database
			if (barcode != null && barcode.trim().length() == 0)
			{
				barcode = null;
			}
			aliquotSpecimenArray.setBarcode(barcode);

			//Explicity set barcode to null if it is empty as its a unique key in the database
			if (label != null && label.trim().length() == 0)
			{
				label = null;
			}
			aliquotSpecimenArray.setName(label);

			try
			{
				if (containerId != null)
				{
					//Setting the storage container of the aliquot
//					StorageContainer container = (StorageContainer) dao.retrieve(
//							StorageContainer.class.getName(), new Long(containerId));
//
//					if (container != null)
//					{
					StorageContainer storageContainerObj = new StorageContainer();
					storageContainerObj.setId(new Long(containerId));

						//check for closed Storage Container
						checkStatus(dao, storageContainerObj, "Storage Container");

						StorageContainerBizLogic storageContainerBizLogic = (StorageContainerBizLogic) BizLogicFactory
								.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);

						//check for all validations on the storage container.
						storageContainerBizLogic.checkContainer(dao, containerId, posDim1, posDim2,
								sessionDataBean,false);

						aliquotSpecimenArray.setStorageContainer(storageContainerObj);
						String sourceObjectName = StorageContainer.class.getName();
						String[] selectColumnName = {"name"};
						String[] whereColumnName = {"id"}; //"storageContainer."+Constants.SYSTEM_IDENTIFIER
						String[] whereColumnCondition = {"="};
						Object[] whereColumnValue = {containerId};
						String joinCondition = null;

						List list = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);

						if (!list.isEmpty())
						{
							storageContainerObj.setName((String)list.get(0));
							aliquotMap.put(storageContainerNameKey,(String)list.get(0));
						}
//					}
				}
				else
				{
					aliquotSpecimenArray.setStorageContainer(null);
				}
			}
			catch (SMException sme)
			{
				sme.printStackTrace();
				throw handleSMException(sme);
			}

			//Setting the attributes - storage positions, available, acivity status & lineage
			if (containerId != null)
			{
				aliquotSpecimenArray.setPositionDimensionOne(new Integer(posDim1));
				aliquotSpecimenArray.setPositionDimensionTwo(new Integer(posDim2));
			}
			else
			{
				aliquotSpecimenArray.setPositionDimensionOne(null);
				aliquotSpecimenArray.setPositionDimensionTwo(null);
			}
			aliquotSpecimenArray.setAvailable(Boolean.TRUE);
			aliquotSpecimenArray.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
			//aliquotSpecimenArray.setLineage(Constants.ALIQUOT);

			//Inserting an aliquot in the database			
			specimenArrayBizLogic.insert(aliquotSpecimenArray, dao, sessionDataBean);	
			//postInsert(aliquotSpecimenArray, dao, sessionDataBean);
			
			
		}
		
		if (parentSpecimenArray != null)
		{	
			SpecimenArray oldSpecimenArray = (SpecimenArray) dao.retrieve(SpecimenArray.class.getName(), specimenArray.getId());;
			updateParentSpecimenArray(parentSpecimenArray);			
			specimenArrayBizLogic.update(dao, parentSpecimenArray, oldSpecimenArray, sessionDataBean);	
		}
		
		//Populate aliquot map with parent specimenArray's data
		populateParentSpecimenArrayData(aliquotMap, specimenArray, parentSpecimenArray);
		
	}	 
	
	/**
	 *  This method allocates available position to single specimen
	 * @param object
	 * @param aliquotMap
	 * @param usedPositionsList
	 * @throws DAOException
	 */
	private void allocatePositionToSingleSpecimen(Object object, Map aliquotMap, List usedPositionsList) throws DAOException
	{
		int specimenNumber = ((Integer) object).intValue();
		String specimenKey = "SpecimenArray:";
		List positionsToBeAllocatedList = new ArrayList();
		String containerNameKey = specimenKey + specimenNumber + "_StorageContainer_name";
		String containerIdKey = specimenKey + specimenNumber + "_StorageContainer_id";
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
		
		throw new DAOException("The container you specified does not have enough space to allocate storage position for Aliquot Number " + specimenNumber);
	}
	
	private void updateParentSpecimenArray(SpecimenArray parentSpecimenArray)
	{
		parentSpecimenArray.setAvailable(Boolean.valueOf(false));
		parentSpecimenArray.setAliquot(true);
		Collection specimenArrayContentCollection = parentSpecimenArray.getSpecimenArrayContentCollection();
		if(specimenArrayContentCollection != null && !specimenArrayContentCollection.isEmpty())
		{
			Iterator itr = specimenArrayContentCollection.iterator();
			while(itr.hasNext())
			{
				SpecimenArrayContent arrayContent = (SpecimenArrayContent) itr.next();
				if (arrayContent.getSpecimen() instanceof MolecularSpecimen) 
				{
					arrayContent.getInitialQuantity().setValue(Double.valueOf("0"));
				}
			}
		}
	}
	
	/**
	 * This function populates the parent specimenArray information in aliquot map. This map will be
	 * be retrieved in ForwardToProcessor & will be set in the request scope. Then the map will
	 * be retrieved from request scope in SpecimenArrayAliquotAction if the page is of "SpecimenArrayAliquotSummary" &
	 * the formbean will be populated with the appropriate data.
	 * 
	 * @param aliquotMap Map
	 * @param specimenArray SpecimenArray
	 */
	private void populateParentSpecimenArrayData(Map aliquotMap, SpecimenArray specimenArray, SpecimenArray parentSpecimenArray)
	{
		aliquotMap.put(Constants.ALIQUOT_SPECIMEN_ARRAY_TYPE, parentSpecimenArray.getSpecimenArrayType().getName());
		aliquotMap.put(Constants.ALIQUOT_SPECIMEN_CLASS, parentSpecimenArray.getSpecimenArrayType().getSpecimenClass());
		aliquotMap.put(Constants.ALIQUOT_SPECIMEN_TYPES, parentSpecimenArray.getSpecimenArrayType().getSpecimenTypeCollection());	
		aliquotMap.put(Constants.ALIQUOT_ALIQUOT_COUNTS, String.valueOf(specimenArray.getAliquotCount()));
		
		specimenArray.setAliqoutMap(aliquotMap);
	}
	
	/**
	 * 
	 * @param parentSpecimenArray SpecimenArray
	 * @param aliquotSpecimenArray SpecimenArray
	 * @param aliquotCount int
	 * @return Collection
	 */
	private Collection PopulateSpecimenArrayContentCollectionForAliquot(SpecimenArray parentSpecimenArray, SpecimenArray aliquotSpecimenArray, int aliquotCount)
	{
		Collection parentSpecimenArrayContentCollection = parentSpecimenArray.getSpecimenArrayContentCollection();
		Collection specimenArrayContentCollection =  new HashSet();		
		Iterator iter = parentSpecimenArrayContentCollection.iterator();
		SpecimenArrayContent specimenArrayContent = null;	
		
		for(int i=0; iter.hasNext(); i++)
		{
			SpecimenArrayContent parentSpecimenArrayContent = (SpecimenArrayContent) iter.next();
			specimenArrayContent = new SpecimenArrayContent();
			
			/**
			 * Start: Change for API Search   --- Jitendra 06/10/2006
			 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
			 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
			 * So we removed default class level initialization on domain object and are initializing in method
			 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
			 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
			 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
			 */			
			ApiSearchUtil.setSpecimenArrayContentDefault(specimenArrayContent);
			//End:-  Change for API Search 
			
			specimenArrayContent.setSpecimen(parentSpecimenArrayContent.getSpecimen());
			specimenArrayContent.setPositionDimensionOne(parentSpecimenArrayContent.getPositionDimensionOne());
			specimenArrayContent.setPositionDimensionTwo(parentSpecimenArrayContent.getPositionDimensionTwo());
			specimenArrayContent.setSpecimenArray(aliquotSpecimenArray);				
			Quantity quantity = null;
			if (parentSpecimenArrayContent.getSpecimen() instanceof MolecularSpecimen) 
			{
				if(aliquotCount > 0)
				{
					Double parentInitialQuantity = parentSpecimenArrayContent.getInitialQuantity().getValue();
					double initialQuantity = parentInitialQuantity.doubleValue()/aliquotCount;		
					quantity = new Quantity();
					quantity.setValue(new Double(initialQuantity));	
					specimenArrayContent.setInitialQuantity(quantity);
					// reset quantity value of parent array content to 0.0
					//parentSpecimenArrayContent.getInitialQuantity().setValue(Double.valueOf("0"));
				}
				
				specimenArrayContent.setConcentrationInMicrogramPerMicroliter(parentSpecimenArrayContent.getConcentrationInMicrogramPerMicroliter());
			}
					
			specimenArrayContentCollection.add(specimenArrayContent);
		}
		return specimenArrayContentCollection;
	}
	
	public long getNextAvailableNumber(String sourceObjectName) throws DAOException
	{
		String[] selectColumnName = {"max(IDENTIFIER) as MAX_NAME"};
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		dao.openSession(null);
		List list = dao.retrieve(sourceObjectName, selectColumnName);
		dao.closeSession();
		if (list!=null && !list.isEmpty())
		{
			List columnList = (List) list.get(0);
			if (!columnList.isEmpty())
			{
				String str = (String) columnList.get(0);
				if (!str.equals(""))
				{
					long no = Long.parseLong(str);
					return no + 1;
				}
			}
		}
		return 1;
	}
	
	public void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{		
		SpecimenArray specimenArrayAliquot = (SpecimenArray) obj;
		String specimenKey = "SpecimenArray:";
		Map aliquotMap = specimenArrayAliquot.getAliqoutMap();
		try
		{
			for (int i = 1; i <= specimenArrayAliquot.getAliquotCount(); i++)
			{				
				String containerIdKey = specimenKey + i + "_StorageContainer_id";
				String posDim1Key = specimenKey + i + "_positionDimensionOne";
				String posDim2Key = specimenKey + i + "_positionDimensionTwo";
				String storageContainerNameKey = specimenKey + i + "_StorageContainer_name";
				
				String contId = (String) aliquotMap.get(containerIdKey);
				String contName = (String) aliquotMap.get(storageContainerNameKey);
				String posOne = (String) aliquotMap.get(posDim1Key);
				String posTwo = (String) aliquotMap.get(posDim2Key);
				
				StorageContainer storageContainer = new StorageContainer();
				storageContainer.setId(new Long(contId));
				storageContainer.setName(contName);
				
				Map containerMap = StorageContainerUtil.getContainerMapFromCache();
				StorageContainerUtil.deleteSinglePositionInContainerMap(storageContainer,containerMap,new Integer(posOne).intValue(),new Integer(posTwo).intValue());
			}			
		}
		catch (Exception e)
		{

		}

	}
	
	
}

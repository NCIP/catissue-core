/**
 * <p>Title: StorageContainerHDAO Class>
 * <p>Description:	StorageContainerHDAO is used to add Storage Container information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 23, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.domain.ContainerPosition;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.namegenerator.LabelGenerator;
import edu.wustl.catissuecore.namegenerator.LabelGeneratorFactory;
import edu.wustl.catissuecore.namegenerator.NameGeneratorException;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.tree.TreeDataInterface;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.exception.UserNotAuthorizedException;

/**
 * StorageContainerHDAO is used to add Storage Container information into the
 * database using Hibernate.
 * @author Vaishali_Khandelwal
 */
public class SimilarContainerBizLogic extends StorageContainerBizLogic implements TreeDataInterface
{

	/**
	 * Saves the storageContainer object in the database.
	 * @param obj The storageType object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws BizLogicException
	{
		StorageContainer container = (StorageContainer) obj;
		container.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);

		List contList = new ArrayList();
		int noOfContainers = container.getNoOfContainers().intValue();
		Map simMap = container.getSimilarContainerMap();
		// --- common values for all similar containers ---
		loadStorageType(dao, container);
		Logger.out.debug(simMap);
		int checkButton = Integer.parseInt((String) simMap.get("checkedButton"));
		//int checkButton = 1;

		for (int i = 1; i <= noOfContainers; i++)
		{
			String simContPrefix = "simCont:" + i + "_";
			String IdKey = simContPrefix + "Id";
			String parentContNameKey = simContPrefix + "parentContName";
			String contName = (String) simMap.get(simContPrefix + "name");
			String barcode = (String) simMap.get(simContPrefix + "barcode");

			if (barcode != null && barcode.equals("")) // this is done because barcode is empty string set by struts
			{ // but barcode in DB is unique but can be null.
				barcode = null;
			}
			StorageContainer cont = new StorageContainer(container);
			if (checkButton == 1) // site
			{
				String siteId = (String) simMap.get(simContPrefix + "siteId");
				String siteName = (String) simMap.get(simContPrefix + "siteName");
				
				
				Site site = new Site();

				/**
				 * Start: Change for API Search   --- Jitendra 06/10/2006
				 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
				 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
				 * So we removed default class level initialization on domain object and are initializing in method
				 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
				 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
				 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
				 */
				ApiSearchUtil.setSiteDefault(site);
				//End:- Change for API Search   -

				site.setId(new Long(siteId));
				site.setName(siteName);
				cont.setSite(site);
				loadSite(dao, cont); // <<----

			}
			else
			// parentContainer
			{
				StorageContainer parentContainer = null;
				String parentId = (String) simMap.get(simContPrefix + "parentContainerId");
				String posOne = (String) simMap.get(simContPrefix + "positionDimensionOne");
				String posTwo = (String) simMap.get(simContPrefix + "positionDimensionTwo");

				Object object = dao.retrieve(StorageContainer.class.getName(), new Long(parentId));
				if (object != null)
				{

					parentContainer = (StorageContainer) object;

					cont.setSite(parentContainer.getSite());

				}
				
				StorageContainerBizLogic storageContainerBizLogic = (StorageContainerBizLogic) BizLogicFactory.getInstance().getBizLogic(
						Constants.STORAGE_CONTAINER_FORM_ID);
				try
				{
					//check for all validations on the storage container.
					storageContainerBizLogic.checkContainer(dao, parentContainer.getId().toString(), posOne, posTwo, sessionDataBean, false,null);
				}
				catch (SMException sme)
				{
					sme.printStackTrace();
					throw handleSMException(sme);
				}

				//		StorageContainer parentContainer = new StorageContainer();
				//	parentContainer.setId(new Long(parentId));
				
				ContainerPosition cntPos = cont.getLocatedAtPosition();
				if(cntPos == null)
					cntPos = new ContainerPosition();
				
				cntPos.setPositionDimensionOne(new Integer(posOne));
				cntPos.setPositionDimensionTwo(new Integer(posTwo));
				cntPos.setOccupiedContainer(cont);
				cntPos.setParentContainer(parentContainer);
				
				cont.setLocatedAtPosition(cntPos);

				
			//	cont.setParent(parentContainer); // <<----

				//chk for positions 
				// check for availability of position
				

				
				/*boolean canUse = isContainerAvailableForPositions(dao, cont);

				if (!canUse)
				{
					throw new DAOException(ApplicationProperties.getValue("errors.storageContainer.inUse"));
				} */

				// Have to set Site object for parentContainer
				loadSite(dao, parentContainer); // 17-07-2006
				loadSiteFromContainerId(dao, parentContainer);
				
				cntPos.setPositionDimensionOne(new Integer(posOne));
				cntPos.setPositionDimensionTwo(new Integer(posTwo));
				cntPos.setOccupiedContainer(cont);
				cont.setLocatedAtPosition(cntPos);
				cont.setSite(parentContainer.getSite()); // 16-07-2006 chetan
				Logger.out.debug("^^>> " + parentContainer.getSite());
				simMap.put(parentContNameKey, parentContainer.getName());
			}
			//StorageContainer cont = new StorageContainer();
			cont.setName(contName); // <<----
			cont.setBarcode(barcode); // <<----     		
			//by falguni
			//Storage container label generator
			
			//Call Storage container label generator if its specified to use automatic label generator
			if(edu.wustl.catissuecore.util.global.Variables.isStorageContainerLabelGeneratorAvl )
			{
				LabelGenerator storagecontLblGenerator;
				try 
				{
					storagecontLblGenerator = LabelGeneratorFactory.getInstance(Constants.STORAGECONTAINER_LABEL_GENERATOR_PROPERTY_NAME);
					storagecontLblGenerator.setLabel(cont);
				}
				catch (NameGeneratorException e) 
				{
					throw new DAOException(e.getMessage());
				}
			}
			
			simMap.put(simContPrefix + "name",cont.getName());

			Logger.out.debug("cont.getCollectionProtocol().size() " + cont.getCollectionProtocolCollection().size());
			cont.setActivityStatus("Active");
			dao.insert(cont.getCapacity(), sessionDataBean, true, true);
			dao.insert(cont, sessionDataBean, true, true);

			contList.add(cont);
			container.setId(cont.getId());
			simMap.put(IdKey, cont.getId().toString());
			//simMap.put(parentContNameKey,cont.getParent().getName());
		}
	}

	synchronized public void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		StorageContainer container = (StorageContainer) obj;
		try
		{
			int noOfContainers = container.getNoOfContainers().intValue();
			Map simMap = container.getSimilarContainerMap();
			int checkButton = Integer.parseInt((String) simMap.get("checkedButton"));
			for (int i = 1; i <= noOfContainers; i++)
			{

				String simContPrefix = "simCont:" + i + "_";
				String contName = (String) simMap.get(simContPrefix + "name");
				String Id = (String) simMap.get(simContPrefix + "Id");
				StorageContainer cont = new StorageContainer(container);
				//Logger.out.info("contName:" + contName);

				cont.setId(new Long(Id));
				cont.setName(contName); //by falguni ...Container name is generated via label generator.
				if (checkButton == 2)
				{
					String parentId = (String) simMap.get(simContPrefix + "parentContainerId");
					String posOne = (String) simMap.get(simContPrefix + "positionDimensionOne");
					String posTwo = (String) simMap.get(simContPrefix + "positionDimensionTwo");
					String parentContName = (String) simMap.get(simContPrefix + "parentContName");
					StorageContainer parentContainer = new StorageContainer();
					parentContainer.setId(new Long(parentId));
					parentContainer.setName(parentContName);
					
					ContainerPosition cntPos = cont.getLocatedAtPosition();
					
					cntPos.setPositionDimensionOne(new Integer(posOne));
					cntPos.setPositionDimensionTwo(new Integer(posTwo));
					cntPos.setOccupiedContainer(cont);
					cntPos.setParentContainer(parentContainer);					

		//			cont.setParent(parentContainer); // <<----

				}

				Map containerMap = StorageContainerUtil.getContainerMapFromCache();
				StorageContainerUtil.addStorageContainerInContainerMap(cont, containerMap);
				
			}
		}
		catch (Exception e)
		{

		}
	}

	/**
	 * Overriding the parent class's method to validate the enumerated attribute values
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
	{
		StorageContainer container = (StorageContainer) obj;
		Map similarContainerMap = container.getSimilarContainerMap();
		String containerPrefixKey = "simCont:";
		String parentContainerId = "_parentContainerId";
		List positionsToBeAllocatedList = new ArrayList();
		List usedPositionsList = new ArrayList();

		for (int i = 1; i <= container.getNoOfContainers().intValue(); i++)
		{
			StorageContainerUtil.prepareContainerMap(dao, similarContainerMap, containerPrefixKey,
					positionsToBeAllocatedList, usedPositionsList, i,parentContainerId);
		}		
		for (int i = 0; i < positionsToBeAllocatedList.size(); i++)
		{
			StorageContainerUtil.allocatePositionToSingleContainerOrSpecimen(positionsToBeAllocatedList.get(i), similarContainerMap,
				usedPositionsList,containerPrefixKey,parentContainerId);
		} 		
		if (container.getNoOfContainers().intValue() > 1 && similarContainerMap.size() > 0)
		{
			for (int i = 1; i <= container.getNoOfContainers().intValue(); i++)
			{
				int checkedButtonStatus = Integer.parseInt((String) similarContainerMap.get("checkedButton"));
				String siteId = (String) similarContainerMap.get("simCont:" + i + "_siteId");
				if (checkedButtonStatus == 2 || siteId == null)
				{
					String parentContId = (String) similarContainerMap.get("simCont:" + i + "_parentContainerId");
					String positionDimensionOne = (String) similarContainerMap.get("simCont:" + i + "_positionDimensionOne");
					String positionDimensionTwo = (String) similarContainerMap.get("simCont:" + i + "_positionDimensionTwo");
					if (parentContId.equals("-1") || positionDimensionOne.equals("-1") || positionDimensionTwo.equals("-1"))
					{
						throw new DAOException(ApplicationProperties.getValue("errors.item.required", ApplicationProperties
								.getValue("similarcontainers.location")));

					}
				}
				else
				{
					if (siteId.equals("-1"))
					{
						throw new DAOException(ApplicationProperties.getValue("errors.item.required", ApplicationProperties
								.getValue("storageContainer.site")));
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.AbstractDAO, java.lang.Object)
	 */
	public String getObjectId(AbstractDAO dao, Object domainObject) 
	{
		StringBuffer sb = new StringBuffer();
		sb.append(Site.class.getName());
		
		if (domainObject instanceof StorageContainer)
		{
			StorageContainer storageContainer = (StorageContainer) domainObject;
			Map similarContainerMap = storageContainer.getSimilarContainerMap();
			Object keys[] = similarContainerMap.keySet().toArray();
			Set<Long> scIds = new HashSet<Long>();
			Set<Long> siteIds = new HashSet<Long>();
			
			for(Object key : keys)
			{
				if(key.toString().contains("parentContainerId"))
				{
					scIds.add(Long.valueOf(similarContainerMap.get(key).toString()));
				}
				if(key.toString().contains("siteId"))
				{
					siteIds.add(Long.valueOf(similarContainerMap.get(key).toString()));
				}
			}
			
			for(Long scId : scIds)
			{
				Site site = null;
	            Object object = null;
				try 
				{
					object = dao.retrieve(StorageContainer.class.getName(),scId);
				} 
				catch (DAOException e) 
				{
					e.printStackTrace();
				}
	            if (object != null)
	            {
	                StorageContainer parentContainer = (StorageContainer) object;
	                site = parentContainer.getSite();
	            }
				if (site != null)
				{
					if(!siteIds.contains(site.getId()))
					{
						sb.append("_"+site.getId().toString());
					}
					siteIds.add(site.getId());
				}
			}
			
			for(Long siteId : siteIds)
			{
				sb.append("_"+siteId.toString());
			}
		}
		return sb.toString(); 
	}
	
	/**
	 * To get PrivilegeName for authorization check from 'PermissionMapDetails.xml'
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getPrivilegeName(java.lang.Object)
	 */
	protected String getPrivilegeKey(Object domainObject)
	{
		return new StorageContainerBizLogic().getPrivilegeKey(domainObject);
	}

}
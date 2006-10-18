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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.tree.TreeDataInterface;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;

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
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
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

				List list = dao.retrieve(StorageContainer.class.getName(), Constants.SYSTEM_IDENTIFIER, parentId);
				if (list.size() != 0)
				{

					parentContainer = (StorageContainer) list.get(0);

					cont.setSite(parentContainer.getSite());

				}

				//		StorageContainer parentContainer = new StorageContainer();
				//	parentContainer.setId(new Long(parentId));
				cont.setPositionDimensionOne(new Integer(posOne));
				cont.setPositionDimensionTwo(new Integer(posTwo));

				cont.setParent(parentContainer); // <<----

				//chk for positions 
				// check for availability of position
				boolean canUse = isContainerAvailableForPositions(dao, cont);

				if (!canUse)
				{
					throw new DAOException(ApplicationProperties.getValue("errors.storageContainer.inUse"));
				}

				// Have to set Site object for parentContainer
				loadSite(dao, parentContainer); // 17-07-2006
				loadSiteFromContainerId(dao, parentContainer);
				cont.setPositionDimensionOne(new Integer(posOne));
				cont.setPositionDimensionTwo(new Integer(posTwo));
				cont.setSite(parentContainer.getSite()); // 16-07-2006 chetan
				Logger.out.debug("^^>> " + parentContainer.getSite());
				simMap.put(parentContNameKey, parentContainer.getName());
			}
			//StorageContainer cont = new StorageContainer();
			cont.setName(contName); // <<----
			cont.setBarcode(barcode); // <<----     		

			Logger.out.debug(cont.getParent() + " <<<<---- parentContainer");
			Logger.out.debug("cont.getCollectionProtocol().size() " + cont.getCollectionProtocolCollection().size());
			cont.setActivityStatus("Active");
			dao.insert(cont.getCapacity(), sessionDataBean, true, true);
			dao.insert(cont, sessionDataBean, true, true);

			contList.add(cont);
			container.setId(cont.getId());
			simMap.put(IdKey, cont.getId().toString());
			//simMap.put(parentContNameKey,cont.getParent().getName());
		}
		Iterator itr = contList.iterator();
		while (itr.hasNext())
		{
			StorageContainer cont = (StorageContainer) itr.next();
			//        		Inserting authorization data
			Set protectionObjects = new HashSet();
			protectionObjects.add(cont);
			try
			{
				SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null, protectionObjects, getDynamicGroups(cont));
			}
			catch (SMException e)
			{
				throw handleSMException(e);
			}
		}
		//cache handling

	}

	public void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
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
				Logger.out.info("contName:" + contName);

				cont.setId(new Long(Id));
				cont.setName(contName);
				if (checkButton == 2)
				{
					String parentId = (String) simMap.get(simContPrefix + "parentContainerId");
					String posOne = (String) simMap.get(simContPrefix + "positionDimensionOne");
					String posTwo = (String) simMap.get(simContPrefix + "positionDimensionTwo");
					String parentContName = (String) simMap.get(simContPrefix + "parentContName");
					StorageContainer parentContainer = new StorageContainer();
					parentContainer.setId(new Long(parentId));
					parentContainer.setName(parentContName);
					cont.setPositionDimensionOne(new Integer(posOne));
					cont.setPositionDimensionTwo(new Integer(posTwo));

					cont.setParent(parentContainer); // <<----

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

		if (container.getNoOfContainers().intValue() > 1 && container.getSimilarContainerMap().size() > 0)
		{
			for (int i = 1; i <= container.getNoOfContainers().intValue(); i++)
			{
				int checkedButtonStatus = Integer.parseInt((String) container.getSimilarContainerMap().get("checkedButton"));
				String siteId = (String) container.getSimilarContainerMap().get("simCont:" + i + "_siteId");
				if (checkedButtonStatus == 2 || siteId == null)
				{
					String parentContId = (String) container.getSimilarContainerMap().get("simCont:" + i + "_parentContainerId");
					String positionDimensionOne = (String) container.getSimilarContainerMap().get("simCont:" + i + "_positionDimensionOne");
					String positionDimensionTwo = (String) container.getSimilarContainerMap().get("simCont:" + i + "_positionDimensionTwo");
					Logger.out.debug(i + " parentContId " + parentContId + " positionDimensionTwo " + positionDimensionOne);
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

}
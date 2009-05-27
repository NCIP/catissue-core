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
import edu.wustl.common.audit.AuditManager;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.AuditException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.tree.TreeDataInterface;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;

/**
 * StorageContainerHDAO is used to add Storage Container information into the
 * database using Hibernate.
 * @author Vaishali_Khandelwal
 */
public class SimilarContainerBizLogic extends StorageContainerBizLogic implements TreeDataInterface
{

	private transient Logger logger = Logger.getCommonLogger(SimilarContainerBizLogic.class);
	/**
	 * Saves the storageContainer object in the database.
	 * @param obj The storageType object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws BizLogicException
	{
		StorageContainer container = (StorageContainer) obj;
		container.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());

		List contList = new ArrayList();
		int noOfContainers = container.getNoOfContainers().intValue();
		Map simMap = container.getSimilarContainerMap();
		// --- common values for all similar containers ---
		loadStorageType(dao, container);
		logger.debug(simMap);
		int checkButton = Integer.parseInt((String) simMap.get("checkedButton"));
		//int checkButton = 1;

		try
		{
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

					Object object = dao.retrieveById(StorageContainer.class.getName(), new Long(parentId));
					if (object != null)
					{

						parentContainer = (StorageContainer) object;

						cont.setSite(parentContainer.getSite());

					}

					IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
					StorageContainerBizLogic storageContainerBizLogic = (StorageContainerBizLogic) factory.getBizLogic(
							Constants.STORAGE_CONTAINER_FORM_ID);
					//check for all validations on the storage container.
					storageContainerBizLogic.checkContainer(dao, parentContainer.getId().toString(), posOne, posTwo, sessionDataBean, false,null);

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
					logger.debug("^^>> " + parentContainer.getSite());
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
					storagecontLblGenerator = LabelGeneratorFactory.getInstance(Constants.STORAGECONTAINER_LABEL_GENERATOR_PROPERTY_NAME);
					storagecontLblGenerator.setLabel(cont);

				}

				simMap.put(simContPrefix + "name",cont.getName());

				logger.debug("cont.getCollectionProtocol().size() " + cont.getCollectionProtocolCollection().size());
				cont.setActivityStatus("Active");
				AuditManager auditManager = getAuditManager(sessionDataBean);
				dao.insert(cont.getCapacity());
				auditManager.insertAudit(dao,cont.getCapacity());

				dao.insert(cont);
				auditManager.insertAudit(dao,cont);

				contList.add(cont);
				container.setId(cont.getId());
				simMap.put(IdKey, cont.getId().toString());
				//simMap.put(parentContNameKey,cont.getParent().getName());
			}
		}
		catch(DAOException daoExp)
		{
			logger.debug(daoExp.getMessage(), daoExp);
			throw getBizLogicException(daoExp, daoExp.getErrorKeyName(),daoExp.getMsgValues());
		} catch (NameGeneratorException e) 
		{
			logger.debug(e.getMessage(), e);
			throw getBizLogicException(e, "utility.error", "");
		} catch (AuditException e) {
			logger.debug(e.getMessage(), e);
			throw getBizLogicException(e, e.getErrorKeyName(),e.getMsgValues());
		}
	}

	synchronized public void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws BizLogicException
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
			logger.debug(e.getMessage(), e);
		}
	}

	/**
	 * Overriding the parent class's method to validate the enumerated attribute values
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		try
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
						
						throw getBizLogicException(null, "errors.item.required", ApplicationProperties
								.getValue("similarcontainers.location"));
						
					}
				}
				else
				{
					if (siteId.equals("-1"))
					{
						throw getBizLogicException(null, "errors.item.required", ApplicationProperties
								.getValue("storageContainer.site"));
					}
				}
			}
		}
		}
		catch(ApplicationException exp)
		{
			logger.debug(exp.getMessage(), exp);
			throw getBizLogicException(exp, exp.getErrorKeyName(),exp.getMsgValues());
		}
		return true;
	}
	
	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.DAO, java.lang.Object)
	 */
	public String getObjectId(DAO dao, Object domainObject) 
	{
		StringBuffer sb = new StringBuffer();
		sb.append(Site.class.getName());
		try 
		{
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

					object = dao.retrieveById(StorageContainer.class.getName(),scId);

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
		} 
		catch(DAOException daoExp)
		{
			logger.debug(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			//throw getBizLogicException(daoExp, "dao.error", "");
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
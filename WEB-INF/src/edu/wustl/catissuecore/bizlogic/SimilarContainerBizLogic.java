/**
 * <p>
 * Title: StorageContainerHDAO Class>
 * <p>
 * Description: StorageContainerHDAO is used to add Storage Container
 * information into the database using Hibernate.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00 Created on Jul 23, 2005
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
public class SimilarContainerBizLogic extends StorageContainerBizLogic
{

	private transient final Logger logger = Logger.getCommonLogger(SimilarContainerBizLogic.class);

	/**
	 * Saves the storageContainer object in the database.
	 * @param dao : dao
	 * @param obj
	 *            The storageType object to be saved.
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @throws BizLogicException : BizLogicException
	 */
	@Override
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		final StorageContainer container = (StorageContainer) obj;
		container.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());

		final List contList = new ArrayList();
		final int noOfContainers = container.getNoOfContainers().intValue();
		final Map simMap = container.getSimilarContainerMap();
		// --- common values for all similar containers ---
		new StorageTypeBizLogic().loadStorageType(dao, container);
		this.logger.debug(simMap);
		final int checkButton = Integer.parseInt((String) simMap.get("checkedButton"));
		// int checkButton = 1;

		try
		{
			for (int i = 1; i <= noOfContainers; i++)
			{
				final String simContPrefix = "simCont:" + i + "_";
				final String IdKey = simContPrefix + "Id";
				final String parentContNameKey = simContPrefix + "parentContName";
				final String contName = (String) simMap.get(simContPrefix + "name");
				String barcode = (String) simMap.get(simContPrefix + "barcode");

				if (barcode != null && barcode.equals("")) // this is done
				// because barcode
				// is empty string
				// set by struts
				{ // but barcode in DB is unique but can be null.
					barcode = null;
				}
				final StorageContainer cont = new StorageContainer(container);
				if (checkButton == 1) // site
				{
					final String siteId = (String) simMap.get(simContPrefix + "siteId");
					final String siteName = (String) simMap.get(simContPrefix + "siteName");

					final Site site = new Site();

					/**
					 * Start: Change for API Search --- Jitendra 06/10/2006 In
					 * Case of Api Search, previoulsy it was failing since there
					 * was default class level initialization on domain object.
					 * For example in User object, it was initialized as
					 * protected String lastName=""; So we removed default class
					 * level initialization on domain object and are
					 * initializing in method setAllValues() of domain object.
					 * But in case of Api Search, default values will not get
					 * set since setAllValues() method of domainObject will not
					 * get called. To avoid null pointer exception, we are
					 * setting the default values same as we were setting in
					 * setAllValues() method of domainObject.
					 */
					ApiSearchUtil.setSiteDefault(site);
					site.setId(new Long(siteId));
					site.setName(siteName);
					cont.setSite(site);
					new SiteBizLogic().loadSite(dao, cont);

				}
				else
				// parentContainer
				{
					StorageContainer parentContainer = null;
					final String parentId = (String) simMap
							.get(simContPrefix + "parentContainerId");
					final String posOne = (String) simMap.get(simContPrefix
							+ "positionDimensionOne");
					final String posTwo = (String) simMap.get(simContPrefix
							+ "positionDimensionTwo");

					final Object object = dao.retrieveById(StorageContainer.class.getName(),
							new Long(parentId));
					if (object != null)
					{

						parentContainer = (StorageContainer) object;
						cont.setSite(parentContainer.getSite());
					}

					final IFactory factory = AbstractFactoryConfig.getInstance()
							.getBizLogicFactory();
					final StorageContainerBizLogic storageContainerBizLogic = (StorageContainerBizLogic) factory
							.getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
					// check for all validations on the storage container.
					storageContainerBizLogic.checkContainer(dao,
							parentContainer.getId().toString(), posOne, posTwo, sessionDataBean,
							false, null);
					ContainerPosition cntPos = cont.getLocatedAtPosition();
					if (cntPos == null)
					{
						cntPos = new ContainerPosition();
					}

					cntPos.setPositionDimensionOne(new Integer(posOne));
					cntPos.setPositionDimensionTwo(new Integer(posTwo));
					cntPos.setOccupiedContainer(cont);
					cntPos.setParentContainer(parentContainer);
					cont.setLocatedAtPosition(cntPos);
					// Have to set Site object for parentContainer
					SiteBizLogic siteBiz = new SiteBizLogic();
					siteBiz.loadSite(dao, parentContainer);
					siteBiz.loadSiteFromContainerId(dao, parentContainer);

					cntPos.setPositionDimensionOne(new Integer(posOne));
					cntPos.setPositionDimensionTwo(new Integer(posTwo));
					cntPos.setOccupiedContainer(cont);
					cont.setLocatedAtPosition(cntPos);
					cont.setSite(parentContainer.getSite());
					this.logger.debug("^^>> " + parentContainer.getSite());
					simMap.put(parentContNameKey, parentContainer.getName());
				}
				// StorageContainer cont = new StorageContainer();
				cont.setName(contName); // <<----
				cont.setBarcode(barcode); // <<----
				// by falguni
				// Storage container label generator

				// Call Storage container label generator if its specified to
				// use automatic label generator
				if (edu.wustl.catissuecore.util.global.Variables.isStorageContainerLabelGeneratorAvl)
				{
					LabelGenerator storagecontLblGenerator;
					storagecontLblGenerator = LabelGeneratorFactory
							.getInstance(Constants.STORAGECONTAINER_LABEL_GENERATOR_PROPERTY_NAME);
					storagecontLblGenerator.setLabel(cont);

				}

				simMap.put(simContPrefix + "name", cont.getName());

				this.logger.debug("cont.getCollectionProtocol().size() "
						+ cont.getCollectionProtocolCollection().size());
				cont.setActivityStatus("Active");
				final AuditManager auditManager = this.getAuditManager(sessionDataBean);
				dao.insert(cont.getCapacity());
				auditManager.insertAudit(dao, cont.getCapacity());

				dao.insert(cont);
				auditManager.insertAudit(dao, cont);

				contList.add(cont);
				container.setId(cont.getId());
				simMap.put(IdKey, cont.getId().toString());
				// simMap.put(parentContNameKey,cont.getParent().getName());
			}
		}
		catch (final DAOException daoExp)
		{
			this.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (final NameGeneratorException e)
		{
			this.logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw this.getBizLogicException(e, "utility.error", "");
		}
		catch (final AuditException e)
		{
			this.logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
	}

	/**
	 * @param obj : obj
	 * @param dao : dao
	 * @param sessionDataBean : sessionDataBean
	 * @throws BizLogicException : BizLogicException
	 */
	@Override
	synchronized public void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		final StorageContainer container = (StorageContainer) obj;
		try
		{
			final int noOfContainers = container.getNoOfContainers().intValue();
			final Map simMap = container.getSimilarContainerMap();
			final int checkButton = Integer.parseInt((String) simMap.get("checkedButton"));
			for (int i = 1; i <= noOfContainers; i++)
			{

				final String simContPrefix = "simCont:" + i + "_";
				final String contName = (String) simMap.get(simContPrefix + "name");
				final String Id = (String) simMap.get(simContPrefix + "Id");
				final StorageContainer cont = new StorageContainer(container);
				// Logger.out.info("contName:" + contName);

				cont.setId(new Long(Id));
				cont.setName(contName); // by falguni ...Container name is
				// generated via label generator.
				if (checkButton == 2)
				{
					final String parentId = (String) simMap
							.get(simContPrefix + "parentContainerId");
					final String posOne = (String) simMap.get(simContPrefix
							+ "positionDimensionOne");
					final String posTwo = (String) simMap.get(simContPrefix
							+ "positionDimensionTwo");
					final String parentContName = (String) simMap.get(simContPrefix
							+ "parentContName");
					final StorageContainer parentContainer = new StorageContainer();
					parentContainer.setId(new Long(parentId));
					parentContainer.setName(parentContName);

					final ContainerPosition cntPos = cont.getLocatedAtPosition();

					cntPos.setPositionDimensionOne(new Integer(posOne));
					cntPos.setPositionDimensionTwo(new Integer(posTwo));
					cntPos.setOccupiedContainer(cont);
					cntPos.setParentContainer(parentContainer);

					// cont.setParent(parentContainer); // <<----

				}
			}

		}
		catch (final Exception e)
		{
			this.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		super.postInsert(obj, dao, sessionDataBean);
	}

	/**
	 * Overriding the parent class's method to validate the enumerated attribute
	 * values
	 * @param obj : obj
	 * @param dao : dao
	 * @param operation : operation
	 * @throws BizLogicException : BizLogicException
	 * @return boolean
	 */
	@Override
	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		try
		{
			final StorageContainer container = (StorageContainer) obj;
			final Map similarContainerMap = container.getSimilarContainerMap();
			final String containerPrefixKey = "simCont:";
			final String parentContainerId = "_parentContainerId";
			final List positionsToBeAllocatedList = new ArrayList();
			final List usedPositionsList = new ArrayList();

			for (int i = 1; i <= container.getNoOfContainers().intValue(); i++)
			{
				StorageContainerUtil.prepareContainerMap(dao, similarContainerMap,
						containerPrefixKey, positionsToBeAllocatedList, usedPositionsList, i,
						parentContainerId);
			}
			/*for (int i = 0; i < positionsToBeAllocatedList.size(); i++)
			{
				StorageContainerUtil.allocatePositionToSingleContainerOrSpecimen(
						positionsToBeAllocatedList.get(i), similarContainerMap, usedPositionsList,
						containerPrefixKey, parentContainerId);
			}*/
			if (container.getNoOfContainers().intValue() > 1 && similarContainerMap.size() > 0)
			{
				for (int i = 1; i <= container.getNoOfContainers().intValue(); i++)
				{
					final int checkedButtonStatus = Integer.parseInt((String) similarContainerMap
							.get("checkedButton"));
					final String siteId = (String) similarContainerMap.get("simCont:" + i
							+ "_siteId");
					if (checkedButtonStatus == 2 || siteId == null)
					{
						final String parentContId = (String) similarContainerMap.get("simCont:" + i
								+ "_parentContainerId");
						final String positionDimensionOne = (String) similarContainerMap
								.get("simCont:" + i + "_positionDimensionOne");
						final String positionDimensionTwo = (String) similarContainerMap
								.get("simCont:" + i + "_positionDimensionTwo");
						if (parentContId.equals("-1") || positionDimensionOne.equals("-1")
								|| positionDimensionTwo.equals("-1"))
						{

							throw this.getBizLogicException(null, "errors.item.required",
									ApplicationProperties.getValue("similarcontainers.location"));

						}
					}
					else
					{
						if (siteId.equals("-1"))
						{
							throw this.getBizLogicException(null, "errors.item.required",
									ApplicationProperties.getValue("storageContainer.site"));
						}
					}
				}
			}
		}
		catch (final ApplicationException exp)
		{
			this.logger.error(exp.getMessage(), exp);
			exp.printStackTrace();
			throw this.getBizLogicException(exp, exp.getErrorKeyName(), exp.getMsgValues());
		}
		return true;
	}

	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check
	 * (non-Javadoc)
	 * @param dao : dao
	 * @param domainObject : domainObject
	 * @return String
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.DAO,
	 *      java.lang.Object)
	 */
	@Override
	public String getObjectId(DAO dao, Object domainObject)
	{
		final StringBuffer sb = new StringBuffer();
		sb.append(Site.class.getName());
		try
		{
			if (domainObject instanceof StorageContainer)
			{
				final StorageContainer storageContainer = (StorageContainer) domainObject;
				final Map similarContainerMap = storageContainer.getSimilarContainerMap();
				final Object keys[] = similarContainerMap.keySet().toArray();
				final Set<Long> scIds = new HashSet<Long>();
				final Set<Long> siteIds = new HashSet<Long>();

				for (final Object key : keys)
				{
					if (key.toString().contains("parentContainerId"))
					{
						scIds.add(Long.valueOf(similarContainerMap.get(key).toString()));
					}
					if (key.toString().contains("siteId"))
					{
						siteIds.add(Long.valueOf(similarContainerMap.get(key).toString()));
					}
				}

				for (final Long scId : scIds)
				{
					Site site = null;
					Object object = null;

					object = dao.retrieveById(StorageContainer.class.getName(), scId);

					if (object != null)
					{
						final StorageContainer parentContainer = (StorageContainer) object;
						site = parentContainer.getSite();
					}
					if (site != null)
					{
						if (!siteIds.contains(site.getId()))
						{
							sb.append("_" + site.getId().toString());
						}
						siteIds.add(site.getId());
					}
				}

				for (final Long siteId : siteIds)
				{
					sb.append("_" + siteId.toString());
				}
			}
		}
		catch (final DAOException daoExp)
		{
			this.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			// throw getBizLogicException(daoExp, "dao.error", "");
		}
		return sb.toString();
	}

	/**
	 * To get PrivilegeName for authorization check from
	 * 'PermissionMapDetails.xml' (non-Javadoc)
	 * @param domainObject : domainObject
	 * @return String
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getPrivilegeName(java.lang.Object)
	 */
	@Override
	protected String getPrivilegeKey(Object domainObject)
	{
		return new StorageContainerBizLogic().getPrivilegeKey(domainObject);
	}

}
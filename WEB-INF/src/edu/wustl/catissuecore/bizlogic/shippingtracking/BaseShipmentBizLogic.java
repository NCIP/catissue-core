/**
 * <p>Title: BaseShipmentBizLogic Class>
 * <p>Description:	Manipulate shipment information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author nilesh_ghone
 * @version 1.00
 * Created on July 16, 2008
 */

package edu.wustl.catissuecore.bizlogic.shippingtracking;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.ehcache.CacheException;
import edu.wustl.catissuecore.bizlogic.CatissueDefaultBizLogic;
import edu.wustl.catissuecore.bizlogic.SpecimenEventParametersBizLogic;
import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.ContainerPosition;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.shippingtracking.BaseShipment;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.shippingtracking.Constants;
import edu.wustl.catissuecore.util.shippingtracking.MailUtility;
import edu.wustl.common.audit.AuditManager;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.AuditException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;

/**
 * Manipulate Shipment information into the database using Hibernate.
 */
public abstract class BaseShipmentBizLogic extends CatissueDefaultBizLogic
{
	/**
	 * common logger for BaseShipmentBizLogic class.
	 */
	Logger logger = Logger.getCommonLogger(BaseShipmentBizLogic.class);
	/**
	 * Saves the baseShipment object in the database.
	 * @param obj the user object to be saved.
	 * @param sessionDataBean the current session bean.
	 * @param dao the object of DAO class.
	 * @throws BizLogicException if some bizlogic operation fails.
	 */
	@Override
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws BizLogicException
	{
		try
		{
			AuditManager auditManager = getAuditManager(sessionDataBean);
			if (!(obj instanceof BaseShipment))
			{
				logger.debug("Invalid object passed");
				throw new BizLogicException(ErrorKey.getErrorKey("errors.invalid.object.passed"),null,"invalid object found in BaseShipmentBizLogic");
			}
			BaseShipment baseShipment = (BaseShipment) obj;
			Long userId = sessionDataBean.getUserId();
			setShipmentSystemProperties(baseShipment);
			setShipmentContactPersons(dao, baseShipment, userId);
			setShipmentSites(dao, baseShipment);
			List<SpecimenPosition> specimenPositionList = new ArrayList<SpecimenPosition>();
			List<ContainerPosition> containerPositionList = new ArrayList<ContainerPosition>();
    		StorageContainer container = saveOrUpdateContainer(baseShipment
					.getContainerCollection(), dao, sessionDataBean,
					edu.wustl.common.util.global.Constants.ADD,
					specimenPositionList);
			Collection<StorageContainer> containerCollection = updateContainerDetails(
					baseShipment.getContainerCollection(), dao,
					sessionDataBean, true, containerPositionList);
			if (container != null)
			{
				containerCollection.add(container);
			}
			baseShipment.getContainerCollection().clear();
			baseShipment.getContainerCollection().addAll(containerCollection);
			dao.insert(baseShipment);
			auditManager.insertAudit(dao,baseShipment);
			boolean mailStatus = sendNotification(baseShipment, sessionDataBean);
			if (!mailStatus)
			{
				logger.debug("failed to send email..");
			}
    		Map containerMap = StorageContainerUtil.getContainerMapFromCache();
			if (specimenPositionList != null && !specimenPositionList.isEmpty())
			{
				for (SpecimenPosition specimenPosition : specimenPositionList)
				{
					StorageContainerUtil.insertSinglePositionInContainerMap(
							specimenPosition.getStorageContainer(),
							containerMap, specimenPosition
									.getPositionDimensionOne().intValue(),
							specimenPosition.getPositionDimensionTwo()
									.intValue());
				}
			}
			if (containerPositionList != null
					&& !containerPositionList.isEmpty())
			{
				for (ContainerPosition containerPosition : containerPositionList)
				{
					StorageContainerUtil.insertSinglePositionInContainerMap(
							containerPosition.getParentContainer(),
							containerMap, containerPosition
									.getPositionDimensionOne().intValue(),
							containerPosition.getPositionDimensionTwo()
									.intValue());
				}
			}
		}
		catch (DAOException daoException)
		{
			logger.debug(daoException.getMessage(), daoException);
			throw new BizLogicException(daoException.getErrorKey(),daoException,daoException.getMsgValues());   //DAOException(bizLogicException.getMessage());
		} catch (AuditException e) {
			logger.debug(e.getMessage(), e);
			throw getBizLogicException(e, e.getErrorKeyName(),e.getMsgValues());
		}
	}
	/**
	 *  this method updates the container details.
	 * @param containerCollection collection of StorageContainer objects.
	 * @param dao the object of DAO class.
	 * @param sessionDataBean containing the session details.
	 * @param updateSite condition to check.
	 * @param containerPositionList list of container positions.
	 * @return containerObjectsCollection collection of storage containers.
	 * @throws BizLogicException if some bizlogic operation fails.
	 * @throws DAOException if some database operation fails.
	 */
	protected Collection<StorageContainer> updateContainerDetails(
			Collection<StorageContainer> containerCollection, DAO dao,
			SessionDataBean sessionDataBean, boolean updateSite,
			List<ContainerPosition> containerPositionList)
			throws BizLogicException, DAOException
	{
		// Get site to be set for containers part of the shipment
		Site site = null;
		if (updateSite)
		{
			site = (Site) (dao.retrieve(Site.class.getName(), "name",
					Constants.IN_TRANSIT_SITE_NAME)).get(0);
		}

		// Collection to return which will contain the container objects
		// retirved from the DB
		Collection<StorageContainer> containerObjectsCollection = new HashSet<StorageContainer>();

		// StorageContainerBizLogic containerBizLogic=new
		// StorageContainerBizLogic();
		Iterator<StorageContainer> containerIterator = containerCollection
				.iterator();
		while (containerIterator.hasNext())
		{
			StorageContainer container = containerIterator.next();

			// Update containers which have been included in the shipment
			// Do not update container that was created for keeping specimens
			// included in the shipment
			if (container.getStorageType() != null &&
					container.getStorageType().getName()
					.equals(Constants.SHIPMENT_CONTAINER_TYPE_NAME))
			{
			}
			else
			{
				if (container.getName() != null)
				{
					container = getContainerByNameOrBarcode
						(container.getName(), dao,Constants.CONTAINER_PROPERTY_NAME);
				}
				else if (container.getBarcode() != null)
				{
					container = getContainerByNameOrBarcode
						(container.getBarcode(),dao,Constants.CONTAINER_PROPERTY_BARCODE);
				}
				if (container != null)
				{
					if (updateSite)
					{
						container.setSite(site);
					}
					deleteLocatedAtPosition(container,
							sessionDataBean,dao,
							containerPositionList);
					containerObjectsCollection.add(container);
				}
			}
		}
		return containerObjectsCollection;
	}
	/**
	 * gets the container on the basis of either label or barcode.
	 * @param nameOrBarcode containing the fetch condition.
	 * @param dao the object of DAO class.
	 * @param property containing the property name.
	 * @return container the object of StorageContainer class.
	 * @throws DAOException if some database operation fails.
	 */
	protected StorageContainer getContainerByNameOrBarcode(	String nameOrBarcode,DAO dao,String property)
				throws DAOException
	{
		StorageContainer container = null;
		if (nameOrBarcode != null)
		{
			List<StorageContainer> containerListLabel = getContainerByProperty
								(dao,property,nameOrBarcode);
			if (containerListLabel != null && containerListLabel.size() > 0)
			{
				container = containerListLabel.get(0);
			}
		}
		return container;
	}
	/**
	 * this method calls the retreive() method to fetch the container.
	 * @param dao the object of DAO class.
	 * @param propertyName property of the container to be fetched.
	 * @param propertyValue value of the property.
	 * @return list of storage containers.
	 * @throws DAOException if some database operation fails.
	 */
	protected List<StorageContainer> getContainerByProperty(DAO dao,
			String propertyName, String propertyValue) throws DAOException
	{
		return dao.retrieve(StorageContainer.class.getName(), propertyName,propertyValue);
	}
	/**
	 * this method updates the specimen details.
	 * @param specimenPositionCollection collection of specimen position.
	 * @param container object of StorageContainer class.
	 * @param dao object of DAO class.
	 * @param sessionDataBean containing the session details.
	 * @param operation operation to be performed.
	 * @param specimenPositionList containing the list of specimen position.
	 * @throws DAOException if asome database operation fails.
	 */
	protected void updateSpecimenDetails(Collection<SpecimenPosition> specimenPositionCollection,
			StorageContainer container, DAO dao,
			SessionDataBean sessionDataBean, String operation,
			List<SpecimenPosition> specimenPositionList) throws DAOException
	{
		Iterator<SpecimenPosition> specimenPositionIterator = specimenPositionCollection
				.iterator();
		while (specimenPositionIterator.hasNext())
		{
			SpecimenPosition specimenPosition = specimenPositionIterator.next();
			Specimen specimen = null;
			if (specimenPosition.getSpecimen() != null)
			{
				boolean deleted = false;
				if (operation.equals(edu.wustl.catissuecore.util.global.Constants.EDIT))
				{
					if (specimenPosition.getSpecimen().getSpecimenPosition() == null)
					{
						deleted = true;
					}
				}
				String choice = "";
				String labelOrBarcode = "";
				if (specimenPosition.getSpecimen().getLabel() != null &&
						!specimenPosition.getSpecimen().getLabel().trim().equals(""))
				{
					labelOrBarcode = specimenPosition.getSpecimen().getLabel();
					choice = Constants.SPECIMEN_PROPERTY_LABEL;
				}
				else if (specimenPosition.getSpecimen().getBarcode() != null &&
						!specimenPosition.getSpecimen().getBarcode().trim().equals(""))
				{
					labelOrBarcode = specimenPosition.getSpecimen().getBarcode();
					choice = Constants.SPECIMEN_PROPERTY_BARCODE;
				}

				if (labelOrBarcode != null)
				{
					specimen = getSpecimenByLabelOrBarcode(labelOrBarcode, dao,	choice);
				}

				if (deleted)
				{
					// If specimen has been deleted then delete the
					// corresponding row from SpecimenPosition table and update
					// the container cache
					Map disabledCont = null;
					try
					{
						SpecimenEventParametersBizLogic bizLogic = new SpecimenEventParametersBizLogic();
						disabledCont = bizLogic
								.getContForDisabledSpecimenFromCache();
					}
					catch (Exception e1)
					{
						logger.debug(e1.getMessage(), e1);
						e1.printStackTrace();
					}
					if (disabledCont == null)
					{
						disabledCont = new TreeMap();
					}
					// This specimen has to be deleted hence delete the
					// specimenPosition object
					SpecimenPosition prevPosition = specimen
							.getSpecimenPosition();
					specimen.setSpecimenPosition(null);
					dao.update(specimen);
					if (prevPosition != null)
					{
						dao.delete(prevPosition);
					}
					try
					{
						CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager.getInstance();
						catissueCoreCacheManager
								.addObjectToCache(
										edu.wustl.catissuecore.util.global.Constants.MAP_OF_CONTAINER_FOR_DISABLED_SPECIEN,
										(Serializable) disabledCont);
					}
					catch (CacheException e)
					{
						logger.debug(e.getMessage(), e);
						e.printStackTrace();
					}
				}
				else
				{
					SpecimenPosition position = null;
					if (specimen != null)
					{
						position = specimen.getSpecimenPosition();
						/**
						 * Sachin store this position in Map with container
						 * id,pos1, pos2 1. Nwe Psotion 2. set pos1,poso2 2, set
						 * container with id 4. set position object in a list
						 */
						// Ravi : changes : start
						if (position != null)
						{
							if (position.getStorageContainer() != null)
							{
								StorageType storageType = position
										.getStorageContainer()
										.getStorageType();
								if (storageType != null
										&& ((storageType.getName() != null)
												&& !(storageType.getName().trim().equals(Constants.SHIPMENT_CONTAINER_TYPE_NAME))))
								{
									SpecimenPosition tempPosition = new SpecimenPosition();
									tempPosition.setId(position.getId());
									tempPosition.setPositionDimensionOne(position.getPositionDimensionOne());
									tempPosition.setPositionDimensionTwo(position.getPositionDimensionTwo());
									tempPosition.setStorageContainer(position
											.getStorageContainer());
									tempPosition.setSpecimen(position
											.getSpecimen());
									specimenPositionList.add(tempPosition);
								}

							}
						}
						if (operation.equals(edu.wustl.catissuecore.util.global.Constants.ADD))
						{
							if (position == null)
							{
								position = new SpecimenPosition();
								specimen.setSpecimenPosition(position);
							}
						}
						else
						{
							if (specimenPosition.getSpecimen()
									.getSpecimenPosition() != null)
							{
								position = specimen.getSpecimenPosition();
								if (specimenPosition != null)
								{
									position.setPositionDimensionOne(specimenPosition.getPositionDimensionOne());
									position.setPositionDimensionTwo(1);
								}
							}
						}

						position.setSpecimen(specimen);
						position.setStorageContainer(container);
						position.setPositionDimensionOne(specimenPosition
								.getPositionDimensionOne());
						position.setPositionDimensionTwo(specimenPosition
								.getPositionDimensionTwo());
						dao.update(specimen);
					}
				}
			}
		}
	}
	/**
	 * this method checks whether specimen is present or not.
	 * @param specimenPositionCollection collection of specimen position.
	 * @param specimen specimen whose presence is to be checked.
	 * @return boolean containing the result.
	 */
	protected boolean isSpecimenPresent(Collection<SpecimenPosition> specimenPositionCollection
			,Specimen specimen)
	{
		boolean isPresent = false;
		if (specimenPositionCollection != null)
		{
			Iterator<SpecimenPosition> spPosIterator = specimenPositionCollection.iterator();
			while (spPosIterator.hasNext())
			{
				SpecimenPosition specimenPosition = spPosIterator.next();
				if (specimen.getId() != null && specimen.getId()
						.equals(specimenPosition.getSpecimen().getId()))
				{
					isPresent = true;
				}
			}
		}
		return isPresent;
	}
	/**
	 * retreives the specimen by label or barcode.
	 * @param labelOrBarcode to fetch the specimen.
	 * @param dao the object of DAO class.
	 * @param choice string containing the property name.
	 * @return specimen fetched.
	 * @throws DAOException if database operation fails.
	 */
	protected Specimen getSpecimenByLabelOrBarcode(String labelOrBarcode,DAO dao
			, String choice) throws DAOException
	{
		Specimen specimen = null;
		if (labelOrBarcode != null)
		{
			// Check for specimen with such label
			List<Specimen> specimenListLabel = getSpecimenByProperty(dao,choice, labelOrBarcode);
			if (specimenListLabel != null && specimenListLabel.size() > 0)
			{
				specimen = specimenListLabel.get(0);
			}
		}
		return specimen;
	}
	/**
	 * this method calls retreive() method of DAO class to fetch the specimen.
	 * @param dao the object of DAO class.
	 * @param propertyName name of the property.
	 * @param propertyValue value of the property.
	 * @return the list of the specimen.
	 * @throws DAOException if some database operation fails.
	 */
	protected List<Specimen> getSpecimenByProperty(DAO dao,	String propertyName,
			String propertyValue) throws DAOException
	{
		return dao.retrieve(Specimen.class.getName(), propertyName,propertyValue);
	}

	/**
	 * Updates the persistent object in the database.
	 * @param dao
	 * @param sessionDataBean
	 * @param dao
	 * @param sessionDataBean
	 * @param obj
	 *            The object to be updated.
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @param operation
	 * @param specimenPositionList
	 * @throws BizLogicException
	 * @throws UserNotAuthorizedException
	 * @throws DAOException
	 * @throws DAOException
	 * @throws BizLogicException
	 */
	// TODO - Implementation required.
	/*
	 * protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean
	 * sessionDataBean) throws DAOException, UserNotAuthorizedException {
	 * }
	 */

	/*
	 * This mehtod saves the container, if any has been created for the
	 * specimens present in the shipment if no specimens are present it returns
	 * null
	 */
	/**
	 * performs the saving/updating operation of container.
	 * @param containerCollection collection of storage container objects.
	 * @param dao the object of DAO class.
	 * @param sessionDataBean containing the session details.
	 * @param specimenPositionList list of specimen postition.
	 * @param operation the operation to be performed.
	 * @return object of storage container.
	 * @throws DAOException if some database operation fails.
	 * @throws BizLogicException if bizlogic operation fails.
	 */
	protected StorageContainer saveOrUpdateContainer(Collection<StorageContainer> containerCollection, DAO dao,
			SessionDataBean sessionDataBean, String operation,
			List<SpecimenPosition> specimenPositionList)
			throws DAOException, BizLogicException
	{
		AuditManager auditManager = getAuditManager(sessionDataBean);
		StorageContainer storageContainer = null;
		Site site = null;
		StorageType storageTypeInTransit = null;
		List<Site> siteList = dao.retrieve(Site.class.getName(), "name",Constants.IN_TRANSIT_SITE_NAME);
		List<StorageType> storageTypeList = dao.retrieve(StorageType.class.getName(),
				"name", Constants.SHIPMENT_CONTAINER_TYPE_NAME);
		if (siteList != null && siteList.size() == 1)
		{
			site = siteList.get(0);
		}
		else
		{
			logger.debug("shipment in transit site is empty");
			throw new BizLogicException(ErrorKey.getErrorKey("error.shipment.inTransitSite.empty"),null,"");
		}
		if(storageTypeList != null && storageTypeList.size() == 1)
		{
			storageTypeInTransit = storageTypeList.get(0);
		}
		else
		{
			logger.debug("shipment storage type is empty");
			throw new BizLogicException(ErrorKey.getErrorKey("error.shipment.storageType.empty"),null,"");
		}
		Iterator<StorageContainer> containerIterator = containerCollection.iterator();
		while (containerIterator.hasNext())
		{
			StorageContainer container = containerIterator.next();
			StorageType storageType = container.getStorageType();
			if ((storageType != null)
					&& ((storageType.getName() != null) && (!storageType
							.getName().trim().equals("") && (storageType
							.getName().trim()
							.equals(Constants.SHIPMENT_CONTAINER_TYPE_NAME)))))
			{
				storageContainer = container;
				try
				{
					container.setSite(site);
					container.setStorageType(storageTypeInTransit);
					container.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
					Collection<SpecimenPosition> spPosCollection = container
						.getSpecimenPositionCollection();
					/**
					 * Sachin: 1. Get Specimen position from Container 2. Get
					 * Container position from Container 2. Set Specimen
					 * Position Collection null 2. Set Container Position
					 * Collection null 3. Creater shipment container call
					 * storageContainerBizLogic insert (new shipment container
					 * not required in caTissue) 4. Iterate on specimen position
					 * and call transfereventBixlogic insert. 5. Iterate on
					 * continer position and call storagecontainerbizLogic
					 * update.
					 */

					if (operation.trim().equals(edu.wustl.common.util.global.Constants.ADD))
					{
						dao.insert(container.getCapacity());
						dao.insert(container);
						auditManager.insertAudit(dao,container.getCapacity());
						auditManager.insertAudit(dao,container);
					}
					else if (operation.trim()
							.equals(edu.wustl.common.util.global.Constants.EDIT))
					{
						// bug 11410 start
						StorageContainer containerInTransit = null;
						List<StorageContainer> containerInTransitList = dao.retrieve
						(StorageContainer.class.getName(),
								edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER, container.getId());
						if (containerInTransitList != null
								&& containerInTransitList.size() == 1)
						{
							containerInTransit = containerInTransitList.get(0);
						}
						if (containerInTransit == null)
						{
							dao.insert(container.getCapacity());
							dao.insert(container);
							
							auditManager.insertAudit(dao,container.getCapacity());
							auditManager.insertAudit(dao,container);
						}
						// bug 11410 end
						int newOneDimCapacity = container.getCapacity()
							.getOneDimensionCapacity();
						Capacity capacity = null;
						List<Capacity> capacityList = dao.retrieve
								(Capacity.class.getName(),
										edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER,
								container.getCapacity().getId());
						if (capacityList != null && capacityList.size() == 1)
						{
							capacity = capacityList.get(0);
							capacity.setOneDimensionCapacity(newOneDimCapacity);
						}
						if (capacity != null)
						{
							dao.update(capacity);
						}
						container.setCapacity(capacity);
					}
					// Update specimens storage location to the newly created
					// container if the shipment contains specimens
					if (container != null)
					{
						updateSpecimenDetails(spPosCollection,
								container, dao,sessionDataBean,
								operation,specimenPositionList);
					}
				}
				catch (ApplicationException exception)
				{
					logger.debug(exception.getMessage(),exception);
					throw getBizLogicException(exception, exception.getErrorKeyName(), exception.getMsgValues());
					//throw new BizLogicException(exception.getErrorKey(),exception,exception.getMsgValues()); 
				}
			}
		}
		return storageContainer;
	}

	/**
	 * Overriding the parent class's method to validate the enumerated attribute values.
	 * @param dao the object of DAO class.
	 * @param obj the base shipment object.
	 * @param operation string containing the operation to be performed.
	 * @return isValid the boolean containing the validity.
	 * @throws BizLogicException if bizlogic operation fails.
	 */
	@Override
	protected boolean validate(Object obj, DAO dao, String operation)throws BizLogicException
	{
		boolean isValid = true;
		//StringBuffer errorMsg = new StringBuffer();
		if (!(obj instanceof BaseShipment))
		{
			logger.debug("Invalid object passed");
			throw new BizLogicException(ErrorKey.getErrorKey("errors.invalid.object.passed"),null,"");	
		}
		BaseShipment baseShipment = (BaseShipment) obj;
		if (baseShipment == null)
		{
			logger.debug("domain object found null in BaseShipmentBizLogic");
			throw new BizLogicException(ErrorKey.getErrorKey("errors.domain.object.null"),null,"domain object found null in BaseShipmentBizLogic");
			//throw new DAOException(ApplicationProperties.getValue("errors.domain.object.null"));
		}
		Collection containerCollection = baseShipment.getContainerCollection();
		if (containerCollection == null || containerCollection.isEmpty())
		{
			logger.debug("Container collection is found empty");
			throw new BizLogicException(ErrorKey.getErrorKey("errors.null.empty.container"),null,"Container collection is found empty");
			//throw new DAOException(ApplicationProperties.getValue("errors.null.empty.container"));
		}
		try
		{
		List siteList = dao.retrieve(Site.class.getName(),
				edu.wustl.catissuecore.util.global.Constants.SYSTEM_NAME,"In Transit");
		Site site = null;
		if (siteList != null && siteList.size() == 1)
		{
			site = (Site) siteList.get(0);
		}
		Long inTransitSiteId = site.getId();
		if (containerCollection != null)
		{
			Iterator<StorageContainer> containerIterator = containerCollection
					.iterator();
			while (containerIterator.hasNext())
			{
				StorageContainer container = containerIterator.next();
				if (container != null)
				{
					if (container.getName() != null
							&& !container.getName().equals("")
							&& !container.getName()
							.contains(Constants.IN_TRANSIT_CONTAINER_NAME_PREFIX))
					{
						if (container.getId() == null)
						{
							String containerName = "";
							if (container.getName() != null)
							{
								containerName = container.getName();
								container = getContainerByNameOrBarcode(
										container.getName(), dao,
										Constants.CONTAINER_PROPERTY_NAME);
							}
							else
							{
								containerName = container.getBarcode();
								container = getContainerByNameOrBarcode(
										container.getBarcode(),dao,
										Constants.CONTAINER_PROPERTY_BARCODE);
							}
							if (container != null)
							{
								// bug 11410
								if (operation.equals(edu.wustl.catissuecore.util.global.Constants.ADD))
								{
									validateShipmentContainer(baseShipment,container);
								}
							}
							else
							{
								// No container with such name or barcode exists
								/*errorMsg.append("<li>No Container with name or barcode "
										+ containerName + " exists\n</li>") ;*/
								throw getBizLogicException(null, "shipment.NoContainerExists",containerName);
							}
						}
					}
					else if (container.getStorageType() != null
							&& container.getStorageType().getName().equals(
									Constants.SHIPMENT_CONTAINER_TYPE_NAME))
					{
						isValid = specimenBelongsToSite(container
								.getSpecimenPositionCollection(), dao,
								baseShipment.getSenderSite().getId(), true);
					}
				}
			}
		}
		else
		{
			//errorMsg.append("<li>Shipment is empty.</li>") ;//shipment.emptyShipment
			throw getBizLogicException(null, "shipment.emptyShipment",null);
		}
		/*if (errorMsg != null && errorMsg.length() > 0)
		{
			logger.debug(errorMsg.toString());
			throw new BizLogicException(null,null,errorMsg.toString());
			//throw new DAOException(errorMsg.toString());
		}*/
		}
		catch(DAOException e)
		{
			logger.debug(e.getMessage(), e);
			throw getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
			
		}
		return isValid;
	}
	/**
	 * this function validates the container in the shipment.
	 * @param oldShipment the object of BaseShipment class.
	 * @param updatedShipment the object of BaseShipment class,updated object.
	 * @throws DAOException if database operation fails.
	 * @throws BizLogicException 
	 */
	// bug 11410
	private void validateContainerInShipment(BaseShipment oldShipment,
			BaseShipment updatedShipment) throws DAOException, BizLogicException
	{
		boolean isBelongsToShipment = false;
		Collection containerCollection = new HashSet();
		containerCollection = updatedShipment.getContainerCollection();
		Iterator it = containerCollection.iterator();
		while (it.hasNext())
		{
			StorageContainer container = (StorageContainer) it.next();
			if (container != null && container.getId() != null)
			{
				Iterator it1 = oldShipment.getContainerCollection().iterator();
				while (it1.hasNext())
				{
					StorageContainer containerOld = (StorageContainer) it1.next();
					if (containerOld != null && containerOld.getId() != null)
					{
						if (containerOld.getId().longValue() == container
								.getId().longValue())
						{
							isBelongsToShipment = true;
							break;
						}
					}
				}
				if (!isBelongsToShipment)
				{
					validateShipmentContainer(updatedShipment, container);
				}
			}
		}
	}
	/**
	 * validates the shipment container.
	 * @param baseShipment the object of base shipment class.
	 * @param container container to be validated.
	 * @return errorMsg containing the error string if validation fails.
	 * @throws DAOException if database operation fails.
	 * @throws BizLogicException 
	 */
	private void validateShipmentContainer(BaseShipment baseShipment,
			StorageContainer container) throws DAOException, BizLogicException
	{
		boolean isValid = true;
		isValid = containerBelongsToSite(container, baseShipment
				.getSenderSite().getId());
		//bug 12568
		if (!isValid)
		{
			throw getBizLogicException(null, "shipment.containerValidation", container.getName()+":"+container.getSite().getName());
		}
		else if (container.getName().contains(
				Constants.IN_TRANSIT_CONTAINER_NAME_PREFIX))
		{
			throw getBizLogicException(null, "shipment.containerValidation", container.getName());
		}		
	}
	/**
	 * this function validates the site of the user.
	 * @param dao the object of DAO class.
	 * @param userId id of the user related to the site.
	 * @param siteId id of the site.
	 * @return isValidUser if the user is valid.
	 * @throws DAOException if database operation fails.
	 */
	// bug 11410 end
	protected boolean validateUsersSite(DAO dao, Long userId, Long siteId)throws DAOException
	{
		boolean isValidUser = false;
		if (userId != null)
		{
			List userList = dao.retrieve(User.class.getName(),
					edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER, userId);
			if (userList != null && userList.size() == 1)
			{
				User user = (User) userList.get(0);
				if (user != null && user.getSiteCollection() != null)
				{
					Collection<Site> siteCollection = user.getSiteCollection();
					if (siteCollection != null)
					{
						Iterator<Site> siteIterator = siteCollection.iterator();
						while (siteIterator.hasNext())
						{
							Site site = siteIterator.next();
							if (site.getId().equals(siteId))
							{
								isValidUser = true;
								break;
							}
						}
					}
				}
			}
		}
		return isValidUser;
	}
	/**
	 * validates the specimen belonging to the site.
	 * @param specimenPositionCollection collection containing specimen position.
	 * @param dao the object of DAO class.
	 * @param siteId containing the site id.
	 * @param belongsTo whether specimen belongs to the site
	 * @return containerBelongsTo boolean containing the result.
	 * @throws BizLogicException if bizlogic operation fails.
	 */
	protected boolean specimenBelongsToSite(
			Collection<SpecimenPosition> specimenPositionCollection, DAO dao,
			Long siteId, boolean belongsTo) throws BizLogicException
	{
		StringBuffer errorMsg = new StringBuffer();
		boolean containerBelongsTo = false;
		// It is the It Transit Container, so check for the sites of the
		// specimens
		try
		{
			if (specimenPositionCollection != null)
			{
				Iterator<SpecimenPosition> specimenPosIterator = specimenPositionCollection.iterator();
				while (specimenPosIterator.hasNext())
				{
					SpecimenPosition position = specimenPosIterator.next();
					if (position != null)
					{
						Specimen specimen = position.getSpecimen();
						if (specimen != null)
						{
//							if (specimen.getId() != null)
//							{
//						This specimen belongs to this shipment, so do not
//								// validate
//							}
							if(specimen.getId()==null)
							{
								String label = "";
								String choice = "";
								if (specimen.getLabel() != null
									&& !specimen.getLabel().equals(""))
								{
									label = specimen.getLabel();
									choice = Constants.SPECIMEN_PROPERTY_LABEL;
								}
								else if (specimen.getBarcode() != null
									&& !specimen.getBarcode().equals(""))
								{
									label = specimen.getBarcode();
									choice = Constants.SPECIMEN_PROPERTY_BARCODE;
								}
								specimen = getSpecimenByLabelOrBarcode(label, dao,
									choice);
								if (specimen != null)
								{
									if (specimen.getSpecimenPosition() != null)
									{
										if (!specimen
											.getSpecimenPosition()
											.getStorageContainer()
											.getName()
											.contains(Constants.IN_TRANSIT_CONTAINER_NAME_PREFIX))
										{
											containerBelongsTo = containerBelongsToSite(specimen.getSpecimenPosition().getStorageContainer(),siteId);
										}
										else
										{
											containerBelongsTo = !belongsTo;
										}
										if (containerBelongsTo != belongsTo)
										{
											if (belongsTo)
											{
												throw getBizLogicException(null, "shipment.specimenContainerValidation", label+":"+specimen.getSpecimenPosition().getStorageContainer().getSite().getName());
											}
											else
											{
												throw getBizLogicException(null, "shipmentRequest.specimenContainerValidation", label+":"+specimen.getSpecimenPosition().getStorageContainer().getSite().getName());
											}
										}
									}
									else
									{
										// Specimen is virtually located
										throw getBizLogicException(null, "shipment.virtual.specimen", label);
									}
								}
								else
								{
// 								No specimen with such name or barcode exists
									containerBelongsTo = false;
									throw getBizLogicException(null, "shipment.NoSpecimenExists", label);
								}
							}
						}
					}
//					else
//					{
//						// SpecimenPosition object is null...
//						// This is never expected to happen
//					}
				}
			}
//			else
//			{
//				// Newly created container but doesnot contain any specimens
//				// Shud not have been created
//			}
		}
		catch(DAOException e)
		{
			logger.debug(e.getMessage(), e);
			throw getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
			//throw new BizLogicException(ErrorKey.getErrorKey("dao.error"),e,errorMsg.toString());
		}
		/*if (errorMsg != null && errorMsg.length() > 0)
		{
			logger.debug(errorMsg.toString());
			throw new BizLogicException(ErrorKey.getErrorKey("dao.error"),null,errorMsg.toString());
			//throw new DAOException(errorMsg.toString());
		}*/
		return containerBelongsTo;
	}
	/**
	 * checks whether container belongs to a site.
	 * @param container object of StorageContainer class.
	 * @param siteId containing the site id.
	 * @return belongs the result of the operation.
	 * @throws DAOException if some database operation fails.
	 */
	protected boolean containerBelongsToSite(StorageContainer container,
			Long siteId) throws DAOException
	{
		boolean belongs = false;
		// Assuming no contianer exists without being associated with any site
		if (container.getSite() != null && container.getSite().getId() != null
				&& container.getSite().getId().equals(siteId))
		{
			// Container belongs to this site
			belongs = true;
		}
		else
		{
			belongs = false;
		}
		return belongs;
	}

	/**
	 * Send mail notification to receiver site contact person.
	 * @param baseShipment object of BaseShipment class.
	 * @param sessionDataBean containing session details.
	 * @return Mail notification successful or not.
	 */
	protected boolean sendNotification(BaseShipment baseShipment,SessionDataBean sessionDataBean)
	{
		String[] toUser = getEmailAddressesForMailNotification(baseShipment);
		String subject = getNotificationMailSubject(baseShipment);
		String body = getNotificationMailBody(baseShipment);
		return MailUtility.sendEmailToUser(toUser, null, subject, body);
	}

	/**
	 * Get email addresses to send mail notification.
	 * @param baseShipment object of BaseShipment class.
	 * @return email addresses to send mail notifications.
	 */
	protected abstract String[] getEmailAddressesForMailNotification(
			BaseShipment baseShipment);

	/**
	 * Sets the contact persons (sender/receiver) details in shipment object.
	 * @param dao the DAO Object.
	 * @param baseShipment BaseShipment object to set sender/receiver details.
	 * @param userId long variable containing userId.
	 * @throws DAOException if any database operation fails.
	 */
	protected void setShipmentContactPersons(DAO dao,BaseShipment baseShipment, Long userId)throws DAOException
	{
		User sender = null;
		List list = null;
		if (userId != null)
		{
			list = dao.retrieve(User.class.getName(),
					edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER, userId);
			if (!list.isEmpty())
			{
				sender = (User) list.get(0);
			}
		}
		baseShipment.setSenderContactPerson(sender);
	}

	/**
	 * Sets the sender/receiver site details in shipment object.
	 * @param dao object of DAO.
	 * @param baseShipment BaseShipment object to set sender/receiver site details.
	 * @throws DAOException if any database operation fails.
	 */
	protected void setShipmentSites(DAO dao, BaseShipment baseShipment)
			throws DAOException
	{
		Site senderSite = null;
		Site receiverSite = null;
		List list = null;
		if (baseShipment.getSenderSite() != null
				&& baseShipment.getSenderSite().getId() != null)
		{
			list = dao.retrieve(Site.class.getName(),
					edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER, baseShipment.getSenderSite()
							.getId());
			if (!list.isEmpty())
			{
				senderSite = (Site) list.get(0);
			}
		}
		if (baseShipment.getReceiverSite() != null
				&& baseShipment.getReceiverSite().getId() != null)
		{
			list = dao.retrieve(Site.class.getName(),
					edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER, baseShipment.getReceiverSite()
							.getId());
			if (!list.isEmpty())
			{
				receiverSite = (Site) list.get(0);
			}
		}
		baseShipment.setSenderSite(senderSite);
		baseShipment.setReceiverSite(receiverSite);
	}

	/**
	 * Set shipment properties.
	 * @param shipment object of BaseShipment class.
	 * @param operation
	 */
	protected void setShipmentSystemProperties(BaseShipment shipment)
	{
		shipment.setCreatedDate(new Date());
		shipment.setActivityStatus(Constants.ACTIVITY_STATUS_IN_TRANSIT);
	}

	/**
	 * Get the notification mail subject.
	 * @param shipment object of BaseShipment class.
	 * @return Notification mail subject.
	 */
	protected abstract String getNotificationMailSubject(BaseShipment shipment);

	/**
	 * Get the notification mail body.
	 * @param shipment object of BaseShipment class.
	 * @return Notification mail body.
	 */
	protected abstract String getNotificationMailBody(BaseShipment shipment);

	/**
	 * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param oldObj old object of base shipment.
	 * @param sessionDataBean the session in which the object is saved.
	 * @param dao object of DAO class.
	 * @throws BizLogicException if any bizlogic operation fails.
	 */
	@Override
	protected void update(DAO dao, Object obj, Object oldObj,
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		try
		{
			if (!(obj instanceof BaseShipment))
			{
				throw new BizLogicException(ErrorKey.getErrorKey("errors.invalid.object.passed"),null,"");
				//throw new DAOException(ApplicationProperties.getValue("errors.invalid.object.passed"));
			}
			BaseShipment shipment = (BaseShipment) obj;
			BaseShipment oldShipment = (BaseShipment) oldObj;
			Long userId = sessionDataBean.getUserId();
			updateShipmentSystemProperties(shipment, oldShipment);
			setShipmentContactPersons(dao, shipment, userId);
			setShipmentSites(dao, shipment);
			// Ravi : changes : start
			List<SpecimenPosition> specimenPositionList = new ArrayList<SpecimenPosition>();
			List<ContainerPosition> containerPositionList = new ArrayList<ContainerPosition>();
			StorageContainer container = saveOrUpdateContainer(shipment
					.getContainerCollection(), dao, sessionDataBean,
					edu.wustl.common.util.global.Constants.EDIT,
					specimenPositionList);
					Collection<StorageContainer> containerCollection = updateContainerDetails(
					shipment.getContainerCollection(), dao, sessionDataBean,
					true, containerPositionList);
			if (container != null)
			{
				containerCollection.add(container);
			}
			shipment.getContainerCollection().clear();
			shipment.getContainerCollection().addAll(containerCollection);
			this.validateContainerInShipment(oldShipment, shipment);// bug 11410
			dao.update(shipment);
			Map containerMap = StorageContainerUtil.getContainerMapFromCache();
			if (specimenPositionList != null && !specimenPositionList.isEmpty())
			{
				for (SpecimenPosition specimenPosition : specimenPositionList)
				{
					StorageContainerUtil.insertSinglePositionInContainerMap(
							specimenPosition.getStorageContainer(),
							containerMap, specimenPosition
									.getPositionDimensionOne().intValue(),
							specimenPosition.getPositionDimensionTwo()
									.intValue());
				}
			}
			if (containerPositionList != null
					&& !containerPositionList.isEmpty())
			{
				for (ContainerPosition containerPosition : containerPositionList)
				{
					StorageContainerUtil.insertSinglePositionInContainerMap(
							containerPosition.getParentContainer(),
							containerMap, containerPosition
									.getPositionDimensionOne().intValue(),
							containerPosition.getPositionDimensionTwo()
									.intValue());
				}
			}

			// Add mailing functionality
			boolean mailStatus = sendNotification(shipment, sessionDataBean);
			if (!mailStatus)
			{
				logger.debug("failed to send email...");
			}
		}
		catch (DAOException daoException)
		{
			logger.debug(daoException.getMessage(), daoException);
			//throw new BizLogicException(ErrorKey.getErrorKey("dao.error"),null,daoException.getMessage());
			throw getBizLogicException(daoException, daoException.getErrorKeyName(), daoException.getMsgValues());
			//throw new DAOException(bizLogicException.getMessage());
		}
	}
	/**
	 * this method update shipment system properties.
	 * @param shipment object of BaseShipment class.
	 * @param oldShipment object of BaseShipment class.
	 */
	protected void updateShipmentSystemProperties(BaseShipment shipment,BaseShipment oldShipment)
	{
		shipment.setCreatedDate(oldShipment.getCreatedDate());
		if (shipment.getActivityStatus() == null
				|| shipment.getActivityStatus().trim().equals(""))
		{
			shipment.setActivityStatus(oldShipment.getActivityStatus());
		}
	}
	/**
	 * gets the shipment details.
	 * @param objectsClassName the class names.
	 * @param selectColumnName selected columns.
	 * @param whereClause string used in forming query.
	 * @param whereColumnValue column value condition.
	 * @param orderByField for order by clause.
	 * @param startIndex the start index.
	 * @param numOfRecords integer containing number of records.
	 * @return dataList list of objects.
	 */
	// selectColumnName consists of comma separated column list.
	public List<Object[]> getShipmentDetails(String objectsClassName,
			String selectColumnName, String whereClause,
			Object[] whereColumnValue, String orderByField, int startIndex,
			int numOfRecords)
	{
		List dataList = null;
		LinkedList paramValuesList = new LinkedList();
		StringBuffer sqlBuff = new StringBuffer();
		DAO dao = null;
		try
		{
			dao = AppUtility.openDAOSession(null);
			sqlBuff.append("select " + selectColumnName + " from " + objectsClassName + " " + "shipment " + "where " + whereClause + " order by shipment." + orderByField + " desc");
			for (Object object : whereColumnValue)
			{
				paramValuesList.add(object);
			}
			dataList = ((HibernateDAO)dao).executeQuery(sqlBuff.toString(), startIndex,numOfRecords, paramValuesList);
		}
		catch (ApplicationException appException)
		{
			logger.debug(appException.getMessage(), appException);
			appException.printStackTrace();
		}
		finally
		{
			try
			{
				AppUtility.closeDAOSession(dao);
			}
			catch (ApplicationException e)
			{
				e.printStackTrace();
			}
		}
		return dataList;
	}
	/**
	 * this function gets the shipment count.
	 * @param objectsClassName class names.
	 * @param whereClause clause forming query.
	 * @param whereColumnValue column value in where clause.
	 * @param orderByField field in order by clause.
	 * @param startIndex starting index.
	 * @param numOfRecords integer containing number of records.
	 * @return recordCount count of records.
	 */
	public static int getShipmentsCount(String objectsClassName,
			String whereClause, Object[] whereColumnValue, String orderByField,
			int startIndex, int numOfRecords)
	{
		int recordCount = 0;
		List dataList = null;
		StringBuffer sqlBuff = new StringBuffer();
		LinkedList paramValuesList = new LinkedList();
		DAO dao = null;
		try
		{
			dao = AppUtility.openDAOSession(null);
			sqlBuff.append("select count(shipment) from " + objectsClassName + " " + "shipment where " + whereClause);
			for (Object object : whereColumnValue)
			{
				paramValuesList.add(object);
			}
			dataList = ((HibernateDAO)dao).executeQuery(sqlBuff.toString(), startIndex,numOfRecords, paramValuesList);
			if (dataList != null && dataList.size() == 1)
			{
				recordCount = (Integer) dataList.get(0);
			}
			else
			{
				recordCount = 0;
			}
		}
		catch(ApplicationException excp)
		{
			recordCount = 0;
			excp.printStackTrace();
		}
		finally
		{
			try
			{
				AppUtility.closeDAOSession(dao);
			}
			catch (ApplicationException e)
			{
				e.printStackTrace();
			}
		}
		return recordCount;
	}
	/**
	 * this method returns the list of ids of site.
	 * @param userId site corresponding to user id.
	 * @param isAdmin to decide whether the user is admin or not.
	 * @return list of site ids.
	 * @throws BizLogicException if some bizlogic operation fails.
	 */
	public List<Long> getPermittedSiteIdsForUser(Long userId, boolean isAdmin)
			throws BizLogicException
	{
		List<Long> siteIdsList = new ArrayList<Long>();
		Collection<Site> siteCollection = getPermittedSitesForUser(userId,
				isAdmin);
		for (Site site : siteCollection)
		{
			siteIdsList.add(site.getId());
		}
		return siteIdsList;
	}
	/**
	 * this method gives the collection of sites.
	 * @param userId the id of user.
	 * @param isAdmin decides whether the user is admin or not.
	 * @return collection of sites.
	 * @throws BizLogicException if some bizlogic operation fails.
	 */
	public Collection<Site> getPermittedSitesForUser(Long userId,
			boolean isAdmin) throws BizLogicException
	{
		Collection<Site> siteList = null;
		if (!isAdmin)
		{
			List userList = retrieve(User.class.getName(),
					edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER, userId);
			if (userList != null && userList.size() == 1)
			{
				User user = (User) userList.get(0);
				if (user != null && user.getSiteCollection() != null)
				{
					siteList = (Collection<Site>) retrieveAttribute(User.class
							.getName(), user.getId(),
							"elements(siteCollection)");
				}
			}
		}
		else
		{
			siteList = retrieve(Site.class.getName(),
					Status.ACTIVITY_STATUS.toString(), Status.ACTIVITY_STATUS_ACTIVE.toString());
		}
		return siteList;
	}
	/**
	 * executes the query.
	 * @param queryString the query to execute.
	 * @return list the list of objects.
	 * @throws SQLException if query operation fails.
	 * @throws ApplicationException wrapping the dao exception.
	 */
	public List executeSQL(String queryString) throws SQLException,ApplicationException
	{
		List list = null;
		DAO dao = AppUtility.openJDBCSession();
		list = dao.executeQuery(queryString);
		AppUtility.closeDAOSession(dao);
		return list;
	}
	/**
	 * deletes the container located at some position.
	 * @param container the storage container.
	 * @param sessionDataBean containing session details.
	 * @param dao the object of DAO class.
	 * @param containerPositionList containing the list of containers position.
	 * @throws DAOException if some database operation fails.
	 */
	protected void deleteLocatedAtPosition(StorageContainer container,
			SessionDataBean sessionDataBean, DAO dao,
			List<ContainerPosition> containerPositionList) throws DAOException
	{
		if (container.getLocatedAtPosition() != null)
		{
			Long containerPosId = container.getLocatedAtPosition().getId();
			if (containerPosId != null)
			{
				List<ContainerPosition> containerPosList = (List<ContainerPosition>) dao
						.retrieve(ContainerPosition.class.getName(),
								edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER, containerPosId);

				if (containerPositionList != null && containerPosList != null)
				{
					for (ContainerPosition containerPos : containerPosList)
					{
						if (containerPos.getParentContainer() != null)
						{
							StorageContainer parentContainer = (StorageContainer) containerPos
									.getParentContainer();
							StorageType storageType = parentContainer.getStorageType();
							if (storageType != null
									&& ((storageType.getName() != null) && !(storageType.getName().trim().equals(Constants.SHIPMENT_CONTAINER_TYPE_NAME))))
							{
								containerPositionList.add(containerPos);
							}
						}
						else
						{
							containerPositionList.add(containerPos);
						}
					}
				}
				if (containerPosList != null && containerPosList.size() == 1)
				{
					ContainerPosition containerPosition = (ContainerPosition) containerPosList
							.get(0);
					if (containerPosition != null)
					{
						container.setLocatedAtPosition(null);
						dao.delete(containerPosition);
					}
				}
			}
		}
	}

	/**
	 * Custom method for Add Shipment Case.
	 * @param dao object of DAO class.
	 * @param domainObject the domain object of base shipment.
	 * @return sb string containing site name with site id's.
	 */
	public String getObjectId(DAO dao, Object domainObject)
	{
		BaseShipment baseShipment = (BaseShipment) domainObject;
		StringBuffer stringBufferTemp = new StringBuffer();
		if (baseShipment.getSenderSite() != null)
		{
			stringBufferTemp.append(Site.class.getName()).append("_").append(
					baseShipment.getSenderSite().getId().toString());
			return stringBufferTemp.toString();
		}
		else
		{
			return null;
		}
	}
	/**
	 * To get PrivilegeName for authorization check from
	 * 'PermissionMapDetails.xml' (non-Javadoc).
	 * @param domainObject the domain object.
	 * @return ADD_EDIT_SHIPMENT
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getPrivilegeName(java.lang.Object)
	 */
	protected String getPrivilegeKey(Object domainObject)
	{
		return Constants.ADD_EDIT_SHIPMENT;
	}
}
/**
 * <p>Title: ShipmentRequestBizLogic Class>
 * <p>Description:	Add shipment information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author nilesh_ghone
 * @version 1.00
 * Created on July 16, 2008
 */

package edu.wustl.catissuecore.bizlogic.shippingtracking;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.shippingtracking.BaseShipment;
import edu.wustl.catissuecore.domain.shippingtracking.ShipmentRequest;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.shippingtracking.Constants;
import edu.wustl.catissuecore.util.shippingtracking.ShipmentMailFormatterUtility;
import edu.wustl.common.audit.AuditManager;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.AuditException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.util.DAOConstants;
import edu.wustl.security.exception.UserNotAuthorizedException;

/**
 * Manipulate ShipmentRequest information into the database using Hibernate.
 */
public class ShipmentRequestBizLogic extends BaseShipmentBizLogic
{

	/**
	 *  logger object
	 */
	public static transient Logger logger = Logger.getCommonLogger(ShipmentRequestBizLogic.class);

	/**
	 * Overriding the parent class's method to validate the enumerated attribute values.
	 * @param obj object to be validated.
	 * @param dao object of DAO class.
	 * @param operation string containing operation of request.
	 * @return true or false based on validation results.
	 * @throws BizLogicException if database operation fails.
	 */
	public boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		boolean isValid = true;
		boolean isSpecimenLocatedVirtual = false;
		//StringBuffer errorMsg=new StringBuffer();
		if (obj instanceof ShipmentRequest == false)
		{
			//throw new DAOException(ApplicationProperties.getValue("errors.invalid.object.passed"));
			logger.debug("Invalid object passed.");
			throw new BizLogicException(ErrorKey.getErrorKey("errors.invalid.object.passed"), null,
					"object is not the instance of ShipMent request class");
		}
		ShipmentRequest shipmentRequest = (ShipmentRequest) obj;
		if (shipmentRequest == null)
		{
			//throw new DAOException(ApplicationProperties.getValue("errors.domain.object.null"));
			logger.debug("Shipment object is null.");
			throw new BizLogicException(ErrorKey.getErrorKey("errors.domain.object.null"), null,
					"shipment object is null");
		}
		/* Check whether the shipment is created for the request or not.
		  		If shipment already created for the request, validations not required.
		   If the request is rejecting, no need of any validations.
		*/
		try
		{
			if (!shipmentRequest.isRequestProcessed()
					&& (shipmentRequest.getActivityStatus() != null && !shipmentRequest
							.getActivityStatus().equals(Constants.ACTIVITY_STATUS_REJECTED)))
			{
				boolean isEmptyContainerCollection = false;
				if (shipmentRequest.getContainerCollection() != null
						&& shipmentRequest.getContainerCollection().size() > 0)
				{
					Iterator<StorageContainer> containerIterator = shipmentRequest
							.getContainerCollection().iterator();
					while (containerIterator.hasNext())
					{
						StorageContainer container = containerIterator.next();
						if (container != null)
						{
							String containerName = "";
							if (container.getName() != null
									&& !container.getName().trim().equals(""))
							{
								containerName = container.getName();
								container = getContainerByNameOrBarcode(container.getName(), dao,
										Constants.CONTAINER_PROPERTY_NAME);
							}
							else if (container.getBarcode() != null
									&& !container.getBarcode().trim().equals(""))
							{
								containerName = container.getBarcode();
								container = getContainerByNameOrBarcode(container.getBarcode(),
										dao, Constants.CONTAINER_PROPERTY_BARCODE);
							}
							if (container != null)
							{
								isValid = containerBelongsToSite(container, shipmentRequest
										.getSenderSite().getId());
								if (isValid)
								{
									throw getBizLogicException(null,
											"shipment.container.RequestingSite", containerName);

								}
								else if (container.getName().contains(
										Constants.IN_TRANSIT_CONTAINER_NAME_PREFIX))
								{
									throw getBizLogicException(null,
											"shipment.containerInShipment", containerName);
								}
							}
							else
							{
								//No container with such name or barcode exists
								throw getBizLogicException(null, "shipment.NoContainerExists",
										containerName);
							}
						}
					}
				}
				else
				{
					isEmptyContainerCollection = true;
				}
				boolean isEmptySpecimenCollection = false;
				boolean isBelongsToInTransit = false;
				if (shipmentRequest.getSpecimenCollection() != null
						&& shipmentRequest.getSpecimenCollection().size() > 0)
				{
					Iterator<Specimen> specimenIterator = shipmentRequest.getSpecimenCollection()
							.iterator();
					while (specimenIterator.hasNext())
					{
						Specimen specimen = specimenIterator.next();
						if (specimen != null)
						{
							String specimenName = "";
							if (specimen.getLabel() != null
									&& !specimen.getLabel().trim().equals(""))
							{
								specimenName = specimen.getLabel();
								specimen = getSpecimenByLabelOrBarcode(specimenName, dao,
										Constants.SPECIMEN_PROPERTY_LABEL);
							}
							else if (specimen.getBarcode() != null
									&& !specimen.getBarcode().trim().equals(""))
							{
								specimenName = specimen.getBarcode();
								specimen = getSpecimenByLabelOrBarcode(specimenName, dao,
										Constants.SPECIMEN_PROPERTY_BARCODE);
							}
							if (specimen != null)
							{
								isValid = specimenBelongsToSite(specimen, shipmentRequest
										.getSenderSite().getId());
								isBelongsToInTransit = specimenBelongsToInTransitSite(specimen);
								isSpecimenLocatedVirtual = specimenBelongsToVirtualSite(specimen);
								if (isValid && !isBelongsToInTransit)
								{
									throw getBizLogicException(null,
											"shipment.specimenInRequestingSite", specimenName);
								}
								else if (isBelongsToInTransit)
								{
									throw getBizLogicException(null, "shipment.specimenInShipment",
											specimenName);
								}
								else if (isSpecimenLocatedVirtual)
								{
									throw getBizLogicException(null, "shipment.virtual.specimen",
											specimenName);
								}
							}
							else
							{
								//No container with such name or barcode exists
								throw getBizLogicException(null, "shipment.NoSpecimenExists",
										specimenName);
							}
						}
					}
				}
				else
				{
					isEmptySpecimenCollection = true;
				}
				if (isEmptyContainerCollection && isEmptySpecimenCollection)
				{
					throw getBizLogicException(null, "shipment.noSpecimenInRequest", null);
				}
				/*if(errorMsg!=null && errorMsg.length()>0)
				{
					//throw new DAOException(errorMsg.toString());
					throw new BizLogicException(ErrorKey.getErrorKey("dao.error"),null,errorMsg.toString());
				}*/
			}
		}
		catch (DAOException ex)
		{
			logger.debug(ex.getMessage(), ex);
			//throw new BizLogicException(ErrorKey.getErrorKey("dao.error"),ex,errorMsg.toString());
			throw getBizLogicException(ex, ex.getErrorKeyName(), ex.getMsgValues());
		}
		return true;
	}

	/**
	 * checks whether a specimen belongs to an InTransit site.
	 * @param specimen to be checked.
	 * @return true/false result of the operation.
	 */
	private boolean specimenBelongsToInTransitSite(Specimen specimen)
	{
		boolean belongsTo = false;
		if (specimen != null && specimen.getSpecimenPosition() != null
				&& specimen.getSpecimenPosition().getStorageContainer() != null
				&& specimen.getSpecimenPosition().getStorageContainer().getName() != null)
		{
			if (specimen.getSpecimenPosition().getStorageContainer().getName().contains(
					Constants.IN_TRANSIT_CONTAINER_NAME_PREFIX))
			{
				belongsTo = true;
			}
		}
		return belongsTo;
	}

	/**
	 * checks whether a specimen belongs to a particular site.
	 * @param specimen to be checked.
	 * @param id of the site to be checked.
	 * @return true/false the result of the operation.
	 */
	private boolean specimenBelongsToSite(Specimen specimen, Long id)
	{
		boolean belongs = false;
		if (specimen != null)
		{
			if (specimen.getSpecimenPosition() != null)
			{
				if (specimen.getSpecimenPosition().getStorageContainer() != null
						&& specimen.getSpecimenPosition().getStorageContainer().getSite() != null
						&& specimen.getSpecimenPosition().getStorageContainer().getSite().getId()
								.equals(id))
				{
					belongs = true;
				}
			}
		}
		return belongs;
	}

	/**
	 *  To check if the specimen belongs to a virtual site.
	 *  @param specimen to be checked.
	 *  @return boolean result of the check operation.
	 */
	private boolean specimenBelongsToVirtualSite(Specimen specimen)
	{
		boolean belongsToVirtual = false;
		if (specimen != null)
		{
			if (specimen.getSpecimenPosition() == null)
			{
				// Virtually located.
				belongsToVirtual = true;
			}
		}
		return belongsToVirtual;
	}

	/**
	 * Saves the shipment request object in the database.
	 * @param obj The user object to be saved.
	 * @param dao the object of DAO class.
	 * @param sessionDataBean The session details.
	 * @throws BizLogicException throws BizLogicException
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			if (!(obj instanceof ShipmentRequest))
			{
				//throw new DAOException(ApplicationProperties.getValue("errors.invalid.object.passed"));
				logger.debug("invalid object passed");
				throw new BizLogicException(ErrorKey.getErrorKey("errors.invalid.object.passed"),
						null, "Invalid object passed");
			}
			ShipmentRequest shipmentRequest = (ShipmentRequest) obj;
			// Check if Saving Draft, In saving draft case no receiver site is required,
			//because it is not actual request.
			if (!shipmentRequest.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DRAFTED))
			{
				Long receiverSiteId = null;
				if (shipmentRequest.getContainerCollection() != null
						&& shipmentRequest.getContainerCollection().size() > 0)
				{
					receiverSiteId = ((StorageContainer) shipmentRequest.getContainerCollection()
							.iterator().next()).getSite().getId();
				}
				else if (shipmentRequest.getSpecimenCollection() != null
						&& shipmentRequest.getSpecimenCollection().size() > 0)
				{
					receiverSiteId = ((Specimen) shipmentRequest.getSpecimenCollection().iterator()
							.next()).getSpecimenPosition().getStorageContainer().getSite().getId();
				}
				Site site = null;
				if (receiverSiteId != null)
				{
					site = new Site();
					site.setId(receiverSiteId);
				}
				shipmentRequest.setReceiverSite(site);
			}
			else
			{
				shipmentRequest.setReceiverSite(shipmentRequest.getSenderSite());
			}
			setShipmentSystemProperties(shipmentRequest);
			setShipmentContactPersons(dao, shipmentRequest, sessionDataBean.getUserId());
			setShipmentSites(dao, shipmentRequest);
			Collection<StorageContainer> containerCollection = updateContainerDetails(
					shipmentRequest.getContainerCollection(), dao, sessionDataBean, false, null);
			//Set the collection containing the container objects to the shipment object
			shipmentRequest.getContainerCollection().clear();
			shipmentRequest.getContainerCollection().addAll(containerCollection);
			// Do not update site of containers included in the shipment request
			Collection<Specimen> specimenCollection = updateSpecimenCollection(shipmentRequest
					.getSpecimenCollection(), dao, sessionDataBean);
			//Set the collection containing the container objects to the shipment object
			shipmentRequest.getSpecimenCollection().clear();
			shipmentRequest.getSpecimenCollection().addAll(specimenCollection);
			dao.insert(shipmentRequest);
			AuditManager auditManager = getAuditManager(sessionDataBean);
			auditManager.insertAudit(dao, shipmentRequest);
			// Check if Saving Draft, In saving draft case, mail notification not required.
			if (!shipmentRequest.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DRAFTED))
			{
				//Add mailing functionality
				boolean mailStatus = sendNotification(shipmentRequest, sessionDataBean);
				if (!mailStatus)
				{
					logger.debug("failed to send email");
					//					logger.debug(ApplicationProperties.getValue("errors.mail.sending.failed"),new BizLogicException(ErrorKey.getErrorKey("errors.mail.sending.failed"),null,"error occured in sending the mail"));
				}
			}
		}
		catch (DAOException daoException)
		{
			logger.debug(daoException.getMessage(), daoException);
			//			throw new DAOException(bizLogicException.getMessage());
			//throw new BizLogicException(ErrorKey.getErrorKey("dao.error"),daoException,"error occured in insertion");
			throw getBizLogicException(daoException, daoException.getErrorKeyName(), daoException
					.getMsgValues());
		}
		catch (AuditException e)
		{
			logger.debug(e.getMessage(), e);
			throw getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
	}

	/**
	 * Set shipment properties.
	 * @param shipment BaseShipment object.
	 */
	protected void setShipmentSystemProperties(BaseShipment shipment)
	{
		shipment.setCreatedDate(new Date());
		if (shipment.getActivityStatus() != null)
		{
			if (!shipment.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DRAFTED))
			{
				shipment.setActivityStatus(Constants.ACTIVITY_STATUS_IN_PROGRESS);
			}
		}
		else
		{
			shipment.setActivityStatus(Constants.ACTIVITY_STATUS_IN_PROGRESS);
		}
	}

	/**
	 * updates specimen collection.
	 * @param specimenCollection to be updated.
	 * @param dao the object of DAO class.
	 * @param sessionDataBean The session details.
	 * @return collection of specimen.
	 * @throws DAOException if some database operation fails.

	 */
	private Collection<Specimen> updateSpecimenCollection(Collection<Specimen> specimenCollection,
			DAO dao, SessionDataBean sessionDataBean) throws DAOException
	{
		// Collection to return which will contain the container objects retirved from the DB
		Collection<Specimen> specimenObjectsCollection = new HashSet<Specimen>();
		//StorageContainerBizLogic containerBizLogic=new StorageContainerBizLogic();
		Iterator<Specimen> specimenIterator = specimenCollection.iterator();
		while (specimenIterator.hasNext())
		{
			Specimen specimen = specimenIterator.next();
			if (specimen.getLabel() != null)
			{
				specimen = getSpecimenByLabelOrBarcode(specimen.getLabel(), dao,
						Constants.SPECIMEN_PROPERTY_LABEL);
			}
			else
			{
				specimen = getSpecimenByLabelOrBarcode(specimen.getBarcode(), dao,
						Constants.SPECIMEN_PROPERTY_BARCODE);
			}
			//Add statements for storage type too
			specimenObjectsCollection.add(specimen);
		}
		return specimenObjectsCollection;
	}

	/**
	 * @param shipmentRequest object.
	 * @param dao the object of DAO class.
	 * @param operation to be performed.
	 * @return collection of shipment request.
	 * @throws DAOException throws DAOException
	 */
	public Collection<ShipmentRequest> createRequestObjects(ShipmentRequest shipmentRequest,
			DAO dao, String operation) throws DAOException
	{
		Collection<ShipmentRequest> requestCollection = new HashSet<ShipmentRequest>();
		Map<Long, Site> siteMap = getUniqueSiteMap(shipmentRequest, dao);
		//if(siteMap.size()>0)
		if (!(siteMap.isEmpty()))
		{
			Collection<StorageContainer> containerCollection = new HashSet<StorageContainer>();
			Collection<Specimen> specimenCollection = new HashSet<Specimen>();
			for (Long siteId : siteMap.keySet())
			{
				Site site = siteMap.get(siteId);
				Iterator<StorageContainer> containerIterator = shipmentRequest
						.getContainerCollection().iterator();
				while (containerIterator.hasNext())
				{
					StorageContainer container = containerIterator.next();
					if (container.getSite() != null && container.getSite().getId() != null
							&& container.getSite().getId().equals(siteId))
					{
						containerCollection.add(container);
					}
				}
				Iterator<Specimen> specimenIterator = shipmentRequest.getSpecimenCollection()
						.iterator();
				while (specimenIterator.hasNext())
				{
					Specimen specimen = specimenIterator.next();
					if (specimen != null && specimen.getSpecimenPosition() != null
							&& specimen.getSpecimenPosition().getStorageContainer() != null)
					{
						if (specimen.getSpecimenPosition().getStorageContainer().getSite().getId()
								.equals(siteId))
						{
							specimenCollection.add(specimen);
						}
					}
				}
				//if(containerCollection.size()>0 || specimenCollection.size()>0)
				if (!(containerCollection.isEmpty()) || !(specimenCollection.isEmpty()))
				{
					ShipmentRequest request = createShipmentRequest(shipmentRequest,
							containerCollection, specimenCollection, site);
					requestCollection.add(request);
					containerCollection.clear();
					specimenCollection.clear();
				}
			}
		}
		return requestCollection;
	}

	/**
	 * creates shipment request.
	 * @param shipmentRequest request object.
	 * @param containerCollection container collection of request.
	 * @param specimenCollection specimen collection of request.
	 * @param site receiver site.
	 * @return shipment request object.
	 */
	private ShipmentRequest createShipmentRequest(ShipmentRequest shipmentRequest,
			Collection<StorageContainer> containerCollection,
			Collection<Specimen> specimenCollection, Site site)
	{
		ShipmentRequest shiRequest = new ShipmentRequest();
		shiRequest.setSenderComments(shipmentRequest.getSenderComments());
		shiRequest.setSenderSite(shipmentRequest.getSenderSite());
		shiRequest.setSendDate(shipmentRequest.getSendDate());
		shiRequest.setLabel(shipmentRequest.getLabel());
		shiRequest.setReceiverSite(site);
		setShipmentSystemProperties(shiRequest);
		shiRequest.setSenderContactPerson(shipmentRequest.getSenderContactPerson());
		shiRequest.setReceiverContactPerson(shipmentRequest.getReceiverContactPerson());
		shiRequest.getContainerCollection().clear();
		shiRequest.getContainerCollection().addAll(containerCollection);
		shiRequest.getSpecimenCollection().clear();
		shiRequest.getSpecimenCollection().addAll(specimenCollection);
		return shiRequest;
	}

	/**
	 * gets the site map.
	 * @param shipmentRequest request object.
	 * @param dao the object of DAO class.
	 * @return site map.
	 * @throws DAOException if some database operation fails.
	 */
	private Map<Long, Site> getUniqueSiteMap(ShipmentRequest shipmentRequest, DAO dao)
			throws DAOException
	{
		Map<Long, Site> siteMap = new HashMap<Long, Site>();
		Collection<StorageContainer> containerObjectsCollection = new HashSet<StorageContainer>();
		Collection<StorageContainer> containerCollection = shipmentRequest.getContainerCollection();
		Iterator<StorageContainer> containerIterator = containerCollection.iterator();
		while (containerIterator.hasNext())
		{
			StorageContainer container = containerIterator.next();
			//Get site for containers
			if (container.getName() != null && !container.getName().trim().equals(""))
			{
				container = getContainerByNameOrBarcode(container.getName(), dao,
						Constants.CONTAINER_PROPERTY_NAME);
			}
			else if (container.getBarcode() != null && !container.getBarcode().trim().equals(""))
			{
				container = getContainerByNameOrBarcode(container.getBarcode(), dao,
						Constants.CONTAINER_PROPERTY_BARCODE);
			}
			if (container != null)
			{
				Site site = container.getSite();
				if (site != null)
				{
					siteMap.put(site.getId(), site);
				}
				containerObjectsCollection.add(container);
			}
		}
		shipmentRequest.getContainerCollection().clear();
		shipmentRequest.getContainerCollection().addAll(containerObjectsCollection);
		Collection<Specimen> specimenObjectsCollection = new HashSet<Specimen>();
		Collection<Specimen> specimenCollection = shipmentRequest.getSpecimenCollection();
		Iterator<Specimen> specimenIterator = specimenCollection.iterator();
		while (specimenIterator.hasNext())
		{
			Specimen specimen = specimenIterator.next();
			//Get site for containers
			if (specimen.getLabel() != null && !specimen.getLabel().trim().equals(""))
			{
				specimen = getSpecimenByLabelOrBarcode(specimen.getLabel(), dao,
						Constants.SPECIMEN_PROPERTY_LABEL);
			}
			else if (specimen.getBarcode() != null && !specimen.getBarcode().trim().equals(""))
			{
				specimen = getSpecimenByLabelOrBarcode(specimen.getBarcode(), dao,
						Constants.SPECIMEN_PROPERTY_BARCODE);
			}
			if (specimen != null && specimen.getSpecimenPosition() != null
					&& specimen.getSpecimenPosition().getStorageContainer() != null)
			{
				Site site = specimen.getSpecimenPosition().getStorageContainer().getSite();
				if (site != null)
				{
					siteMap.put(site.getId(), site);
				}
				specimenObjectsCollection.add(specimen);
			}
		}
		shipmentRequest.getSpecimenCollection().clear();
		shipmentRequest.getSpecimenCollection().addAll(specimenObjectsCollection);
		return siteMap;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.shippingtracking.BaseShipmentBizLogic#getNotificationMailSubject
	 * (edu.wustl.catissuecore.domain.shippingtracking.BaseShipment)
	 */
	/**
	 * gets the notification mail subject.
	 * @param baseShipment BaseShipment object.
	 * @return notification mail subject.
	 */
	@Override
	protected String getNotificationMailSubject(BaseShipment baseShipment)
	{
		//bug 12816 start
		//to add rejected message if request gets rejected
		if (baseShipment.getActivityStatus().equals("Rejected"))
		{
			return ShipmentMailFormatterUtility
					.getRejectShipmentRequestMailSubject((ShipmentRequest) baseShipment);
		}
		else
		//bug 12816 end
		{
			return ShipmentMailFormatterUtility
					.getCreateShipmentRequestMailSubject((ShipmentRequest) baseShipment);
		}
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.shippingtracking.BaseShipmentBizLogic#getNotificationMailBody
	 * (edu.wustl.catissuecore.domain.shippingtracking.BaseShipment)
	 */
	/**
	 * gets the notification mail body.
	 * @param baseShipment BaseShipment object.
	 * @return notification mail body.
	 */
	@Override
	protected String getNotificationMailBody(BaseShipment baseShipment)
	{
		//bug 12816 start
		if (baseShipment.getActivityStatus().equals("Rejected"))
		{
			return ShipmentMailFormatterUtility
					.formatRejectShipmentRequestMailBody((ShipmentRequest) baseShipment);
		}
		else
		//bug 12816 end
		{
			return ShipmentMailFormatterUtility
					.formatCreateShipmentRequestMailBody((ShipmentRequest) baseShipment);
		}
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.shippingtracking.BaseShipmentBizLogic
	 * #getEmailAddressesForMailNotification(edu.wustl.catissuecore.domain.shippingtracking.BaseShipment)
	 */
	/**
	 * gets the notification mail addresses.
	 * @param baseShipment BaseShipment object.
	 * @return notification mail addresses.
	 */
	@Override
	protected String[] getEmailAddressesForMailNotification(BaseShipment baseShipment)
	{
		ShipmentRequest shipmentRequest = (ShipmentRequest) baseShipment;
		String[] toUser = new String[2];
		toUser[0] = shipmentRequest.getSenderContactPerson().getEmailAddress();
		toUser[1] = shipmentRequest.getReceiverSite().getEmailAddress();
		return toUser;
	}

	/**
	 * fetches the shipment request.
	 * @param selectColumnName selected column name.
	 * @param columnName represents column names.
	 * @param orderByField field for orderby clause.
	 * @param siteId contains the site id.
	 * @param startIndex starting index.
	 * @param numOfRecords represent the numbe of records.
	 * @param additionalWhereClause where clause.
	 * @return list of requests.
	 * @throws BizLogicException if some bizlogic operation fails.
	 */
	public List<Object[]> getShipmentRequests(String selectColumnName, String columnName,
			String orderByField, Long[] siteId, int startIndex, int numOfRecords,
			String additionalWhereClause) throws BizLogicException
	{
		StringBuffer whereClause = new StringBuffer();
		for (int counter = 0; counter < siteId.length; counter++)
		{
			whereClause.append(DAOConstants.OR_JOIN_CONDITION + " shipment." + columnName + "=? ");
		}
		if (additionalWhereClause != null && additionalWhereClause.length() > 0)
		{
			whereClause.append(" " + additionalWhereClause);
		}

		List<Object[]> shipmentsList = null;
		shipmentsList = getShipmentDetails(ShipmentRequest.class.getName(), selectColumnName,
				whereClause.substring(2), siteId, orderByField, startIndex, numOfRecords);
		return shipmentsList;
	}

	/**
	 * Saves the shipment request object in the database.
	 * @param identifier the request id.
	 * @throws DAOException if some database operation fails.
	 * @return ShipmentRequest object.
	 * @throws BizLogicException throws BizLogicException 
	 */
	public ShipmentRequest getShipmentRequestObject(long identifier) throws BizLogicException
	{
		ShipmentRequest shipmentRequest = null;
		List shipmentList = retrieve(ShipmentRequest.class.getName(), "id", identifier);
		shipmentRequest = (ShipmentRequest) shipmentList.get(0);

		//Retrieve specimenCollection which has been lazily initialized
		Collection<Specimen> specimenCollection = (Collection<Specimen>) retrieveAttribute(
				ShipmentRequest.class.getName(), identifier, "elements(specimenCollection)");
		shipmentRequest.getSpecimenCollection().clear();
		shipmentRequest.getSpecimenCollection().addAll(specimenCollection);
		//Retrieve containerCollection which has been lazy initialed
		Collection<StorageContainer> containerCollection = (Collection<StorageContainer>) retrieveAttribute(
				ShipmentRequest.class.getName(), identifier, "elements(containerCollection)");
		shipmentRequest.getContainerCollection().clear();
		shipmentRequest.getContainerCollection().addAll(containerCollection);
		//Get SpecimenPositionCollection for every container which has been lazily initialled.
		Iterator<StorageContainer> containerIterator = containerCollection.iterator();
		while (containerIterator.hasNext())
		{
			StorageContainer container = containerIterator.next();
			Collection<SpecimenPosition> spPosCollection = (Collection<SpecimenPosition>) retrieve(
					SpecimenPosition.class.getName(), "storageContainer.id", container.getId());
			Collection<SpecimenPosition> spPosObjCollection = new HashSet<SpecimenPosition>();
			Iterator<SpecimenPosition> spPosIterator = spPosCollection.iterator();
			while (spPosIterator.hasNext())
			{
				SpecimenPosition specimenPosition = spPosIterator.next();
				Specimen specimen = (Specimen) retrieveAttribute(SpecimenPosition.class.getName(),
						specimenPosition.getId(), "specimen");
				specimenPosition.setSpecimen(specimen);
				specimenPosition.setStorageContainer(container);
				spPosObjCollection.add(specimenPosition);
			}
			container.setSpecimenPositionCollection(new HashSet<SpecimenPosition>());
			container.getSpecimenPositionCollection().addAll(spPosObjCollection);
		}
		return shipmentRequest;
	}

	/**
	 * fetches the shipment request.
	 * @param columnName represents column names.
	 * @param orderByField field for orderby clause.
	 * @param siteId contains the site ids.
	 * @param startIndex starting index.
	 * @param numOfRecords represent the numbe of records.
	 * @return count of requests.
	 * @throws BizLogicException if some bizlogic operation fails.
	 */
	public int getShipmentRequestsCount(String columnName, String orderByField, Long[] siteId,
			int startIndex, int numOfRecords) throws BizLogicException
	{
		String[] whereColumnName = new String[siteId.length];
		String[] whereColumnCondition = new String[siteId.length];
		Object[] whereColumnValue = new Object[siteId.length];
		String whereClause = "";
		for (int counter = 0; counter < siteId.length; counter++)
		{
			whereClause += DAOConstants.OR_JOIN_CONDITION + " shipment." + columnName + "=? ";
		}
		whereClause = whereClause.substring(2);
		int count = getShipmentsCount(ShipmentRequest.class.getName(), whereClause, siteId,
				orderByField, startIndex, numOfRecords);
		return count;
	}

	/**
	 * updates the object.
	 * @param dao the object of DAO class.
	 * @param obj The user object to be updated.
	 * @param oldObj the old object.
	 * @param sessionDataBean The session details
	 * @throws BizLogicException throws BizLogicException
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			if (!(obj instanceof ShipmentRequest))
			{
				//				throw new DAOException(ApplicationProperties.getValue("errors.invalid.object.passed"));
				throw new BizLogicException(ErrorKey.getErrorKey("errors.invalid.object.passed"),
						null, "object is not instance of ShipmentRequest class");
			}
			ShipmentRequest shipmentRequest = (ShipmentRequest) obj;
			//Do not update site of containers included in the shipment request
			Collection<StorageContainer> containerCollection = updateContainerDetails(
					shipmentRequest.getContainerCollection(), dao, sessionDataBean, false, null);
			//Set the collection containing the container objects to the shipment object
			shipmentRequest.getContainerCollection().clear();
			shipmentRequest.getContainerCollection().addAll(containerCollection);
			//			Do not update site of containers included in the shipment request
			Collection<Specimen> specimenCollection = updateSpecimenCollection(shipmentRequest
					.getSpecimenCollection(), dao, sessionDataBean);
			//Set the collection containing the container objects to the shipment object
			shipmentRequest.getSpecimenCollection().clear();
			shipmentRequest.getSpecimenCollection().addAll(specimenCollection);
			//bug 12557
			if (oldObj != null)
			{
				updateShipmentSystemProperties(shipmentRequest, (ShipmentRequest) oldObj);
			}
			setShipmentContactPersons(dao, shipmentRequest, sessionDataBean.getUserId());
			setShipmentSites(dao, shipmentRequest);
			dao.update(shipmentRequest);
			//Add mailing functionality
			boolean mailStatus = sendNotification(shipmentRequest, sessionDataBean);
			if (!mailStatus)
			{
				logger.debug("failed to send email");
				//				logger.debug(ApplicationProperties.getValue("errors.mail.sending.failed"),new BizLogicException(ErrorKey.getErrorKey("errors.mail.sending.failed"),null,"mail sending failed"));
			}
		}
		catch (DAOException daoException)
		{
			logger.debug(daoException.getMessage(), daoException);
			//throw new DAOException(bizLogicException.getMessage());
			//throw new BizLogicException(ErrorKey.getErrorKey("dao.error"),daoException,daoException.getMessage());
			throw getBizLogicException(daoException, daoException.getErrorKeyName(), daoException
					.getMsgValues());
		}
	}

	/**
	 * Custom method for Add Shipment Case.
	 * @param dao the object of DAO class.
	 * @param domainObject representing the shipment request object.
	 * @return site name with site id.
	 */
	public String getObjectId(DAO dao, Object domainObject)
	{
		ShipmentRequest shipmentRequest = (ShipmentRequest) domainObject;
		StringBuffer tempString = new StringBuffer();
		if (shipmentRequest.getActivityStatus() != null
				&& shipmentRequest.getActivityStatus().equals(Constants.ACTIVITY_STATUS_REJECTED))
		{
			if (shipmentRequest.getReceiverSite() != null)
			{
				tempString.append(Site.class.getName()).append("_").append(
						shipmentRequest.getReceiverSite().getId().toString());
				return tempString.toString();
			}
		}
		else
		{
			if (shipmentRequest.getSenderSite() != null)
			{
				tempString.append(Site.class.getName()).append("_").append(
						shipmentRequest.getSenderSite().getId().toString());
				return tempString.toString();
			}
		}
		return null;
	}

	/**
	 * Close the drafted request, once the actual request(s) is generated.
	 * Mark the drafted request status as 'Closed'.
	 * @param requestId id of the request to be drafted.
	 * @param daoType defines the type of DAO.
	 * @param sessionDataBean The session details.
	 * @throws ApplicationException throws ApplicationException
	 */
	public void closeDraftedRequest(long requestId, SessionDataBean sessionDataBean, int daoType)
			throws ApplicationException
	{
		ShipmentRequest shipmentRequest = getShipmentRequestObject(requestId);
		shipmentRequest.setActivityStatus(Status.ACTIVITY_STATUS_CLOSED.toString());
		DAO dao = AppUtility.openDAOSession(null);
		try
		{
			dao.openSession(sessionDataBean);
			dao.update(shipmentRequest);
			dao.commit();
		}
		catch (DAOException daoExp)
		{
			logger.debug(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
		}
		finally
		{
			AppUtility.closeDAOSession(dao);
		}
	}
}
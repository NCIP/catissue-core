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
import edu.wustl.common.util.global.CommonConstants;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;

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
	@Override
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
		final ShipmentRequest shipmentRequest = (ShipmentRequest) obj;
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
					final Iterator<StorageContainer> containerIterator = shipmentRequest
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
								container = this.getContainerByNameOrBarcode(container.getName(),
										dao, Constants.CONTAINER_PROPERTY_NAME);
							}
							else if (container.getBarcode() != null
									&& !container.getBarcode().trim().equals(""))
							{
								containerName = container.getBarcode();
								container = this.getContainerByNameOrBarcode(
										container.getBarcode(), dao,
										Constants.CONTAINER_PROPERTY_BARCODE);
							}
							if (container != null)
							{
								isValid = this.containerBelongsToSite(container, shipmentRequest
										.getSenderSite().getId());
								if (isValid)
								{
									throw this.getBizLogicException(null,
											"shipment.container.RequestingSite", containerName);

								}
								else if (container.getName().contains(
										Constants.IN_TRANSIT_CONTAINER_NAME_PREFIX))
								{
									throw this.getBizLogicException(null,
											"shipment.containerInShipment", containerName);
								}
							}
							else
							{
								//No container with such name or barcode exists
								throw this.getBizLogicException(null, "shipment.NoContainerExists",
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
					final Iterator<Specimen> specimenIterator = shipmentRequest
							.getSpecimenCollection().iterator();
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
								specimen = this.getSpecimenByLabelOrBarcode(specimenName, dao,
										Constants.SPECIMEN_PROPERTY_LABEL);
							}
							else if (specimen.getBarcode() != null
									&& !specimen.getBarcode().trim().equals(""))
							{
								specimenName = specimen.getBarcode();
								specimen = this.getSpecimenByLabelOrBarcode(specimenName, dao,
										Constants.SPECIMEN_PROPERTY_BARCODE);
							}
							if (specimen != null)
							{
								isValid = this.specimenBelongsToSite(specimen, shipmentRequest
										.getSenderSite().getId());
								isBelongsToInTransit = this
										.specimenBelongsToInTransitSite(specimen);
								isSpecimenLocatedVirtual = this
										.specimenBelongsToVirtualSite(specimen);
								if (isValid && !isBelongsToInTransit)
								{
									throw this.getBizLogicException(null,
											"shipment.specimenInRequestingSite", specimenName);
								}
								else if (isBelongsToInTransit)
								{
									throw this.getBizLogicException(null,
											"shipment.specimenInShipment", specimenName);
								}
								else if (isSpecimenLocatedVirtual)
								{
									throw this.getBizLogicException(null,
											"shipment.virtual.specimen", specimenName);
								}
							}
							else
							{
								//No container with such name or barcode exists
								throw this.getBizLogicException(null, "shipment.NoSpecimenExists",
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
					throw this.getBizLogicException(null, "shipment.noSpecimenInRequest", null);
				}
				/*if(errorMsg!=null && errorMsg.length()>0)
				{
					//throw new DAOException(errorMsg.toString());
					throw new BizLogicException(ErrorKey.getErrorKey("dao.error"),null,errorMsg.toString());
				}*/
			}
		}
		catch (final DAOException ex)
		{
			logger.debug(ex.getMessage(), ex);
			//throw new BizLogicException(ErrorKey.getErrorKey("dao.error"),ex,errorMsg.toString());
			throw this.getBizLogicException(ex, ex.getErrorKeyName(), ex.getMsgValues());
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
	@Override
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
			final ShipmentRequest shipmentRequest = (ShipmentRequest) obj;
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
					receiverSiteId = (shipmentRequest.getSpecimenCollection().iterator().next())
							.getSpecimenPosition().getStorageContainer().getSite().getId();
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
			this.setShipmentSystemProperties(shipmentRequest);
			this.setShipmentContactPersons(dao, shipmentRequest, sessionDataBean.getUserId());
			this.setShipmentSites(dao, shipmentRequest);
			final Collection<StorageContainer> containerCollection = this.updateContainerDetails(
					shipmentRequest.getContainerCollection(), dao, sessionDataBean, false, null);
			//Set the collection containing the container objects to the shipment object
			shipmentRequest.getContainerCollection().clear();
			shipmentRequest.getContainerCollection().addAll(containerCollection);
			// Do not update site of containers included in the shipment request
			final Collection<Specimen> specimenCollection = this.updateSpecimenCollection(
					shipmentRequest.getSpecimenCollection(), dao, sessionDataBean);
			//Set the collection containing the container objects to the shipment object
			shipmentRequest.getSpecimenCollection().clear();
			shipmentRequest.getSpecimenCollection().addAll(specimenCollection);
			dao.insert(shipmentRequest);
			final AuditManager auditManager = this.getAuditManager(sessionDataBean);
			auditManager.insertAudit(dao, shipmentRequest);
			// Check if Saving Draft, In saving draft case, mail notification not required.
			if (!shipmentRequest.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DRAFTED))
			{
				//Add mailing functionality
				final boolean mailStatus = this.sendNotification(shipmentRequest, sessionDataBean);
				if (!mailStatus)
				{
					logger.debug("failed to send email");
					//					logger.debug(ApplicationProperties.getValue("errors.mail.sending.failed"),new BizLogicException(ErrorKey.getErrorKey("errors.mail.sending.failed"),null,"error occured in sending the mail"));
				}
			}
		}
		catch (final DAOException daoException)
		{
			logger.debug(daoException.getMessage(), daoException);
			//			throw new DAOException(bizLogicException.getMessage());
			//throw new BizLogicException(ErrorKey.getErrorKey("dao.error"),daoException,"error occured in insertion");
			throw this.getBizLogicException(daoException, daoException.getErrorKeyName(),
					daoException.getMsgValues());
		}
		catch (final AuditException e)
		{
			logger.debug(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
	}

	/**
	 * Set shipment properties.
	 * @param shipment BaseShipment object.
	 */
	@Override
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
		final Collection<Specimen> specimenObjectsCollection = new HashSet<Specimen>();
		//StorageContainerBizLogic containerBizLogic=new StorageContainerBizLogic();
		final Iterator<Specimen> specimenIterator = specimenCollection.iterator();
		while (specimenIterator.hasNext())
		{
			Specimen specimen = specimenIterator.next();
			if (specimen.getLabel() != null)
			{
				specimen = this.getSpecimenByLabelOrBarcode(specimen.getLabel(), dao,
						Constants.SPECIMEN_PROPERTY_LABEL);
			}
			else
			{
				specimen = this.getSpecimenByLabelOrBarcode(specimen.getBarcode(), dao,
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
		final Collection<ShipmentRequest> requestCollection = new HashSet<ShipmentRequest>();
		final Map<Long, Site> siteMap = this.getUniqueSiteMap(shipmentRequest, dao);
		//if(siteMap.size()>0)
		if (!(siteMap.isEmpty()))
		{
			final Collection<StorageContainer> containerCollection = new HashSet<StorageContainer>();
			final Collection<Specimen> specimenCollection = new HashSet<Specimen>();
			for (final Long siteId : siteMap.keySet())
			{
				final Site site = siteMap.get(siteId);
				final Iterator<StorageContainer> containerIterator = shipmentRequest
						.getContainerCollection().iterator();
				while (containerIterator.hasNext())
				{
					final StorageContainer container = containerIterator.next();
					if (container.getSite() != null && container.getSite().getId() != null
							&& container.getSite().getId().equals(siteId))
					{
						containerCollection.add(container);
					}
				}
				final Iterator<Specimen> specimenIterator = shipmentRequest.getSpecimenCollection()
						.iterator();
				while (specimenIterator.hasNext())
				{
					final Specimen specimen = specimenIterator.next();
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
					final ShipmentRequest request = this.createShipmentRequest(shipmentRequest,
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
		final ShipmentRequest shiRequest = new ShipmentRequest();
		shiRequest.setSenderComments(shipmentRequest.getSenderComments());
		shiRequest.setSenderSite(shipmentRequest.getSenderSite());
		shiRequest.setSendDate(shipmentRequest.getSendDate());
		shiRequest.setLabel(shipmentRequest.getLabel());
		shiRequest.setReceiverSite(site);
		this.setShipmentSystemProperties(shiRequest);
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
		final Map<Long, Site> siteMap = new HashMap<Long, Site>();
		final Collection<StorageContainer> containerObjectsCollection = new HashSet<StorageContainer>();
		final Collection<StorageContainer> containerCollection = shipmentRequest
				.getContainerCollection();
		final Iterator<StorageContainer> containerIterator = containerCollection.iterator();
		while (containerIterator.hasNext())
		{
			StorageContainer container = containerIterator.next();
			//Get site for containers
			if (container.getName() != null && !container.getName().trim().equals(""))
			{
				container = this.getContainerByNameOrBarcode(container.getName(), dao,
						Constants.CONTAINER_PROPERTY_NAME);
			}
			else if (container.getBarcode() != null && !container.getBarcode().trim().equals(""))
			{
				container = this.getContainerByNameOrBarcode(container.getBarcode(), dao,
						Constants.CONTAINER_PROPERTY_BARCODE);
			}
			if (container != null)
			{
				final Site site = container.getSite();
				if (site != null)
				{
					siteMap.put(site.getId(), site);
				}
				containerObjectsCollection.add(container);
			}
		}
		shipmentRequest.getContainerCollection().clear();
		shipmentRequest.getContainerCollection().addAll(containerObjectsCollection);
		final Collection<Specimen> specimenObjectsCollection = new HashSet<Specimen>();
		final Collection<Specimen> specimenCollection = shipmentRequest.getSpecimenCollection();
		final Iterator<Specimen> specimenIterator = specimenCollection.iterator();
		while (specimenIterator.hasNext())
		{
			Specimen specimen = specimenIterator.next();
			//Get site for containers
			if (specimen.getLabel() != null && !specimen.getLabel().trim().equals(""))
			{
				specimen = this.getSpecimenByLabelOrBarcode(specimen.getLabel(), dao,
						Constants.SPECIMEN_PROPERTY_LABEL);
			}
			else if (specimen.getBarcode() != null && !specimen.getBarcode().trim().equals(""))
			{
				specimen = this.getSpecimenByLabelOrBarcode(specimen.getBarcode(), dao,
						Constants.SPECIMEN_PROPERTY_BARCODE);
			}
			if (specimen != null && specimen.getSpecimenPosition() != null
					&& specimen.getSpecimenPosition().getStorageContainer() != null)
			{
				final Site site = specimen.getSpecimenPosition().getStorageContainer().getSite();
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
		final ShipmentRequest shipmentRequest = (ShipmentRequest) baseShipment;
		final String[] toUser = new String[2];
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
		final StringBuffer whereClause = new StringBuffer();
		if (additionalWhereClause != null && additionalWhereClause.length() > 0)
		{
			whereClause.append(additionalWhereClause);
			whereClause.append(" (");
		}
		for (final Long element : siteId)
		{
			whereClause.append(" shipment." + columnName + "=? "
					+ CommonConstants.OR_JOIN_CONDITION);
		}
		String whereClauseString = whereClause.toString();
		whereClauseString = whereClauseString.substring(0, (whereClauseString.length() - 2));
		whereClauseString = whereClauseString + ")";

		List<Object[]> shipmentsList = null;
		shipmentsList = this.getShipmentDetails(ShipmentRequest.class.getName(), selectColumnName,
				whereClauseString, siteId, orderByField, startIndex, numOfRecords);
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
		final List shipmentList = this.retrieve(ShipmentRequest.class.getName(), "id", identifier);
		shipmentRequest = (ShipmentRequest) shipmentList.get(0);

		//Retrieve specimenCollection which has been lazily initialized
		final Collection<Specimen> specimenCollection = (Collection<Specimen>) this
				.retrieveAttribute(ShipmentRequest.class.getName(), identifier,
						"elements(specimenCollection)");
		shipmentRequest.getSpecimenCollection().clear();
		shipmentRequest.getSpecimenCollection().addAll(specimenCollection);
		//Retrieve containerCollection which has been lazy initialed
		final Collection<StorageContainer> containerCollection = (Collection<StorageContainer>) this
				.retrieveAttribute(ShipmentRequest.class.getName(), identifier,
						"elements(containerCollection)");
		shipmentRequest.getContainerCollection().clear();
		shipmentRequest.getContainerCollection().addAll(containerCollection);
		this.getSpecimenPositionCollection(containerCollection);
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
		final StringBuffer whereClause = new StringBuffer();
		whereClause.append(" shipment.activityStatus != '"
				+ Status.ACTIVITY_STATUS_CLOSED.toString() + "' AND ");
		whereClause.append(" (");

		for (final Long element : siteId)
		{
			whereClause.append(" shipment." + columnName + "=? "
					+ CommonConstants.OR_JOIN_CONDITION);
		}
		String whereClauseString = whereClause.toString();
		whereClauseString = whereClauseString.substring(0, (whereClauseString.length() - 2));
		whereClauseString = whereClauseString + ")";

		final int count = getShipmentsCount(ShipmentRequest.class.getName(), whereClauseString,
				siteId, orderByField, startIndex, numOfRecords);
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
	@Override
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			if (!(obj instanceof ShipmentRequest))
			{
				//throw new DAOException(ApplicationProperties.getValue("errors.invalid.object.passed"));
				throw new BizLogicException(ErrorKey.getErrorKey("errors.invalid.object.passed"),
						null, "object is not instance of ShipmentRequest class");
			}
			final ShipmentRequest shipmentRequest = (ShipmentRequest) obj;
			//Do not update site of containers included in the shipment request
			final Collection<StorageContainer> containerCollection = this.updateContainerDetails(
					shipmentRequest.getContainerCollection(), dao, sessionDataBean, false, null);
			//Set the collection containing the container objects to the shipment object
			shipmentRequest.getContainerCollection().clear();
			shipmentRequest.getContainerCollection().addAll(containerCollection);
			//			Do not update site of containers included in the shipment request
			final Collection<Specimen> specimenCollection = this.updateSpecimenCollection(
					shipmentRequest.getSpecimenCollection(), dao, sessionDataBean);
			//Set the collection containing the container objects to the shipment object
			shipmentRequest.getSpecimenCollection().clear();
			shipmentRequest.getSpecimenCollection().addAll(specimenCollection);
			//bug 12557
			if (oldObj != null)
			{
				this.updateShipmentSystemProperties(shipmentRequest, (ShipmentRequest) oldObj);
			}
			this.setShipmentContactPersons(dao, shipmentRequest, sessionDataBean.getUserId());
			this.setShipmentSites(dao, shipmentRequest);
			dao.update(shipmentRequest);
			//Add mailing functionality
			final boolean mailStatus = this.sendNotification(shipmentRequest, sessionDataBean);
			if (!mailStatus)
			{
				logger.debug("failed to send email");
				//				logger.debug(ApplicationProperties.getValue("errors.mail.sending.failed"),new BizLogicException(ErrorKey.getErrorKey("errors.mail.sending.failed"),null,"mail sending failed"));
			}
		}
		catch (final DAOException daoException)
		{
			logger.debug(daoException.getMessage(), daoException);
			//throw new DAOException(bizLogicException.getMessage());
			//throw new BizLogicException(ErrorKey.getErrorKey("dao.error"),daoException,daoException.getMessage());
			throw this.getBizLogicException(daoException, daoException.getErrorKeyName(),
					daoException.getMsgValues());
		}
	}

	/**
	 * Custom method for Add Shipment Case.
	 * @param dao the object of DAO class.
	 * @param domainObject representing the shipment request object.
	 * @return site name with site id.
	 */
	@Override
	public String getObjectId(DAO dao, Object domainObject)
	{
		final ShipmentRequest shipmentRequest = (ShipmentRequest) domainObject;
		final StringBuffer tempString = new StringBuffer();
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
		final ShipmentRequest shipmentRequest = this.getShipmentRequestObject(requestId);
		shipmentRequest.setActivityStatus(Status.ACTIVITY_STATUS_CLOSED.toString());
		final DAO dao = AppUtility.openDAOSession(null);
		try
		{
			dao.update(shipmentRequest);
			dao.commit();
		}
		catch (final DAOException daoExp)
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
/**
 * <p>Title: ShipmentBizLogic Class>
 * <p>Description:	Manipulate shipment information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author nilesh_ghone
 * @version 1.00
 * Created on July 16, 2008
 */

package edu.wustl.catissuecore.bizlogic.shippingtracking;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.shippingtracking.BaseShipment;
import edu.wustl.catissuecore.domain.shippingtracking.Shipment;
import edu.wustl.catissuecore.domain.shippingtracking.ShipmentRequest;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.shippingtracking.Constants;
import edu.wustl.catissuecore.util.shippingtracking.ShipmentMailFormatterUtility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.util.DAOConstants;
import edu.wustl.security.exception.UserNotAuthorizedException;

/**
 * Manipulate Shipment information into the database using Hibernate.
 * @author
 */
public class ShipmentBizLogic extends BaseShipmentBizLogic
{
	Logger logger = Logger.getCommonLogger(ShipmentBizLogic.class);
	/*
	 * (non-Javadoc)
	 * @see
	 * edu.wustl.catissuecore.bizlogic.shippingtracking.BaseShipmentBizLogic
	 * #getNotificationMailSubject
	 * (edu.wustl.catissuecore.domain.shippingtracking.BaseShipment)
	 */
	/**
	 * returns the notification mail subject.
	 * @param shipment object of BaseShipment class.
	 * @return string representing the motification.
	 */
	@Override
	protected String getNotificationMailSubject(BaseShipment shipment)
	{
		return ShipmentMailFormatterUtility
				.getCreateShipmentMailSubject((Shipment) shipment);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * edu.wustl.catissuecore.bizlogic.shippingtracking.BaseShipmentBizLogic
	 * #getEmailAddressesForMailNotification
	 * (edu.wustl.catissuecore.domain.shippingtracking.BaseShipment)
	 */
	/**
	 * returns the array of strings containing email addresses for notification.
	 * @param baseShipment object of BaseShipment class.
	 * @return the array of string giving email addresses.
	 */
	@Override
	protected String[] getEmailAddressesForMailNotification(
			BaseShipment baseShipment)
	{
		Shipment shipment = (Shipment) baseShipment;
		String[] toUser = new String[2];
		toUser[0] = shipment.getSenderContactPerson().getEmailAddress();
		toUser[1] = shipment.getReceiverSite().getEmailAddress();
		return toUser;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * edu.wustl.catissuecore.bizlogic.shippingtracking.BaseShipmentBizLogic
	 * #getNotificationMailBody
	 * (edu.wustl.catissuecore.domain.shippingtracking.BaseShipment)
	 */
	/**
	 * gets the contents of notification mail.
	 * @param shipment object of BaseShipment class.
	 * @return string containing the body of notification mail.
	 */
	@Override
	protected String getNotificationMailBody(BaseShipment shipment)
	{
		return ShipmentMailFormatterUtility
				.formatCreateShipmentMailBody((Shipment) shipment);
	}
	/**
	 * gets the shipment object.
	 * @param identifier of the shipment object.
	 * @return shipment object.
	 * @throws BizLogicException if some database operation fails.
	 */
	public Shipment getShipmentObject(Long identifier) throws BizLogicException
	{
		Shipment shipment = null;
		try
		{
			List shipmentList = retrieve(Shipment.class.getName(), "id", identifier);
			shipment = (Shipment) shipmentList.get(0);

			// Retrieve containerCollection which has been lazy initialed
			Collection<StorageContainer> containerCollection = (Collection<StorageContainer>)
				retrieveAttribute(
				Shipment.class.getName(), identifier, "elements(containerCollection)");

			shipment.getContainerCollection().clear();
			shipment.getContainerCollection().addAll(containerCollection);

			// Get SpecimenPositionCollection for every container which has been
			// lazily initialled.
			Iterator<StorageContainer> containerIterator = containerCollection
				.iterator();
			while (containerIterator.hasNext())
			{
				StorageContainer container = containerIterator.next();
				Collection<SpecimenPosition> spPosCollection = (Collection<SpecimenPosition>) retrieve(
					SpecimenPosition.class.getName(), "storageContainer.id",
					container.getId());
				Collection<SpecimenPosition> spPosObjCollection = new HashSet<SpecimenPosition>();
				Iterator<SpecimenPosition> spPosIterator = spPosCollection
					.iterator();
				while (spPosIterator.hasNext())
				{
					SpecimenPosition specimenPosition = spPosIterator.next();
					Specimen specimen = (Specimen) retrieveAttribute(
						SpecimenPosition.class.getName(), specimenPosition
								.getId(), "specimen");
					specimenPosition.setSpecimen(specimen);
					specimenPosition.setStorageContainer(container);
					spPosObjCollection.add(specimenPosition);
				}
				container.setSpecimenPositionCollection(new HashSet<SpecimenPosition>());
				container.getSpecimenPositionCollection()
					.addAll(spPosObjCollection);
			}
		}
		catch(BizLogicException bizLogicException)
		{
			logger.debug(bizLogicException.getMessage(), bizLogicException);
			//throw new BizLogicException(ErrorKey.getErrorKey("dao.error"),bizLogicException,"error occurred in retreive : ShipmentBizLogic");
			throw getBizLogicException(bizLogicException, bizLogicException.getErrorKeyName(), bizLogicException.getMsgValues());//janu
		}
		return shipment;
	}
	/**
	 * gets the shipments.
	 * @param selectColumnName to be selected in the query.
	 * @param columnName name of the column for the query.
	 * @param orderByField defines the field for the orderby clause.
	 * @param siteId denotes the site id.
	 * @param startIndex the starting index.
	 * @param numOfRecords integer denoteing the number of records.
	 * @return list of shipment objects.
	 * @throws BizLogicException if some bizlogic operation fails.
	 */
	public List<Object[]> getShipments(String selectColumnName,
			String columnName, String orderByField, Long[] siteId,
			int startIndex, int numOfRecords) throws BizLogicException
	{
		String[] whereColumnName = new String[siteId.length];
		String[] whereColumnCondition = new String[siteId.length];
		Object[] whereColumnValue = new Object[siteId.length];

		StringBuffer whereClause = new StringBuffer();

		for (int counter = 0; counter < siteId.length; counter++)
		{
//			whereClause += Constants.OR_JOIN_CONDITION + " shipment."
//					+ columnName + "=? ";
			whereClause.append(DAOConstants.OR_JOIN_CONDITION + " shipment."
					+ columnName + "=? ");
		}
//		whereClause = whereClause + Constants.AND_JOIN_CONDITION
//				+ " shipment.activityStatus!='"
//				+ Constants.ACTIVITY_STATUS_RECEIVED + "' ";
		whereClause.append(DAOConstants.AND_JOIN_CONDITION
				+ " shipment.activityStatus!='"
				+ Constants.ACTIVITY_STATUS_RECEIVED + "' ");
		String whereClauseString = whereClause.toString().substring(2);
		//whereClause = whereClause.substring(2);
		List<Object[]> shipmentsList = null;
		String joinCondition = DAOConstants.OR_JOIN_CONDITION;
		shipmentsList = getShipmentDetails(Shipment.class.getName(),
				selectColumnName, whereClauseString, siteId, orderByField,
				startIndex, numOfRecords);

		return shipmentsList;
	}
	/**
	 * gives the shipment count by creating the query based on some values.
	 * @param columnName column name to be looked for.
	 * @param orderByField field to be used in order by clause.
	 * @param siteId denotes the site id.
	 * @param startIndex integer denoting the starting index.
	 * @param numOfRecords integer denoting the number of records.
	 * @return integer representing the no. of records.
	 * @throws BizLogicException if some bizlogic operation fails.
	 */
	public int getShipmentsCount(String columnName, String orderByField,
			Long[] siteId, int startIndex, int numOfRecords)
			throws BizLogicException
	{
		String[] whereColumnName = new String[siteId.length];
		String[] whereColumnCondition = new String[siteId.length];
		Object[] whereColumnValue = new Object[siteId.length];

		String whereClause = "";

		for (int counter = 0; counter < siteId.length; counter++)
		{
			whereClause += DAOConstants.OR_JOIN_CONDITION + " shipment."
					+ columnName + "=? ";
		}
		whereClause = whereClause + DAOConstants.AND_JOIN_CONDITION
				+ " shipment.activityStatus!='"
				+ Constants.ACTIVITY_STATUS_RECEIVED + "' ";
		whereClause = whereClause.substring(2);
		String joinCondition = DAOConstants.OR_JOIN_CONDITION;
		int count = getShipmentsCount(Shipment.class.getName(), whereClause,
				siteId, orderByField, startIndex, numOfRecords);
		return count;
	}
	/**
	 * this method updates the shipment object.
	 * @param obj the object to be updated.
	 * @param dao the object of DAO class.
	 * @param sessionDataBean containing the information of the session.
	 * @throws BizLogicException if some database operation fails
	 */
	@Override
	protected void postInsert(Object obj, DAO dao,
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		if (obj != null && obj instanceof Shipment)
		{
			if (((Shipment) obj).getShipmentRequest() != null)
			{
				ShipmentRequest shipmentRequest = ((Shipment) obj)
						.getShipmentRequest();
				shipmentRequest
						.setActivityStatus(Constants.ACTIVITY_STATUS_PROCESSED);
				shipmentRequest.setRequestProcessed(Boolean.TRUE);
				ShipmentRequestBizLogic shipmentRequestBizLogic = new ShipmentRequestBizLogic();
				shipmentRequestBizLogic.update(dao, shipmentRequest, null,sessionDataBean);//bug 12557
			}
		}
		super.postInsert(obj, dao, sessionDataBean);
	}
	/**
	 * @param arg0 the object of DAO class.
	 * @param arg1 the object of Object class.
	 * @param arg2 the object of Object class.
	 * @param arg3 containing the session details.
	 * @throws BizLogicException thrown if some bizlogic operation fails.
	 * @throws UserNotAuthorizedException thrown if user is not found authorized.
	 */
	@Override
	protected void postUpdate(DAO arg0, Object arg1, Object arg2,
			SessionDataBean arg3) throws BizLogicException
	{
		super.postUpdate(arg0, arg1, arg2, arg3);
	}
	/**
	 * @param obj of Object class.
	 * @param dao the object of DAO class.
	 * @param sessionDataBean containing the session details.
	 * @throws BizLogicException if some database operation fails.
	 */
	protected void preInsert(Object obj, DAO dao,
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		try
		{
			if (obj != null && obj instanceof Shipment)
			{
				Shipment shipment = (Shipment) obj;
				if (shipment.getShipmentRequest() != null
					&& shipment.getShipmentRequest().getId() != null)
				{
					List<ShipmentRequest> requestList = dao.retrieve(
						ShipmentRequest.class.getName(),
						edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER, shipment
								.getShipmentRequest().getId());
					if (requestList != null && requestList.size() == 1)
					{
						shipment.setShipmentRequest((ShipmentRequest) requestList
							.get(0));
					}
				}
			}
		}
		catch(DAOException e)
		{
			logger.debug("Database operation failed." , e);
			//throw new BizLogicException(ErrorKey.getErrorKey("dao.error"),e,"Database operation failed.");
			throw getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
	}
	/**
	 * @param dao the object of DAO class.
	 * @param obj1 the object of Object class.
	 * @param obj2 the object of Object class.
	 * @param sessionDataBean containing the session details.
	 * @throws BizLogicException thrown if some bizlogic operation fails.
	 * @throws UserNotAuthorizedException thrown if user is not found authorized.
	 */
	protected void preUpdate(DAO dao, Object obj1, Object obj2,
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		preInsert(obj1, dao, sessionDataBean);
	}
	/**
	 * updates the cache memory.
	 * @param storageContainerFrom object of StorageContainer class.
	 * @param specimen object of Specimen class.
	 */
	public void updateCache(StorageContainer storageContainerFrom,
			Specimen specimen)
	{
		Map containerMap = StorageContainerUtil.getContainerMapFromCache();
		StorageContainerUtil.insertSinglePositionInContainerMap(
				storageContainerFrom, containerMap, specimen
						.getSpecimenPosition().getPositionDimensionOne()
						.intValue(), specimen.getSpecimenPosition()
						.getPositionDimensionTwo().intValue());
	}
}
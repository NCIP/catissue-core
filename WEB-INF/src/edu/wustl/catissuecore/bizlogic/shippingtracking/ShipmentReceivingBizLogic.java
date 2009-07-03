/**
 * ShipmentReceivingBizLogic - BizLogic for receiving the shipment.
 * @author nilesh_ghone
 */
package edu.wustl.catissuecore.bizlogic.shippingtracking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.domain.shippingtracking.BaseShipment;
import edu.wustl.catissuecore.domain.shippingtracking.Shipment;
import edu.wustl.catissuecore.domain.shippingtracking.ShipmentRequest;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.shippingtracking.Constants;
import edu.wustl.catissuecore.util.shippingtracking.ShipmentMailFormatterUtility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.exception.UserNotAuthorizedException;
/**
 * this class contains the bizlogic for shipment receiving.
 */
public class ShipmentReceivingBizLogic extends ShipmentBizLogic
{
	Logger logger = Logger.getCommonLogger(ShipmentReceivingBizLogic.class);
	
	/**
	 * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param dao The object of DAO class.
	 * @param oldObj The old object.
	 * @param sessionDataBean The session in which the object is saved.
	 * @throws DAOException 
	 * @throws DAOException if some database operation fails.
	 * @throws UserNotAuthorizedException if user is not found authenticated.
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws BizLogicException
	{
		if (!(obj instanceof Shipment))
		{
			logger.debug("Invalid object is passed to bizlogic.");
			throw new BizLogicException(ErrorKey.getErrorKey("errors.invalid.object.passed"),null,"Object is not the instance of Shipment class.");
		}
		Shipment shipment = (Shipment) obj;
		//Shipment oldShipment = (Shipment) oldObj;
		try
		{
		Collection<StorageContainer> containerCollection = shipment.getContainerCollection();
		StorageContainerBizLogic containerBizLogic=new StorageContainerBizLogic();
		boolean shipmentContainer = false;
		for(StorageContainer storageContainer : containerCollection)
		{
			StorageType storageType=storageContainer.getStorageType();
			if(!shipmentContainer && (storageType!=null) && ((storageType.getName()!=null)
					&& (storageType.getName().trim()
							.equals(Constants.SHIPMENT_CONTAINER_TYPE_NAME))))
			{
				// This is the container created for specimens
				// Update the specimens and dispose this container
				processInTransitContainer(dao,sessionDataBean,storageContainer);
			}
			else
			{
				// containers
				processContainedContainer(dao,sessionDataBean,storageContainer);
			}
		}
		shipment.setActivityStatus(Constants.ACTIVITY_STATUS_RECEIVED);

		dao.update(shipment);

		//	Add mailing functionality
		
		}
		catch(DAOException ex)
		{
			logger.debug("DAO related problem occurred", ex);
			//throw new BizLogicException(ErrorKey.getErrorKey("dao.error"),ex,"Problem occured in update : ShipmentReceivingBizLogic");
			throw getBizLogicException(ex, ex.getErrorKeyName(), ex.getMsgValues());//janu
		}
		boolean mailStatus = sendNotification(shipment,sessionDataBean);
		if (!mailStatus)
		{
			logger.debug("failed to send email..");
//			logger.debug(ApplicationProperties.getValue("errors.mail.sending.failed"),AppUtility.getApplicationException(null, "errors.mail.sending.failed", "Mail sending operation failed."));				
		}
	}
	/**
	 * gets the notification mail.
	 * @param shipment object of base shipment class.
	 * @return the body of matl.
	 */
	protected String getNotificationMailBody(BaseShipment shipment)
	{
		return ShipmentMailFormatterUtility.formatShipmentReceivedMailBody((Shipment) shipment);
	}
	/**
	 * gets the notification mail subject.
	 * @param shipment object of BaseShipment class.
	 * @return mail subject.
	 */
	protected String getNotificationMailSubject(BaseShipment shipment)
	{
		return ShipmentMailFormatterUtility.getShipmentReceivedMailSubject((Shipment) shipment);
	}
	/**
	 * processes the container contained within the shipment.
	 * @param dao the object of DAO class.
	 * @param sessionDataBean containing the session details.
	 * @param storageContainer the container to be processed.
	 * @throws DAOException 
	 * @throws DAOException 
	 * @throws DAOException if some database error occurs.
	 * @throws UserNotAuthorizedException if user is not found authorized.
	 */
	private void processContainedContainer(DAO dao, SessionDataBean sessionDataBean,
			StorageContainer storageContainer) throws BizLogicException, DAOException
	{
		if(storageContainer!=null && storageContainer.getActivityStatus() != null)
		{
			if (storageContainer.getActivityStatus().equals(Constants.ACCEPT))
			{
				if(storageContainer.getLocatedAtPosition()!=null
						&& storageContainer.getLocatedAtPosition()
							.getParentContainer()!=null)
				{
					StorageContainer parentContainer=null;
					List<StorageContainer> containerList=null;
					Long parentContianerId=storageContainer
						.getLocatedAtPosition().getParentContainer().getId();
					if(parentContianerId!=null)
					{
						containerList=dao.retrieve(StorageContainer.class.getName(),
								edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER,
								storageContainer.getLocatedAtPosition()
									.getParentContainer().getId());
						if(containerList!=null && containerList.size()==1)
						{
							parentContainer=containerList.get(0);
						}
					}
					else if(storageContainer.getLocatedAtPosition()
								.getParentContainer().getName()!=null)
					{
						containerList=dao.retrieve(StorageContainer.class.getName(),"name",
								storageContainer.getLocatedAtPosition().getParentContainer().getName());
						if(containerList!=null && containerList.size()==1)
						{
							parentContainer=containerList.get(0);
						}
					}
					if(parentContainer!=null)
					{
						storageContainer.getLocatedAtPosition()
							.setParentContainer(parentContainer);
						storageContainer.setSite(parentContainer.getSite());
					}
				}
				else if(storageContainer.getSite()!=null
						&& storageContainer.getSite().getId()!=null)
				{
					List siteList=dao.retrieve(Site.class.getName(),
							edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER,
								storageContainer.getSite().getId());
					if(siteList!=null && siteList.size()==1)
					{
						storageContainer.setSite((Site)siteList.get(0));
					}
					storageContainer.setLocatedAtPosition(null);
				}
				storageContainer.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());//bug 12820
				dao.update(storageContainer);
				//** Sachin Write same logic as SpecimenPostion
				// Ravi : changes : start
				if(storageContainer.getLocatedAtPosition()!=null
						&& storageContainer.getLocatedAtPosition()
							.getParentContainer()!=null)
				{
					Map containerMap = StorageContainerUtil.getContainerMapFromCache();
					StorageContainerUtil.deleteSinglePositionInContainerMap(
							storageContainer.getLocatedAtPosition().getParentContainer(),containerMap,
						storageContainer.getLocatedAtPosition().getPositionDimensionOne()
						.intValue(), storageContainer.getLocatedAtPosition().getPositionDimensionTwo().intValue());
				}
			}
			else if (storageContainer.getActivityStatus().equals(Constants.REJECT_AND_DESTROY))
			{
				// Reject and Destroy - Disable container
				StorageContainer container=(StorageContainer)dao.retrieve(
						StorageContainer.class.getName(),
						edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER,storageContainer.getId()).get(0);
				container.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.toString());
				dao.update(container);
			}
			//bug 12820
			else if (Constants.REJECT_AND_RESEND.equals(storageContainer.getActivityStatus()))
			{
				StorageContainer container=(StorageContainer)dao.retrieve(
						StorageContainer.class.getName(),
						edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER,storageContainer.getId()).get(0);
				container.setActivityStatus(Status.ACTIVITY_STATUS_REJECT.toString());
				dao.update(container);
			}
		}
		else
		{
//			throw new DAOException("storage continer or storage container's activity_status found null.");
			logger.debug("storage continer or storage container's activity_status found null.");
			throw new BizLogicException(ErrorKey.getErrorKey("storagecontainer.or.activitystatus.null"),null,"");
			//throw AppUtility.getApplicationException(null, "errors.mail.sending.failed", "storage continer or storage container's activity_status found null.");
		}
	}
	/**
	 * processes the container in InTransit state.
	 * @param dao the object of DAO class.
	 * @param sessionDataBean containing the session details.
	 * @param storageContainer object of StorageContainer class to be processed.
	 * @throws DAOException if some database operation fails.
	 * @throws UserNotAuthorizedException if user is found un auhorized.
	 */
	private void processInTransitContainer(DAO dao, SessionDataBean sessionDataBean,
			StorageContainer storageContainer) throws DAOException
	{
		Collection<SpecimenPosition> spPosCollection=storageContainer.getSpecimenPositionCollection();
		for (SpecimenPosition specimenPosition : spPosCollection)
		{
			SpecimenPosition retrievedSpPos=null;
			Specimen specimen=null;
			List<SpecimenPosition> spPosList=dao.retrieve(SpecimenPosition.class.getName()
					,edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER,specimenPosition.getId());
			if(spPosList!=null && spPosList.size()==1)
			{
				retrievedSpPos=spPosList.get(0);
				specimen=retrievedSpPos.getSpecimen();
				setSpecimenPositionContents(dao,specimen,specimenPosition);
				setSpecimenActivityStatus(specimen, specimenPosition);
			}
			if(specimen!=null && specimen.getActivityStatus() != null)
			{
				if (!specimen.getActivityStatus().equals(Constants.REJECT_AND_DESTROY))
				{
					// Set specimen's Activity_status to "Active"
					specimen.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
				}
				else
				{
					// Specimen - Rejected and Destroyed.
					specimen.setActivityStatus(Status.ACTIVITY_STATUS_CLOSED.toString());
					specimen.setIsAvailable(false);
					specimen.setSpecimenPosition(null);
				}
				dao.update(specimen);
				if(specimen.getSpecimenPosition() == null)
				{
					// specimen located virtually or specimen is 'rejected and destroyed'
					SpecimenPosition specimenPositionTemp = (SpecimenPosition) (dao.retrieve(
								SpecimenPosition.class.getName(),
								"specimen.id", specimen.getId())).get(0);
					dao.delete(specimenPositionTemp);
				}
				else
				{
					// update specimen position accordingly.
					dao.update(specimen.getSpecimenPosition());
					// Update catch - the position is now occupied.
					updateStorageLocations((TreeMap)StorageContainerUtil
							.getContainerMapFromCache(),specimen);
				}
			}
			else
			{
				logger.debug("specimen or specimen's activity_status found null.");
				throw new DAOException(ErrorKey.getErrorKey("specimen.or.activitystatus.null"),null,"");
				
			}
		}
		// disable shipment container.
		StorageContainer container=(StorageContainer)dao.retrieve(StorageContainer.class.getName(),
				edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER,storageContainer.getId()).get(0);
		container.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.toString());
		disposeShipmentContainer(container, dao, sessionDataBean);
	}
/**
 * sets the specimen position contents.
 * @param dao the object of DAO class.
 * @param specimen contents to be set.
 * @param specimenPosition position of specimen.
 * @throws DAOException if some database operation fails.
 */
	private void setSpecimenPositionContents(DAO dao, Specimen specimen,
			SpecimenPosition specimenPosition) throws DAOException
	{
		List<StorageContainer> containerList=null;
		StorageContainer container=null;
		StorageType storageType = specimenPosition.getStorageContainer()!=null ?  specimenPosition.getStorageContainer().getStorageType() : null;

		if ((storageType!=null) && ((storageType.getName()!=null)
				&& (storageType.getName().trim().equals(Constants.SHIPMENT_CONTAINER_TYPE_NAME))))
		{
			// Storage Location is Virtual.
			container = null;
		}
		else
		{
			// Storage Location is either Auto or Manual.
			if((specimenPosition.getStorageContainer().getName()!=null))
			{
				containerList=dao.retrieve(StorageContainer.class.getName(),
						"name",specimenPosition.getStorageContainer().getName());
			}
			else if(specimenPosition.getStorageContainer().getId()!=null)
			{
				containerList=dao.retrieve(StorageContainer.class.getName(),
						edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER,
							specimenPosition.getStorageContainer().getId());
			}
			if(containerList!=null && containerList.size()==1)
			{
				container=containerList.get(0);
			}
		}
		if(container!=null)
		{
			// Storage Location is either Auto or Manual.
			specimen.getSpecimenPosition().setStorageContainer(container);
			specimen.getSpecimenPosition().setPositionDimensionOne(
					specimenPosition.getPositionDimensionOne());
			specimen.getSpecimenPosition().setPositionDimensionTwo(
					specimenPosition.getPositionDimensionTwo());
		}
		else
		{
			// Storage Location is Virtual.
			specimen.setSpecimenPosition(null);
		}
	}
	/**
	 * Set the user selected specimen's activity status.
	 * @param specimen Specimen to update
	 * @param specimenPosition User updated specimen.
	 */
	private void setSpecimenActivityStatus(Specimen specimen, SpecimenPosition specimenPosition)
	{
		specimen.setActivityStatus(specimenPosition.getSpecimen().getActivityStatus());
	}
	/**
	 * Dispose/Disable shipment container.
	 * @param storageContainer container to dispose.
	 * @param dao object of DAO class.
	 * @param sessionDataBean containing session details.
	 * @throws DAOException 
	 * @throws DAOException if some database error occurs.
	 * @throws UserNotAuthorizedException if user is not found authorized.
	 */
	private void disposeShipmentContainer(StorageContainer storageContainer, DAO dao,
			SessionDataBean sessionDataBean) throws DAOException
	{
		storageContainer.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.toString());
		dao.update(storageContainer);
	}
	/**
	 * post update method overridden.
	 * @param dao the object of DAO class.
	 * @param obj1 object to be updated.
	 * @param obj2 old object.
	 * @param sessionDataBean containing the session details.
	 * @throws BizLogicException if some bizlogic operation fails.
	 * @throws UserNotAuthorizedException if user is not found authorized.
	 */
	protected void postUpdate(DAO dao, Object obj1, Object obj2,
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		super.postUpdate(dao, obj1, obj2,
				sessionDataBean);
//		if(obj1!=null && obj1 instanceof Shipment)
//		{
//			Shipment shipment=(Shipment)obj1;
//			if(shipment.getShipmentRequest()!=null)
//			{
//				ShipmentRequest shipmentRequest=shipment.getShipmentRequest();
//				shipmentRequest.setActivityStatus(Constants.ACTIVITY_STATUS_PROCESSED);
//				ShipmentRequestBizLogic shipmentRequestBizLogic=new ShipmentRequestBizLogic();
//				try {
//					dao.update(shipmentRequest,sessionDataBean,true, false, true);
//				} catch (DAOException e) {
//					Logger.out.debug(e.getMessage(),e);
//					throw new BizLogicException(e.getMessage());
//				}
//				//shipmentRequestBizLogic.update(shipmentRequest, Constants.HIBERNATE_DAO);
//			}
//		}
	}
	/**
	 * pre update method.
	 * @param dao object of DAO class.
	 * @param obj1 object to be updated.
	 * @param obj2 the old object.
	 * @param sessionDataBean containing the session details.
	 * @throws BizLogicException if some bizlogic operation fails.
	 * @throws UserNotAuthorizedException if user is not found authorized.
	 */
	protected void preUpdate(DAO dao, Object obj1, Object obj2,
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		if(obj1 instanceof Shipment)
		{
			Shipment shipment=(Shipment)obj1;
			if(shipment.getShipmentRequest()!=null && shipment.getShipmentRequest().getId()!=null)
			{
				List<ShipmentRequest> requestList;
				try {
					requestList = dao.retrieve(ShipmentRequest.class.getName(),
								edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER,
								shipment.getShipmentRequest().getId());
					if(requestList!=null && requestList.size()==1)
					{
						shipment.setShipmentRequest((ShipmentRequest)requestList.get(0));
					}
				}
				catch (DAOException e)
				{
					logger.debug(e.getMessage(), e);
					//e.printStackTrace();
					throw getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
				}
			}
		}
	}
	/**
	 * Custom method for shipment receiving - used while checking privileges.
	 * @param dao object of DAO class.
	 * @param domainObject whose id is to be retreived.
	 * @return String containing site name with receiver site id.
	 */
	public String getObjectId(DAO dao, Object domainObject)
	{
		BaseShipment baseShipment = (BaseShipment) domainObject;
		StringBuffer sb = new StringBuffer();
		if (baseShipment.getReceiverSite() != null)
		{
			sb.append(Site.class.getName()).append("_").append(
					baseShipment.getReceiverSite().getId().toString());
			return sb.toString();
		}
		else
		{
			return null;
		}
	}
	/**
	 * Get available container map in sites for which user have permission.
	 * @param siteIds user permitted site ids.
	 * @param shipmentContainerStorageTypeId shipment container storage type id
	 * @return Available container map.
	 */
	// Commented due to removing 'auto' functionality.
	/*
	public Map getAvailableContainerMap(List<Long> siteIds,
		long shipmentContainerStorageTypeId) throws DAOException {
		String[] selectColumnName = { Constants.SYSTEM_IDENTIFIER, "name",
				"capacity.oneDimensionCapacity",
				"capacity.twoDimensionCapacity",
				"storageType.id" };
		List list = null;
		if (siteIds!=null && siteIds.size() > 0) {
			String[] whereColumnNames = new String[siteIds.size()];
			String[] whereColumnCondition = new String[siteIds.size()];
			Object[] whereColumnValue = new Object[siteIds.size()];
			int counter = 0;
			for (Long siteId : siteIds) {
				whereColumnNames[counter] = new String("site.id");
				whereColumnCondition[counter] = new String("=");
				whereColumnValue[counter] = new Long(siteId);
				counter++;
			}
			list = retrieve(StorageContainer.class.getName(), selectColumnName, whereColumnNames,
					whereColumnCondition, whereColumnValue, Constants.OR_JOIN_CONDITION);
		} else {
			list = retrieve(StorageContainer.class.getName(), selectColumnName);
		}
		Map containerMap = new TreeMap();
		Logger.out.info("===================== list size:" + list.size());

		StorageContainerBizLogic storageContainerBizLogic = new StorageContainerBizLogic();

		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			Object containerList[] = (Object[]) itr.next();
			// If shipment container storage type, do not put it in map.
			if (containerList[4] != null && String.valueOf(containerList[4])!=null
				&&  String.valueOf(containerList[4]).trim().equals(
					String.valueOf(shipmentContainerStorageTypeId))) {
				continue;
			}
			Map positionMap = storageContainerBizLogic.getAvailablePositionMapForContainer(String
					.valueOf(containerList[0]), 0, containerList[2].toString(),
					containerList[3].toString());

			if (!positionMap.isEmpty()) {
				// Logger.out.info("---------"+container.getName()+"------"+container.getId());
				NameValueBean nvb = new NameValueBean(containerList[1],
						containerList[0]);
				containerMap.put(nvb, positionMap);

			}
		}

		return containerMap;
	}
	*/
	/**
	 * @param containerMap Map of containers
	 * @param specimen Current Specimen
	 */
	// remove free position from catch (because it is now occupied).
	void updateStorageLocations(TreeMap containerMap, Specimen specimen)
	{
		try
		{
			if (specimen.getSpecimenPosition() != null
					&& specimen.getSpecimenPosition().getStorageContainer() != null)
			{
				StorageContainerUtil.deleteSinglePositionInContainerMap(specimen.getSpecimenPosition()
						.getStorageContainer(), containerMap, specimen.getSpecimenPosition().getPositionDimensionOne()
						.intValue(), specimen.getSpecimenPosition().getPositionDimensionTwo().intValue());
			}
		}
		catch (Exception e)
		{
			logger.debug("Exception occured while updating aliquots"+e.getMessage(),e);
			e.printStackTrace();
		}
	}
}

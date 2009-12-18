/**
 * ShipmentReceivingBizLogic - BizLogic for receiving the shipment.
 * @author nilesh_ghone
 */

package edu.wustl.catissuecore.bizlogic.shippingtracking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.domain.shippingtracking.BaseShipment;
import edu.wustl.catissuecore.domain.shippingtracking.Shipment;
import edu.wustl.catissuecore.domain.shippingtracking.ShipmentRequest;
import edu.wustl.catissuecore.util.Position;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.shippingtracking.Constants;
import edu.wustl.catissuecore.util.shippingtracking.ShipmentMailFormatterUtility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.exception.DAOException;

/**
 * this class contains the bizlogic for shipment receiving.
 */
public class ShipmentReceivingBizLogic extends ShipmentBizLogic
{

	private transient final Logger logger = Logger
			.getCommonLogger( ShipmentReceivingBizLogic.class );

	/**
	 * Overridden method
	 * @param obj - shipment object
	 * @param dao - DAO
	 * @param operation - operation
	 * @return boolean
	 * @throws BizLogicException - BizLogicException
	 * bug 12806
	 */
	@Override
	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		super.validate( obj, dao, operation );
		boolean isValid = true;
		final Shipment shipment = (Shipment) obj;
		isValid = this.validateDuplicateSpecimenPositionsInShipment( shipment );
		return isValid;
	}

	/**
	 * bug 12806
	 * This method validates storage positions of specimens.
	 * Checks whether specimens have given duplicate storage positions.
	 * @param shipment - shipment
	 * @return boolean
	 * @throws BizLogicException - BizLogicException
	 */
	private boolean validateDuplicateSpecimenPositionsInShipment(Shipment shipment)
			throws BizLogicException
	{
		final boolean isValid = true;
		final Collection < StorageContainer > containerCollection = shipment
				.getContainerCollection();
		for (final StorageContainer storageContainer : containerCollection)
		{
			final StorageType storageType = storageContainer.getStorageType();
			if (storageType != null
					&& ( ( storageType.getName() != null ) && ( Constants.SHIPMENT_CONTAINER_TYPE_NAME
							.equals( storageType.getName().trim() ) ) ))
			{
				final Collection < SpecimenPosition > spPosCollection = storageContainer
						.getSpecimenPositionCollection();
				if (spPosCollection.size() > 1)
				{
					final Iterator < SpecimenPosition > iterator = spPosCollection.iterator();
					final List < String > specimenPosList = new ArrayList < String >();
					while (iterator.hasNext())
					{
						final SpecimenPosition pos = iterator.next();
						if (pos != null)
						{
							final StorageContainer container = pos.getStorageContainer();
							if (pos.getPositionDimensionOne() != 0
									|| pos.getPositionDimensionTwo() != 0)
							{
								String storageValue = "";
								if (container.getName() == null)
								{
									storageValue = StorageContainerUtil.getStorageValueKey( null,
											container.getId().toString(), pos.getPositionDimensionOne(),
											pos.getPositionDimensionTwo() );
								}
								else
								{
									storageValue = StorageContainerUtil.getStorageValueKey( container
											.getName(), null, pos.getPositionDimensionOne(), pos
											.getPositionDimensionTwo() );
								}
								if (specimenPosList.contains( storageValue ))
								{
									throw this.getBizLogicException( null,
											"shipment.samePositionForSpecimens", null );
								}
								else
								{
									specimenPosList.add( storageValue );									
								}
							}
						}
					}
				}
			}

		}
		return isValid;
	}

	/**
	 * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param dao The object of DAO class.
	 * @param oldObj The old object.
	 * @param sessionDataBean The session in which the object is saved.
	 * @throws BizLogicException - BizLogicException
	*/
	@Override
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		if (!( obj instanceof Shipment ))
		{
			this.logger.debug( "Invalid object is passed to bizlogic." );
			throw new BizLogicException( ErrorKey.getErrorKey( "errors.invalid.object.passed" ) ,
					null , "Object is not the instance of Shipment class." );
		}
		final Shipment shipment = (Shipment) obj;
		//Shipment oldShipment = (Shipment) oldObj;
		try
		{
			final Collection < StorageContainer > containerCollection = shipment
					.getContainerCollection();

			final boolean shipmentContainer = false;
			for (final StorageContainer storageContainer : containerCollection)
			{
				final StorageType storageType = storageContainer.getStorageType();
				if (!shipmentContainer
						&& ( storageType != null )
						&& ( ( storageType.getName() != null ) && ( storageType.getName().trim()
								.equals( Constants.SHIPMENT_CONTAINER_TYPE_NAME ) ) ))
				{
					// This is the container created for specimens
					// Update the specimens and dispose this container
					this.processInTransitContainer( dao, storageContainer );
				}
				else
				{
					// containers
					this.processContainedContainer( dao, storageContainer );
				}
			}
			shipment.setActivityStatus( Constants.ACTIVITY_STATUS_RECEIVED );

			dao.update( shipment,oldObj);

			//	Add mailing functionality

		}
		catch (final DAOException ex)
		{
			this.logger.error( "DAO related problem" + " occurred" + ex.getMessage(), ex );
			//throw new BizLogicException(ErrorKey.getErrorKey("dao.error"),ex,"Problem occured in update : ShipmentReceivingBizLogic");
			throw this.getBizLogicException( ex, ex.getErrorKeyName(), ex.getMsgValues() );//janu
		}
		final boolean mailStatus = this.sendNotification( shipment, sessionDataBean );
		if (!mailStatus)
		{
			this.logger.info( "failed to send email.." );
			//logger.debug(ApplicationProperties.getValue("errors.mail.sending.failed"),AppUtility.getApplicationException(null, "errors.mail.sending.failed", "Mail sending operation failed."));
		}
	}

	/**
	 * gets the notification mail.
	 * @param shipment object of base shipment class.
	 * @return the body of matl.
	 */
	@Override
	protected String getNotificationMailBody(BaseShipment shipment)
	{
		return ShipmentMailFormatterUtility.formatShipmentReceivedMailBody( (Shipment) shipment );
	}

	/**
	 * gets the notification mail subject.
	 * @param shipment object of BaseShipment class.
	 * @return mail subject.
	 */
	@Override
	protected String getNotificationMailSubject(BaseShipment shipment)
	{
		return ShipmentMailFormatterUtility.getShipmentReceivedMailSubject( (Shipment) shipment );
	}

	/**
	 * processes the container contained within the shipment.
	 * @param dao the object of DAO class.
	 * @param storageContainer the container to be processed.
	 * @throws BizLogicException - BizLogicException
	 * @throws DAOException if some database error occurs.
	 */
	private void processContainedContainer(DAO dao,
			StorageContainer storageContainer) throws BizLogicException, DAOException
	{
		if (storageContainer != null && storageContainer.getActivityStatus() != null)
		{
			if (storageContainer.getActivityStatus().equals( Constants.ACCEPT ))
			{
				if (storageContainer.getLocatedAtPosition() != null
						&& storageContainer.getLocatedAtPosition().getParentContainer() != null)
				{
					StorageContainer parentContainer = null;
					List < StorageContainer > containerList = null;
					final Long parentContianerId = storageContainer.getLocatedAtPosition()
							.getParentContainer().getId();
					if (parentContianerId != null)
					{
						containerList = dao.retrieve( StorageContainer.class.getName(),
								edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER,
								storageContainer.getLocatedAtPosition().getParentContainer()
										.getId() );
						if (containerList != null && containerList.size() == 1)
						{
							parentContainer = containerList.get( 0 );
						}
					}
					else if (storageContainer.getLocatedAtPosition().getParentContainer().getName() != null)
					{
						containerList = dao.retrieve( StorageContainer.class.getName(), "name",
								storageContainer.getLocatedAtPosition().getParentContainer()
										.getName() );
						if (containerList != null && containerList.size() == 1)
						{
							parentContainer = containerList.get( 0 );
						}
					}
					if (parentContainer != null)
					{
						storageContainer.getLocatedAtPosition()
								.setParentContainer( parentContainer );
						storageContainer.setSite( parentContainer.getSite() );
					}
				}
				else if (storageContainer.getSite() != null
						&& storageContainer.getSite().getId() != null)
				{
					final List siteList = dao.retrieve( Site.class.getName(),
							edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER,
							storageContainer.getSite().getId() );
					if (siteList != null && siteList.size() == 1)
					{
						storageContainer.setSite( (Site) siteList.get( 0 ) );
					}
					storageContainer.setLocatedAtPosition( null );
				}
				storageContainer.setActivityStatus( Status.ACTIVITY_STATUS_ACTIVE.toString() );//bug 12820
				dao.update( storageContainer );
			}
			else if (storageContainer.getActivityStatus().equals( Constants.REJECT_AND_DESTROY ))
			{
				// Reject and Destroy - Disable container
				final StorageContainer container = (StorageContainer) dao.retrieve(
						StorageContainer.class.getName(),
						edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER,
						storageContainer.getId() ).get( 0 );
				container.setActivityStatus( Status.ACTIVITY_STATUS_DISABLED.toString() );
				dao.update( container );
			}
			//bug 12820
			else if (Constants.REJECT_AND_RESEND.equals( storageContainer.getActivityStatus() ))
			{
				final StorageContainer container = (StorageContainer) dao.retrieve(
						StorageContainer.class.getName(),
						edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER,
						storageContainer.getId() ).get( 0 );
				container.setActivityStatus( Status.ACTIVITY_STATUS_REJECT.toString() );
				dao.update( container );
			}
		}
		else
		{
			//			throw new DAOException("storage continer or storage container's activity_status found null.");
			this.logger
					.debug( "storage continer or storage container's activity_status found null." );
			throw new BizLogicException( ErrorKey
					.getErrorKey( "storagecontainer.or.activitystatus.null" ) , null , "" );
			//throw AppUtility.getApplicationException(null, "errors.mail.sending.failed", "storage continer or storage container's activity_status found null.");
		}
	}

	/**
	 * processes the container in InTransit state.
	 * @param dao the object of DAO class.
	 * @param storageContainer object of StorageContainer class to be processed.
	 * @throws DAOException if some database operation fails.
	 * @throws BizLogicException - BizLogicException
	 */
	private void processInTransitContainer(DAO dao,StorageContainer storageContainer) throws DAOException, BizLogicException
	{
		final Collection < SpecimenPosition > spPosCollection = storageContainer
				.getSpecimenPositionCollection();
		final HashSet < String > storageContainerIds = new HashSet < String >();
		for (final SpecimenPosition specimenPosition : spPosCollection)
		{
			SpecimenPosition retrievedSpPos = null;
			Specimen specimen = null;
			final List < SpecimenPosition > spPosList = dao.retrieve( SpecimenPosition.class
					.getName(), edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER,
					specimenPosition.getId() );
			if (spPosList != null && spPosList.size() == 1)
			{
				retrievedSpPos = spPosList.get( 0 );
				specimen = retrievedSpPos.getSpecimen();
				//Bug 14263
				this.setSpecimenPositionContents( dao, specimen, specimenPosition,
						storageContainerIds );
				this.setSpecimenActivityStatus( specimen, specimenPosition );
			}
			if (specimen != null && specimen.getActivityStatus() != null)
			{
				if (specimen.getActivityStatus().equals( Constants.REJECT_AND_DESTROY ))
				{
					
					// Specimen - Rejected and Destroyed.
					specimen.setActivityStatus( Status.ACTIVITY_STATUS_CLOSED.toString() );
					specimen.setIsAvailable( false );
					specimen.setSpecimenPosition( null );
				}
				else
				{
					// Set specimen's Activity_status to "Active"
					specimen.setActivityStatus( Status.ACTIVITY_STATUS_ACTIVE.toString() );
				}
				dao.update( specimen );
				if (specimen.getSpecimenPosition() == null)
				{
					// specimen located virtually or specimen is 'rejected and destroyed'
					final SpecimenPosition specimenPositionTemp = (SpecimenPosition) ( dao
							.retrieve( SpecimenPosition.class.getName(), "specimen.id", specimen
									.getId() ) ).get( 0 );
					dao.delete( specimenPositionTemp );
				}
				else
				{
					// update specimen position accordingly.
					dao.update( specimen.getSpecimenPosition() );
					// Update catch - the position is now occupied.
				}
			}
			else
			{
				this.logger.debug( "specimen or specimen's activity_status found null." );
				throw new DAOException( ErrorKey.getErrorKey( "specimen.or.activitystatus.null" ) ,
						null , "" );

			}
		}
		// disable shipment container.
		final StorageContainer container = (StorageContainer) dao.retrieve(
				StorageContainer.class.getName(),
				edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER,
				storageContainer.getId() ).get( 0 );
		container.setActivityStatus( Status.ACTIVITY_STATUS_DISABLED.toString() );
		this.disposeShipmentContainer( container, dao );
	}

	/**
	 * sets the specimen position contents.
	 * @param dao - the object of DAO class.
	 * @param specimen - contents to be set.
	 * @param specimenPosition - position of specimen.
	 * @param storageContainerIds - storageContainer Ids
	 * @throws BizLogicException - BizLogicException
	 */
	private void setSpecimenPositionContents(DAO dao, Specimen specimen,
			SpecimenPosition specimenPosition, HashSet< String > storageContainerIds)
			throws BizLogicException
	{
		try
		{
			List < StorageContainer > containerList = null;
			StorageContainer container = null;
			final StorageType storageType = specimenPosition.getStorageContainer() != null
					? specimenPosition.getStorageContainer().getStorageType()
					: null;

			if (( storageType != null )
					&& ( ( storageType.getName() != null ) && ( storageType.getName().trim()
							.equals( Constants.SHIPMENT_CONTAINER_TYPE_NAME ) ) ))
			{
				// Storage Location is Virtual.
				container = null;
			}
			else
			{
				// Storage Location is either Auto or Manual.
				if (( specimenPosition.getStorageContainer().getName() != null ))
				{
					containerList = dao.retrieve( StorageContainer.class.getName(), "name",
							specimenPosition.getStorageContainer().getName() );
				}
				else if (specimenPosition.getStorageContainer().getId() != null)
				{
					containerList = dao.retrieve( StorageContainer.class.getName(),
							edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER,
							specimenPosition.getStorageContainer().getId() );
				}
				if (containerList != null && containerList.size() == 1)
				{
					container = containerList.get( 0 );
				}
			}
			if (container != null)
			{
				final JDBCDAO jdbcdao = this.openJDBCSession();
				String containerId = "";
				if (container.getId() != null)
				{
					containerId = container.getId().toString();
				}
				if (specimenPosition.getPositionDimensionOne() == null
						|| specimenPosition.getPositionDimensionOne() == 0
						|| specimenPosition.getPositionDimensionTwo() == null
						|| specimenPosition.getPositionDimensionTwo() == 0)
				{
					final Position position = StorageContainerUtil
							.getFirstAvailablePositionsInContainer( container, storageContainerIds,
									dao );
					this.setPositionDataToSpecimen( position.getXPos(), position.getYPos(),
							container, specimen );
					this.addStorageValues( specimen, storageContainerIds );
				}
				else
				{
					final boolean isAvailable = StorageContainerUtil.isPositionAvailable( jdbcdao,
							containerId, container.getName(), specimenPosition
									.getPositionDimensionOne().toString(), specimenPosition
									.getPositionDimensionTwo().toString() );
					// Storage Location is either Auto or Manual.
					this.closeJDBCSession( jdbcdao );
					if (isAvailable)
					{
						this.setPositionDataToSpecimen( specimenPosition.getPositionDimensionOne(),
								specimenPosition.getPositionDimensionTwo(), container, specimen );
					}
					else
					{
						throw this.getBizLogicException( null, "shipment.samePositionForSpecimens",
								null );
					}
				}
			}
			else
			{
				// Storage Location is Virtual.
				specimen.setSpecimenPosition( null );
			}
		}
		catch (final ApplicationException exp)
		{
			this.logger.debug( exp.getMessage(), exp );
			throw this.getBizLogicException( exp, exp.getErrorKeyName(), exp.getMsgValues() );
		}

	}

	/**
	 * Sets storage positions
	 * @param pos1 - position 1
	 * @param pos2 - position 2
	 * @param container - StorageContainer obj
	 * @param specimen - Specimen obj
	 */
	private void setPositionDataToSpecimen(Integer pos1, Integer pos2, StorageContainer container,
			Specimen specimen)
	{
		specimen.getSpecimenPosition().setStorageContainer( container );
		specimen.getSpecimenPosition().setPositionDimensionOne( pos1 );
		specimen.getSpecimenPosition().setPositionDimensionTwo( pos2 );
	}

	/**
	 * Add storage values as container name or id : pos1,pos2
	 * @param specimen - Specimen obj
	 * @param storageContainerIds - Hashset containing storagevalues
	 */
	private void addStorageValues(Specimen specimen, Set < String > storageContainerIds)
	{
		String storageValue = null;
		final Long containerId = specimen.getSpecimenPosition().getStorageContainer().getId();
		final Integer pos1 = specimen.getSpecimenPosition().getPositionDimensionOne();
		final Integer pos2 = specimen.getSpecimenPosition().getPositionDimensionTwo();
		final String containerName = specimen.getSpecimenPosition().getStorageContainer().getName();
		if (containerName != null)
		{
			storageValue = StorageContainerUtil
					.getStorageValueKey( containerName, null, pos1, pos2 );
		}
		else
		{
			storageValue = StorageContainerUtil
					.getStorageValueKey( null, containerId.toString(), pos1, pos2 );
		}
		if (!storageContainerIds.contains( storageValue ))
		{
			storageContainerIds.add( storageValue );
		}
	}

	/**
	 * Set the user selected specimen's activity status.
	 * @param specimen Specimen to update
	 * @param specimenPosition User updated specimen.
	 */
	private void setSpecimenActivityStatus(Specimen specimen, SpecimenPosition specimenPosition)
	{
		specimen.setActivityStatus( specimenPosition.getSpecimen().getActivityStatus() );
	}

	/**
	 * Dispose/Disable shipment container.
	 * @param storageContainer container to dispose.
	 * @param dao object of DAO class.
	 * @throws DAOException if some database error occurs.
	*/
	private void disposeShipmentContainer(StorageContainer storageContainer, DAO dao ) throws DAOException
	{
		storageContainer.setActivityStatus( Status.ACTIVITY_STATUS_DISABLED.toString() );
		dao.update( storageContainer );
	}

	/**
	 * post update method overridden.
	 * @param dao the object of DAO class.
	 * @param obj1 object to be updated.
	 * @param obj2 old object.
	 * @param sessionDataBean containing the session details.
	 * @throws BizLogicException if some bizlogic operation fails.
	 */
	@Override
	protected void postUpdate(DAO dao, Object obj1, Object obj2, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		super.postUpdate( dao, obj1, obj2, sessionDataBean );
	}

	/**
	 * pre update method.
	 * @param dao object of DAO class.
	 * @param obj1 object to be updated.
	 * @param obj2 the old object.
	 * @param sessionDataBean containing the session details.
	 * @throws BizLogicException if some bizlogic operation fails.
	 */
	@Override
	protected void preUpdate(DAO dao, Object obj1, Object obj2, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		if (obj1 instanceof Shipment)
		{
			final Shipment shipment = (Shipment) obj1;
			if (shipment.getShipmentRequest() != null
					&& shipment.getShipmentRequest().getId() != null)
			{
				List < ShipmentRequest > requestList;
				try
				{
					requestList = dao.retrieve( ShipmentRequest.class.getName(),
							edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER,
							shipment.getShipmentRequest().getId() );
					if (requestList != null && requestList.size() == 1)
					{
						shipment.setShipmentRequest( requestList.get( 0 ) );
					}
				}
				catch (final DAOException e)
				{
					this.logger.error( e.getMessage(), e );
					throw this.getBizLogicException( e, e.getErrorKeyName(), e.getMsgValues() );
				}
			}
		}
	}

	/**
	 * Custom method for shipment receiving - used while checking privileges.
	 * @param dao object of DAO class.
	 * @param domainObject whose id is to be retrieved.
	 * @return String containing site name with receiver site id.
	 */
	@Override
	public String getObjectId(DAO dao, Object domainObject)
	{
		final BaseShipment baseShipment = (BaseShipment) domainObject;
		final StringBuffer buffer = new StringBuffer();
		String objId=null;
		if (baseShipment.getReceiverSite() == null)
		{
			objId = null;
		}
		else
		{
			buffer.append( Site.class.getName() ).append( '_' ).append(
					baseShipment.getReceiverSite().getId().toString() );
			objId = buffer.toString();
		}
		return objId;
	}

}
/**
 * <p>Title: BaseShipment </p>
 * <p>Description: Base Shipment details.</p>
 * Copyright:    Copyright (c) year
 * Company:
 * @author nilesh_ghone
 * @version 1.00
 */

package edu.wustl.catissuecore.domain.shippingtracking;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.actionForm.shippingtracking.BaseShipmentForm;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.shippingtracking.ShippingTrackingUtility;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.bizlogic.IActivityStatus;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.logger.Logger;

/**
 * Shipment details. Shipment contains specimen(s) and/or container(s). Shipment
 * shipped to sites. Shipment has status.
 * @hibernate.class table="CATISSUE_BASE_SHIPMENT"
 */
public class BaseShipment extends AbstractDomainObject implements Serializable, IActivityStatus
{

	/**
	 * Required field if implements Serializable.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * System generated unique id.
	 */
	protected Long id;

	/**
	 * Shipment label.
	 */
	protected String label;

	/**
	 * Sender site.
	 */
	protected Site senderSite;

	/**
	 * Receiver site.
	 */
	protected Site receiverSite;

	/**
	 * Contact person at the sender site.
	 */
	protected User senderContactPerson;

	/**
	 * Contact person at the receiver site.
	 */
	protected User receiverContactPerson;

	/**
	 * Sender's comments.
	 */
	protected String senderComments;

	/**
	 * Receiver's comments.
	 */
	protected String receiverComments;

	/**
	 */
	protected Date createdDate;

	/**
	 * Shipment creation date.
	 */
	protected Date sendDate;

	/**
	 * Containers in shipment.
	 */
	protected Collection containerCollection = new HashSet();

	/**
	 * Shipment status - Activity Status of shipment, it could be IN-TRANSIT,
	 * RECEIVED.
	 */
	protected String activityStatus;

	/**
	 * Returns the id of the shipment.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
	 *               unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence"
	 *                            value="CATISSUE_BASE_SHIPMENT_SEQ"
	 * @return Returns the id.
	 * @see #setId(Long)
	 */
	@Override
	public Long getId()
	{
		return this.id;
	}

	/**
	 * Sets the shipment identifier.
	 * @param idParam
	 *            Shipment Id
	 * @see #getId()
	 */
	@Override
	public void setId(Long idParam)
	{
		this.id = idParam;
	}

	/**
	 * Returns the label of the shipment/request.
	 * @hibernate.property name="label" type="string" column="LABEL" length="50"
	 * @return Returns the activityStatus.
	 * @see #setLabel(String)
	 */
	public String getLabel()
	{
		return this.label;
	}

	/**
	 * Sets the shipment label.
	 * @param labelParam label to set.
	 * @see #getLabel()
	 */
	public void setLabel(String labelParam)
	{
		this.label = labelParam;
	}

	/**
	 * Gets the shipment sender site details.
	 * @hibernate.many-to-one column="SENDER_SITE_ID"
	 *                        class="edu.wustl.catissuecore.domain.Site"
	 *                        constrained="true"
	 * @return the sender site details.
	 * @see #setSenderSite(Site)
	 */
	public Site getSenderSite()
	{
		return this.senderSite;
	}

	/**
	 * Sets the shipment sender site details.
	 * @param senderSite1
	 *            Shipment sender site details
	 * @see #getSenderSite()
	 */
	public void setSenderSite(Site senderSite1)
	{
		this.senderSite = senderSite1;
	}

	/**
	 * Gets the shipment receiver site details.
	 * @hibernate.many-to-one column="RECEIVER_SITE_ID"
	 *                        class="edu.wustl.catissuecore.domain.Site"
	 *                        constrained="true"
	 * @return the shipment receiver site details
	 * @see #setReceiverSite(Site)
	 */
	public Site getReceiverSite()
	{
		return this.receiverSite;
	}

	/**
	 * Sets the shipment receiver site details.
	 * @param receiverSiteParam
	 *            Shipment receiver site details
	 * @see #getReceiverSite()
	 */
	public void setReceiverSite(Site receiverSiteParam)
	{
		this.receiverSite = receiverSiteParam;
	}

	/**
	 * @hibernate.many-to-one column="SENDER_USER_ID"
	 *                        class="edu.wustl.catissuecore.domain.User"
	 *                        constrained="true"
	 * @return the shipment sender contact person details.
	 * @see #setSenderContactPerson(User)
	 */
	public User getSenderContactPerson()
	{
		return this.senderContactPerson;
	}

	/**
	 * Sets the shipment sender contact person.
	 * @param senderContactPersonParam contact person at the sender side.
	 * @see #getSenderContactPerson()
	 */
	public void setSenderContactPerson(User senderContactPersonParam)
	{
		this.senderContactPerson = senderContactPersonParam;
	}

	/**
	 * Gets the shipment receiver side contact person details.
	 * @hibernate.many-to-one column="RECEIVER_USER_ID"
	 *                        class="edu.wustl.catissuecore.domain.User"
	 *                        constrained="true"
	 * @return the shipment receiver contact person.
	 * @see #setReceiverContactPerson(User)
	 */
	public User getReceiverContactPerson()
	{
		return this.receiverContactPerson;
	}

	/**
	 * Sets the shipment receiver side contact person details.
	 * @param receiverContactPersonParam
	 *            Shipment receiver side contact person details
	 * @see #getReceiverContactPerson()
	 */
	public void setReceiverContactPerson(User receiverContactPersonParam)
	{
		this.receiverContactPerson = receiverContactPersonParam;
	}

	/**
	 * Returns the sender comments.
	 * @hibernate.property name="senderComments" type="string"
	 *                     column="SENDER_COMMENTS" length="255"
	 * @return Returns the title.
	 * @see #setSenderComments(String)
	 */
	public String getSenderComments()
	{
		return this.senderComments;
	}

	/**
	 * Sets the Shipment sender's comments.
	 * @param senderCommentsParam
	 *            Shipment sender's comments
	 * @see #getSenderComments()
	 */
	public void setSenderComments(String senderCommentsParam)
	{
		this.senderComments = senderCommentsParam;
	}

	/**
	 * Returns the shipment receiver comments.
	 * @hibernate.property name="receiverComments" type="string"
	 *                     column="RECEIVER_COMMENTS" length="255"
	 * @return Returns the shipment receiver comments.
	 * @see #setReceiverComments(String)
	 */
	public String getReceiverComments()
	{
		return this.receiverComments;
	}

	/**
	 * Sets the shipment receiver comments.
	 * @param receiverCommentsParam
	 *            Shipment receiver comments
	 * @see #getReceiverComments()
	 */
	public void setReceiverComments(String receiverCommentsParam)
	{
		this.receiverComments = receiverCommentsParam;
	}

	/**
	 * Returns the shipment creation date.
	 * @hibernate.property name="createdDate" type="date" column="CREATED_DATE"
	 * @return Returns the dateAdded.
	 * @see #setCreatedDate(Date)
	 */
	public Date getCreatedDate()
	{
		return this.createdDate;
	}

	/**
	 * Sets the Shipment creation date.
	 * @param createdDateParam
	 *            date of shipment created.
	 * @see #getCreatedDate()
	 */
	public void setCreatedDate(Date createdDateParam)
	{
		this.createdDate = createdDateParam;
	}

	/**
	 * Returns the shipment send date.
	 * @hibernate.property name="sendDate" type="date" column="SEND_DATE"
	 * @return the shipment send date.
	 * @see #setSendDate(Date)
	 */
	public Date getSendDate()
	{
		return this.sendDate;
	}

	/**
	 * Sets the Shipment send date.
	 * @param sendDateParam
	 *            Shipment send date
	 * @see #getSendDate()
	 */
	public void setSendDate(Date sendDateParam)
	{
		this.sendDate = sendDateParam;
	}

	/**
	 * Gets the containers in the shipment.
	 * @hibernate.set name="containerCollection"
	 *                table="CATISSUE_SHIPMENT_CONTAINER_REL" cascade="none"
	 *                inverse="false" lazy="false"
	 * @hibernate.collection-key column="BASE_SHIPMENT_ID"
	 * @hibernate.collection-many-to-many
	 *                                    class="edu.wustl.catissuecore.domain.StorageContainer"
	 *                                    column="CONTAINER_ID"
	 * @return Returns the containers in the shipment.
	 * @see #setContainerCollection(Collection)
	 */
	public Collection getContainerCollection()
	{
		return this.containerCollection;
	}

	/**
	 * Sets the shipment containers.
	 * @param containerCollectionParam collection of Container objects.
	 * @see #getContainerCollection()
	 */
	public void setContainerCollection(Collection containerCollectionParam)
	{
		this.containerCollection = containerCollectionParam;
	}

	/**
	 * Returns the activitystatus of the shipment/request.
	 * @hibernate.property name="activityStatus" type="string"
	 *                     column="ACTIVITY_STATUS" length="50"
	 * @return Returns the activityStatus.
	 * @see #setActivityStatus(String)
	 */
	public String getActivityStatus()
	{
		return this.activityStatus;
	}

	/**
	 * Sets the activitystatus of the shipment/request.
	 * @param activityStatusParam
	 *            Status of the shipment
	 * @see edu.wustl.common.bizlogic.IActivityStatus#setActivityStatus(java.lang.String)
	 */
	public void setActivityStatus(String activityStatusParam)
	{
		this.activityStatus = activityStatusParam;
	}

	/**
	 * @see edu.wustl.common.domain.AbstractDomainObject#setAllValues(edu.wustl.common.actionForm.IValueObject)
	 * @param arg0 object containing all form values.
	 * @throws AssignDataException if some assigning operation fails.
	 */
	@Override
	public void setAllValues(IValueObject arg0) throws AssignDataException
	{
		if (arg0 instanceof BaseShipmentForm)
		{
			final BaseShipmentForm shipmentForm = (BaseShipmentForm) arg0;
			this.setBasicShipmentProperties(shipmentForm);
			this.setShipmentContents(shipmentForm);
		}
	}

	/**
	 * sets the shipment comments.
	 * @param shipmentForm form containing all values.
	 */
	protected void setShipmentContents(BaseShipmentForm shipmentForm)
	{
		final Collection<StorageContainer> updatedContainerCollection = new HashSet<StorageContainer>();
		if (shipmentForm.isAddOperation())
		{
			this.containerCollection.clear();
		}

		final StorageContainer storageContainer = this.populateSpecimenContents(shipmentForm);
		this.populateContainerContents(shipmentForm, updatedContainerCollection);

		if (!shipmentForm.isAddOperation())
		{
			if (storageContainer != null)// bug 11410
			{
				updatedContainerCollection.add(storageContainer);
			}
			this.containerCollection.clear();
			this.containerCollection.addAll(updatedContainerCollection);
		}
		else
		{
			if (storageContainer != null)
			{
				this.containerCollection.add(storageContainer);
			}
		}
	}

	/**
	 * this method populates the container contents.
	 * @param shipmentForm form contraining all values.
	 * @param updatedContainerCollection collection of container objects.
	 */
	protected void populateContainerContents(BaseShipmentForm shipmentForm,
			Collection<StorageContainer> updatedContainerCollection)
	{
		final int containerCount = shipmentForm.getContainerCounter();
		String fieldValue = "";
		StorageContainer container = null;
		final List<String> lblOrBarcodeList = new ArrayList<String>();
		int containerNum = 0;

		if (containerCount > 0)
		{
			for (int containerCounter = 1; containerCounter <= containerCount; containerCounter++)
			{
				if (shipmentForm.isAddOperation())
				{
					fieldValue = (String) shipmentForm.getContainerDetails("containerLabel_"
							+ containerCounter);
				}
				else
				{
					if (shipmentForm.getContainerLabelChoice() != null
							&& shipmentForm.getContainerLabelChoice().trim().equals(
									"ContainerLabel"))
					{
						fieldValue = (String) shipmentForm.getContainerDetails("containerLabel_"
								+ containerCounter);
					}
					else if (shipmentForm.getContainerLabelChoice() != null
							&& shipmentForm.getContainerLabelChoice().trim().equals(
									"ContainerBarcode"))
					{
						fieldValue = (String) shipmentForm.getSpecimenDetails("containerBarcode_"
								+ containerCounter);
					}
				}

				if (fieldValue != null && !fieldValue.trim().equals(""))
				{
					containerNum++;
					lblOrBarcodeList.add(fieldValue);// bug 11026
					container = new StorageContainer();
					if (shipmentForm.getContainerLabelChoice().equals("ContainerLabel"))
					{
						container.setName(fieldValue);
					}
					else if (shipmentForm.getContainerLabelChoice().equals("ContainerBarcode"))
					{
						container.setBarcode(fieldValue);
					}
					this.containerCollection.add(container);

					if (!shipmentForm.isAddOperation())
					{
						StorageContainer containerFromCollection = ShippingTrackingUtility
								.getContainerFromCollection(this.containerCollection, fieldValue);
						if (containerFromCollection == null)
						{
							containerFromCollection = container;
						}
						updatedContainerCollection.add(containerFromCollection);
					}
				}
			}
			if (!shipmentForm.isAddOperation() && containerNum > 0)
			{
				if (updatedContainerCollection != null && !updatedContainerCollection.isEmpty())// bug 11410
				{
					this.containerCollection.clear();
					this.containerCollection.addAll(updatedContainerCollection);
				}
			}
			shipmentForm.setLblOrBarcodeContainerL(lblOrBarcodeList);
			//bug 11026

		}

	}

	/**
	 * this method populates specimen contents.
	 * @param shipmentForm form containing all values.
	 * @return object of StorageContainer class.
	 */
	private StorageContainer populateSpecimenContents(BaseShipmentForm shipmentForm)
	{
		StorageContainer container = null;
		final int specimenCount = shipmentForm.getSpecimenCounter();
		final List<String> lblOrBarcodeList = new ArrayList<String>();
		String fieldValue = "";
		boolean containsSpecimens = false;
		Specimen specimen = null;
		int numOfSpecimens = 0;
		final Collection<SpecimenPosition> updatedSpecimenPosCollection = new HashSet<SpecimenPosition>();

		if (specimenCount > 0)
		{
			if (shipmentForm.isAddOperation())
			{
				container = ShippingTrackingUtility.createInTransitContainer(shipmentForm);
			}
			else
			{
				container = ShippingTrackingUtility.getInTransitContainer(this.containerCollection);
				if (container == null)// bug 11410
				{
					container = ShippingTrackingUtility.createInTransitContainer(shipmentForm);
				}
			}

			for (int specimenCounter = 1; specimenCounter <= specimenCount; specimenCounter++)
			{
				// Get specimen label or barcode
				if (shipmentForm.isAddOperation())
				{
					fieldValue = (String) shipmentForm.getSpecimenDetails("specimenLabel_"
							+ specimenCounter);
				}
				else
				{
					if (shipmentForm.getSpecimenLabelChoice() != null
							&& shipmentForm.getSpecimenLabelChoice().trim().equals("SpecimenLabel"))
					{
						fieldValue = (String) shipmentForm.getSpecimenDetails("specimenLabel_"
								+ specimenCounter);
					}
					else if (shipmentForm.getSpecimenLabelChoice() != null
							&& shipmentForm.getSpecimenLabelChoice().trim().equals(
									"SpecimenBarcode"))
					{
						fieldValue = (String) shipmentForm.getSpecimenDetails("specimenBarcode_"
								+ specimenCounter);
					}
				}

				if (fieldValue != null && !fieldValue.trim().equals(""))
				{
					containsSpecimens = true;
					lblOrBarcodeList.add(fieldValue); // bug 11026
					specimen = new Specimen();
					if (shipmentForm.getSpecimenLabelChoice().equalsIgnoreCase("SpecimenLabel"))
					{
						specimen.setLabel(fieldValue);
					}
					else if (shipmentForm.getSpecimenLabelChoice().equals("SpecimenBarcode"))
					{
						specimen.setBarcode(fieldValue);
					}

					if (shipmentForm.isAddOperation())
					{
						numOfSpecimens++;

						// Get SpecimenPosition object and set it to specimen
						final SpecimenPosition specimenPosition = this.createSpecimenPosition(
								specimen, container, specimenCounter);
						specimen.setSpecimenPosition(specimenPosition);

						// Add the SpecimenPostion object to container
						container.getSpecimenPositionCollection().add(specimenPosition);
					}
					else
					{
						final SpecimenPosition specimenPosFromCollection = ShippingTrackingUtility
								.getSpecimenPositionFromCollection(container
										.getSpecimenPositionCollection(), fieldValue);
						numOfSpecimens++;
						if (specimenPosFromCollection == null)
						{
							// A new specimen is being added
							// Get SpecimenPosition object and set it to
							// specimen
							final SpecimenPosition specimenPosition = this.createSpecimenPosition(
									specimen, container, specimenCounter);
							specimen.setSpecimenPosition(specimenPosition);

							// Add the SpecimenPostion object to container
							container.getSpecimenPositionCollection().add(specimenPosition);
							updatedSpecimenPosCollection.add(specimenPosition);
						}
						else
						{
							// This specimen was already present in the shipment
							specimenPosFromCollection.setPositionDimensionOne(specimenCounter);
							specimen = specimenPosFromCollection.getSpecimen();
							updatedSpecimenPosCollection.add(specimenPosFromCollection);
						}
					}
				}
			}
			container.getCapacity().setOneDimensionCapacity(numOfSpecimens);
			// Process the specimens which have been deleted from the shipment
			this.updateSpecimenPositionToVirtual(container.getSpecimenPositionCollection(),
					updatedSpecimenPosCollection);
			if (!shipmentForm.isAddOperation())
			{
				container.getSpecimenPositionCollection().clear();
				container.getSpecimenPositionCollection().addAll(updatedSpecimenPosCollection);
			}
			shipmentForm.setLblOrBarcodeSpecimenL(lblOrBarcodeList);// bug 11026

		}
		if (!containsSpecimens)
		{
			container = null;
		}
		return container;

	}

	/**
	 * this method updates the specimen position setting it to virtual.
	 * @param specimenPositionCollection collection of specimens.
	 * @param updatedSpecimenPosCollection collection to be updated.
	 */
	private void updateSpecimenPositionToVirtual(
			Collection<SpecimenPosition> specimenPositionCollection,
			Collection<SpecimenPosition> updatedSpecimenPosCollection)
	{
		final Iterator<SpecimenPosition> specimenPosIterator = specimenPositionCollection
				.iterator();
		while (specimenPosIterator.hasNext())
		{
			final SpecimenPosition position = specimenPosIterator.next();
			if (position != null && position.getSpecimen() != null
					&& position.getSpecimen().getLabel() != null)
			{
				final SpecimenPosition specimenPosFromCollection = ShippingTrackingUtility
						.getSpecimenPositionFromCollection(updatedSpecimenPosCollection, position
								.getSpecimen().getLabel());
				if (specimenPosFromCollection == null)
				{
					position.getSpecimen().setSpecimenPosition(null);
				}
			}

		}
	}

	/**
	 * creates specimen position.
	 * @param specimen whose position is to be created.
	 * @param container inside which specimen position is to be created.
	 * @param positionDimensionOne dimension of the container.
	 * @return position of the specimen.
	 */
	protected SpecimenPosition createSpecimenPosition(Specimen specimen,
			StorageContainer container, int positionDimensionOne)
	{
		final SpecimenPosition position = new SpecimenPosition();
		position.setSpecimen(specimen);
		// Put the specimen at (positionDimensionOne,1), since the container is
		// of dimension (number of specimens,1)
		position.setPositionDimensionOne(positionDimensionOne);
		position.setPositionDimensionTwo(1);
		position.setStorageContainer(container);

		return position;
	}

	/**
	 * sets the basic shipment properties like label,site,comments,etc.
	 * @param shipmentForm form containing all values.
	 * @throws AssignDataException if some assigning operation fails.
	 */
	protected void setBasicShipmentProperties(BaseShipmentForm shipmentForm)
			throws AssignDataException
	{
		if (shipmentForm.getId() != 0L)
		{
			this.id = shipmentForm.getId();
		}
		this.label = shipmentForm.getLabel();
		this.senderComments = shipmentForm.getSenderComments();
		this.senderSite = this.createSitObject(shipmentForm.getSenderSiteId());
		this.receiverSite = this.createSitObject(shipmentForm.getReceiverSiteId());
		try
		{
			this.setShipmentDateProperty(shipmentForm);
			// this.sendDate=Utility.parseDate(shipmentForm.getSendDate());
		}
		catch (final ParseException e)
		{
			Logger.out.error(e.getMessage());
			throw new AssignDataException(ErrorKey.getErrorKey("errors.item"), e, "item missing");
		}

	}

	/**
	 * This Method sets Shipment date Property.
	 * @param shipmentForm shipment Form
	 * @throws ParseException Parse Exception
	 */
	protected void setShipmentDateProperty(BaseShipmentForm shipmentForm) throws ParseException
	{
		if (shipmentForm.getSendDate() != null && shipmentForm.getSendDate().trim().length() != 0)
		{
			final Calendar calendar = Calendar.getInstance();
			Date date;
			date = CommonUtilities.parseDate(shipmentForm.getSendDate(), CommonUtilities
					.datePattern(shipmentForm.getSendDate()));
			calendar.setTime(date);
			if (shipmentForm.getSendTimeHour() != null
					&& !shipmentForm.getSendTimeHour().trim().equals(""))
			{
				calendar
						.set(Calendar.HOUR_OF_DAY, Integer.parseInt(shipmentForm.getSendTimeHour()));
			}
			if (shipmentForm.getSendTimeMinutes() != null
					&& !shipmentForm.getSendTimeMinutes().trim().equals(""))
			{
				calendar.set(Calendar.MINUTE, Integer.parseInt(shipmentForm.getSendTimeMinutes()));
			}
			this.sendDate = calendar.getTime();
		}
	}

	/**
	 * creates the object of Site class.
	 * @param siteId whose object is to be created.
	 * @return object of Site class.
	 */
	protected Site createSitObject(long siteId)
	{
		final Site site = new Site();
		site.setId(siteId);
		return site;
	}
}

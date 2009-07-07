/**
 * <p>Title: Shipment </p>
 * <p>Description: Shipment details.</p>
 * Copyright:    Copyright (c) year
 * Company:
 * @author Nilesh Ghone
 * @version 1.00
 */

package edu.wustl.catissuecore.domain.shippingtracking;

import java.util.List;

import edu.wustl.catissuecore.actionForm.shippingtracking.ShipmentForm;
import edu.wustl.catissuecore.actionForm.shippingtracking.ShipmentReceivingForm;
import edu.wustl.catissuecore.domain.ContainerPosition;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.shippingtracking.Constants;
import edu.wustl.catissuecore.util.shippingtracking.ShippingTrackingUtility;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * Shipment details. Shipment contains specimen(s) and/or container(s). Shipment
 * shipped to sites. Shipment has status (In-transit etc.).
 * @hibernate.joined-subclass table="CATISSUE_SHIPMENT"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class Shipment extends BaseShipment
{

	/**
	 *
	 */
	private static final long serialVersionUID = -4710584673076795120L;

	/**
	 * returns the message label.
	 * @return message label.
	 */
	@Override
	public String getMessageLabel()
	{
		return this.getLabel();
	}

	/**
	 * Reference of ShipmentRequest for which the Shipment has been creted.
	 * Can be null in case Shipmentis created independent of a request
	 */
	protected ShipmentRequest shipmentRequest;
	/**
	 * Shipment barcode.
	 */
	protected String barcode;

	/**
	 * @return Returns the shipment barcode.
	 * @hibernate.property name="barcode" type="string" column="BARCODE" length="255"
	 * @see #setBarcode(String)
	 */
	public String getBarcode()
	{
		return this.barcode;
	}

	/**
	 * Sets the shipment barcode.
	 * @param barcode shipment barcode
	 * @see #getBarcode()
	 */
	public void setBarcode(String barcode)
	{
		this.barcode = barcode;
	}

	/*
	 * Used by hibernate for reflection
	 */
	/**
	 * default constructor.
	 */
	public Shipment()
	{

	}

	/**
	 * constructor.assigns all values to the object shipment.
	 * @param form ShipmentForm.
	 * @throws AssignDataException if some assigning problem occurs.
	 */
	public Shipment(AbstractActionForm form) throws AssignDataException
	{
		this();
		this.setAllValues(form);
	}

	/**
	 * sets all values to the shipment object.
	 * @param arg0 containing the form values.
	 * @throws AssignDataException if some assigning operation fails.
	 * @see edu.wustl.common.domain.AbstractDomainObject#setAllValues(edu.wustl.common.actionForm.IValueObject)
	 */
	@Override
	public void setAllValues(IValueObject arg0) throws AssignDataException
	{
		if (arg0 instanceof ShipmentReceivingForm)
		{
			final ShipmentReceivingForm shipmentReceivingForm = (ShipmentReceivingForm) arg0;

			this.setBasicShipmentReceivingProperties(shipmentReceivingForm);
			this.setShipmentReceivingContents(shipmentReceivingForm);
		}
		else
		{
			super.setAllValues(arg0);
			if (arg0 instanceof ShipmentForm)
			{
				this.barcode = ((ShipmentForm) arg0).getBarcode();
				if (((ShipmentForm) arg0).getShipmentRequestId() != 0)
				{
					final ShipmentRequest request = new ShipmentRequest();
					request.setId(((ShipmentForm) arg0).getShipmentRequestId());
					this.shipmentRequest = request;
				}
			}
		}
	}

	/**
	 * Set the basic shipment properties e.g. receiverComments, activityStatus etc.
	 * @param shipmentReceivingForm form containing all values.
	 */
	private void setBasicShipmentReceivingProperties(ShipmentReceivingForm shipmentReceivingForm)
	{
		this.receiverComments = shipmentReceivingForm.getReceiverComments();
		this.activityStatus = Constants.ACTIVITY_STATUS_RECEIVED;
	}

	/**
	 * Set the specimens and containers.
	 * @param shipmentReceivingForm form containing all values.
	 */
	private void setShipmentReceivingContents(ShipmentReceivingForm shipmentReceivingForm)
	{
		// set the specimens and containers in containerCollection
		this.populateContainerContents(shipmentReceivingForm);
		this.populateSpecimenContents(shipmentReceivingForm);
	}

	/**
	 * Set the specimen(s).
	 * @param shipmentReceivingForm form containing all values.
	 */
	private void populateSpecimenContents(ShipmentReceivingForm shipmentReceivingForm)
	{
		StorageContainer container = null;
		container = ShippingTrackingUtility.getInTransitContainer(this.containerCollection);
		if (container != null)
		{
			if (container.getSpecimenPositionCollection() != null)
			{
				final List<Specimen> specimenCollectionInForm = shipmentReceivingForm
						.getSpecimenCollection();
				for (final SpecimenPosition spPosition : container.getSpecimenPositionCollection())
				{
					if (spPosition != null && spPosition.getSpecimen() != null
							&& spPosition.getSpecimen().getId() != null)
					{
						String containerId = "";
						String containerName = "";
						String pos1 = "";
						String pos2 = "";
						final Specimen specimen = spPosition.getSpecimen();
						final String storageLocationSelection
						= (String) shipmentReceivingForm
								.getSpecimenDetails("specimenStorageLocation_" + specimen.getId());
						if (storageLocationSelection != null
								&& storageLocationSelection.trim().equals("1"))
						{
							specimen.setSpecimenPosition(null);
						}
						else if (storageLocationSelection != null
								&& storageLocationSelection.trim().equals("2"))
						{
							containerId = (String) shipmentReceivingForm
									.getSpecimenDetails("containerId_" + specimen.getId());
							pos1 = (String) shipmentReceivingForm.getSpecimenDetails("pos1_"
									+ specimen.getId());
							pos2 = (String) shipmentReceivingForm.
							getSpecimenDetails("pos2_"
									+ specimen.getId());
						}
						else if (storageLocationSelection != null
								&& storageLocationSelection.trim().equals("3"))
						{
							containerName = (String) shipmentReceivingForm
									.getSpecimenDetails("selectedContainerName_"
											+ specimen.getId());
							pos1 = (String) shipmentReceivingForm
							.getSpecimenDetails("position1_"
									+ specimen.getId());
							pos2 = (String) shipmentReceivingForm
							.getSpecimenDetails("position2_"
									+ specimen.getId());
						}
						if ((containerId != null || containerName != null)
								&& pos1 != null
								&& pos2 != null && !pos2.trim().equals(""))
						{
							final StorageContainer spPosContainer
							= new StorageContainer();
							if (storageLocationSelection != null
									&& storageLocationSelection.trim()
									.equals("3"))
							{
								spPosContainer.setName(containerName);
							}
							else if (storageLocationSelection != null
									&& storageLocationSelection.trim()
									.equals("2"))
							{
								spPosContainer.setId(Long.parseLong(containerId));
							}
							spPosition.setPositionDimensionOne(Integer.parseInt(pos1));
							spPosition.setPositionDimensionTwo(Integer.parseInt(pos2));
							spPosition.setStorageContainer(spPosContainer);
						}
						// Set the user selected activity status for specimen.
						for (final Specimen specimenInForm : specimenCollectionInForm)
						{
							if (specimen.getId().equals(specimenInForm.getId()))
							{
								specimen.setActivityStatus
								(specimenInForm.getActivityStatus());
								break;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Sets the container(s).
	 * @param shipmentReceivingForm form containing all values.
	 */
	private void populateContainerContents(ShipmentReceivingForm shipmentReceivingForm)
	{
		String siteFromUser = "";
		String parentContainerSelected = "";
		String containerId = "";
		String containerName = "";
		String positionDim1 = "";
		String positionDim2 = "";
		final List<StorageContainer> containerCollection = shipmentReceivingForm
				.getContainerCollection();
		if (containerCollection != null && containerCollection.size() > 0)
		{
			for (final StorageContainer storageContainer : containerCollection)
			{
				final StorageContainer containerObject = this.getContainerObject(storageContainer
						.getId());
				if (containerObject != null)
				{
					parentContainerSelected = (String) shipmentReceivingForm
							.getContainerDetails("containerStorageLocation_"
									+ storageContainer.getId());
					if (parentContainerSelected != null
							&& parentContainerSelected.trim().equals("Site"))
					{
						siteFromUser
						= (String) shipmentReceivingForm.getContainerDetails("siteId_"
								+ storageContainer.getId());
						if (siteFromUser != null
								&& !siteFromUser.trim().equals(""))
						{
							final Site site = new Site();
							site.setId(Long.parseLong(siteFromUser));
							containerObject.setSite(site);
						}
						else
						{
							containerObject.setSite(this.receiverSite);
						}
					}
					else if (parentContainerSelected != null
							&& parentContainerSelected.trim().equals("Auto"))
					{
						containerId = (String) shipmentReceivingForm
								.getContainerDetails("parentContainerId_"
										+ storageContainer.getId());
						positionDim1 = (String) shipmentReceivingForm
								.getContainerDetails("positionDimensionOne_"
										+ storageContainer.getId());
						positionDim2 = (String) shipmentReceivingForm
								.getContainerDetails("positionDimensionTwo_"
										+ storageContainer.getId());
						if (containerId != null
								&& positionDim1 != null && positionDim2 != null)
						{
							final ContainerPosition position = containerObject
									.getLocatedAtPosition();
							final StorageContainer container = new StorageContainer();
							container.setId(Long.parseLong(containerId));
							if (position != null)
							{
								position.setParentContainer(container);
								position.setPositionDimensionOne
								(Integer.parseInt(positionDim1));
								position.setPositionDimensionTwo
								(Integer.parseInt(positionDim2));
							}
							else
							{
								final ContainerPosition containerPosition
								= new ContainerPosition();
								containerPosition
								.setOccupiedContainer(containerObject);
								containerPosition.setParentContainer(container);
								containerPosition.setPositionDimensionOne(Integer
										.parseInt(positionDim1));
								containerPosition.setPositionDimensionTwo(Integer
										.parseInt(positionDim2));
								containerObject.
								setLocatedAtPosition(containerPosition);
							}
						}
					}
					else if (parentContainerSelected != null
							&& parentContainerSelected.trim().equals("Manual"))
					{
						containerName = (String) shipmentReceivingForm
								.getContainerDetails("selectedContainerNameCont_"
										+ storageContainer.getId());
						positionDim1 = (String) shipmentReceivingForm
								.getContainerDetails("contPosition1_"
										+ storageContainer.getId());
						positionDim2 = (String) shipmentReceivingForm
								.getContainerDetails("contPosition2_"
										+ storageContainer.getId());
						if (containerName != null
								&& positionDim1 != null && positionDim2 != null)
						{
							final ContainerPosition position = containerObject
									.getLocatedAtPosition();
							final StorageContainer container = new StorageContainer();
							container.setName(containerName);
							if (position != null)
							{
								position.setParentContainer(container);
								position.setPositionDimensionOne
								(Integer.parseInt(positionDim1));
								position.setPositionDimensionTwo
								(Integer.parseInt(positionDim2));
							}
							else
							{
								final ContainerPosition containerPosition
								= new ContainerPosition();
								containerPosition
								.setOccupiedContainer(containerObject);
								containerPosition.setParentContainer(container);
								containerPosition.setPositionDimensionOne(Integer
										.parseInt(positionDim1));
								containerPosition.setPositionDimensionTwo(Integer
										.parseInt(positionDim2));
								containerObject
								.setLocatedAtPosition(containerPosition);
							}
						}
					}
					containerObject.setActivityStatus(storageContainer.getActivityStatus());
				}
			} //for
		} // if
	} // method

	/**
	 * gets the container object.
	 * @param identifier id of the container to get.
	 * @return StorageContainer object.
	 */
	private StorageContainer getContainerObject(Long identifier)
	{
		StorageContainer storageContainer = null;
		if (this.containerCollection != null)
		{
			for (final Object container : this.containerCollection)
			{
				if (container instanceof StorageContainer)
				{
					if (((StorageContainer) container).getId().equals(identifier))
					{
						storageContainer = (StorageContainer) container;
					}
				}
			}
		}
		return storageContainer;
	}

	/**
	 * gets the shipment request.
	 * @return shipmentRequest.
	 */
	public ShipmentRequest getShipmentRequest()
	{
		return this.shipmentRequest;
	}

	/**
	 * sets the shipment request.
	 * @param shipmentRequest the request to set.
	 */
	public void setShipmentRequest(ShipmentRequest shipmentRequest)
	{
		this.shipmentRequest = shipmentRequest;
	}
}

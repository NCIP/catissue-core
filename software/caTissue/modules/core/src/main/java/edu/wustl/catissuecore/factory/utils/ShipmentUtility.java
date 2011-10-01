package edu.wustl.catissuecore.factory.utils;

import java.util.List;

import edu.wustl.catissuecore.actionForm.shippingtracking.ShipmentReceivingForm;
import edu.wustl.catissuecore.domain.ContainerPosition;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.shippingtracking.Shipment;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.shippingtracking.Constants;
import edu.wustl.catissuecore.util.shippingtracking.ShippingTrackingUtility;

public class ShipmentUtility
{
	/**
	 * Set the basic shipment properties e.g. receiverComments, activityStatus etc.
	 * @param shipmentReceivingForm form containing all values.
	 */
	public static void setBasicShipmentReceivingProperties(Shipment shipment, ShipmentReceivingForm shipmentReceivingForm)
	{
		/*this.receiverComments = shipmentReceivingForm.getReceiverComments();
		this.activityStatus = Constants.ACTIVITY_STATUS_RECEIVED;*/
		shipment.setReceiverComments(shipmentReceivingForm.getReceiverComments());
		shipment.setActivityStatus(Constants.ACTIVITY_STATUS_RECEIVED);

	}

	/**
	 * Set the specimens and containers.
	 * @param shipmentReceivingForm form containing all values.
	 */
	public static void setShipmentReceivingContents(Shipment shipment,ShipmentReceivingForm shipmentReceivingForm)
	{
		// set the specimens and containers in containerCollection
		populateContainerContents(shipment, shipmentReceivingForm);
		populateSpecimenContents(shipment, shipmentReceivingForm);

	}

	/**
	 * Set the specimen(s).
	 * @param shipmentReceivingForm form containing all values.
	 */
	public static void populateSpecimenContents(Shipment shipment,ShipmentReceivingForm shipmentReceivingForm)
	{
		StorageContainer container = null;
		container = ShippingTrackingUtility.getInTransitContainer(shipment.getContainerCollection());
		if (container != null)
		{
			if (container.getSpecimenPositionCollection() != null)
			{
				final List<Specimen> specimenCollectionInForm = shipmentReceivingForm.getSpecimenCollection();
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
						final String storageLocationSelection = (String) shipmentReceivingForm
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
							pos2 = (String) shipmentReceivingForm.getSpecimenDetails("pos2_"
									+ specimen.getId());
						}
						else if (storageLocationSelection != null
								&& storageLocationSelection.trim().equals("3"))
						{
							containerName = (String) shipmentReceivingForm
									.getSpecimenDetails("selectedContainerName_" + specimen.getId());
							pos1 = (String) shipmentReceivingForm.getSpecimenDetails("position1_"
									+ specimen.getId());
							pos2 = (String) shipmentReceivingForm.getSpecimenDetails("position2_"
									+ specimen.getId());
						}
						StorageContainer spPosContainer = null;
						if (containerId != null || containerName != null)
						{
							InstanceFactory<StorageContainer> scInstFact = DomainInstanceFactory.getInstanceFactory(StorageContainer.class);
							spPosContainer = scInstFact.createObject();//new StorageContainer();
							if (storageLocationSelection != null
									&& storageLocationSelection.trim().equals("3"))
							{
								spPosContainer.setName(containerName);
							}
							else if (storageLocationSelection != null
									&& storageLocationSelection.trim().equals("2"))
							{
								spPosContainer.setId(Long.parseLong(containerId));
							}
						}
						//Bug 14267
						//pos1 and pos2 will be "" in case of manual option selection on shipment receiving page.
						if (pos1 != null && pos2 != null && !pos2.trim().equals(""))
						{
							spPosition.setPositionDimensionOne(Integer.parseInt(pos1));
							spPosition.setPositionDimensionTwo(Integer.parseInt(pos2));
						}
						else // if 0 was not set then it will show positions from in transit container
						{
							spPosition.setPositionDimensionOne(0);
							spPosition.setPositionDimensionTwo(0);
						}
						if(spPosContainer!=null)
						{
							spPosition.setStorageContainer(spPosContainer);
						}
						// Set the user selected activity status for specimen.
						for (final Specimen specimenInForm : specimenCollectionInForm)
						{
							if (specimen.getId().equals(specimenInForm.getId()))
							{
								specimen.setActivityStatus(specimenInForm.getActivityStatus());
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
	public static void populateContainerContents(Shipment shipment, ShipmentReceivingForm shipmentReceivingForm)
	{
		InstanceFactory<StorageContainer> scInstFact = DomainInstanceFactory.getInstanceFactory(StorageContainer.class);
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
				final StorageContainer containerObject = getContainerObject(shipment, storageContainer.getId());
				if (containerObject != null)
				{
					parentContainerSelected = (String) shipmentReceivingForm
							.getContainerDetails("containerStorageLocation_"
									+ storageContainer.getId());
					if (parentContainerSelected != null
							&& parentContainerSelected.trim().equals("Site"))
					{
						siteFromUser = (String) shipmentReceivingForm.getContainerDetails("siteId_"
								+ storageContainer.getId());
						if (siteFromUser != null && !siteFromUser.trim().equals(""))
						{
							InstanceFactory<Site> instFact = DomainInstanceFactory.getInstanceFactory(Site.class);
							final Site site = instFact.createObject();
							site.setId(Long.parseLong(siteFromUser));
							containerObject.setSite(site);
						}
						else
						{
							containerObject.setSite(shipment.getReceiverSite());//this.receiverSite
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
						if (containerId != null && positionDim1 != null && positionDim2 != null)
						{
							final ContainerPosition position = containerObject
									.getLocatedAtPosition();
							final StorageContainer container = scInstFact.createObject();//new StorageContainer();
							container.setId(Long.parseLong(containerId));
							if (position != null)
							{
								position.setParentContainer(container);
								position.setPositionDimensionOne(Integer.parseInt(positionDim1));
								position.setPositionDimensionTwo(Integer.parseInt(positionDim2));
							}
							else
							{
								final ContainerPosition containerPosition = (ContainerPosition)DomainInstanceFactory.getInstanceFactory(ContainerPosition.class).createObject();//new ContainerPosition();
								containerPosition.setOccupiedContainer(containerObject);
								containerPosition.setParentContainer(container);
								containerPosition.setPositionDimensionOne(Integer
										.parseInt(positionDim1));
								containerPosition.setPositionDimensionTwo(Integer
										.parseInt(positionDim2));
								containerObject.setLocatedAtPosition(containerPosition);
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
								.getContainerDetails("contPosition1_" + storageContainer.getId());
						positionDim2 = (String) shipmentReceivingForm
								.getContainerDetails("contPosition2_" + storageContainer.getId());
						if (containerName != null && positionDim1 != null && positionDim2 != null)
						{
							final ContainerPosition position = containerObject
									.getLocatedAtPosition();
							final StorageContainer container = scInstFact.createObject();//new StorageContainer();
							container.setName(containerName);
							if (position != null)
							{
								position.setParentContainer(container);
								position.setPositionDimensionOne(Integer.parseInt(positionDim1));
								position.setPositionDimensionTwo(Integer.parseInt(positionDim2));
							}
							else
							{
								final ContainerPosition containerPosition = (ContainerPosition)DomainInstanceFactory.getInstanceFactory(ContainerPosition.class).createObject();//new ContainerPosition();
								containerPosition.setOccupiedContainer(containerObject);
								containerPosition.setParentContainer(container);
								containerPosition.setPositionDimensionOne(Integer
										.parseInt(positionDim1));
								containerPosition.setPositionDimensionTwo(Integer
										.parseInt(positionDim2));
								containerObject.setLocatedAtPosition(containerPosition);
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
	public static StorageContainer getContainerObject(Shipment shipment, Long identifier)
	{
		StorageContainer storageContainer = null;
		if (shipment.getContainerCollection()!= null)
		{
			for (final Object container : shipment.getContainerCollection())
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


}

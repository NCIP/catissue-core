package edu.wustl.catissuecore.factory.utils;

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
import edu.wustl.catissuecore.domain.shippingtracking.BaseShipment;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.Validator;

public class BaseShipmentUtility
{
	public static void setShipmentContents(BaseShipment baseShipment,BaseShipmentForm shipmentForm)
	{
		final Collection<StorageContainer> updatedContainerCollection = new HashSet<StorageContainer>();
		if (shipmentForm.isAddOperation())
		{
			baseShipment.getContainerCollection().clear();
		}

		final StorageContainer storageContainer = populateSpecimenContents(baseShipment,shipmentForm);
		populateContainerContents(baseShipment,shipmentForm, updatedContainerCollection);

		if (!shipmentForm.isAddOperation())
		{
			if (storageContainer != null)// bug 11410
			{
				updatedContainerCollection.add(storageContainer);
			}
			baseShipment.getContainerCollection().clear();
			baseShipment.getContainerCollection().addAll(updatedContainerCollection);
		}
		else
		{
			if (storageContainer != null)
			{
				baseShipment.getContainerCollection().add(storageContainer);
			}
		}
	}

	/**
	 * this method populates the container contents.
	 * @param shipmentForm form contraining all values.
	 * @param updatedContainerCollection collection of container objects.
	 */
	public static void populateContainerContents(BaseShipment baseShipment,BaseShipmentForm shipmentForm,
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
					InstanceFactory<StorageContainer> scInstFact = DomainInstanceFactory.getInstanceFactory(StorageContainer.class);
					container = scInstFact.createObject();//new StorageContainer();
					if (shipmentForm.getContainerLabelChoice().equals("ContainerLabel"))
					{
						container.setName(fieldValue);
					}
					else if (shipmentForm.getContainerLabelChoice().equals("ContainerBarcode"))
					{
						container.setBarcode(fieldValue);
					}

					baseShipment.getContainerCollection().add(container);

					if (!shipmentForm.isAddOperation())
					{
						StorageContainer containerFromCollection = null;// =ShippingTrackingUtility.getContainerFromCollection(baseShipment.getContainerCollection(), fieldValue);
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
					/*this.containerCollection.clear();
					this.containerCollection.addAll(updatedContainerCollection);*/
					baseShipment.getContainerCollection().clear();
					baseShipment.getContainerCollection().addAll(updatedContainerCollection);
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
	public static StorageContainer populateSpecimenContents(BaseShipment baseShipment, BaseShipmentForm shipmentForm)
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
				container = BaseShipmentUtility.createInTransitContainer(baseShipment, shipmentForm);
			}
			else
			{
				container = BaseShipmentUtility.createInTransitContainer(baseShipment,shipmentForm);
				if (container == null)// bug 11410
				{
					container = BaseShipmentUtility.createInTransitContainer(baseShipment,shipmentForm);
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

				if(!Validator.isEmpty(fieldValue))
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
						final SpecimenPosition specimenPosition = createSpecimenPosition(specimen, container, specimenCounter);
						specimen.setSpecimenPosition(specimenPosition);

						// Add the SpecimenPostion object to container
						container.getSpecimenPositionCollection().add(specimenPosition);
					}
					else
					{
						final SpecimenPosition specimenPosFromCollection = BaseShipmentUtility.getSpecimenPositionFromCollection(container
										.getSpecimenPositionCollection(), fieldValue);
						numOfSpecimens++;
						if (specimenPosFromCollection == null)
						{
							// A new specimen is being added
							// Get SpecimenPosition object and set it to
							// specimen
							final SpecimenPosition specimenPosition =createSpecimenPosition(specimen, container, specimenCounter);
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
			updateSpecimenPositionToVirtual(container.getSpecimenPositionCollection(),
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

	public static SpecimenPosition getSpecimenPositionFromCollection(
			Collection<SpecimenPosition> specimenPositionCollection,
			String fieldValue) {
		// TODO Auto-generated method stub
		return null;
	}

	public static StorageContainer createInTransitContainer(BaseShipment baseShipment, BaseShipmentForm shipmentForm)
	{
		return null;
	}

	/**
	 * this method updates the specimen position setting it to virtual.
	 * @param specimenPositionCollection collection of specimens.
	 * @param updatedSpecimenPosCollection collection to be updated.
	 */
	public static void updateSpecimenPositionToVirtual(
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
				final SpecimenPosition specimenPosFromCollection = BaseShipmentUtility.getSpecimenPositionFromCollection(updatedSpecimenPosCollection, position
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
	public static SpecimenPosition createSpecimenPosition(Specimen specimen,
			StorageContainer container, int positionDimensionOne)
	{
		InstanceFactory<SpecimenPosition> instFact = DomainInstanceFactory.getInstanceFactory(SpecimenPosition.class);
		final SpecimenPosition position = instFact.createObject();//new SpecimenPosition();
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
	public static void setBasicShipmentProperties(BaseShipment baseShipment, BaseShipmentForm shipmentForm)
			throws AssignDataException
	{
		if (shipmentForm.getId() != 0L)
		{
			//this.id = shipmentForm.getId();
			baseShipment.setId(shipmentForm.getId());
		}
		/*this.label = shipmentForm.getLabel();
		this.senderComments = shipmentForm.getSenderComments();
		this.senderSite = this.createSitObject(shipmentForm.getSenderSiteId());
		this.receiverSite = this.createSitObject(shipmentForm.getReceiverSiteId());*/

		baseShipment.setLabel(shipmentForm.getLabel());
		baseShipment.setSenderComments(shipmentForm.getSenderComments());
		baseShipment.setSenderSite(createSitObject(shipmentForm.getSenderSiteId()));
		baseShipment.setReceiverSite(createSitObject(shipmentForm.getReceiverSiteId()));

	}

	/**
	 * This Method sets Shipment date Property.
	 * @param shipmentForm shipment Form
	 * @throws ParseException Parse Exception
	 */
	public static void setShipmentDateProperty(BaseShipment baseShipment, BaseShipmentForm shipmentForm) throws ParseException
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
			//this.sendDate = calendar.getTime();
			baseShipment.setSendDate(calendar.getTime());
		}
	}

	/**
	 * creates the object of Site class.
	 * @param siteId whose object is to be created.
	 * @return object of Site class.
	 */
	public static Site createSitObject(long siteId)
	{
		InstanceFactory<Site> instFact = DomainInstanceFactory.getInstanceFactory(Site.class);
		final Site site = instFact.createObject();//new Site();
		site.setId(siteId);
		return site;
	}
}

package edu.wustl.catissuecore.factory.utils;

import java.util.Collection;
import java.util.HashSet;

import edu.wustl.catissuecore.actionForm.shippingtracking.BaseShipmentForm;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.shippingtracking.Shipment;
import edu.wustl.catissuecore.domain.shippingtracking.ShipmentRequest;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.util.global.Validator;

public class ShipmentRequestUtility
{

	/**
	 * sets the shipment contents.
	 * @param shipmentForm object of BaseShipmentForm class.
	 */

	public static void setShipmentContents(ShipmentRequest shipmentReq, BaseShipmentForm shipmentForm)
	{
		final Collection<StorageContainer> updatedContainerCollection = new HashSet<StorageContainer>();
		//Call to super class's method to set information related to populate container info
		BaseShipmentUtility.populateContainerContents(shipmentReq,shipmentForm, updatedContainerCollection);
		if (!shipmentForm.isAddOperation())
		{
			/*this.containerCollection.clear();
			this.containerCollection.addAll(updatedContainerCollection);*/
			shipmentReq.getContainerCollection().clear();
			shipmentReq.getContainerCollection().addAll(updatedContainerCollection);
		}
		// Populate the specimenCollection
		BaseShipmentUtility.populateSpecimenContents(shipmentReq, shipmentForm);
		//this.populateSpecimenCollection(shipmentForm);
	}
	/**
	 * populates specimen collection.
	 * @param shipmentForm form containing all values.
	 */
	public static void populateSpecimenCollection(ShipmentRequest shipmentReq, BaseShipmentForm shipmentForm)
	{
		final int specimenCount = shipmentForm.getSpecimenCounter();
		String fieldValue = "";
		Specimen specimen = null;
		//this.specimenCollection.clear();
		shipmentReq.getContainerCollection().clear();

		if (specimenCount > 0)
		{
			for (int specimenCounter = 1; specimenCounter <= specimenCount; specimenCounter++)
			{
				fieldValue = (String) shipmentForm.getSpecimenDetails("specimenLabel_"
						+ specimenCounter);
				if(!Validator.isEmpty(fieldValue))
				{
					specimen = new Specimen();
					if (shipmentForm.getSpecimenLabelChoice().equalsIgnoreCase("SpecimenLabel"))
					{
						specimen.setLabel(fieldValue);
					}
					else if (shipmentForm.getSpecimenLabelChoice().equals("SpecimenBarcode"))
					{
						specimen.setBarcode(fieldValue);
					}
					shipmentReq.getSpecimenCollection().add(specimen);
				}
			}
		}
	}

	/**
	 * sets the basic shipment request properties.
	 * @param shipmentForm form object containing all values.
	 * @throws AssignDataException if some assignment operation fails.
	 */
	public static void setBasicShipmentRequestProperties(Shipment shipment,BaseShipmentForm shipmentForm)
			throws AssignDataException
	{
		if (shipmentForm.getId() != 0L)
		{
			//this.id = shipmentForm.getId();
			shipment.setId(shipmentForm.getId());
		}
		/*
		this.senderComments = shipmentForm.getSenderComments();
		this.senderSite = ShippingTrackingUtility.createSitObject(shipmentForm.getSenderSiteId());
		this.label = shipmentForm.getLabel();
		*/

		shipment.setSenderComments(shipmentForm.getSenderComments());
		shipment.setSenderSite(BaseShipmentUtility.createSitObject(shipmentForm.getSenderSiteId()));
		shipment.setLabel(shipmentForm.getLabel());
		if (shipmentForm.getActivityStatus() != null
				&& !shipmentForm.getActivityStatus().trim().equals(""))
		{
			//this.activityStatus = shipmentForm.getActivityStatus();
			shipment.setActivityStatus(shipmentForm.getActivityStatus());
		}
		try
		{
			BaseShipmentUtility.setShipmentDateProperty(shipment,shipmentForm);
		}
		catch (Exception e)
		{
		}
	}

}

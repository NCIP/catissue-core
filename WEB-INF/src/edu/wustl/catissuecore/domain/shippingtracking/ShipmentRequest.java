/**
 * <p>Title: ShipmentRequest </p>
 * <p>Description: Shipment request details.</p>
 * Copyright:    Copyright (c) year
 * Company:
 * @author nilesh_ghone
 * @version 1.00
 */

package edu.wustl.catissuecore.domain.shippingtracking;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import edu.wustl.catissuecore.actionForm.shippingtracking.BaseShipmentForm;
import edu.wustl.catissuecore.actionForm.shippingtracking.ShipmentRequestForm;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

/**
 * Shipment details. Shipment contains specimen(s) and/or container(s). Shipment
 * shipped to sites. Shipment has status assigned.
 * @hibernate.joined-subclass table="CATISSUE_SHIPMENT_REQUEST"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */

/**
 * Represents shipment request.
 */
public class ShipmentRequest extends BaseShipment
{

	/**
	 * represents the collection ofspecimens.
	 */
	public Collection<Specimen> specimenCollection = new HashSet<Specimen>();

	/**
	 * gets the specimen collection.
	 * @return specimenCollection collection of specimens.
	 */
	public Collection<Specimen> getSpecimenCollection()
	{
		return specimenCollection;
	}

	/**
	 * sets the specimen collection.
	 * @param specimenCollection specimen collection to set.
	 */
	public void setSpecimenCollection(Collection<Specimen> specimenCollection)
	{
		this.specimenCollection = specimenCollection;
	}

	/**
	 * gets the message label.
	 * @return msgLabel the message label.
	 */
	public String getMessageLabel()
	{
		String msgLabel = "";
		if (this.receiverSite != null && this.receiverSite.getName() != null)
		{
			msgLabel = "site " + this.receiverSite.getName();
		}
		else
		{
			msgLabel = "required sites.";
		}
		return msgLabel;
	}

	/**
	 *  Required field if implements Serializable.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Used to check whether the shipment created for the request.
	 */
	private boolean requestProcessed = false;

	/**
	 * Get whether the shipment created for the request (i.e. Request processed or not).
	 * @return requestProcessed true if shipment created for the request otherwise false.
	 */
	public boolean isRequestProcessed()
	{
		return requestProcessed;
	}

	/**
	 * Set the request processed or not.
	 * @param requestProcessed true if shipment created for the request otherwise false
	 */
	public void setRequestProcessed(boolean requestProcessed)
	{
		this.requestProcessed = requestProcessed;
	}

	/**
	 * default constructor.
	 */
	public ShipmentRequest()
	{

	}

	/**
	 * constructor.
	 * @param form to set all values.
	 * @throws AssignDataException if some assigning operation fails.
	 */
	public ShipmentRequest(AbstractActionForm form) throws AssignDataException
	{
		this();
		this.setAllValues(form);
	}

	/**
	 * sets all values to the object.
	 * @param arg0 the object representing the form.
	 * @throws AssignDataException if some assigning operation fails.
	 */
	public void setAllValues(IValueObject arg0) throws AssignDataException
	{
		if (arg0 instanceof ShipmentRequestForm)
		{
			BaseShipmentForm shipmentForm = (ShipmentRequestForm) arg0;
			setBasicShipmentRequestProperties(shipmentForm);
			setShipmentContents(shipmentForm);
		}
	}

	/**
	 * sets the shipment contents.
	 * @param shipmentForm object of BaseShipmentForm class.
	 */
	protected void setShipmentContents(BaseShipmentForm shipmentForm)
	{
		Collection<StorageContainer> updatedContainerCollection = new HashSet<StorageContainer>();
		//Call to super class's method to set information related to populate container info
		populateContainerContents(shipmentForm, updatedContainerCollection);
		if (!shipmentForm.isAddOperation())
		{
			this.containerCollection.clear();
			this.containerCollection.addAll(updatedContainerCollection);
		}
		// Populate the specimenCollection
		populateSpecimenCollection(shipmentForm);
	}

	/**
	 * populates specimen collection.
	 * @param shipmentForm form containing all values.
	 */
	private void populateSpecimenCollection(BaseShipmentForm shipmentForm)
	{
		int specimenCount = shipmentForm.getSpecimenCounter();
		String fieldValue = "";
		boolean containsSpecimens = false;
		Specimen specimen = null;
		int numOfSpecimens = 0;
		this.specimenCollection.clear();
		if (specimenCount > 0)
		{
			for (int specimenCounter = 1; specimenCounter <= specimenCount; specimenCounter++)
			{
				fieldValue = (String) shipmentForm.getSpecimenDetails("specimenLabel_"
						+ specimenCounter);
				if (fieldValue != null && !fieldValue.trim().equals(""))
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
					this.specimenCollection.add(specimen);
				}
			}
		}
	}

	/**
	 * sets the basic shipment request properties.
	 * @param shipmentForm form object containing all values.
	 * @throws AssignDataException if some assignment operation fails.
	 */
	protected void setBasicShipmentRequestProperties(BaseShipmentForm shipmentForm)
			throws AssignDataException
	{
		if (shipmentForm.getId() != 0l)
		{
			this.id = shipmentForm.getId();
		}
		this.senderComments = shipmentForm.getSenderComments();
		this.senderSite = createSitObject(shipmentForm.getSenderSiteId());
		this.label = shipmentForm.getLabel();
		if (shipmentForm.getActivityStatus() != null
				&& !shipmentForm.getActivityStatus().trim().equals(""))
		{
			this.activityStatus = shipmentForm.getActivityStatus();
		}
		try
		{
			if (shipmentForm.getSendDate() != null
					&& shipmentForm.getSendDate().trim().length() != 0)
			{
				Calendar calendar = Calendar.getInstance();
				Date date;
				date = Utility.parseDate(shipmentForm.getSendDate(), Utility
						.datePattern(shipmentForm.getSendDate()));
				calendar.setTime(date);
				if (shipmentForm.getSendTimeHour() != null
						&& !shipmentForm.getSendTimeHour().trim().equals(""))
				{
					calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(shipmentForm
							.getSendTimeHour()));
				}
				if (shipmentForm.getSendTimeMinutes() != null
						&& !shipmentForm.getSendTimeMinutes().trim().equals(""))
				{
					calendar.set(Calendar.MINUTE, Integer.parseInt(shipmentForm
							.getSendTimeMinutes()));
				}
				this.sendDate = calendar.getTime();
			}
		}
		catch (ParseException e)
		{
			Logger.out.error(e.getMessage());
			throw new AssignDataException(ErrorKey.getErrorKey("errors.item"), e, "item missing");
		}
	}
}

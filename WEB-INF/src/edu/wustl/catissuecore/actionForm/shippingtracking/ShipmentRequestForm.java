/**
 * <p>Title:ShipmentRequestForm Class</p>
 * <p>Description:ShipmentForm class is the subclass of he BaseShipmentForm bean classes. </p>
 * Copyright: Copyright (c) 2008
 * Company:
 * @author vijay_chittem
 * @version 1.00
 * Created on July 16, 2008
 */

package edu.wustl.catissuecore.actionForm.shippingtracking;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.shippingtracking.ShipmentRequest;
import edu.wustl.catissuecore.util.shippingtracking.Constants;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;

/**
 * this class holds the form values for shipment request.
 */
public class ShipmentRequestForm extends BaseShipmentForm
{

	/**
	 *
	 */
	private static final long	serialVersionUID	= 1L;

	/**
	 * Returns the id assigned to form bean.
	 * @return shipment request form id.
	 */
	@Override
	public int getFormId()
	{
		return Constants.SHIPMENT_REQUEST_FORM_ID;
	}

	/**
	 * this method sets all the values.
	 * @param arg0 domain object to set.
	 */
	public void setAllValues(AbstractDomainObject arg0)
	{
		if (arg0 instanceof ShipmentRequest)
		{
			ShipmentRequest shipmentRequest = (ShipmentRequest) arg0;
			populateBasicShipmentProperties(shipmentRequest);
			populateShipmentRequestContents(shipmentRequest);
		}
	}

	/**
	 * populates the shipment request contents.
	 * @param shipmentRequest object of ShipmentRequest class.
	 */
	protected void populateShipmentRequestContents(ShipmentRequest shipmentRequest)
	{
		populateContentContainers(shipmentRequest);
		populateContentSpecimens(shipmentRequest);
	}

	/**
	 * populates the specimen contents.
	 * @param shipmentRequest object of ShipmentRequest class.
	 */
	private void populateContentSpecimens(ShipmentRequest shipmentRequest)
	{
		this.specimenLabelChoice = "SpecimenLabel";
		this.specimenCounter = 0;
		if (shipmentRequest.getSpecimenCollection() != null)
		{
			Iterator < Specimen > specimenIterator = shipmentRequest.getSpecimenCollection()
					.iterator();
			while (specimenIterator.hasNext())
			{
				Specimen specimen = specimenIterator.next();
				if (specimen != null && specimen.getLabel() != null)
				{
					this.specimenCounter++;
					this.specimenDetailsMap.put("specimenLabel_" + specimenCounter, specimen
							.getLabel());
					this.specimenDetailsMap.put("specimenBarcode_" + specimenCounter, specimen
							.getBarcode());
				}
			}
		}
	}

	/**
	 * populates the containers in the shipment.
	 * @param shipmentRequest object of ShipmentRequest class.
	 */
	private void populateContentContainers(ShipmentRequest shipmentRequest)
	{
		this.containerLabelChoice = "ContainerLabel";
		this.containerCounter = 0;
		if (shipmentRequest.getContainerCollection() != null)
		{
			Iterator < StorageContainer > containerIterator = shipmentRequest
					.getContainerCollection().iterator();
			while (containerIterator.hasNext())
			{
				StorageContainer container = containerIterator.next();
				if (container != null && container.getName() != null)
				{
					this.containerCounter++;
					this.containerDetailsMap.put("containerLabel_" + containerCounter, container
							.getName());
					this.containerDetailsMap.put("containerBarcode_" + containerCounter, container
							.getBarcode());
				}
			}
		}
	}

	/**
	 * this method calidates the action form.
	 * @param mapping ActionMapping object.
	 * @param request HttpServletRequest object.
	 * @return errors object of ActionErrors class.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		validateBasicShipmentRequestInfo(errors);
		validateShipmentContentDetails(errors);
		return errors;
	}

	/**
	 * Resets the values of all the fields. Is called by the overridden reset
	 * method defined in ActionForm.
	 * */
	/*
	 * protected void reset() { this.activityStatus=""; this.containerCounter=0;
	 * this.containerDetailsMap=new HashMap(); this.createdDate=""; this.id=0l;
	 * this.label=""; // this.operation=""; this.receiverComments="";
	 * this.receiverSiteId=0l; this.sendDate=""; this.senderComments="";
	 * this.senderContactId=0l; this.senderSiteId=0l; this.specimenCounter=0;
	 * this.specimenDetailsMap=new HashMap(); }
	 */
	/**
	 * validates shipment request.
	 * @param errors object of ActionErrors class.
	 */
	protected void validateBasicShipmentRequestInfo(ActionErrors errors)
	{
		Validator validator = new Validator();

		String dateErrorString = validator.validateDate(this.sendDate, false);
		if (validator.isEmpty(this.label))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
					ApplicationProperties.getValue("shipment.label")));
		}

		// checking for empty requesterSiteId (default = 0 is empty)
		if (this.senderSiteId == 0)
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
					ApplicationProperties.getValue("shipment.request.requesterSite")));
		}

		if (dateErrorString != null && !dateErrorString.trim().equals(""))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(dateErrorString,
					ApplicationProperties.getValue("shipment.sendDate")));
		}
		if (!validator.isValidOption("" + (this.senderSiteId)))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
					ApplicationProperties.getValue("shipment.senderSite")));
		}
	}

}

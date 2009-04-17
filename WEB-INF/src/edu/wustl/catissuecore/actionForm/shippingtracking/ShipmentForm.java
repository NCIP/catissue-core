/**
 * <p>Title:ShipmentForm Class</p>
 * <p>Description:ShipmentForm class is the subclass of he BaseShipmentForm bean classes. </p>
 * Copyright: Copyright (c) 2008
 * Company:
 * @author vijay_chittem
 * @version 1.00
 * Created on July 16, 2008
 */
package edu.wustl.catissuecore.actionForm.shippingtracking;
import edu.wustl.catissuecore.domain.shippingtracking.Shipment;
import edu.wustl.catissuecore.util.shippingtracking.Constants;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.XMLPropertyHandler;

/**
 * CreateNewShipment Form.
 * CreateNewShipmentForm contains all the common fields in BaseshipmentForm
 **/
public class ShipmentForm extends BaseShipmentForm
{
	/**
	 * variable containing id of the shipment request.
	 */
	protected long shipmentRequestId=0;
	/**
	 * Serializable class requires Serial version id.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Shipment/Container Bar code.
	 */
	private String barcode;
	/**
	 * Gets the shipment barcode.
	 * @return the bar code
	 * @see #setBarcode(String)
	 */
	public String getBarcode()
	{
		return barcode;
	}
	/**
	 * The string to determine if barcode field is editable.
	 */
	private String isBarcodeEditable = XMLPropertyHandler.getValue(Constants.IS_BARCODE_EDITABLE);
	/**
	 * checks the editable property of the barcode.
	 * @return isBarcodeEditable representing the string containing the editable check.
	 */
	public String getIsBarcodeEditable()
	{
		return isBarcodeEditable;
	}
	/**
	 * sets the barcode editable check.
	 * @param isBarcodeEditable string containing the check on editable property.
	 */
	public void setIsBarcodeEditable(String isBarcodeEditable)
	{
		this.isBarcodeEditable = isBarcodeEditable;
	}

	/**
	 * Sets Shipment/Container Bar code.
	 * @param barcode the bar code to set
	 * @see #getBarcode()
	 */
	public void setBarcode(String barcode)
	{
		this.barcode = barcode;
	}

	/**
	 * Returns the id assigned to form bean.
	 * @return shipment form id.
	 */
	@Override
	public int getFormId()
	{
		return Constants.SHIPMENT_FORM_ID;
	}
	/**
     * Resets the values of all the fields.
     * Is called by the overridden reset method defined in ActionForm.
     * */
	@Override
	protected void reset()
	{
		this.barcode = null;
	}
	/**
	 * this method overrides the corresponding method in the base class.
	 * @param abstractDomainObject the domain object.
	 */
	@Override
	public void setAllValues(AbstractDomainObject abstractDomainObject)
	{
		super.setAllValues(abstractDomainObject);
		if(abstractDomainObject instanceof Shipment)
		{
			Shipment shipment=(Shipment)abstractDomainObject;
			this.barcode=shipment.getBarcode();
			if(shipment.getShipmentRequest()!=null && shipment.getShipmentRequest().getId()!=null)
			{
				this.shipmentRequestId=shipment.getShipmentRequest().getId();
			}
		}
	}
	/**
	 * gets the shipment request id.
	 * @return shipmentRequestId the id of shipment request.
	 */
	public long getShipmentRequestId()
	{
		return shipmentRequestId;
	}
	/**
	 * sets the shipment request id.
	 * @param shipmentRequestId the id to set.
	 */
	public void setShipmentRequestId(long shipmentRequestId)
	{
		this.shipmentRequestId = shipmentRequestId;
	}
}

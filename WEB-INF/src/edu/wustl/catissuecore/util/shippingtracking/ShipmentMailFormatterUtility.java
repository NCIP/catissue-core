/**
 * <p>Title: ShipmentMailFormatterUtility </p>
 * <p>Description: Utility contains various mail formatting.</p>
 * Copyright:    Copyright (c) year
 * Company:
 * @author nilesh_ghone
 * @version 1.00
 */

package edu.wustl.catissuecore.util.shippingtracking;

import edu.wustl.catissuecore.domain.shippingtracking.Shipment;
import edu.wustl.catissuecore.domain.shippingtracking.ShipmentRequest;

/**
 *	this is a utility class for shipment mail formatter.
 */
public class ShipmentMailFormatterUtility
{

	/**
	 * Gets the shipment mail body.
	 * @param shipment created shipment
	 * @return shipment mail body.
	 */
	public static String formatCreateShipmentMailBody(Shipment shipment)
	{
		return "Shipment created";
	}

	/**
	 * Gets the shipment mail subject.
	 * @param shipment created shipment
	 * @return Shipment mail subject.
	 */
	public static String getCreateShipmentMailSubject(Shipment shipment)
	{
		//TODO - exact contents required
		return "Shipment created";
	}

	/**
	 * Gets the shipment received mail subject.
	 * @param shipment created shipment
	 * @return Shipment mail subject.
	 */
	public static String getShipmentReceivedMailSubject(Shipment shipment)
	{
		return "Shipment received";
	}

	/**
	 * Gets the shipment received mail body.
	 * @param shipment created shipment
	 * @return Shipment mail subject.
	 */
	public static String formatShipmentReceivedMailBody(Shipment shipment)
	{
		return "Shipment received";
	}

	/**
	 * Gets the shipment request mail body.
	 * @param shipmentRequest object of ShipmentRequest class.
	 * @return shipment request mail body
	 */
	public static String formatCreateShipmentRequestMailBody(ShipmentRequest shipmentRequest)
	{
		//TODO - exact contents required
		return "Shipment request created";
	}

	/**
	 * Gets the shipment request mail subject.
	 * @param shipmentRequest object of ShipmentRequest class.
	 * @return shipment request mail subject.
	 */
	public static String getCreateShipmentRequestMailSubject(ShipmentRequest shipmentRequest)
	{
		return "Shipment request created";
	}

	//bug 12816 start
	/**
	 * Gets the shipment request rejected mail body.
	 * @param shipmentRequest object of ShipmentRequest class.
	 * @return shipment request mail body
	 */
	public static String formatRejectShipmentRequestMailBody(ShipmentRequest shipmentRequest)
	{
		//TODO - exact contents required
		return "Shipment request rejected";
	}

	/**
	 * Gets the shipment request rejected mail subject.
	 * @param shipmentRequest object of ShipmentRequest class.
	 * @return shipment request mail subject.
	 */
	public static String getRejectShipmentRequestMailSubject(ShipmentRequest shipmentRequest)
	{
		return "Shipment request rejected";
	}
	//bug 12816 end
}

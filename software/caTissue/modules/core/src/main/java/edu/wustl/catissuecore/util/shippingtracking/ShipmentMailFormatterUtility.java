/**
 * <p>Title: ShipmentMailFormatterUtility </p>
 * <p>Description: Utility contains various mail formatting.</p>
 * Copyright:    Copyright (c) year
 * Company:
 * @author nilesh_ghone
 * @version 1.00
 */

package edu.wustl.catissuecore.util.shippingtracking;

import java.util.Collection;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
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
		StringBuffer bodyText=new StringBuffer();
		
		
		bodyText.append("Shipment Details:\n Shipment Label: "+shipment.getLabel()+"\nShipment Barcode: "+shipment.getBarcode());
		bodyText.append("\nSender Site: "+shipment.getSenderSite().getName()+"\nReceiver Site: "+shipment.getReceiverSite().getName());
		bodyText.append("\nSender :"+shipment.getSenderContactPerson().getFirstName()+","+shipment.getSenderContactPerson().getLastName()+"\nReceiver: "+shipment.getReceiverContactPerson().getFirstName()+","+shipment.getReceiverContactPerson().getLastName());
		bodyText.append("\nSent On :"+shipment.getSendDate()+"\nComments: "+shipment.getSenderComments());
		if(!shipment.getContainerCollection().isEmpty() && shipment.getContainerCollection()!=null)
		{
			bodyText.append("\n"+"Container Details:");
			
			bodyText.append("\n"+"Container Label: ");
			
			Collection<StorageContainer> storageContainerCollection=shipment.getContainerCollection();
			
			
			for(StorageContainer container:storageContainerCollection)
			{				
				bodyText.append(container.getName());
				
				Collection<SpecimenPosition> specimenPosCollection=container.getSpecimenPositionCollection();
				bodyText.append("\nSpecimen Label:");
				for(SpecimenPosition specimenPosition:specimenPosCollection)
				{
					bodyText.append("\n"+specimenPosition.getSpecimen().getLabel());
				}
			}
		}	
		return bodyText.toString();
	}

	/**
	 * Gets the shipment mail subject.
	 * @param shipment created shipment
	 * @return Shipment mail subject.
	 */
	public static String getCreateShipmentMailSubject(Shipment shipment)
	{
		//TODO - exact contents required
		return "New Shipment "+shipment.getLabel()+" created";
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
		StringBuffer mailBody= new StringBuffer();
		mailBody.append("\nShipment Request Label: "+shipmentRequest.getLabel());
		mailBody.append("\nRequester's Site: "+shipmentRequest.getSenderSite().getName());
		mailBody.append("\nSent On: "+shipmentRequest.getCreatedDate());
		mailBody.append("\nSender Comments: "+shipmentRequest.getSenderComments());
		if(!shipmentRequest.getContainerCollection().isEmpty() && shipmentRequest.getContainerCollection()!=null)
		{
			mailBody.append("\n"+"Container Details:");
			
			mailBody.append("\n"+"Container Label: ");
			Collection<StorageContainer> storageContainerCollection=shipmentRequest.getContainerCollection();
			for(StorageContainer container:storageContainerCollection)
			{
				mailBody.append("\n"+container.getName());
			}
		}
		Collection<Specimen> specimenCollection=shipmentRequest.getSpecimenCollection();
		if(!shipmentRequest.getSpecimenCollection().isEmpty())
		{
			mailBody.append("\nSpecimen Details:"+"\nSpecimen Label:");
			
			for(Specimen specimen:specimenCollection)
			{				
				mailBody.append("\n"+specimen.getLabel());
			}
		}
		
		
		
		return mailBody.toString();
	}

	/**
	 * Gets the shipment request mail subject.
	 * @param shipmentRequest object of ShipmentRequest class.
	 * @return shipment request mail subject.
	 */
	public static String getCreateShipmentRequestMailSubject(ShipmentRequest shipmentRequest)
	{
		return "New Shipment Request "+shipmentRequest.getLabel()+" created";
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

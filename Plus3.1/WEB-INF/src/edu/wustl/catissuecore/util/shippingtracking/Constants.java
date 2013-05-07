/**
 * <p>Title: Constants Class>
 * <p>Description:  This class stores the constants used in the operations in the application.</p>
 * Copyright:    Copyright (c) year
 * Company:
 * @author vijay_chittem
 * @version 1.00
 * Created on July 11, 2008
 */

package edu.wustl.catissuecore.util.shippingtracking;

/**
 * This class stores the constants used in the operations in the application.
 */
public class Constants
{

	// Identifiers for various Form beans
	public static final int DASHBOARD_FORM_ID = 1;
	public static final int ONE = 1;
	public static final int BASESHIPMENT_FORM_ID = 100;
	public static final int SHIPMENT_FORM_ID = 101;
	public static final int SHIPMENT_REQUEST_FORM_ID = 102;
	public static final int SHIPMENT_RECEIVING_FORM_ID = 103;
	// Name of list stored in request which contain possible sending sites
	// and their contact persons respectively
	public static final String SENDERS_SITE_LIST = "senderSiteList";
	public static final String SENDERS_LIST = "sendersList";
	// Name of list stored in request which contain possible recieving sites
	//and their contact persons respectively
	public static final String RECIEVERS_SITE_LIST = "recieverSiteList";
	public static final String RECIEVERS_LIST = "recievesList";
	// For Shipment Request
	public static final String REQUESTERS_SITE_LIST = "requesterSiteList";
	// Name of list stored in request which contains specimens part of the shipment
	public static final String SPECIMEN_LIST = "specimenList";

	// Name of list stored in request which contains containers part of the shipment
	public static final String CONTAINER_LIST = "containerList";

	// Activity status for shipment/request
	public static final String ACTIVITY_STATUS_IN_TRANSIT = "In Transit";
	public static final String ACTIVITY_STATUS_IN_PROGRESS = "In Progress";
	public static final String ACTIVITY_STATUS_RECEIVED = "Received";
	public static final String ACTIVITY_STATUS_REJECTED = "Rejected";
	public static final String ACTIVITY_STATUS_PROCESSED = "Processed";
	public static final String ACTIVITY_STATUS_DRAFTED = "Drafted";
	// Storage Type Name for Container created for storing specimens to be shipped
	public static final String SHIPMENT_CONTAINER_TYPE_NAME = "Shipment Container";
	// Site for Containers created for shipping specimens
	public static final String IN_TRANSIT_SITE_NAME = "In Transit";
	//Prefix of In Transit container
	public static final String IN_TRANSIT_CONTAINER_NAME_PREFIX = "In Transit Container ";
	//Property name for specimen label and barcode and container name and barcode
	public static final String SPECIMEN_PROPERTY_LABEL = "label";
	public static final String SPECIMEN_PROPERTY_BARCODE = "barcode";
	public static final String CONTAINER_PROPERTY_NAME = "name";
	public static final String CONTAINER_PROPERTY_BARCODE = "barcode";
	public static final String SHIPMENT_REQUEST_NAME_PREFIX = "Shipment_Request_";
	//Name of list stored in request which contains Id of RequestReceived
	//and ShipmentReceived and OutgoingShipment
	public static final String REQUEST_RECEIVED_ID = "requestId";
	public static final String SHIPMENT_RECEIVED_ID = "receivedShipmentId";
	public static final String OUTGOING_SHIPMENT_ID = "outgoingshipmentid";
	public static final String VIEW = "view";
	//Mapping constants
	public static final String VIEW_SHIPMENT = "viewShipment";
	public static final String EDIT_SHIPMENT = "editShipment";
	public static final String ACCEPT_OR_REJECT_SHIPMENT = "acceptOrRejectShipment";
	public static final String CREATE_SHIPMENT_FOR_REQUEST = "createShipmentForRequest";
	public static final String VIEW_SHIPMENT_REQUEST = "viewShipmentRequest";
	public static final String VIEW_NONEDITABLE_SHIPMENT_REQUEST = "viewNonEditableShipmentRequest";
	public static final String EDIT_SHIPMENT_REQUEST = "editShipmentRequest";
	public static final String CREATE_SHIPMENT = "createShipment";
	public static final String CREATE_SHIPMENT_REQUEST = "createShipmentRequest";

	// ShipmentReceiving status
	public static final String ACCEPT = "Accept";
	public static final String REJECT_AND_DESTROY = "Reject & Destroy";
	public static final String REJECT_AND_RESEND = "Reject & Return";
	public static final String STORAGE_LOCATION_VIRTUAL = "Virtual";
	public static final String STORAGE_LOCATION_AUTO = "Auto";
	public static final String STORAGE_LOCATION_MANUAL = "Manual";
	public static final String SHIPMENT = "Shipment";
	public static final String ADD_EDIT_SHIPMENT = "ADD_EDIT_SHIPMENT";
	public static final String NAME = "name";
	//For Shipping And Tracking summary print
	public static final String PACKING_SLIP_HEADER_FILE = "packingSlipHeaderJsp";
	public static final String PACKING_SLIP_FOOTER_FILE = "packingSlipFooterJsp";
	//Constants for barcode editable
	public static final String IS_BARCODE_EDITABLE = "barcode.isEditable";
}

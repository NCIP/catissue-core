
package edu.wustl.catissuecore.printservicemodule;

/**
 * This class contains Print Web Service Constants.
 * @author prashant_bandal
 *
 */
public final class PrintWebServiceConstants
{

	/**
	 * private constructor.
	 */
	private PrintWebServiceConstants()
	{

	}

	//Constants for keys in printer.properties file.
	/**
	 * Specify PRINTER_WEBSERVICE_CLIENT.
	 */
	public static final String PRINTER_WEBSERVICE_CLIENT = "printer.webServiceClient";
	/**
	 * Specify CAP_LABEL_FILENAME.
	 */
	public static final String CAP_LABEL_FILENAME = "cap.label.filename";
	/**
	 * Specify CAP_SIDE_LABEL_FILENAME.
	 */
	public static final String CAP_SIDE_LABEL_FILENAME = "cap.side.label.filename";
	/**
	 * Specify SIDE_LABEL_FILENAME.
	 */
	public static final String SIDE_LABEL_FILENAME = "side.label.filename";
	/**
	 * Specify PRNTER_DIRECTORY.
	 */
	public static final String PRNTER_DIRECTORY = "printer.directory";
	/**
	 * Specify RULES_INPUT_FILENAME.
	 */
	public static final String RULES_INPUT_FILENAME = "rules.input.filename";

	/**
	 * Specify INDEX_SPECIMEN_CLASS.
	 */
	public static final int INDEX_SPECIMEN_CLASS = 0;
	/**
	 * Specify INDEX_SPECIMEN_TYPE.
	 */
	public static final int INDEX_SPECIMEN_TYPE = 1;
	/**
	 * Specify INDEX_LABEL_FORMAT.
	 */
	public static final int INDEX_LABEL_FORMAT = 2;
	/**
	 * Specify INDEX_DATA_ON_LABEL.
	 */
	public static final int INDEX_DATA_ON_LABEL = 3;
	/**
	 * Specify INDEX_PRINTER.
	 */
	public static final int INDEX_PRINTER = 4;
	/**
	 * Specify INDEX_WORKSTAION_IP.
	 */
	public static final int INDEX_WORKSTAION_IP = 5;

	/**
	 * Specify ANY.
	 */
	public static final String ANY = "Any";
	/**
	 * Specify USER_IPADDRESS.
	 */
	public static final String USER_IPADDRESS = "IPAddress";
	/**
	 * Specify SPECIMEN_LABEL.
	 */
	public static final String SPECIMEN_LABEL = "Specimen Label";
	/**
	 * Specify SPECIMEN_CLASS.
	 */
	public static final String SPECIMEN_CLASS = "Specimen Class";
	/**
	 * Specify SPECIMEN_BARCODE.
	 */
	public static final String SPECIMEN_BARCODE = "Specimen Barcode";
	/**
	 * Specify SPECIMEN_BARCODE.
	 */
	public static final String SPECIMEN_TYPE = "Specimen Type";
	/**
	 * Specify PRINTER.
	 */
	public static final String PRINTER = "Printer";
	/**
	 * Specify WORKSTATION_IP.
	 */
	public static final String WORKSTATION_IP = "Workstation IP";
	/**
	 * Specify CONCENTRATION.
	 */
	public static final String CONCENTRATION = "Concentration";
	/**
	 * Specify QUANTITY.
	 */
	public static final String QUANTITY = "Quantity";
	/**
	 * Specify CP_TITLE.
	 */
	public static final String CP_TITLE = "CP Title";
	/**
	 * Specify PARTICIPANT_PROTOCOL_IDENTIFIER.
	 */
	public static final String PARTICIPANT_PROTOCOL_IDENTIFIER = "Participant Protocol Identifier";
	/**
	 * Specify SPECIMEN_TISSUE_SITE.
	 */
	public static final String SPECIMEN_TISSUE_SITE = "Tissue site";
	/**
	 * Specify PATHOLOGICAL_STATUS.
	 */
	public static final String PATHOLOGICAL_STATUS = "Pathological status";
	/**
	 * Specify LABEL QUANTITY.
	 */
	public static final String LABELQUANTITY = "LabelQuantity";
	/**
	 * Specify SPECIMEN_IDENTIFIER.
	 */
	public static final String SPECIMEN_IDENTIFIER = "Specimen Identifier";

	/**
	 * Specify SPECIMEN_COLLECTION_STATUS.
	 */
	public static final String SPECIMEN_COLLECTION_STATUS = "Collection Status";
	/**
	 * Specify SPECIMEN_CREATED_ON.
	 */
	public static final String SPECIMEN_CREATED_ON = "Created On";
	/**
	 * Specify SPECIMEN_LINEAGE.
	 */
	public static final String SPECIMEN_LINEAGE = "Lineage";
	/**
	 * Specify SPECIMEN_COMMENT.
	 */
	public static final String SPECIMEN_COMMENT = "Comment";
	/**
	 * Specify SPECIMEN_MESSAGE_LABEL.
	 */
	public static final String SPECIMEN_MESSAGE_LABEL = "Message Label";
	/**
	 * Specify SPECIMEN_STORAGE_CONTAINER_NAME.
	 */
	public static final String SPECIMEN_STORAGE_CONTAINER_NAME = "Storage Container";
	/**
	 * Specify SPECIMEN_POSITION_DIMENSION_ONE.
	 */
	public static final String SPECIMEN_POSITION_DIMENSION_ONE = "Position Dimension One";
	/**
	 * Specify POSITION_DIMENSION_TWO.
	 */
	public static final String SPECIMEN_POSITION_DIMENSION_TWO = "Position Dimension Two";

	/**
	 * Specify SIDE.
	 */
	public static final String SIDE = "Slide";
	/**
	 * Specify CAP.
	 */
	public static final String CAP = "Cap";
	/**
	 * Specify CAP_PLUS_SIDE.
	 */
	public static final String CAP_PLUS_SIDE = "Cap + Slide";

	/**
	 * Specify NEW LINE.
	 */
	public static final String NEWLINE = "\r\n";

	//constants for displaying the left hand side field names in the label printing
	/**
	 * Specify LABEL NAME.
	 */
	public static final String DISPLAY_LABELNAME = "labelName";
	/**
	 * Specify SPECIMEN_LABEL.
	 */
	public static final String DISPLAY_SPECIMEN_LABEL = "specimen.label";
	/**
	 * Specify SPECIMEN_CLASS.
	 */
	public static final String DISPLAY_SPECIMEN_CLASS = "specimen.class";
	/**
	 * Specify SPECIMEN_BARCODE.
	 */
	public static final String DISPLAY_SPECIMEN_BARCODE = "specimen.barcode";
	/**
	 * Specify SPECIMEN_TYPE.
	 */
	public static final String DISPLAY_SPECIMEN_TYPE = "specimen.type";
	/**
	 * Specify DISPLAY_WORKSTATION_IP.
	 */
	public static final String DISPLAY_WORKSTATION_IP = "workstation.IP";
	/**
	 * Specify DISPLAY_CONCENTRATION.
	 */
	public static final String DISPLAY_CONCENTRATION = "specimen.concentration";
	/**
	 * Specify DISPLAY_QUANTITY.
	 */
	public static final String DISPLAY_QUANTITY = "specimen.quantity";
	/**
	 * Specify DISPLAY_CP_TITLE.
	 */
	public static final String DISPLAY_CP_TITLE = "collectionProtocol.title";
	/**
	 * Specify DISPLAY_PARTICIPANT_PROTOCOL_IDENTIFIER.
	 */
	public static final String DISPLAY_PARTICIPANT_PROTOCOL_IDENTIFIER = "cpr.PPI";
	/**
	 * Specify specimen.tissueSite.
	 */
	public static final String DISPLAY_SPECIMEN_TISSUE_SITE = "specimen.tissueSite";
	/**
	 * Specify specimen.pathologicalStatus.
	 */
	public static final String DISPLAY_PATHOLOGICAL_STATUS = "specimen.pathologicalStatus";
	/**
	 * Specify specimen.collectionStatus.
	 */
	public static final String DISPLAY_SPECIMEN_COLLECTION_STATUS = "specimen.collectionStatus";
	/**
	 * Specify specimen.createdOn.
	 */
	public static final String DISPLAY_SPECIMEN_CREATED_ON = "specimen.createdOn";
	/**
	 * Specify specimen.lineage.
	 */
	public static final String DISPLAY_SPECIMEN_LINEAGE = "specimen.lineage";
	/**
	 * Specify specimen.comment.
	 */
	public static final String DISPLAY_SPECIMEN_COMMENT = "specimen.comment";
	/**
	 * Specify specimen.messageLabel.
	 */
	public static final String DISPLAY_SPECIMEN_MESSAGE_LABEL = "specimen.messageLabel";
	/**
	 * Specify specimen.storageContainerName.
	 */
	public static final String DISPLAY_SPECIMEN_STORAGE_CONTAINER_NAME = "specimen.storageContainerName";
	/**
	 * Specify printer.
	 */
	public static final String DISPLAY_PRINTER = "printer";
	/**
	 * Specify label Quantity.
	 */
	public static final String DISPLAY_LABELQUANTITY = "labelQuantity";
	/**
	 * Specify END.
	 */
	public static final String DISPLAY_END = "END";

}

package com.krishagni.catissueplus.core.printer.printService.factory;


public enum DisplayPrintLabelType {
	LABEL ("LABEL"), 
	CLASS ("Class"), 
	BARCODE ("Barcode"), 
	TISSUE_SITE ("TissueSite"),
	CONCENTRATION ("Concentration"), 
	QUANTITY ("Quantity"), 
	PATHOLOGICAL_STATUS ("PathologicalStatus"), 
	COLLECTION_STATUS ("CollectionStatus"), 
	CREATED_ON ("CreatedOn"), 
	LINEAGE ("Lineage"), 
	TYPE ("Type"),
	COMMENT ("Comment"),
	DISPLAY_PRINTER ("Printer"),
  WORKSTATION_IP("WorkstationIP"),
  DISPLAY_LABELQUANTITY ("LabelQuantity"),
	LABEL_NAME ("LABELNAME"), 
	IDENTIFIER("Id"),
	STORAGE_CONTAINER_NAME("StorageContainer"), 
	POSITION_DIMENSION_ONE("Position One Dimension"),
	POSITION_DIMENSION_TWO("Position TWO Dimension"), 
	CP_TITLE("CP Short Title"), 
	PARTICIPANT_PROTOCOL_IDENTIFIER("PPI");

	public String value;

	private DisplayPrintLabelType(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

}


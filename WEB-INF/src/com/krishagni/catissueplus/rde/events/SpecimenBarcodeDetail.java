package com.krishagni.catissueplus.rde.events;

public class SpecimenBarcodeDetail extends VisitBarcodeDetail {
	private String containerName;
	
	private String column;
	
	private String row;

	public String getContainerName() {
		return containerName;
	}

	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getRow() {
		return row;
	}

	public void setRow(String row) {
		this.row = row;
	}
}

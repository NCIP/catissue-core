package com.krishagni.openspecimen.rde.events;

import java.util.Date;

public class VisitRegDetail {
	private String barcode;
	
	private Date visitDate;

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}
}

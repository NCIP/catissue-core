package com.krishagni.openspecimen.rde.events;

import java.util.ArrayList;
import java.util.List;

public class BarcodeDetail {
	private String barcode;
	
	private List<BarcodePartDetail> parts = new ArrayList<>();
	
	private boolean erroneous;

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public List<BarcodePartDetail> getParts() {
		return parts;
	}

	public void setParts(List<BarcodePartDetail> parts) {
		this.parts = parts;
		
		for (BarcodePartDetail part : parts) {
			if (part.getErrorCode() != null) {
				erroneous = true;
				break;
			}
		}
	}

	public boolean isErroneous() {
		return erroneous;
	}

	public void setErroneous(boolean erroneous) {
		this.erroneous = erroneous;
	}
}

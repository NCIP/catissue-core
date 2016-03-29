package com.krishagni.catissueplus.rde.services;

import java.util.List;

import com.krishagni.catissueplus.rde.tokens.BarcodePart;

public interface BarcodeParser {
	public List<BarcodePart> parseVisitBarcode(String barcode);
}

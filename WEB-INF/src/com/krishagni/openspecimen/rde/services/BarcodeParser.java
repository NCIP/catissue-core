package com.krishagni.openspecimen.rde.services;

import java.util.List;

import com.krishagni.openspecimen.rde.tokens.BarcodePart;

public interface BarcodeParser {
	public List<BarcodePart> parseVisitBarcode(String barcode);
}


package com.krishagni.catissueplus.core.barcodegenerator;

public interface BarcodeGenerator<T> {

	public String generateBarcode(String barcodeFormat, T object);

}

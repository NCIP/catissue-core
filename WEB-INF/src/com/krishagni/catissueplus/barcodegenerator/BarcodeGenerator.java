
package com.krishagni.catissueplus.barcodegenerator;

public interface BarcodeGenerator<T> {

	public String generateBarcode(String barcodeFormat, T object);

}

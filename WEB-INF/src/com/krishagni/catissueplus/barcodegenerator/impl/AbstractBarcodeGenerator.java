
package com.krishagni.catissueplus.barcodegenerator.impl;

import java.util.StringTokenizer;

import com.krishagni.catissueplus.barcodegenerator.BarcodeGenerator;
import com.krishagni.catissueplus.tokens.factory.TokenFactory;

public class AbstractBarcodeGenerator<T> implements BarcodeGenerator<T> {

	private TokenFactory tokenFactory;

	public void setTokenFactory(TokenFactory tokenFactory) {
		this.tokenFactory = tokenFactory;
	}

	@Override
	public String generateBarcode(String barcodeFormat, T object) {
		StringTokenizer tokens = new StringTokenizer(barcodeFormat, "%");
		StringBuffer barcode = new StringBuffer();
		while (tokens.hasMoreElements()) {
			String tokenKey = (String) tokens.nextElement();
			barcode.append(tokenFactory.getTokenValue(tokenKey, object));
		}
		return barcode.toString();
	}

}

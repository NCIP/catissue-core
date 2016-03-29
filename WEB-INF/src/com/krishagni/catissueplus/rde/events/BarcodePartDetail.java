package com.krishagni.catissueplus.rde.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.rde.tokens.BarcodePart;

public class BarcodePartDetail {
	private String token;
	
	private String caption;

	private String code;
	
	private String displayValue;
	
	private String errorCode;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDisplayValue() {
		return displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public static BarcodePartDetail from(BarcodePart part) {
		BarcodePartDetail result = new BarcodePartDetail();
		result.setToken(part.getToken());
		result.setCaption(part.getCaption());
		result.setCode(part.getCode());
		result.setDisplayValue(part.getDisplayValue());
		result.setErrorCode(part.getValue() == null ? "INVALID" : null);
		return result;
	}
	
	public static List<BarcodePartDetail> from(List<BarcodePart> parts) {
		List<BarcodePartDetail> result = new ArrayList<BarcodePartDetail>();
		for (BarcodePart part : parts) {
			result.add(BarcodePartDetail.from(part));
		}
		
		return result;
	}
}

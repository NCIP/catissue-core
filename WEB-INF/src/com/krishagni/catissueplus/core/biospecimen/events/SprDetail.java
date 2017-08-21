package com.krishagni.catissueplus.core.biospecimen.events;

public class SprDetail extends FileDetail {
	private String sprText;

	public String getSprText() {
		return sprText;
	}

	public void setSprText(String sprText) {
		this.sprText = sprText;
	}
	
	public boolean isTextContent() {
		return getContentType().startsWith("text/");
	}
	
	public boolean isPdfContent() {
		return getContentType().equals("application/pdf");
	}
	
}

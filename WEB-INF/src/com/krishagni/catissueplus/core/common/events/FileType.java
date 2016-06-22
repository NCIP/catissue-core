package com.krishagni.catissueplus.core.common.events;

public enum FileType {
	TEXT("text"),

	PDF("pdf");
	
	private String type;
	
	private FileType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public static FileType fromType(String type) {
		for (FileType ft : FileType.values()) {
			if (type.equalsIgnoreCase(ft.type)) {
				return ft;
			}
		}
		
		return null;
	}
}

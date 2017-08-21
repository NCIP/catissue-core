package com.krishagni.catissueplus.core.de.events;

public enum FormType {
	DATA_ENTRY_FORMS("DataEntry"),
	
	PARTICIPANT_FORMS("Participant"),
	
	VISIT_FORMS("SpecimenCollectionGroup"),
	
	SPECIMEN_FORMS("Specimen"),
	
	SPECIMEN_EVENT_FORMS("SpecimenEvent"),

	SITE_EXTN_FORM("SiteExtension"),
	
	CP_EXTN_FORM("CollectionProtocolExtension"),
	
	QUERY_FORMS("Query");
	
	private String type;
	
	private FormType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public static FormType fromType(String type) {
		for (FormType ft : FormType.values()) {
			if (type.equalsIgnoreCase(ft.type)) {
				return ft;
			}
		}
		
		return null;
	}
}

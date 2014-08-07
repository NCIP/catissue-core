package com.krishagni.catissueplus.core.privileges;



public enum PrivilegeType {

	ACCESS (0,"ACCESS"),
	ASSIGN_READ (1,"ASSIGN_READ"),
	ASSIGN_USE (2,"ASSIGN_USE"),
	CREATE (3,"CREATE"),
	DEFINE_ANNOTATION (4,"DEFINE_ANNOTATION"),
	DELETE (5,"DELETE"),
	DISTRIBUTION (6,"DISTRIBUTION"),
	EXECUTE (7,"EXECUTE"),
	GENERAL_ADMINISTRATION (8,"GENERAL_ADMINISTRATION"),
	GENERAL_SITE_ADMINISTRATION (9,"GENERAL_SITE_ADMINISTRATION"),
	IDENTIFIED_DATA_ACCESS (10,"IDENTIFIED_DATA_ACCESS"),
	PARTICIPANT_SCG_ANNOTATION (11,"PARTICIPANT_SCG_ANNOTATION"),
	PHI_ACCESS (12,"PHI_ACCESS"),
	PROTOCOL_ADMINISTRATION (13,"PROTOCOL_ADMINISTRATION"),
	QUERY (14,"QUERY"),
	READ (15,"READ"),
	READ_DENIED (16,"READ_DENIED"),
	REGISTRATION (17,"REGISTRATION"),
	REPOSITORY_ADMINISTRATION (18,"REPOSITORY_ADMINISTRATION"),
	SHIPMENT_PROCESSING (19,"SHIPMENT_PROCESSING"),
	SPECIMEN_ACCESSION (20,"SPECIMEN_ACCESSION"),
	SPECIMEN_ANNOTATION (21,"SPECIMEN_ANNOTATION"),
	SPECIMEN_PROCESSING (22,"SPECIMEN_PROCESSING"),
	SPECIMEN_STORAGE (23,"SPECIMEN_STORAGE"),
	STORAGE_ADMINISTRATION (24,"STORAGE_ADMINISTRATION"),
	UPDATE  (25,"UPDATE"),
	USE  (26,"USE"),
	USER_PROVISIONING  (27,"USER_PROVISIONING"),
	WRITE  (28,"WRITE");

	public String value;
	public int srNumber;
	

	private PrivilegeType(int srNumber, String value) {
		this.value = value;
		this.srNumber = srNumber;
	}

	public String value() {
		return this.value;
	}
	
	public static String getEnumNameForValue(String privType){
		String enumValue = null;
		for (PrivilegeType type : PrivilegeType.values()) {
			if( type.value.equalsIgnoreCase(privType)){
				return type.name();
			}
		}
		return enumValue;
	}
	
	public static Boolean isValidPrivilegeType (String privType) {
		for (PrivilegeType type : PrivilegeType.values()) {
			if (type.value().equalsIgnoreCase(privType)) {
				return true;
			}
		}
		return false;
	}
}



package com.krishagni.catissueplus.core.privileges.domain.factory;

public enum PrivilegeType {

	USER_PROVISIONING(0,"User Provisioning"), 
	STORAGE_ADMINSTRATION(1,"Storage Administration"), 
  PROTOCOL_ADMINSTRATION(2,"Protocol Administration"), 
	SPECIMEN_PROCESSING(3,"Specimen Processing"), 
	REGISTRATION(4,"Registration"),
	DISTRIBUTION(5,"Distribution"),
	READ_DENIED(6,"Read Denied");

	public String value;
	public int srNumber;
	

	private PrivilegeType(int srNumber, String value) {
		this.value = value;
		this.srNumber = srNumber;
	}

	public String value() {
		return value;
	}

	public static Boolean isValidPrivilegeType (String privType) {
		for (PrivilegeType type : PrivilegeType.values()) {
			if (type.value.equalsIgnoreCase(privType)) {
				return true;
			}
		}
		System.out.println("dds");
		return false;
	}
}

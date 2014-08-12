
package com.krishagni.catissueplus.core.administrative.domain.factory;

public enum ContainerLabelSchemeType {
	NUMBERS(0, "Numbers"), ALPHABETS_UPPER(1, "ALPHABETS_UPPER"), ALPHABETS_LOWER(2, "ALPHABETS_LOWER"), ROMAN_UPPER(3,
			"ROMAN_UPPER"), ROMAN_LOWER(4, "ROMAN_LOWER");

	public String value;

	public int srNumber;

	private ContainerLabelSchemeType(int srNumber, String value) {
		this.value = value;
		this.srNumber = srNumber;
	}

	public String value() {
		return this.value;
	}

	public static String getEnumNameForValue(String labelShemeType) {
		String enumValue = null;
		for (ContainerLabelSchemeType type : ContainerLabelSchemeType.values()) {
			if (type.value.equalsIgnoreCase(labelShemeType)) {
				return type.name();
			}
		}
		return enumValue;
	}

	/*public static Boolean isValidContainerLabelSchemeType(String labelShemeType) {
		for (ContainerLabelSchemeType type : ContainerLabelSchemeType.values()) {
			if (type.value().equalsIgnoreCase(labelShemeType)) {
				return true;
			}
		}
		return false;
	}*/
}
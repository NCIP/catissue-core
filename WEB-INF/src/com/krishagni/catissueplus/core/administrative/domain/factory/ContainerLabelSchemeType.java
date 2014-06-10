package com.krishagni.catissueplus.core.administrative.domain.factory;


public enum ContainerLabelSchemeType {
	NUMBERS (0, "Numbers"),
	ALPHABATES_UPPER_CASE (1, "Alphabates Upper Case"),
	ALPHABATES_LOWER_CASE (2, "Alphabates Lower Case"),
	ROMAN_UPPER_CASE (3, "Roman Upper Case"),
	ROMAN_LOWER_CASE (4, "Roman Lower Case");

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

	public static Boolean isValidContainerLabelSchemeType(String labelShemeType) {
		for (ContainerLabelSchemeType type : ContainerLabelSchemeType.values()) {
			if (type.value().equalsIgnoreCase(labelShemeType)) {
				return true;
			}
		}
		return false;
	}
}
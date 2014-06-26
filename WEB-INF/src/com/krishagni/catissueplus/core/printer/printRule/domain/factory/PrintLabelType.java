package com.krishagni.catissueplus.core.printer.printRule.domain.factory;


public enum PrintLabelType {
	CAP_SLIDE(0, "CAP+SLIDE"), SLIDE(1, "SLIDE");

	public String value;

	public int srNumber;

	private PrintLabelType(int srNumber, String value) {
		this.value = value;
		this.srNumber = srNumber;
	}

	public String value() {
		return this.value;
	}

	public static String getEnumNameForValue(String printLabelType) {
		String enumValue = null;
		for (PrintLabelType type : PrintLabelType.values()) {
			if (type.value.equalsIgnoreCase(printLabelType)) {
				return type.name();
			}
		}
		return enumValue;
	}

	public static Boolean isValidPrintLabelType(String printLabelType) {
		for (PrintLabelType type : PrintLabelType.values()) {
			if (type.value().equalsIgnoreCase(printLabelType)) {
				return true;
			}
		}
		return false;
	}
}

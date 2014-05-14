
package com.krishagni.catissueplus.core.biospecimen.util.impl;

import com.krishagni.catissueplus.core.biospecimen.util.PpidGenerator;

public class PpidGeneratorImpl implements PpidGenerator {

	public String generatePpid(String ppidFormat, Long updatedValue) {

		int ppidFormatEndIndex = ppidFormat.indexOf("\"", 1) + 1;
		String ppidValue = ppidFormat.substring(0, ppidFormatEndIndex).trim().replace("\"", "");

		Long digitValueCount = Long.parseLong(getDigitValueCount(ppidValue));

		StringBuffer digits = new StringBuffer();
		char[] updatedValueArray = updatedValue.toString().toCharArray();
		StringBuffer ppidMiddleValue = getPpidMiddleValue(updatedValueArray.length, digitValueCount, digits, updatedValue);

		StringBuffer tokenValue = new StringBuffer();
		int ppidMiddleValueStartIndex = ppidValue.indexOf("%");
		int ppidMiddleValueEndIndex = ppidValue.indexOf("d");
		String ppidStartValue = ppidValue.substring(0, ppidMiddleValueStartIndex);
		String ppidEndValue = ppidValue.substring(ppidMiddleValueEndIndex + 1, ppidValue.length());
		tokenValue.append(ppidStartValue);
		tokenValue.append(ppidMiddleValue);
		tokenValue.append(ppidEndValue);
		return tokenValue.toString();
	}

	private static String getDigitValueCount(String format) {
		int beginIndex = format.indexOf("%");
		int endIndex = format.indexOf("d");
		String value = format.substring(beginIndex + 1, endIndex);

		return value;
	}

	private static StringBuffer getPpidMiddleValue(int length, Long digitValueCount, StringBuffer digits,
			Long updatedValue) {
		StringBuffer ppidMiddleValue = new StringBuffer();
		if (length >= digitValueCount) {
			ppidMiddleValue.append(updatedValue);
		}
		else {

			for (int i = 0; i < digitValueCount - length; i++) {
				digits.append("0");
			}
			ppidMiddleValue.append(digits);
			ppidMiddleValue.append(updatedValue);
		}
		return ppidMiddleValue;

	}

}

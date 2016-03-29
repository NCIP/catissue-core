package com.krishagni.catissueplus.rde.tokens.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.catissueplus.rde.tokens.BarcodePart;
import com.krishagni.catissueplus.rde.tokens.BarcodeToken;

public class VisitYearBarcodeToken extends AbstractLabelTmplToken implements BarcodeToken {
	@Override
	public String getName() {
		return "YR_OF_VISIT";
	}

	@Override
	public String getReplacement(Object object) {
		return String.valueOf(getYearOfVisit(object));
	}

	@Override
	public BarcodePart parse(Map<String, Object> contextMap, String input, int startIdx, String... args) {
		return parseYear(input, startIdx, 4);
	}

	protected int getYearOfVisit(Object object) {
		Date visitDate = null;
		if (object instanceof Visit) {
			visitDate = ((Visit) object).getVisitDate();
		} else if (object instanceof Specimen) {
			visitDate = ((Specimen) object).getVisit().getVisitDate();
		} else {
			throw new RuntimeException("Unknown object type: " + object != null ? object.getClass().getName() : "null");
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(visitDate);
		return cal.get(Calendar.YEAR);
	}

	protected BarcodePart parseYear(String input, int startIdx, int numDigits) {
		String[] parts = input.substring(startIdx).split("-", 2);
		String yearStr = parts[0];

		BarcodePart result = new BarcodePart();
		result.setToken(getName());
		result.setStartIdx(startIdx);
		result.setEndIdx(startIdx + yearStr.length());
		result.setCode(yearStr);

		if (StringUtils.isBlank(yearStr) || yearStr.length() != numDigits) {
			return result;
		}

		int year = 0;
		try {
			year = Integer.parseInt(yearStr);
		} catch (NumberFormatException e) {
			return result;
		}

		if (yearStr.length() == 2) {
			yearStr = String.valueOf(year + 2000);
		}

		result.setValue(yearStr);
		result.setDisplayValue(yearStr);
		return result;
	}
}

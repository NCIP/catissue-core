package com.krishagni.openspecimen.rde.tokens.impl;

import java.util.Map;

import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;
import com.krishagni.catissueplus.core.common.domain.ExtensionPvAttrLabelToken;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.openspecimen.rde.tokens.BarcodePart;
import com.krishagni.openspecimen.rde.tokens.BarcodeToken;

public class ExtensionPvAttrBarcodeToken extends ExtensionPvAttrLabelToken implements BarcodeToken {

	@Override
	public BarcodePart parse(Map<String, Object> contextMap, String input,	int startIdx, String ... args) {
		if (args == null || args.length != 1) {
			throw OpenSpecimenException.serverError(new IllegalArgumentException("Invalid arguments to EXTN_PV"));
		}

		String[] parts = input.substring(startIdx).split("-", 2);
		String code = parts[0];

		String pvAttr = args[0];
		BarcodePart result = new BarcodePart();
		result.setToken("$extn." + pvAttr);
		result.setStartIdx(startIdx);
		result.setEndIdx(startIdx + code.length());
		result.setCode(code);

		PermissibleValue pv = getDaoFactory().getPermissibleValueDao().getByConceptCode(pvAttr, code);
		result.setValue(pv);
		if (pv != null) {
			result.setDisplayValue(pv.getValue());
		}

		return result;
	}
}

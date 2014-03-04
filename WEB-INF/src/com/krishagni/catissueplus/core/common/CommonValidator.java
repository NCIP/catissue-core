package com.krishagni.catissueplus.core.common;

import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;


public class CommonValidator {
	public static void ensureValidPermissibleValue(String value, String type) {
		PermissibleValuesManager pvManager = new PermissibleValuesManagerImpl();
		List<String> pvList = pvManager.getPermissibleValueList(type);
		if (pvList.contains(value)) {
			return;
		}
		reportError(ParticipantErrorCode.INVALID_ATTR_VALUE, type);
	}
}

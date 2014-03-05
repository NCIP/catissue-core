package com.krishagni.catissueplus.core.common;

import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;
import gov.nih.nci.logging.api.util.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;


public class CommonValidator {
	public static void ensureValidPermissibleValue(String value, String type) {
		PermissibleValuesManager pvManager = new PermissibleValuesManagerImpl();
		if (pvManager.validate(type, value)) {
			return;
		}
		reportError(ParticipantErrorCode.INVALID_ATTR_VALUE, type);
	}
	
	public static boolean isBlank(String value)
	{
		return StringUtils.isBlank(value);
	}
	
	public static void ensureValidPermissibleValue(String[] value, String type)
	{
		PermissibleValuesManager pvManager = new PermissibleValuesManagerImpl();
		if (pvManager.validate(type, value)) {
			return;
		}
		reportError(ParticipantErrorCode.INVALID_ATTR_VALUE, type);
	}
}

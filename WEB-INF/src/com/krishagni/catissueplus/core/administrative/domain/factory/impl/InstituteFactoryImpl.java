
package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.administrative.domain.factory.InstituteFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.InstituteDetails;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public class InstituteFactoryImpl implements InstituteFactory {

	private static final String INSTITUTE_NAME = "institute name";

	private static final String ACTIVITY_STATUS = "activity_status";

	@Override
	public Institute createInstitute(InstituteDetails details) {
		ObjectCreationException exceptionHandler = new ObjectCreationException();
		Institute institute = new Institute();
		setInstituteName(institute, details.getName(), exceptionHandler);
		setActivityStatus(institute, details.getActivityStatus(), exceptionHandler);
		exceptionHandler.checkErrorAndThrow();
		return institute;
	}

	private void setInstituteName(Institute institute, String name, ObjectCreationException exceptionHandler) {
		if (isBlank(name)) {
			exceptionHandler.addError(UserErrorCode.INVALID_ATTR_VALUE, INSTITUTE_NAME);
			return;
		}
		institute.setName(name);
	}

	private void setActivityStatus(Institute institute, String activityStatus, ObjectCreationException exceptionHandler) {
		if (!CommonValidator.isValidPv(activityStatus, ACTIVITY_STATUS)) {
			exceptionHandler.addError(UserErrorCode.INVALID_ATTR_VALUE, ACTIVITY_STATUS);
			return;
		}

		institute.setActivityStatus(activityStatus);
	}

}

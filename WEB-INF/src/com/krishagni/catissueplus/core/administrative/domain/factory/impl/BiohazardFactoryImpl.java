
package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import com.krishagni.catissueplus.core.administrative.domain.Biohazard;
import com.krishagni.catissueplus.core.administrative.domain.factory.BiohazardErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.BiohazardFactory;
import com.krishagni.catissueplus.core.administrative.events.BiohazardDetails;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public class BiohazardFactoryImpl implements BiohazardFactory {

	private static final String BIOHAZARD_NAME = "biohazard name";

	private static final String BIOHAZARD_TYPE = "biohazard type";

	@Override
	public Biohazard createBiohazard(BiohazardDetails biohazardDetails) {
		Biohazard biohazard = new Biohazard();
		ObjectCreationException exceptionHandler = new ObjectCreationException();

		setName(biohazard, biohazardDetails.getName(), exceptionHandler);
		setType(biohazard, biohazardDetails.getType(), exceptionHandler);
		setComment(biohazard, biohazardDetails.getComment(), exceptionHandler);
		exceptionHandler.checkErrorAndThrow();

		return biohazard;
	}

	private void setComment(Biohazard biohazard, String comment, ObjectCreationException exceptionHandler) {
		biohazard.setComment(comment);
	}

	private void setType(Biohazard biohazard, String type, ObjectCreationException exceptionHandler) {
		if (isBlank(type)) {
			exceptionHandler.addError(BiohazardErrorCode.MISSING_ATTR_VALUE, BIOHAZARD_TYPE);
			return;
		}
		if (!CommonValidator.isValidPv(type, BIOHAZARD_TYPE)) {
			exceptionHandler.addError(BiohazardErrorCode.INVALID_ATTR_VALUE, BIOHAZARD_TYPE);
		}
		biohazard.setType(type);

	}

	private void setName(Biohazard biohazard, String name, ObjectCreationException exceptionHandler) {
		if (isBlank(name)) {
			exceptionHandler.addError(BiohazardErrorCode.MISSING_ATTR_VALUE, BIOHAZARD_NAME);
			return;
		}
		biohazard.setName(name);
	}

	@Override
	public Biohazard patchBiohazard(Biohazard biohazard, BiohazardDetails details) {
		ObjectCreationException exceptionHandler = new ObjectCreationException();
		if (details.isBiohazardNameModified()) {
			setName(biohazard, details.getName(), exceptionHandler);
		}

		if (details.isBiohazardTypeModified()) {
			setType(biohazard, details.getType(), exceptionHandler);
		}

		if (details.isCommentModified()) {
			setComment(biohazard, details.getComment(), exceptionHandler);
		}
		exceptionHandler.checkErrorAndThrow();
		return biohazard;
	}

}


package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;
import com.krishagni.catissueplus.core.administrative.domain.factory.PVErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.PermissibleValueFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.PermissibleValueDetails;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public class PermissibleValueFactoryImpl implements PermissibleValueFactory {

	private DaoFactory daoFactory;

	private static final String VALUE = "pv value";

	private static final String NAME = "name";

	private static final String PERMISSIBLE_VALUE = "permissible value";

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public PermissibleValue createPermissibleValue(PermissibleValueDetails details) {
		ObjectCreationException exceptionHandler = new ObjectCreationException();
		PermissibleValue permissibleValue = new PermissibleValue();

		setConceptCode(permissibleValue, details.getConceptCode(), exceptionHandler);
		setValue(permissibleValue, details.getValue(), exceptionHandler);
		setAttribute(permissibleValue, details.getAttribute(), exceptionHandler);
		setParent(permissibleValue, details.getParentId(), exceptionHandler);
		exceptionHandler.checkErrorAndThrow();
		return permissibleValue;
	}

	private void setConceptCode(PermissibleValue permissibleValue, String conceptCode,
			ObjectCreationException exceptionHandler) {
		permissibleValue.setConceptCode(conceptCode);
	}

	private void setValue(PermissibleValue permissibleValue, String value, ObjectCreationException exceptionHandler) {
		if (isBlank(value)) {
			exceptionHandler.addError(PVErrorCode.INVALID_ATTR_VALUE, VALUE);
			return;
		}
		permissibleValue.setValue(value);
	}

	private void setAttribute(PermissibleValue permissibleValue, String attribute, ObjectCreationException exceptionHandler) {
		if (isBlank(attribute)) {
			exceptionHandler.addError(PVErrorCode.INVALID_ATTR_VALUE, NAME);
			return;
		}
		permissibleValue.setAttribute(attribute);
	}

	private void setParent(PermissibleValue permissibleValue, Long parentId, ObjectCreationException exceptionHandler) {
		if (parentId == null) {
			return;
		}
		PermissibleValue parentPV = daoFactory.getPermissibleValueDao().getPermissibleValue(parentId);

		if (parentPV == null) {
			exceptionHandler.addError(UserErrorCode.INVALID_ATTR_VALUE, PERMISSIBLE_VALUE);
			return;
		}
		permissibleValue.setParent(parentPV);
	}

}

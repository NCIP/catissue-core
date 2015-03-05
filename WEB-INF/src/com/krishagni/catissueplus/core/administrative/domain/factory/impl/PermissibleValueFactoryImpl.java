
package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;
import com.krishagni.catissueplus.core.administrative.domain.factory.PermissibleValueFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.PvErrorCode;
import com.krishagni.catissueplus.core.administrative.events.PermissibleValueDetails;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class PermissibleValueFactoryImpl implements PermissibleValueFactory {

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public PermissibleValue createPermissibleValue(PermissibleValueDetails details) {		
		PermissibleValue permissibleValue = new PermissibleValue();
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);

		setConceptCode(permissibleValue, details.getConceptCode(), ose);
		setValue(permissibleValue, details.getValue(), ose);
		setAttribute(permissibleValue, details.getAttribute(), ose);
		setParent(permissibleValue, details.getParentId(), ose);
		
		ose.checkAndThrow();
		return permissibleValue;
	}

	private void setConceptCode(PermissibleValue permissibleValue, String conceptCode,
			OpenSpecimenException ose) {
		permissibleValue.setConceptCode(conceptCode);
	}

	private void setValue(PermissibleValue permissibleValue, String value, OpenSpecimenException ose) {
		if (StringUtils.isBlank(value)) {
			ose.addError(PvErrorCode.VALUE_REQUIRED);
			return;
			
		}
		
		permissibleValue.setValue(value);
	}

	private void setAttribute(PermissibleValue permissibleValue, String attribute, OpenSpecimenException ose) {
		if (StringUtils.isBlank(attribute)) {
			ose.addError(PvErrorCode.ATTR_NAME_REQUIRED);
			return;
		}
		
		permissibleValue.setAttribute(attribute);
	}

	private void setParent(PermissibleValue permissibleValue, Long parentId, OpenSpecimenException ose) {
		if (parentId == null) {
			return;			
		}
		
		PermissibleValue parentPv = daoFactory.getPermissibleValueDao().getById(parentId);
		if (parentPv == null) {
			ose.addError(PvErrorCode.PARENT_ATTR_NOT_FOUND);
			return;
		}
		
		permissibleValue.setParent(parentPv);
	}
}


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
		setParent(permissibleValue, details, ose);
		setProps(permissibleValue, details, ose);
		
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

	private void setParent(PermissibleValue permissibleValue, PermissibleValueDetails detail, OpenSpecimenException ose) {
		PermissibleValue parentPv = null;
		if (detail.getParentId() != null) {
			parentPv = daoFactory.getPermissibleValueDao().getById(detail.getParentId());
		} else if (StringUtils.isNotBlank(detail.getAttribute()) && StringUtils.isNotBlank(detail.getParentValue())) {
			parentPv = daoFactory.getPermissibleValueDao().getByValue(detail.getAttribute(), detail.getParentValue());
		} else {
			return;
		}
		
		if (parentPv == null) {
			ose.addError(PvErrorCode.PARENT_ATTR_NOT_FOUND);
			return;
		}
		
		permissibleValue.setParent(parentPv);
	}
	
	private void setProps(PermissibleValue permissibleValue, PermissibleValueDetails details, OpenSpecimenException ose) {
		permissibleValue.setProps(details.getProps());
	}
}

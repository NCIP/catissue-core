package com.krishagni.catissueplus.core.common.domain;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.de.domain.DeObject;
import com.krishagni.catissueplus.core.de.domain.DeObject.Attr;

public class ExtensionPvAttrLabelToken extends AbstractLabelTmplToken {

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	@Override
	public String getName() {
		return "EXTN_PV";
	}

	@Override
	public String getReplacement(Object object) {
		throw OpenSpecimenException.serverError(new UnsupportedOperationException("Require PV attr name"));
	}

	@Override
	public String getReplacement(Object object, String ... args) {
		if (args == null || args.length != 1) {
			throw OpenSpecimenException.serverError(new IllegalArgumentException("Invalid arguments to EXTN_PV"));
		}

		String pvAttr = args[0];
		String extnAttr = StringUtils.split(pvAttr, ".")[1];

		BeanWrapper extnWrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
		DeObject extn = (DeObject)extnWrapper.getPropertyValue("extension"); 

		String value = null;
		for (Attr attr : extn.getAttrs()) {
			if (!attr.getName().equals(extnAttr)) {
				continue;
			}

			if (attr.getValue() != null) {
				value = attr.getValue().toString();
			}

			break;
		}

		if (value == null) {
			return LabelTmplToken.EMPTY_VALUE;
		}

		PermissibleValue pv = daoFactory.getPermissibleValueDao().getByValue(pvAttr, value);
		if (pv == null) {
			return null;
		}

		if (StringUtils.isBlank(pv.getConceptCode())) {
			return LabelTmplToken.EMPTY_VALUE;
		}

		return pv.getConceptCode();
	}
}

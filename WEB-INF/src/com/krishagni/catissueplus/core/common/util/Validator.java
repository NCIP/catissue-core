package com.krishagni.catissueplus.core.common.util;

import java.util.List;

import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.errors.ParameterizedError;

public interface Validator {
	public boolean supports(Class<?> klass);
	
	public List<ParameterizedError> validate(Object target);
	
	public boolean validate(Object target, OpenSpecimenException ose);
}

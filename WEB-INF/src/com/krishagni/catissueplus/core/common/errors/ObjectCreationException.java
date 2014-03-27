
package com.krishagni.catissueplus.core.common.errors;

import java.util.ArrayList;
import java.util.List;

public class ObjectCreationException extends RuntimeException {

	/**
	 * Auto generated serial version Id  
	 */
	private static final long serialVersionUID = 4544763173779346609L;

	private CatissueErrorCode errorCode;

	private List<ErroneousField> fields = new ArrayList<ErroneousField>();

	public ObjectCreationException() {

	}

	public int getErrorCode() {
		return errorCode.code();
	}

	public String getMessage() {
		return errorCode.message();
	}

	public void addError(CatissueErrorCode errorCode, String field) {
		fields.add(new ErroneousField(errorCode, field));
	}

	public ErroneousField[] getErroneousFields(){
		return fields.toArray(new ErroneousField[fields.size()]);
	}
	
	public void addError(List<ErroneousField> fields) {
		this.fields.addAll(fields);
	}

	public void checkErrorAndThrow() {
		if (!fields.isEmpty()) {
			throw this;
		}

	}

}

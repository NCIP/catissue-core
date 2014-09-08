
package com.krishagni.catissueplus.core.common.errors;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class OpenSpecimenException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1473557909717365251L;

	private ResponseEvent response;

	private List<ErroneousField> fields = new ArrayList<ErroneousField>();

	public OpenSpecimenException(ResponseEvent response) {
		this.response = response;
	}

	public ResponseEvent getResponse() {
		return response;
	}

	public void setResponse(ResponseEvent response) {
		this.response = response;
	}

	public String getMessage() {
		StringBuilder str = new StringBuilder();
		for (ErroneousField field : fields) {
			str.append(field.getFieldName() + ":" + field.getErrorMessage()).append("\n");
		}

		return str.toString();
	}

}

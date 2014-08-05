package com.krishagni.catissueplus.core.de.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.events.EventStatus;

public class FormContextsAddedEvent extends FormContextsEvent {
	public static FormContextsAddedEvent ok(List<FormContextDetail> formCtxts) {
		FormContextsAddedEvent resp = new FormContextsAddedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setFormCtxts(formCtxts);
		return resp;
	}
	
    public static FormContextsAddedEvent badRequest(ObjectCreationException oce) {
        FormContextsAddedEvent resp = new FormContextsAddedEvent();
        resp.setStatus(EventStatus.BAD_REQUEST);
        resp.setErroneousFields(oce.getErroneousFields());
        resp.setException(oce);
        resp.setMessage(oce.getMessage());
        return resp;
    }

    public static FormContextsAddedEvent serverError(String message, Throwable t) {
        FormContextsAddedEvent resp = new FormContextsAddedEvent();
        resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
        resp.setException(t);
        resp.setMessage(message);
        return resp;
    }

}

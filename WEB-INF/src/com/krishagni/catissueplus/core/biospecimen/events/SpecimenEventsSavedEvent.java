package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.de.events.BulkFormDataSavedEvent;

import edu.common.dynamicextensions.napi.FormData;

public class SpecimenEventsSavedEvent extends ResponseEvent {
	
	private Long formId;
	
    private List<FormData> formDataList;
    
    public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

    public List<FormData> getFormDataList() {
		return formDataList;
	}

	public void setFormDataList(List<FormData> formDataList) {
		this.formDataList = formDataList;
	}

	public static SpecimenEventsSavedEvent setEventStatus(BulkFormDataSavedEvent bulkFormDataSavedEvent) {
		if(bulkFormDataSavedEvent.getStatus() == EventStatus.OK) {
			return successResp(bulkFormDataSavedEvent.getFormId(), bulkFormDataSavedEvent.getFormDataList());
		}else{
			return errorResp(bulkFormDataSavedEvent.getStatus(),bulkFormDataSavedEvent.getMessage(),bulkFormDataSavedEvent.getException());
		}
	}
		
	public static SpecimenEventsSavedEvent badRequest(Exception ex) {
		return errorResp(EventStatus.BAD_REQUEST, ex.getMessage(), ex);
	}
	
	public static SpecimenEventsSavedEvent notAuthorized(Exception ex){
		return errorResp(EventStatus.NOT_AUTHORIZED, ex.getMessage(), ex);
	}

    private static SpecimenEventsSavedEvent successResp(Long formId, List<FormData> formDataList) {
    	SpecimenEventsSavedEvent resp = new SpecimenEventsSavedEvent();
        resp.setFormId(formId);
        resp.setFormDataList(formDataList);
        resp.setStatus(EventStatus.OK);
        return resp;
	}
    
    private static SpecimenEventsSavedEvent errorResp(EventStatus status, String message, Throwable t) {
    	SpecimenEventsSavedEvent resp = new SpecimenEventsSavedEvent();
    	resp.setStatus(status);
		resp.setMessage(message);
		resp.setException(t);
		return resp;		
	}
}

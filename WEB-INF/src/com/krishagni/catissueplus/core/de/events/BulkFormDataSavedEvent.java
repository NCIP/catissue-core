package com.krishagni.catissueplus.core.de.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

import edu.common.dynamicextensions.napi.FormData;

public class BulkFormDataSavedEvent extends ResponseEvent{
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
	
	public static BulkFormDataSavedEvent ok(Long formId, List<FormData> formDataList) {
        BulkFormDataSavedEvent dataTableEvent = new BulkFormDataSavedEvent();
        dataTableEvent.setFormId(formId);
        dataTableEvent.setFormDataList(formDataList);
        dataTableEvent.setStatus(EventStatus.OK);
        return dataTableEvent;
	}

	public static BulkFormDataSavedEvent badRequest(Exception ex) {
		BulkFormDataSavedEvent dataTableEvent = new BulkFormDataSavedEvent();
		dataTableEvent.setStatus(EventStatus.BAD_REQUEST);
		dataTableEvent.setException(ex);
		dataTableEvent.setMessage(ex.getMessage());
		return dataTableEvent;
	}
}

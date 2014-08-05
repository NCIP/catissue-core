package com.krishagni.catissueplus.core.de.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

import edu.common.dynamicextensions.napi.FormData;

public class BulkFormDataEvent extends ResponseEvent{
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
	

    public static BulkFormDataEvent ok(Long formId, List<FormData> formDataList) {
        BulkFormDataEvent dataTableEvent = new BulkFormDataEvent();
        dataTableEvent.setFormId(formId);
        dataTableEvent.setFormDataList(formDataList);
        dataTableEvent.setStatus(EventStatus.OK);
        return dataTableEvent;
    }

	public static BulkFormDataEvent badRequest(Exception ex) {
        BulkFormDataEvent dataTableEvent = new BulkFormDataEvent();
        dataTableEvent.setStatus(EventStatus.BAD_REQUEST);
        dataTableEvent.setException(ex);
        dataTableEvent.setMessage(ex.getMessage());
        return dataTableEvent;
    }
}

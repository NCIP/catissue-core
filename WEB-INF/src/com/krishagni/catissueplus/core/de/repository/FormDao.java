package com.krishagni.catissueplus.core.de.repository;

import java.util.List;
import java.util.Map;

import krishagni.catissueplus.beans.FormContextBean;
import krishagni.catissueplus.beans.FormRecordEntryBean;

import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.catissueplus.core.de.events.FormContextDetail;
import com.krishagni.catissueplus.core.de.events.FormCtxtSummary;
import com.krishagni.catissueplus.core.de.events.FormRecordSummary;
import com.krishagni.catissueplus.core.de.events.FormSummary;
import com.krishagni.catissueplus.core.de.events.ObjectCpDetail;

public interface FormDao extends Dao<FormContextBean>{	
	public List<FormSummary> getAllFormsSummary();
	
	public List<FormSummary> getQueryForms();
	
	public List<FormSummary> getSpecimenEventFormsSummary();
	
	public List<FormContextDetail> getFormContexts(Long formId);
	
	public List<FormCtxtSummary> getCprForms(Long cprId);
		
	public List<FormCtxtSummary> getSpecimenForms(Long specimenId);
	
	public List<FormCtxtSummary> getSpecimenEventForms(Long specimenId);
	
	public List<FormCtxtSummary> getScgForms(Long scgId);
	
	public List<FormRecordSummary> getFormRecords(Long formCtxtId, Long objectId);
		
	public FormContextBean getFormContext(Long formId, Long cpId, String entity);	
	
	public FormContextBean getQueryFormContext(Long formId);
	
	public void saveOrUpdateRecordEntry(FormRecordEntryBean recordEntry);
	
	public FormRecordEntryBean getRecordEntry(Long formCtxtId, Long objectId, Long recordId);

	public FormRecordEntryBean getRecordEntry(Long recordId);

	public Long getRecordsCount(Long formCtxtId, Long objectId);

	public ObjectCpDetail getObjectCpDetail(Map<String, Object> map);

	public Long getFormCtxtId(Long containerId, String entityType, Long cpId);
	
	public List<Long> getFormIds(Long cpId, String entityType);
	
	public List<FormContextBean> getFormContextsById(List<Long> formContextIds);
	
}
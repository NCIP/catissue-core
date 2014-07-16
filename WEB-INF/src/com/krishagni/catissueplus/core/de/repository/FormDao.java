package com.krishagni.catissueplus.core.de.repository;

import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.de.events.*;
import krishagni.catissueplus.beans.FormContextBean;
import krishagni.catissueplus.beans.FormRecordEntryBean;

import com.krishagni.catissueplus.core.common.repository.Dao;

public interface FormDao extends Dao<FormContextBean>{	
	public List<FormSummary> getAllFormsSummary();
	
	public List<FormSummary> getQueryForms();
	
	public List<FormContextDetail> getFormContexts(Long formId);
	
	public List<FormCtxtSummary> getCprForms(Long cprId);
		
	public List<FormCtxtSummary> getSpecimenForms(Long specimenId);
	
	public List<FormCtxtSummary> getScgForms(Long scgId);
	
	public List<FormRecordSummary> getFormRecords(Long formCtxtId, Long objectId);
		
	public FormContextBean getFormContext(Long formId, Long cpId, String entity);	
	
	public FormContextBean getQueryFormContext(Long formId);
	
	public void saveOrUpdateRecordEntry(FormRecordEntryBean recordEntry);
	
	public FormRecordEntryBean getRecordEntry(Long formCtxtId, Long objectId, Long recordId);

	public FormRecordEntryBean getRecordEntry(Long recordId);

	public ObjectCpDetail getObjectCpDetail(Map<String, Object> map);

	public Long getFormCtxtId(Long containerId, String entityType, Long cpId);
	
	public List<Long> getFormIds(Long cpId, String entityType);
}

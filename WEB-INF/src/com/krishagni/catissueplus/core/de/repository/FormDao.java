package com.krishagni.catissueplus.core.de.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import krishagni.catissueplus.beans.FormContextBean;
import krishagni.catissueplus.beans.FormRecordEntryBean;

import com.krishagni.catissueplus.core.administrative.repository.FormListCriteria;
import com.krishagni.catissueplus.core.common.Pair;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.catissueplus.core.de.events.FormContextDetail;
import com.krishagni.catissueplus.core.de.events.FormCtxtSummary;
import com.krishagni.catissueplus.core.de.events.FormRecordSummary;
import com.krishagni.catissueplus.core.de.events.FormSummary;
import com.krishagni.catissueplus.core.de.events.ObjectCpDetail;

public interface FormDao extends Dao<FormContextBean>{	
	public List<FormSummary> getAllFormsSummary(FormListCriteria crit);
	
	public Long getAllFormsCount(FormListCriteria crit);
	
	public boolean isSystemForm(Long formId);

	public Date getUpdateTime(Long formId);

	public List<FormSummary> getQueryForms();
			
	public List<FormSummary> getFormsByEntityType(String entityType);

	public List<FormSummary> getFormsByCpAndEntityType(Long cpId, String[] entityTypes);

	public List<FormContextDetail> getFormContexts(Long formId);
	
	public List<FormCtxtSummary> getCprForms(Long cprId);
		
	public List<FormCtxtSummary> getSpecimenForms(Long specimenId);
	
	public List<FormCtxtSummary> getSpecimenEventForms(Long specimenId);
	
	public List<FormCtxtSummary> getScgForms(Long scgId);
	
	public List<FormCtxtSummary> getFormContexts(Long cpId, String entityType);
	
	public List<FormRecordSummary> getFormRecords(Long formCtxtId, Long objectId);
	
	public FormSummary getFormByContext(Long formCtxtId);
		
	public FormContextBean getFormContext(Long formId, Long cpId, String entity);	
	
	public FormContextBean getQueryFormContext(Long formId);

	public List<FormContextBean> getFormContexts(Collection<Long> cpIds, String entityType);

	public Pair<String, Long> getFormNameContext(Long cpId, String entityType);
	
	public void saveOrUpdateRecordEntry(FormRecordEntryBean recordEntry);
	
	public List<FormRecordEntryBean> getRecordEntries(Long formCtxtId, Long objectId);

	public FormRecordEntryBean getRecordEntry(Long formCtxtId, Long objectId, Long recordId);

	public FormRecordEntryBean getRecordEntry(Long formId, Long recordId);

	public Long getRecordsCount(Long formCtxtId, Long objectId);
	
	public ObjectCpDetail getObjectCpDetail(Map<String, Object> map);

	public Long getFormCtxtId(Long containerId, String entityType, Long cpId);
	
	public List<Long> getFormIds(Long cpId, String entityType);
	
	public List<Long> getFormIds(Long cpId, List<String> entityTypes);
	
	public List<FormContextBean> getFormContextsById(List<Long> formContextIds);
	
	public Map<Long, List<FormRecordSummary>> getFormRecords(Long objectId, String entityType, Long formId);
	
	public List<DependentEntityDetail> getDependentEntities(Long formId);
	
	public String getFormChangeLogDigest(String file);

	public Object[] getLatestFormChangeLog(String file);
	
	public void insertFormChangeLog(String file, String digest, Long formId);
	
	public void deleteFormContexts(Long formId);

	public void deleteRecords(Long formCtxtId, Collection<Long> recordIds);
}

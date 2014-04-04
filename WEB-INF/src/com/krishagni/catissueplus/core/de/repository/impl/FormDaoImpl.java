package com.krishagni.catissueplus.core.de.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import krishagni.catissueplus.beans.FormContextBean;
import krishagni.catissueplus.beans.FormRecordEntryBean;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.de.events.FormContextDetail;
import com.krishagni.catissueplus.core.de.events.FormCtxtSummary;
import com.krishagni.catissueplus.core.de.events.FormRecordSummary;
import com.krishagni.catissueplus.core.de.events.FormSummary;
import com.krishagni.catissueplus.core.de.repository.FormDao;

public class FormDaoImpl extends AbstractDao<FormContextBean> implements FormDao {
	@SuppressWarnings("unchecked")
	@Override
	public List<FormSummary> getAllFormsSummary() {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_ALL_FORMS);
		List<Object[]> rows = query.list();

		List<FormSummary> forms = new ArrayList<FormSummary>();
		for (Object[] row : rows) {
			FormSummary form = new FormSummary();
			form.setFormId((Long)row[0]);
			form.setName((String)row[1]);
			form.setCaption((String)row[2]);
			form.setCreationTime((Date)row[3]);
			form.setModificationTime((Date)row[4]);
			
			Integer minCpId = (Integer)row[6];
			if (minCpId != null && minCpId == -1) {			
			    form.setCpCount(-1);			
			} else {			
			    form.setCpCount((Integer)row[5]);
			}

			UserSummary user = new UserSummary();
			user.setId((Long)row[7]);
			user.setFirstName((String)row[8]);
			user.setLastName((String)row[9]);
			form.setCreatedBy(user);
			
			forms.add(form);						
		}
				
		return forms;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FormSummary> getQueryForms() {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_QUERY_FORMS);
		List<Object[]> rows = query.list();

		List<FormSummary> forms = new ArrayList<FormSummary>();
		for (Object[] row : rows) {
			FormSummary form = new FormSummary();
			form.setFormId((Long)row[0]);
			form.setName((String)row[1]);
			form.setCaption((String)row[2]);
			form.setCreationTime((Date)row[3]);
			form.setModificationTime((Date)row[4]);
			form.setCpCount(-1);

			UserSummary user = new UserSummary();
			user.setId((Long)row[5]);
			user.setFirstName((String)row[6]);
			user.setLastName((String)row[7]);
			form.setCreatedBy(user);
			
			forms.add(form);						
		}
				
		return forms;		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FormContextDetail> getFormContexts(Long formId) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_FORM_CTXTS);
		List<Object[]> rows = query.setLong("formId", formId).list();
		
		List<FormContextDetail> formCtxts = new ArrayList<FormContextDetail>();
		for (Object[] row : rows) {
			FormContextDetail formCtxt = new FormContextDetail();
			formCtxt.setFormCtxtId((Long)row[0]);
			formCtxt.setFormId((Long)row[1]);
			formCtxt.setLevel((String)row[2]);
			
			CollectionProtocolSummary cp = new CollectionProtocolSummary();
			cp.setId((Long)row[3]);
			cp.setShortTitle((String)row[4]);
			cp.setTitle((String)row[5]);
			formCtxt.setCollectionProtocol(cp);
			
			formCtxts.add(formCtxt);
		}
		
		return formCtxts;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FormCtxtSummary> getCprForms(Long cprId) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_CPR_FORMS);
		query.setLong("cprId", cprId);		
		return getEntityForms(query.list());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FormCtxtSummary> getSpecimenForms(Long specimenId) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_SPECIMEN_FORMS);
		query.setLong("specimenId", specimenId);		
		return getEntityForms(query.list());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FormCtxtSummary> getScgForms(Long scgId) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_SCG_FORMS);
		query.setLong("scgId", scgId);		
		return getEntityForms(query.list());
	}
		
	@SuppressWarnings("unchecked")
	@Override
	public List<FormRecordSummary> getFormRecords(Long formCtxtId, Long objectId) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_FORM_RECORDS);
		query.setLong("formCtxtId", formCtxtId).setLong("objectId", objectId);
		
		List<Object[]> rows = query.list();
		
		List<FormRecordSummary> formRecords = new ArrayList<FormRecordSummary>();
		for (Object[] row : rows) {
			FormRecordSummary record = new FormRecordSummary();
			record.setId((Long)row[0]);
			record.setRecordId((Long)row[1]);
			record.setUpdateTime((Date)row[2]);
			
			UserSummary user = new UserSummary();
			user.setId((Long)row[3]);
			user.setFirstName((String)row[4]);
			user.setLastName((String)row[5]);
			record.setUser(user);
			
			formRecords.add(record);
		}
		
		return formRecords;
	}
		
	@SuppressWarnings("unchecked")
	@Override
	public FormContextBean getFormContext(Long formId, Long cpId, String entity) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_FORM_CTXT);		
		query.setLong("formId", formId);
		query.setLong("cpId", cpId);		
		query.setString("entityType", entity);
		
		List<FormContextBean> objs = query.list();
		return objs != null && !objs.isEmpty() ? objs.iterator().next() : null;
	}
		
	@Override
	public void saveOrUpdateRecordEntry(FormRecordEntryBean recordEntry) {
		sessionFactory.getCurrentSession().saveOrUpdate(recordEntry);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public FormRecordEntryBean getRecordEntry(Long formCtxtId, Long objectId, Long recordId) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_RECORD_ENTRY);
		query.setLong("formCtxtId", formCtxtId).setLong("objectId", objectId).setLong("recordId", recordId);
		
		List<FormRecordEntryBean> objs = query.list();
		return objs != null && !objs.isEmpty() ? objs.iterator().next() : null;		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public FormRecordEntryBean getRecordEntry(Long recordId) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_RECORD_ENTRY_BY_REC_ID);
		query.setLong("recordId", recordId);
		
		List<FormRecordEntryBean> objs = query.list();
		return objs != null && !objs.isEmpty() ? objs.iterator().next() : null;		
	}
	
	private List<FormCtxtSummary> getEntityForms(List<Object[]> rows) {
		Map<Long, FormCtxtSummary> formsMap = new LinkedHashMap<Long, FormCtxtSummary>(); 
		
		for (Object[] row : rows) {
			Long cpId = (Long)row[3];
			Long formId = (Long)row[1];

			FormCtxtSummary form = formsMap.get(formId);
			if (form != null && cpId == -1) {
				continue;
			}
			
			form = new FormCtxtSummary();
			form.setFormCtxtId((Long)row[0]);
			form.setFormId(formId);
			form.setFormCaption((String)row[2]);
			form.setNoOfRecords((Integer)row[4]);
			
			formsMap.put(formId, form);
		}
		
		return new ArrayList<FormCtxtSummary>(formsMap.values());		
	}
		
	private static final String FQN = FormContextBean.class.getName();
	
	private static final String GET_ALL_FORMS = FQN + ".getAllFormsSummary";
	
	private static final String GET_QUERY_FORMS = FQN + ".getQueryForms";
	
	private static final String GET_FORM_CTXTS = FQN + ".getFormContexts";
	
	private static final String GET_FORM_CTXT = FQN + ".getFormContext";
	
	private static final String GET_CPR_FORMS = FQN + ".getCprForms";
	
	private static final String GET_SPECIMEN_FORMS = FQN + ".getSpecimenForms";
	
	private static final String GET_SCG_FORMS = FQN + ".getScgForms";
	
	private static final String GET_FORM_RECORDS = FQN + ".getFormRecords";
	
	private static final String RE_FQN = FormRecordEntryBean.class.getName();
	
	private static final String GET_RECORD_ENTRY = RE_FQN + ".getRecordEntry";
	
	private static final String GET_RECORD_ENTRY_BY_REC_ID = RE_FQN + ".getRecordEntryByRecId";

}

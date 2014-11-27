package com.krishagni.catissueplus.core.de.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import krishagni.catissueplus.beans.FormContextBean;
import krishagni.catissueplus.beans.FormRecordEntryBean;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.de.events.FormContextDetail;
import com.krishagni.catissueplus.core.de.events.FormCtxtSummary;
import com.krishagni.catissueplus.core.de.events.FormRecordSummary;
import com.krishagni.catissueplus.core.de.events.FormSummary;
import com.krishagni.catissueplus.core.de.events.ObjectCpDetail;
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
  	public List<FormSummary> getSpecimenEventFormsSummary() {
       Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_SPECIMEN_EVENT_FORMS_SUMMARY);
       List<Object[]> rows = query.list();
  
       List<FormSummary> forms = new ArrayList<FormSummary>();
       for (Object[] row : rows) {
           FormSummary form = new FormSummary();
           form.setFormId((Long) row[0]);
           form.setName((String) row[1]);
           form.setCaption((String) row[2]);
           form.setCreationTime((Date) row[3]);
           form.setModificationTime((Date) row[4]);
           form.setCpCount(-1);
           form.setSysForm((Boolean)row[8]);

           UserSummary user = new UserSummary();
           user.setId((Long) row[5]);
           user.setFirstName((String) row[6]);
           user.setLastName((String) row[7]);
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
			formCtxt.setMultiRecord((Boolean)row[3]);
			formCtxt.setSysForm((Boolean)row[4]);

			CollectionProtocolSummary cp = new CollectionProtocolSummary();
			cp.setId((Long)row[5]);
			cp.setShortTitle((String)row[6]);
			cp.setTitle((String)row[7]);
			formCtxt.setCollectionProtocol(cp);
			
			formCtxts.add(formCtxt);
		}
		
		return formCtxts;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public FormContextBean getQueryFormContext(Long formId) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_QUERY_FORM_CONTEXT);
		List<Object> queryFormContext = query.setLong("formId", formId).setString("entityType", "Query").list();
		return (FormContextBean)(queryFormContext.size() == 1 ? queryFormContext.get(0) : null);
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
    public List<FormCtxtSummary> getSpecimenEventForms(Long specimenId) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_SPECIMEN_EVENT_FORMS);
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FormContextBean> getFormContextsById(List<Long> formContextIds) {
		List<FormContextBean> formContexts = new ArrayList<FormContextBean>();
		
		int i = 0;
		int numIds = formContextIds.size();
		while (i < numIds) {
			List<Long> params = formContextIds.subList(i, i + 500 > numIds ? numIds : i + 500);
			i += 500;
			
			formContexts.addAll(
				sessionFactory.getCurrentSession()
					.getNamedQuery(GET_FORM_CTXTS_BY_ID)
					.setParameterList("ids", params)
					.list());					
		}
		
		return formContexts;
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
			form.setMultiRecord((Boolean)row[4]);
			form.setSysForm((Boolean)row[5]);
			form.setNoOfRecords((Integer)row[6]);
			
			formsMap.put(formId, form);
		}
		
		return new ArrayList<FormCtxtSummary>(formsMap.values());		
	}
		
	@Override
	public ObjectCpDetail getObjectCpDetail(Map<String, Object> dataHookingInformation) {
		ObjectCpDetail objCp = null;
		String entityType = (String)dataHookingInformation.get("entityType"); 
		if (entityType.equals("Participant") ) {
			objCp = getObjectIdForParticipant(dataHookingInformation);
		} else if (entityType.equals("Specimen") || entityType.equals("SpecimenEvent")) {
			objCp = getObjectIdForSpecimen(entityType, dataHookingInformation);
		} else if (entityType.equals("SpecimenCollectionGroup")) {
			objCp = getObjectIdForSCG(dataHookingInformation);
		}
		
		return objCp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long getFormCtxtId(Long containerId, String entityType, Long cpId) {
        Long formCtxtId = null;

        Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_FORM_CTX_ID);
		query.setLong("containerId", containerId);
		query.setString("entityType", entityType);
        query.setLong("cpId", cpId);

        List<Object[]> rows = query.list();

        if (rows == null || rows.isEmpty()) {
            return null;
        }

        for (Object[] row : rows) {
            formCtxtId = (Long) row[0];
            if (cpId.equals((Long) row[1])) {
                break;
            }
        }

		return formCtxtId;
	}
	
	@Override
	public List<Long> getFormIds(Long cpId, String entityType) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_FORM_IDS);
		query.setLong("cpId", cpId).setString("entityType", entityType);
		
		List<Long> formIds = new ArrayList<Long>();
		for (Object id : query.list()) {
			formIds.add((Long)id);
		}
		
		return formIds;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Long getRecordsCount(Long formCtxtId, Long objectId) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_RECORD_CNT);
		query.setLong("formCtxtId", formCtxtId).setLong("objectId", objectId);
		List<Long> result = query.list();
		return result.iterator().next();
	}
	
	@SuppressWarnings("unchecked")
	private ObjectCpDetail getObjectIdForParticipant(Map<String, Object> dataHookingInformation) {
		ObjectCpDetail objCp = new ObjectCpDetail();
		String cpTitle = (String) dataHookingInformation.get("collectionProtocol");
		String ppId = (String) dataHookingInformation.get("ppi");

		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_PARTICIPANT_OBJ_ID);
		query.setString("ppId", ppId);
		query.setString("cpTitle", cpTitle);

		List<Object[]> objs = query.list();
		if (objs == null || objs.isEmpty()) {
			return null;
		}

		Object[] row = objs.get(0);
		objCp.setObjectId((Long) row[0]);
		objCp.setCpId((Long) row[1]);
		return objCp;
	}
	
	@SuppressWarnings("unchecked")
	private ObjectCpDetail getObjectIdForSpecimen(String entityType, Map<String, Object> dataHookingInformation) {
		ObjectCpDetail objCp = new ObjectCpDetail();
		String id = null, label = null, barcode = null;

		if (entityType.equals("Specimen")) {
			id = (String) dataHookingInformation.get("specimenId");
			label = (String) dataHookingInformation.get("specimenLabel");
			barcode = (String) dataHookingInformation.get("specimenBarcode");
		} else if (entityType.equals("SpecimenEvent")) {
			id = (String) dataHookingInformation.get("specimenIdForEvent");
			label = (String) dataHookingInformation.get("specimenLabelForEvent");
			barcode = (String) dataHookingInformation.get("specimenBarcodeForEvent");
		} else {
			throw new RuntimeException("Unknown entity type: " + entityType);
		}

		Criteria query = sessionFactory
				.getCurrentSession()
				.createCriteria(Specimen.class)
				.createAlias("specimenCollectionGroup", "scg")
				.createAlias("scg.collectionProtocolRegistration", "cpr")
				.createAlias("cpr.collectionProtocol", "cp")
				.setProjection(
						Projections.projectionList()
								.add(Projections.property("id"))
								.add(Projections.property("cp.id")));

		if (id != null) {
			Long specimenId = Long.parseLong(id);
			query.add(Restrictions.eq("id", specimenId));
		} else if (label != null) {
			query.add(Restrictions.eq("label", label));
		} else if (barcode != null) {
			query.add(Restrictions.eq("barcode", barcode));
		} else {
			throw new RuntimeException("Require either Specimen ID, Specimen Label or Specimen Barcode");
		}

		List<Object[]> objs = query.list();
		if (objs == null || objs.isEmpty()) {
			return null;
		}

		Object[] row = objs.get(0);
		objCp.setObjectId((Long) row[0]);
		objCp.setCpId((Long) row[1]);
		return objCp;
	}

	@SuppressWarnings("unchecked")
	private ObjectCpDetail getObjectIdForSCG(Map<String, Object> dataHookingInformation) {
		ObjectCpDetail objCp = new ObjectCpDetail();
		String id = (String) dataHookingInformation.get("scgId");
		String name = (String) dataHookingInformation.get("scgName");
		String barcode = (String) dataHookingInformation.get("scgBarcode");

		Criteria query = sessionFactory
				.getCurrentSession()
				.createCriteria(SpecimenCollectionGroup.class)
				.createAlias("collectionProtocolRegistration", "cpr")
				.createAlias("cpr.collectionProtocol", "cp")
				.setProjection(
						Projections.projectionList()
								.add(Projections.property("id"))
								.add(Projections.property("cp.id")));

		if (id != null) {
			Long scgId = Long.parseLong(id);
			query.add(Restrictions.eq("id", scgId));
		} else if (name != null) {
			query.add(Restrictions.eq("name", name));
		} else if (barcode != null) {
			query.add(Restrictions.eq("barcode", barcode));
		} else {
			throw new RuntimeException("Require either SCG ID, SCG Name or SCG Barcode");
		}

		List<Object[]> objs = query.list();
		if (objs == null || objs.isEmpty()) {
			return null;
		}

		Object[] row = objs.get(0);
		objCp.setObjectId((Long) row[0]);
		objCp.setCpId((Long) row[1]);
		return objCp;
	}
	
	private static final String FQN = FormContextBean.class.getName();
	
	private static final String GET_ALL_FORMS = FQN + ".getAllFormsSummary";
	
	private static final String GET_SPECIMEN_EVENT_FORMS_SUMMARY = FQN + ".getSpecimenEventFormsSummary";
	
	private static final String GET_QUERY_FORMS = FQN + ".getQueryForms";
	
	private static final String GET_FORM_CTXTS = FQN + ".getFormContexts";
	
	private static final String GET_FORM_CTXT = FQN + ".getFormContext";
	
	private static final String GET_FORM_CTXTS_BY_ID = FQN + ".getFormContextsById";
	
	private static final String GET_CPR_FORMS = FQN + ".getCprForms";
	
	private static final String GET_SPECIMEN_FORMS = FQN + ".getSpecimenForms";
	
	private static final String GET_SPECIMEN_EVENT_FORMS = FQN + ".getSpecimenEventForms";
	
	private static final String GET_SCG_FORMS = FQN + ".getScgForms";
	
	private static final String GET_FORM_RECORDS = FQN + ".getFormRecords";
	
	private static final String GET_PARTICIPANT_OBJ_ID = FQN + ".getParticipantObjId";

	private static final String GET_FORM_CTX_ID = FQN + ".getFormContextId";

	private static final String RE_FQN = FormRecordEntryBean.class.getName();
	
	private static final String GET_RECORD_ENTRY = RE_FQN + ".getRecordEntry";
	
	private static final String GET_RECORD_ENTRY_BY_REC_ID = RE_FQN + ".getRecordEntryByRecId";
	
	private static final String GET_FORM_IDS = FQN + ".getFormIds";
	
	private static final String GET_QUERY_FORM_CONTEXT = FQN + ".getQueryFormCtxtByContainerId";
	
	private static final String GET_RECORD_CNT = FQN + ".getRecordCount";

}
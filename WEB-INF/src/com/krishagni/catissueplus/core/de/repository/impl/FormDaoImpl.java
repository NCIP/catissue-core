package com.krishagni.catissueplus.core.de.repository.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import krishagni.catissueplus.beans.FormContextBean;
import krishagni.catissueplus.beans.FormRecordEntryBean;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.de.events.FormContextDetail;
import com.krishagni.catissueplus.core.de.events.FormCtxtSummary;
import com.krishagni.catissueplus.core.de.events.FormRecordSummary;
import com.krishagni.catissueplus.core.de.events.FormSummary;
import com.krishagni.catissueplus.core.de.events.ObjectCpDetail;
import com.krishagni.catissueplus.core.de.repository.FormDao;

public class FormDaoImpl extends AbstractDao<FormContextBean> implements FormDao {
	
	@Override
	public Class<FormContextBean> getType() {
		return FormContextBean.class;
	}
	
	@Override
	public FormContextBean getById(Long id) {
		return getById(id, "deletedOn is null");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FormSummary> getAllFormsSummary() {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_ALL_FORMS);
		return getForms(query.list());
	}
	
	@SuppressWarnings("unchecked")
 	@Override
  	public List<FormSummary> getFormsByEntityType(String entityType) {
		List<Object[]> rows = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_FORMS_BY_ENTITY_TYPE)
				.setString("entityType", entityType)
				.list();
		return getForms(rows);
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
	public List<FormCtxtSummary> getFormContexts(Long cpId, String entityType) {
		List<FormContextBean> result = sessionFactory.getCurrentSession()
			.getNamedQuery(GET_FORM_CTXTS_BY_ENTITY)
			.setLong("cpId", cpId)
			.setString("entityType", entityType)
			.list();
				
		return FormCtxtSummary.from(result);
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
	public FormSummary getFormByContext(Long formCtxtId) {
		List<Object[]> rows = sessionFactory.getCurrentSession()
			.getNamedQuery(GET_FORM_BY_CTXT)
			.setLong("formCtxtId", formCtxtId)
			.list();
		
		if (rows.isEmpty()) {
			return null;
		}
		
		Object[] row = rows.iterator().next();
		FormSummary result = new FormSummary();
		result.setFormId((Long)row[0]);
		result.setCaption((String)row[1]);
		return result;		
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
	public List<FormRecordEntryBean> getRecordEntries(Long formCtxtId, Long objectId) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_RECORD_ENTRIES)
				.setLong("formCtxtId", formCtxtId)
				.setLong("objectId", objectId)
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public FormRecordEntryBean getRecordEntry(Long formCtxtId, Long objectId, Long recordId) {
		List<Object[]> rows = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_RECORD_ENTRY)
				.setLong("formCtxtId", formCtxtId)
				.setLong("objectId", objectId)
				.setLong("recordId", recordId)
				.list();
		return CollectionUtils.isEmpty(rows) ? null : getFormRecordEntry(rows.iterator().next());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public FormRecordEntryBean getRecordEntry(Long recordId) {
		List<Object[]> rows = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_RECORD_ENTRY_BY_REC_ID)
				.setLong("recordId", recordId)
				.list();
		return CollectionUtils.isEmpty(rows) ? null : getFormRecordEntry(rows.iterator().next());
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
			form.setCreationTime((Date)row[4]);
			form.setModificationTime((Date)row[5]);
			
			UserSummary user = new UserSummary();
			user.setId((Long)row[6]);
			user.setFirstName((String)row[7]);
			user.setLastName((String)row[8]);
			form.setCreatedBy(user);
			
			form.setMultiRecord((Boolean)row[9]);
			form.setSysForm((Boolean)row[10]);
			form.setNoOfRecords((Integer)row[11]);			
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
		return getFormIds(cpId, Collections.singletonList(entityType));
	}
	
	@Override
	public List<Long> getFormIds(Long cpId, List<String> entityTypes) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_FORM_IDS);
		query.setLong("cpId", cpId).setParameterList("entityTypes", entityTypes);
		
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
	@Override
	public Map<Long, List<FormRecordSummary>> getFormRecords(Long objectId, String entityType, Long formId) {
		
		Query query = null;
		if (formId == null) {
			query = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_RECS_BY_TYPE_AND_OBJECT);					
		} else {
			query = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_RECS)
				.setLong("formId", formId);
		}
		
		List<Object[]> rows = query.setString("entityType", entityType)
				.setLong("objectId", objectId)
				.list();
		
		Map<Long, List<FormRecordSummary>> result = new HashMap<Long, List<FormRecordSummary>>();
		for (Object[] row : rows) {
			Long form = (Long)row[0];
			
			FormRecordSummary record = new FormRecordSummary();
			record.setFcId((Long)row[1]);
			record.setRecordId((Long)row[2]);
			record.setUpdateTime((Date)row[3]);
			
			UserSummary user = new UserSummary();
			user.setId((Long)row[4]);
			user.setFirstName((String)row[5]);
			user.setLastName((String)row[6]);
			record.setUser(user);
			
			List<FormRecordSummary> recs = result.get(form);
			if (recs == null) {
				recs = new ArrayList<FormRecordSummary>();
				result.put(form, recs);
			}
			
			recs.add(record);
		}
				
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DependentEntityDetail> getDependentEntities(Long formId) {
		List<Object[]> rows = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_DEPENDENT_ENTITIES)
				.setLong("formId", formId)
				.list();
		
		return getDependentEntities(rows);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String getFormChangeLogDigest(String file) {
		List<Object> rows = sessionFactory.getCurrentSession()
				.createSQLQuery(GET_CHANGE_LOG_DIGEST_SQL)
				.setString("filename", file)
				.list();
		
		if (rows == null || rows.isEmpty()) {
			return null;
		}		
		return (String)rows.iterator().next();
	}

	@Override
	public void insertFormChangeLog(String file, String digest, Long formId) {
		sessionFactory.getCurrentSession()
				.createSQLQuery(INSERT_CHANGE_LOG_SQL)
				.setString("filename", file)
				.setString("digest", digest)
				.setLong("formId", formId)
				.setTimestamp("executedOn", Calendar.getInstance().getTime())
				.executeUpdate();
	}
	
	@Override
	public void deleteFormContexts(Long formId) {
		sessionFactory.getCurrentSession()
			.createSQLQuery(SOFT_DELETE_FORM_CONTEXTS_SQL)
			.setTimestamp("deletedOn", Calendar.getInstance().getTime())
			.setLong("formId", formId)
			.executeUpdate(); 
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
				.createCriteria(Visit.class)
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
	
	private FormRecordEntryBean getFormRecordEntry(Object[] row) {
		FormRecordEntryBean re = new FormRecordEntryBean();
		re.setIdentifier((Long)row[0]);
		re.setFormCtxtId((Long)row[1]);
		re.setObjectId((Long)row[2]);
		re.setRecordId((Long)row[3]);
		re.setUpdatedBy((Long)row[4]);
		re.setUpdatedTime((Date)row[5]);
		re.setActivityStatusStr((String)row[6]);
		re.setEntityType((String)row[7]);
		return re;
	}
	
	private List<FormSummary> getForms(List<Object[]> rows) {
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
			
			form.setSysForm(row[7] == null ? false : (Boolean) row[7]);

			UserSummary user = new UserSummary();
			user.setId((Long)row[8]);
			user.setFirstName((String)row[9]);
			user.setLastName((String)row[10]);
			form.setCreatedBy(user);
			
			forms.add(form);						
		}		
		
		return forms;
	}
	
	private List<DependentEntityDetail> getDependentEntities(List<Object[]> rows) {
		List<DependentEntityDetail> dependentEntities = new ArrayList<DependentEntityDetail>();
		
		for (Object[] row: rows) {
			String name = (String)row[0];
			int count = ((Integer)row[1]).intValue();
			dependentEntities.add(DependentEntityDetail.from(name, count));
		}
		
		return dependentEntities;
 	}
	
	private static final String FQN = FormContextBean.class.getName();
	
	private static final String GET_ALL_FORMS = FQN + ".getAllFormsSummary";
	
	private static final String GET_FORMS_BY_ENTITY_TYPE = FQN + ".getFormsByEntityType";
	
	private static final String GET_QUERY_FORMS = FQN + ".getQueryForms";
	
	private static final String GET_FORM_CTXTS = FQN + ".getFormContexts";
	
	private static final String GET_FORM_CTXT = FQN + ".getFormContext";
	
	private static final String GET_FORM_CTXTS_BY_ID = FQN + ".getFormContextsById";
	
	private static final String GET_CPR_FORMS = FQN + ".getCprForms";
	
	private static final String GET_SPECIMEN_FORMS = FQN + ".getSpecimenForms";
	
	private static final String GET_SPECIMEN_EVENT_FORMS = FQN + ".getSpecimenEventForms";
	
	private static final String GET_SCG_FORMS = FQN + ".getScgForms";
	
	private static final String GET_FORM_CTXTS_BY_ENTITY = FQN + ".getFormContextsByEntity";
	
	private static final String GET_FORM_BY_CTXT = FQN + ".getFormByCtxt";
	
	private static final String GET_FORM_RECORDS = FQN + ".getFormRecords";
	
	private static final String GET_PARTICIPANT_OBJ_ID = FQN + ".getParticipantObjId";

	private static final String GET_FORM_CTX_ID = FQN + ".getFormContextId";

	private static final String RE_FQN = FormRecordEntryBean.class.getName();
	
	private static final String GET_RECORD_ENTRY = RE_FQN + ".getRecordEntry";
	
	private static final String GET_RECORD_ENTRIES = RE_FQN + ".getRecordEntries";

	private static final String GET_RECORD_ENTRY_BY_REC_ID = RE_FQN + ".getRecordEntryByRecId";
	
	private static final String GET_FORM_IDS = FQN + ".getFormIds";
	
	private static final String GET_QUERY_FORM_CONTEXT = FQN + ".getQueryFormCtxtByContainerId";
	
	private static final String GET_RECORD_CNT = FQN + ".getRecordCount"; 
	
	private static final String GET_RECS_BY_TYPE_AND_OBJECT = FQN  + ".getRecordsByEntityAndObject";
	
	private static final String GET_RECS = FQN + ".getRecords";
	
	private static final String GET_DEPENDENT_ENTITIES = FQN + ".getDependentEntities";
	
	private static final String GET_CHANGE_LOG_DIGEST_SQL =
			"select " +
			"  md5_digest " +
			"from " +
			"  os_import_forms_log fl " +
			"where " +
			"  fl.filename = :filename and fl.executed_on in (" +
			"    select " +
			"      max(executed_on) " +
			"    from " +
			"      os_import_forms_log " +
			"    where " +
			"      filename = :filename )";
	
	private static final String INSERT_CHANGE_LOG_SQL =
			"insert into os_import_forms_log " +
			"	(filename, form_id, md5_digest, executed_on) " +
			"values " +
			"   (:filename, :formId, :digest, :executedOn) ";
	
	private static final String SOFT_DELETE_FORM_CONTEXTS_SQL = 
			"update catissue_form_context set deleted_on = :deletedOn where container_id = :formId";

}
package com.krishagni.catissueplus.core.de.services.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.de.domain.FormErrorCode;
import com.krishagni.catissueplus.core.de.events.AddRecordEntryOp;
import com.krishagni.catissueplus.core.de.events.EntityFormRecords;
import com.krishagni.catissueplus.core.de.events.FileDetail;
import com.krishagni.catissueplus.core.de.events.FormContextDetail;
import com.krishagni.catissueplus.core.de.events.FormCtxtSummary;
import com.krishagni.catissueplus.core.de.events.FormDataDetail;
import com.krishagni.catissueplus.core.de.events.FormFieldSummary;
import com.krishagni.catissueplus.core.de.events.FormRecordCriteria;
import com.krishagni.catissueplus.core.de.events.FormRecordSummary;
import com.krishagni.catissueplus.core.de.events.FormRecordsList;
import com.krishagni.catissueplus.core.de.events.FormSummary;
import com.krishagni.catissueplus.core.de.events.FormType;
import com.krishagni.catissueplus.core.de.events.GetEntityFormRecordsOp;
import com.krishagni.catissueplus.core.de.events.GetFileDetailOp;
import com.krishagni.catissueplus.core.de.events.GetFormRecordsListOp;
import com.krishagni.catissueplus.core.de.events.ListEntityFormsOp;
import com.krishagni.catissueplus.core.de.events.ListFormFields;
import com.krishagni.catissueplus.core.de.events.ObjectCpDetail;
import com.krishagni.catissueplus.core.de.events.RemoveFormContextOp;
import com.krishagni.catissueplus.core.de.repository.FormDao;
import com.krishagni.catissueplus.core.de.services.FormContextProcessor;
import com.krishagni.catissueplus.core.de.services.FormService;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.DataType;
import edu.common.dynamicextensions.domain.nui.FileUploadControl;
import edu.common.dynamicextensions.domain.nui.Label;
import edu.common.dynamicextensions.domain.nui.LookupControl;
import edu.common.dynamicextensions.domain.nui.PageBreak;
import edu.common.dynamicextensions.domain.nui.PermissibleValue;
import edu.common.dynamicextensions.domain.nui.SelectControl;
import edu.common.dynamicextensions.domain.nui.SubFormControl;
import edu.common.dynamicextensions.domain.nui.ValidationErrors;
import edu.common.dynamicextensions.napi.FileControlValue;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.napi.FormDataManager;
import edu.common.dynamicextensions.napi.impl.FormDataManagerImpl;
import edu.common.dynamicextensions.nutility.FileUploadMgr;
import krishagni.catissueplus.beans.FormContextBean;
import krishagni.catissueplus.beans.FormRecordEntryBean;
import krishagni.catissueplus.beans.FormRecordEntryBean.Status;

public class FormServiceImpl implements FormService {
	private static final String PARTICIPANT_FORM = "Participant";
	
	private static final String SCG_FORM = "SpecimenCollectionGroup";
	
	private static final String SPECIMEN_FORM = "Specimen";
	
	private static final String SPECIMEN_EVENT_FORM = "SpecimenEvent";
	
	private static final List<String> COLLECTION_PROTOCOL_EXTNS = Arrays.asList("CollectionProtocolExtension");
	
	private static final List<String> PARTICIPANT_EXTNS = Arrays.asList("ParticipantExtension");
	
	private static final List<String> VISIT_EXTNS = Arrays.asList("VisitExtension");
	
	private static final List<String> SPECIMEN_EXTNS =  Arrays.asList("SpecimenExtension", "DerivativeExtension", "AliquotExtension");
	
	private static Set<String> staticExtendedForms = new HashSet<String>();
	
	private static Map<String, List<String>> customFieldForms = new HashMap<String, List<String>>();

	static {
		staticExtendedForms.add(PARTICIPANT_FORM);
		staticExtendedForms.add(SCG_FORM);
		staticExtendedForms.add(SPECIMEN_FORM);
		
		customFieldForms.put("CollectionProtocol", COLLECTION_PROTOCOL_EXTNS);
		customFieldForms.put("Participant", PARTICIPANT_EXTNS);
		customFieldForms.put("SpecimenCollectionGroup", VISIT_EXTNS);
		customFieldForms.put("Specimen", SPECIMEN_EXTNS);
	}
	
	private FormDao formDao;

	private Map<String, List<FormContextProcessor>> ctxtProcs = new HashMap<String, List<FormContextProcessor>>();

	public void setFormDao(FormDao formDao) {
		this.formDao = formDao;
	}

	public void setCtxtProcs(Map<String, List<FormContextProcessor>> ctxtProcs) {
		this.ctxtProcs = ctxtProcs;
	}

	@Override
    @PlusTransactional
	public ResponseEvent<List<FormSummary>> getForms(RequestEvent<FormType> req) {
		FormType ft = req.getPayload();
		switch (ft) {
			case DATA_ENTRY_FORMS:
				return ResponseEvent.response(formDao.getAllFormsSummary());

			case PARTICIPANT_FORMS:
			case VISIT_FORMS:
			case SPECIMEN_FORMS:
			case SPECIMEN_EVENT_FORMS:
				return ResponseEvent.response(formDao.getFormsByEntityType(ft.getType()));

			case QUERY_FORMS:
			default:
				return ResponseEvent.response(formDao.getQueryForms());
		}
	}

    @Override
    @PlusTransactional
	public ResponseEvent<Container> getFormDefinition(RequestEvent<Long> req) {
    	Long formId = req.getPayload();
		Container container = Container.getContainer(formId);
		if (container == null) {
			return ResponseEvent.userError(FormErrorCode.NOT_FOUND);
		} else {
			return ResponseEvent.response(container);
		}
	}
    
	@Override
	@PlusTransactional
	public ResponseEvent<Boolean> deleteForm(RequestEvent<Long> req) {
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
			
			Long formId = req.getPayload();
			if (Container.softDeleteContainer(formId)) {
				formDao.deleteFormContexts(formId);
				return ResponseEvent.response(true);
			} else {
				return ResponseEvent.userError(FormErrorCode.NOT_FOUND);
			}
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
    
    @Override
    @PlusTransactional
	public ResponseEvent<List<FormFieldSummary>> getFormFields(RequestEvent<ListFormFields> req) {
    	ListFormFields op = req.getPayload();
    	
		Long formId = op.getFormId();
		Container form = Container.getContainer(formId);
		if (form == null) {
			return ResponseEvent.userError(FormErrorCode.NOT_FOUND);
		}
		
		List<FormFieldSummary> fields = getFormFields(form);
		Long cpId = op.getCpId();
		if (!op.isExtendedFields()) {
			return ResponseEvent.response(fields);
		}

		if (cpId == null || cpId < 0) {
			cpId = -1L;
		}
		
		String formName = form.getName();
		List<String> extensionNames = customFieldForms.get(formName);
		if (CollectionUtils.isNotEmpty(extensionNames)) {
			List<Long> extendedFormIds = formDao.getFormIds(-1L, extensionNames);
			FormFieldSummary field = getExtensionField("customFields", "Custom Fields", extendedFormIds);
			fields.add(field);
		}
		
		if (!staticExtendedForms.contains(formName)) {
			return ResponseEvent.response(fields);
		}
		
		List<Long> extendedFormIds = formDao.getFormIds(cpId, formName);
		if (formName.equals(SPECIMEN_FORM)) {
			extendedFormIds.addAll(formDao.getFormIds(cpId, SPECIMEN_EVENT_FORM));
		}
		
		FormFieldSummary field = getExtensionField("extensions", "Extensions", extendedFormIds);
		fields.add(field);

		return ResponseEvent.response(fields);
	}
    
	@Override
	@PlusTransactional
	public ResponseEvent<List<FormContextDetail>> getFormContexts(RequestEvent<Long> req) {
		return ResponseEvent.response(formDao.getFormContexts(req.getPayload()));		
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<FormContextDetail>> addFormContexts(RequestEvent<List<FormContextDetail>> req) { // TODO: check form is deleted
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
			
			List<FormContextDetail> formCtxts = req.getPayload();
			for (FormContextDetail formCtxtDetail : formCtxts) {
				Long formId = formCtxtDetail.getFormId();
				Long cpId = formCtxtDetail.getCollectionProtocol().getId();
				String entity = formCtxtDetail.getLevel();

				FormContextBean formCtxt = formDao.getFormContext(formId, cpId, entity);
				if (formCtxt == null) {
					formCtxt = new FormContextBean();
					formCtxt.setContainerId(formId);
					formCtxt.setCpId(entity == SPECIMEN_EVENT_FORM ? -1 : cpId);
					formCtxt.setEntityType(entity);
					formCtxt.setMultiRecord(formCtxtDetail.isMultiRecord());
				}
				formCtxt.setSortOrder(formCtxtDetail.getSortOrder());

				List<FormContextProcessor> procs = ctxtProcs.get(entity);
				if (procs != null) {
					for (FormContextProcessor proc : procs) {
						proc.onSaveOrUpdate(formCtxt);
					}
				}

				formDao.saveOrUpdate(formCtxt);
				formCtxtDetail.setFormCtxtId(formCtxt.getIdentifier());
			}
			
			return ResponseEvent.response(formCtxts);			
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<FormCtxtSummary>> getEntityForms(RequestEvent<ListEntityFormsOp> req) {
		try {
			ListEntityFormsOp opDetail = req.getPayload();
			
			List<FormCtxtSummary> forms = null;
			
			Long entityId = opDetail.getEntityId();
			switch (opDetail.getEntityType()) {
			    case COLLECTION_PROTOCOL_REGISTRATION:
			    	AccessCtrlMgr.getInstance().ensureReadCprRights(entityId);
			    	forms = formDao.getCprForms(entityId);
			    	break;
			    	
			    case SPECIMEN:
			    	AccessCtrlMgr.getInstance().ensureReadSpecimenRights(entityId);
			    	forms = formDao.getSpecimenForms(opDetail.getEntityId());
			    	break;
			    	
			    case SPECIMEN_COLLECTION_GROUP:
			    	AccessCtrlMgr.getInstance().ensureReadVisitRights(entityId);
			    	forms = formDao.getScgForms(opDetail.getEntityId());
			    	break;
			    	
			    case SPECIMEN_EVENT :
			    	AccessCtrlMgr.getInstance().ensureReadSpecimenRights(entityId);
			    	forms = formDao.getSpecimenEventForms(opDetail.getEntityId());
			    	break;	
			    	
			    case SITE_EXTN:
			    	forms = formDao.getFormContexts(-1L, "SiteExtension");
			    	break;
			    	
			    case CP_EXTN:
			    	forms = formDao.getFormContexts(-1L, "CollectionProtocolExtension");
			    	break;
			    	
			    case PARTICIPANT_EXTN:
			    	forms = formDao.getFormContexts(-1L, "ParticipantExtension");
			    	break;
			    	
			    case VISIT_EXTN:
			    	forms = formDao.getFormContexts(-1L, "VisitExtension");
			    	break;
			    	 
			    case SPECIMEN_EXTN:
			    	forms = formDao.getFormContexts(-1L, "SpecimenExtension");
			    	break;
			    	
			    case ALIQUOT_EXTN:
			    	forms = formDao.getFormContexts(-1L, "AliquotExtension");
			    	break;
			    
			    case DERIVATIVE_EXTN:
			    	forms = formDao.getFormContexts(-1L, "DerivativeExtension");
			    	break;
			}
			
			return ResponseEvent.response(forms);			
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<EntityFormRecords> getEntityFormRecords(RequestEvent<GetEntityFormRecordsOp> req) {
		GetEntityFormRecordsOp opDetail = req.getPayload();
		
		FormSummary form = formDao.getFormByContext(opDetail.getFormCtxtId());
		List<FormRecordSummary> formRecs = formDao.getFormRecords(opDetail.getFormCtxtId(), opDetail.getEntityId());
		
		EntityFormRecords result = new EntityFormRecords();
		result.setFormId(form.getFormId());
		result.setFormCaption(form.getCaption());
		result.setFormCtxtId(opDetail.getFormCtxtId());
		result.setRecords(formRecs);
		
		return ResponseEvent.response(result);
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<FormDataDetail> getFormData(RequestEvent<FormRecordCriteria> req) {
		FormDataManager formDataMgr = new FormDataManagerImpl(false);

		FormRecordCriteria crit = req.getPayload();
		Long formId = crit.getFormId(), recordId = crit.getRecordId();

		FormData formData = formDataMgr.getFormData(formId, recordId);
		if (formData == null) {
			return ResponseEvent.userError(FormErrorCode.REC_NOT_FOUND);
		} else {
			if (formData.getContainer().hasPhiFields() && !isPhiAccessAllowed(formData)) {
				formData.maskPhiFieldValues();
			}

			return ResponseEvent.response(FormDataDetail.ok(formId, recordId, formData));
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<FormDataDetail> saveFormData(RequestEvent<FormDataDetail> req) {
		FormDataDetail detail = req.getPayload();
		
		try {
			User user = AuthUtil.getCurrentUser();
			FormData formData = saveOrUpdateFormData(user, detail.getRecordId(), detail.getFormData());
			return ResponseEvent.response(FormDataDetail.ok(formData.getContainer().getId(), formData.getRecordId(), formData));
		} catch (ValidationErrors ve) {
			return ResponseEvent.userError(FormErrorCode.INVALID_DATA, ve.getMessage());
		} catch(IllegalArgumentException ex) {
			return ResponseEvent.userError(FormErrorCode.INVALID_DATA, ex.getMessage());
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<FormData>> saveBulkFormData(RequestEvent<List<FormData>> req) {
		try{ 
			User user = AuthUtil.getCurrentUser();
			List<FormData> formDataList = req.getPayload();
			List<FormData> savedFormDataList = new ArrayList<FormData>();
			for (FormData formData : formDataList) {
				FormData savedFormData = saveOrUpdateFormData(user, formData.getRecordId(), formData);
				savedFormDataList.add(savedFormData);
			}
			
			return ResponseEvent.response(savedFormDataList);
		} catch(IllegalArgumentException ex) {
			return ResponseEvent.userError(FormErrorCode.INVALID_DATA);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}	
	}

	@Override
	@PlusTransactional
	public ResponseEvent<FileDetail> getFileDetail(RequestEvent<GetFileDetailOp> req) {
		GetFileDetailOp opDetail = req.getPayload();
		
		FormDataManager formDataMgr = new FormDataManagerImpl(false);
		FileControlValue fcv = formDataMgr.getFileControlValue(opDetail.getFormId(), opDetail.getRecordId(), opDetail.getCtrlName());
		if (fcv == null) {
			return ResponseEvent.userError(FormErrorCode.FILE_NOT_FOUND);
		}
		
		return ResponseEvent.response(FileDetail.from(fcv));
	}
	
	@Override
	public ResponseEvent<FileDetail> uploadFile(RequestEvent<MultipartFile> req) {
		MultipartFile input = req.getPayload();
		
		FileDetail fileDetail = new FileDetail();
		fileDetail.setFilename(input.getOriginalFilename());
		fileDetail.setSize(input.getSize());
		fileDetail.setContentType(input.getContentType());
		
		try {
			InputStream in = input.getInputStream();
			String fileId = FileUploadMgr.getInstance().saveFile(in);
			fileDetail.setFileId(fileId);
			return ResponseEvent.response(fileDetail);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}		
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<Long> deleteRecord(RequestEvent<FormRecordCriteria> req) {
		try {
			FormRecordCriteria crit = req.getPayload();
			FormRecordEntryBean recEntry = formDao.getRecordEntry(crit.getFormId(), crit.getRecordId());
			if (recEntry == null) {
				return ResponseEvent.userError(FormErrorCode.REC_NOT_FOUND);
			}
			
			FormContextBean formCtxt = formDao.getById(recEntry.getFormCtxtId());
			if (formCtxt.isSysForm()) {
				return ResponseEvent.userError(FormErrorCode.SYS_REC_DEL_NOT_ALLOWED);
			}
			
			String entityType = recEntry.getEntityType();
			Long objectId = recEntry.getObjectId();
			if (entityType.equals("Participant")) {
				AccessCtrlMgr.getInstance().ensureUpdateCprRights(objectId);
			} else if (entityType.equals("SpecimenCollectionGroup")) {
				AccessCtrlMgr.getInstance().ensureCreateOrUpdateVisitRights(objectId, false);
			} else if (entityType.equals("Specimen") || entityType.equals("SpecimenEvent")) {
				AccessCtrlMgr.getInstance().ensureCreateOrUpdateSpecimenRights(objectId, false);
			}
			
			recEntry.setActivityStatus(Status.CLOSED);
			formDao.saveOrUpdateRecordEntry(recEntry);
			return  ResponseEvent.response(crit.getRecordId());
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<Boolean> removeFormContext(RequestEvent<RemoveFormContextOp> req) {
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
			
			RemoveFormContextOp opDetail = req.getPayload();
			FormContextBean formCtx = formDao.getFormContext(
					opDetail.getFormId(), 
					opDetail.getCpId(), 
					opDetail.getFormType().getType());
			
			if (formCtx == null) {
				return ResponseEvent.userError(FormErrorCode.NO_ASSOCIATION);
			}
			
			if (formCtx.isSysForm()) {
				return ResponseEvent.userError(FormErrorCode.SYS_FORM_DEL_NOT_ALLOWED);
			}

			List<FormContextProcessor> procs = ctxtProcs.get(formCtx.getEntityType());
			if (procs != null) {
				for (FormContextProcessor proc : procs) {
					proc.onRemove(formCtx);
				}
			}
			
			switch (opDetail.getRemoveType()) {
				case SOFT_REMOVE:
					formCtx.setDeletedOn(Calendar.getInstance().getTime());
					break;
					
				case HARD_REMOVE:
					formDao.delete(formCtx);
					break;
			}
			
			return ResponseEvent.response(true);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
		
	@Override
	@PlusTransactional
	public ResponseEvent<Long> addRecordEntry(RequestEvent<AddRecordEntryOp> req) {
		AddRecordEntryOp opDetail = req.getPayload();
		String entityType = (String) opDetail.getRecIntegrationInfo().get("entityType");

		ObjectCpDetail objCp = formDao.getObjectCpDetail(opDetail.getRecIntegrationInfo());
		Long formCtxtId = formDao.getFormCtxtId(opDetail.getContainerId(), entityType, objCp.getCpId());

		FormRecordEntryBean recordEntry = new FormRecordEntryBean();
		recordEntry.setFormCtxtId(formCtxtId);
		recordEntry.setObjectId(objCp.getObjectId());
		recordEntry.setRecordId(opDetail.getRecordId());
		recordEntry.setUpdatedBy(AuthUtil.getCurrentUser().getId());
		recordEntry.setUpdatedTime(Calendar.getInstance().getTime());
		recordEntry.setActivityStatus(Status.ACTIVE);

		formDao.saveOrUpdateRecordEntry(recordEntry);		
		return ResponseEvent.response(recordEntry.getIdentifier());
	}
		
	@Override
	@PlusTransactional
	public ResponseEvent<List<FormRecordsList>> getFormRecords(RequestEvent<GetFormRecordsListOp> req) {
		try {
			GetFormRecordsListOp input = req.getPayload();
			
			String entityType = input.getEntityType();
			Long objectId = input.getObjectId();
			if (entityType.equals("Participant")) {
				AccessCtrlMgr.getInstance().ensureReadCprRights(objectId);
			} else if (entityType.equals("SpecimenCollectionGroup")) {
				AccessCtrlMgr.getInstance().ensureReadVisitRights(objectId);
			} else if (entityType.equals("Specimen") || entityType.equals("SpecimenEvent")) {
				AccessCtrlMgr.getInstance().ensureReadSpecimenRights(objectId);
			}
					
			Map<Long, List<FormRecordSummary>> records = 
					formDao.getFormRecords(objectId, entityType, input.getFormId());
			
			FormDataManager formDataMgr = new FormDataManagerImpl(false);
			
			List<FormRecordsList> result = new ArrayList<FormRecordsList>();
			for (Map.Entry<Long, List<FormRecordSummary>> formRecs : records.entrySet()) {
				Long formId = formRecs.getKey();
				Container container = Container.getContainer(formId);
				
				List<Long> recIds = new ArrayList<Long>();
				Map<Long, FormRecordSummary> recMap = new HashMap<Long, FormRecordSummary>();
				for (FormRecordSummary rec : formRecs.getValue()) {
					recMap.put(rec.getRecordId(), rec);
					recIds.add(rec.getRecordId());
				}
							
				List<FormData> summaryRecs = formDataMgr.getSummaryData(
						container, 
						recIds);
				
				for (FormData rec : summaryRecs) {
					recMap.get(rec.getRecordId()).addFieldValues(rec.getFieldValues());
				}
				
				result.add(FormRecordsList.from(container, recMap.values()));			
			}
			
			return ResponseEvent.response(result);			
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@PlusTransactional
	public ResponseEvent<List<DependentEntityDetail>> getDependentEntities(RequestEvent<Long> req) {
		try {
			return ResponseEvent.response(formDao.getDependentEntities(req.getPayload()));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private FormFieldSummary getExtensionField(String name, String caption, List<Long> extendedFormIds ) {
		FormFieldSummary field = new FormFieldSummary();
		field.setName(name);
		field.setCaption(caption);
		field.setType("SUBFORM");

		List<FormFieldSummary> extensionFields = new ArrayList<FormFieldSummary>();
		for (Long extendedFormId : extendedFormIds) {
			Container form = Container.getContainer(extendedFormId);

			FormFieldSummary extensionField = new FormFieldSummary();
			extensionField.setName(form.getName());
			extensionField.setCaption(form.getCaption());
			extensionField.setType("SUBFORM");
			extensionField.setSubFields(getFormFields(form));
			extensionField.getSubFields().add(0, getRecordIdField(form));
			extensionFields.add(extensionField);
		}

		field.setSubFields(extensionFields);
		return field;
	}
		
	private FormData saveOrUpdateFormData(User user, Long recordId, FormData formData ) {
		Map<String, Object> appData = formData.getAppData();
		if (appData.get("formCtxtId") == null || appData.get("objectId") == null) {
			throw new IllegalArgumentException("Invalid form context id or object id ");
		}

		Long objectId = ((Number) appData.get("objectId")).longValue();
		List<Long> formCtxtId = new ArrayList<Long>();
		formCtxtId.add(((Number) appData.get("formCtxtId")).longValue());
		
		List<FormContextBean> formContexts = formDao.getFormContextsById(formCtxtId);
		if (CollectionUtils.isEmpty(formContexts)) {
			throw new IllegalArgumentException("Invalid form context id");
		}
		
		formData.validate();
		
		FormContextBean formContext = formContexts.get(0);
		String entityType = formContext.getEntityType();
		Container form = formData.getContainer();
		if (entityType.equals("Participant")) {
			AccessCtrlMgr.getInstance().ensureUpdateCprRights(objectId);
		} else if (entityType.equals("SpecimenCollectionGroup")) {
			AccessCtrlMgr.getInstance().ensureCreateOrUpdateVisitRights(objectId, form.hasPhiFields());
		} else if (entityType.equals("Specimen") || entityType.equals("SpecimenEvent")) {
			AccessCtrlMgr.getInstance().ensureCreateOrUpdateSpecimenRights(objectId, form.hasPhiFields());
		}

		formData.setRecordId(recordId);
		
		boolean isInsert = (recordId == null);
		FormRecordEntryBean recordEntry = null;
		
		if (isInsert) {
			if (!formContext.isMultiRecord()) {
				Long noOfRecords = formDao.getRecordsCount(formContext.getIdentifier(), objectId);
				if (noOfRecords >= 1L) {
					throw OpenSpecimenException.userError(FormErrorCode.MULTIPLE_RECS_NOT_ALLOWED);
				}
			}
			
			recordEntry = new FormRecordEntryBean();
			recordEntry.setActivityStatus(Status.ACTIVE);			
		} else {
			recordEntry = formDao.getRecordEntry(formContext.getIdentifier(), objectId, recordId);
			if (recordEntry == null || recordEntry.getActivityStatus() != Status.ACTIVE) {
				throw OpenSpecimenException.userError(FormErrorCode.INVALID_REC_ID, recordId);				
			}
		}

		FormDataManager formDataMgr = new FormDataManagerImpl(false);
		recordId = formDataMgr.saveOrUpdateFormData(null, formData);

		recordEntry.setFormCtxtId(formContext.getIdentifier());
		recordEntry.setObjectId(objectId);
		recordEntry.setRecordId(recordId);
		recordEntry.setUpdatedBy(user.getId());
		recordEntry.setUpdatedTime(Calendar.getInstance().getTime());
		formDao.saveOrUpdateRecordEntry(recordEntry);

		formData.setRecordId(recordId);
		return formData;
	}
	
	private List<FormFieldSummary> getFormFields(Container container) {
        List<FormFieldSummary> fields = new ArrayList<FormFieldSummary>();

        for (Control control : container.getControls()) {        	
            FormFieldSummary field = new FormFieldSummary();
            field.setName(control.getUserDefinedName());
            field.setCaption(control.getCaption());

            if (control instanceof SubFormControl) {
            	SubFormControl sfCtrl = (SubFormControl)control;
            	if (!sfCtrl.isPathLink()) {
                	field.setType("SUBFORM");
                	field.setSubFields(getFormFields(sfCtrl.getSubContainer()));
                	fields.add(field);            		
            	}
            } else if (!(control instanceof Label || control instanceof PageBreak)) {
            	DataType dataType = getType(control);
            	field.setType(dataType.name());
            	                
            	if (control instanceof SelectControl) {
            		SelectControl selectCtrl = (SelectControl)control;
            		List<String> pvs = new ArrayList<String>();
            		for (PermissibleValue pv : selectCtrl.getPvs()) {
            			pvs.add(pv.getValue());
            		}
            		
            		field.setPvs(pvs);
            	} else if (control instanceof LookupControl) {
            		LookupControl luCtrl = (LookupControl)control;
            		field.setLookupProps(luCtrl.getPvSourceProps());
            	}
            	
            	fields.add(field);
            }
        }

        return fields;		
	}

	private boolean isPhiAccessAllowed(FormData formData) {
		FormRecordEntryBean record = formDao.getRecordEntry(formData.getContainer().getId(), formData.getRecordId());

		Long objectId = record.getObjectId();
		String entityType = record.getEntityType();
		
		boolean allowPhiAccess = false;
		if (entityType.equals(PARTICIPANT_FORM) || PARTICIPANT_EXTNS.contains(entityType)) {
			allowPhiAccess = AccessCtrlMgr.getInstance().ensureReadCprRights(objectId);
		} else if (entityType.equals(SCG_FORM) || VISIT_EXTNS.contains(entityType)) {
			allowPhiAccess = AccessCtrlMgr.getInstance().ensureReadVisitRights(objectId, true);
		} else if (entityType.equals(SPECIMEN_FORM) || entityType.equals(SPECIMEN_EVENT_FORM) || SPECIMEN_EXTNS.contains(entityType)) {
			allowPhiAccess = AccessCtrlMgr.getInstance().ensureReadSpecimenRights(objectId, true);
		}

		return allowPhiAccess;
	}

	private FormFieldSummary getRecordIdField(Container form) {
		Control pkCtrl = form.getPrimaryKeyControl();

		FormFieldSummary field = new FormFieldSummary();
		field.setName(pkCtrl.getUserDefinedName());
		field.setCaption(pkCtrl.getCaption());
		field.setType(getType(pkCtrl).name());
		return field;
	}
	
	private DataType getType(Control ctrl) {
		if (ctrl instanceof FileUploadControl) {
			return DataType.STRING;
		} else if (ctrl instanceof LookupControl) {
			return ((LookupControl)ctrl).getValueType();
		} else {
			return ctrl.getDataType();
		}
	}
}

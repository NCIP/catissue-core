package com.krishagni.catissueplus.core.de.services.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import krishagni.catissueplus.beans.FormContextBean;
import krishagni.catissueplus.beans.FormRecordEntryBean;
import krishagni.catissueplus.beans.FormRecordEntryBean.Status;

import org.springframework.web.multipart.MultipartFile;

import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.de.domain.FormErrorCode;
import com.krishagni.catissueplus.core.de.events.AddRecordEntryOp;
import com.krishagni.catissueplus.core.de.events.FileDetail;
import com.krishagni.catissueplus.core.de.events.FormContextDetail;
import com.krishagni.catissueplus.core.de.events.FormCtxtSummary;
import com.krishagni.catissueplus.core.de.events.FormDataDetail;
import com.krishagni.catissueplus.core.de.events.FormFieldSummary;
import com.krishagni.catissueplus.core.de.events.FormRecordSummary;
import com.krishagni.catissueplus.core.de.events.FormSummary;
import com.krishagni.catissueplus.core.de.events.FormType;
import com.krishagni.catissueplus.core.de.events.GenerateBoTemplateOp;
import com.krishagni.catissueplus.core.de.events.GetEntityFormRecordsOp;
import com.krishagni.catissueplus.core.de.events.GetFileDetailOp;
import com.krishagni.catissueplus.core.de.events.GetFormDataOp;
import com.krishagni.catissueplus.core.de.events.ListEntityFormsOp;
import com.krishagni.catissueplus.core.de.events.ListFormFields;
import com.krishagni.catissueplus.core.de.events.ObjectCpDetail;
import com.krishagni.catissueplus.core.de.events.RemoveFormContextOp;
import com.krishagni.catissueplus.core.de.repository.FormDao;
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
import edu.common.dynamicextensions.napi.FileControlValue;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.napi.FormDataManager;
import edu.common.dynamicextensions.napi.impl.FormDataManagerImpl;
import edu.common.dynamicextensions.nutility.FileUploadMgr;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.catissuecore.action.bulkOperations.BOTemplateGeneratorUtil;
import edu.wustl.common.beans.SessionDataBean;

public class FormServiceImpl implements FormService {
	private static final String PARTICIPANT_FORM = "Participant";
	
	private static final String SCG_FORM = "SpecimenCollectionGroup";
	
	private static final String SPECIMEN_FORM = "Specimen";
	
	private static final String SPECIMEN_EVENT_FORM = "SpecimenEvent";
	
	private static Set<String> staticExtendedForms = new HashSet<String>();
	
	static {
		staticExtendedForms.add(PARTICIPANT_FORM);
		staticExtendedForms.add(SCG_FORM);
		staticExtendedForms.add(SPECIMEN_FORM);
	}
	
	private FormDao formDao;
	
	public FormDao getFormDao() {
		return formDao;
	}

	public void setFormDao(FormDao formDao) {
		this.formDao = formDao;
	}
	
    @Override
    @PlusTransactional
	public ResponseEvent<List<FormSummary>> getForms(RequestEvent<FormType> req) {
		switch (req.getPayload()) {
		    case DATA_ENTRY_FORMS:
			    return ResponseEvent.response(formDao.getAllFormsSummary());
			    
		    case SPECIMEN_EVENT_FORMS: 
		    	return ResponseEvent.response(formDao.getSpecimenEventFormsSummary());
					    
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
		Long formId = req.getPayload();
		boolean isAdmin = req.getSessionDataBean().isAdmin();
		
		if (!isAdmin) {
			return ResponseEvent.userError(FormErrorCode.OP_NOT_ALLOWED);
		}
		
		try {
			if (Container.softDeleteContainer(formId)) {
				return ResponseEvent.response(true);
			} else {
				return ResponseEvent.userError(FormErrorCode.NOT_FOUND);
			}
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
		if (!staticExtendedForms.contains(formName)) {
			return ResponseEvent.response(fields);
		}
		
		List<Long> extendedFormIds = formDao.getFormIds(cpId, formName);
		if (formName.equals(SPECIMEN_FORM)) {
			extendedFormIds.addAll(formDao.getFormIds(cpId, SPECIMEN_EVENT_FORM));
		}

		FormFieldSummary field = new FormFieldSummary();
		field.setName("extensions");
		field.setCaption("Extensions");
		field.setType("SUBFORM");

		List<FormFieldSummary> extensionFields = new ArrayList<FormFieldSummary>();
		for (Long extendedFormId : extendedFormIds) {
			form = Container.getContainer(extendedFormId);

			FormFieldSummary extensionField = new FormFieldSummary();
			extensionField.setName(form.getName());
			extensionField.setCaption(form.getCaption());
			extensionField.setType("SUBFORM");
			extensionField.setSubFields(getFormFields(form));

			extensionFields.add(extensionField);
		}

		field.setSubFields(extensionFields);
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
		List<FormContextDetail> formCtxts = req.getPayload();
		for (FormContextDetail formCtxtDetail : formCtxts) {
			Long formId = formCtxtDetail.getFormId();
			Long cpId = formCtxtDetail.getCollectionProtocol().getId();
			String entity = formCtxtDetail.getLevel();
			Integer sortOrder = formCtxtDetail.getSortOrder();
			boolean isMultiRecord = formCtxtDetail.isMultiRecord();
			FormContextBean formCtxt = formDao.getFormContext(formId, cpId, entity);
			if (formCtxt == null) {
				formCtxt = new FormContextBean();
				formCtxt.setContainerId(formId);
				formCtxt.setCpId(entity == SPECIMEN_EVENT_FORM ? -1 : cpId);
				formCtxt.setEntityType(entity);
				formCtxt.setMultiRecord(isMultiRecord);
			}

			formCtxt.setSortOrder(sortOrder);
			formDao.saveOrUpdate(formCtxt);

			formCtxtDetail.setFormCtxtId(formCtxt.getIdentifier());
		}
		return ResponseEvent.response(formCtxts);
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<FormCtxtSummary>> getEntityForms(RequestEvent<ListEntityFormsOp> req) {
		ListEntityFormsOp opDetail = req.getPayload();
		
		List<FormCtxtSummary> forms = null;
		
		switch (opDetail.getEntityType()) {
		    case COLLECTION_PROTOCOL_REGISTRATION:
		    	forms = formDao.getCprForms(opDetail.getEntityId());
		    	break;
		    	
		    case SPECIMEN:
		    	forms = formDao.getSpecimenForms(opDetail.getEntityId());
		    	break;
		    	
		    case SPECIMEN_COLLECTION_GROUP:
		    	forms = formDao.getScgForms(opDetail.getEntityId());
		    	break;
		    	
		    case SPECIMEN_EVENT :
		    	forms = formDao.getSpecimenEventForms(opDetail.getEntityId());
		    	break;	
		}
		
		return ResponseEvent.response(forms);
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<FormRecordSummary>> getEntityFormRecords(RequestEvent<GetEntityFormRecordsOp> req) {
		GetEntityFormRecordsOp opDetail = req.getPayload();
		List<FormRecordSummary> formRecs = formDao.getFormRecords(opDetail.getFormCtxtId(), opDetail.getEntityId());
		return ResponseEvent.response(formRecs);
	}

	@Override
	@PlusTransactional
	public ResponseEvent<FormDataDetail> getFormData(RequestEvent<GetFormDataOp> req) {
		GetFormDataOp opDetail = req.getPayload();		
		Long formId = opDetail.getFormId(), recordId = opDetail.getRecordId();
		FormDataManager formDataMgr = new FormDataManagerImpl(false);
		
		FormData formData = formDataMgr.getFormData(formId, recordId);		
		if (formData == null) {
			return ResponseEvent.userError(FormErrorCode.REC_NOT_FOUND);
		} else {
			return ResponseEvent.response(FormDataDetail.ok(formId, recordId, formData));
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<FormDataDetail> saveFormData(RequestEvent<FormDataDetail> req) {
		FormDataDetail detail = req.getPayload();
		
		try {
			FormData formData = saveOrUpdateFormData(req.getSessionDataBean(), detail.getRecordId(), detail.getFormData());
			return ResponseEvent.response(FormDataDetail.ok(formData.getContainer().getId(), formData.getRecordId(), formData));
		} catch(IllegalArgumentException ex) {
			return ResponseEvent.userError(FormErrorCode.INVALID_DATA);
		} 
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<FormData>> saveBulkFormData(RequestEvent<List<FormData>> req) {
		try{ 
			List<FormData> formDataList = req.getPayload();
			List<FormData> savedFormDataList = new ArrayList<FormData>();
			for (FormData formData : formDataList) {
				FormData savedFormData = saveOrUpdateFormData(req.getSessionDataBean(), formData.getRecordId(), formData);
				savedFormDataList.add(savedFormData);
			}
			
			return ResponseEvent.response(savedFormDataList);
		} catch(IllegalArgumentException ex) {
			return ResponseEvent.userError(FormErrorCode.INVALID_DATA);
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
	public ResponseEvent<List<Long>> deleteRecords(RequestEvent<List<Long>> delRecEntry) {
		List<Long> deletedRecIds = new ArrayList<Long>();
		for(Long recId : delRecEntry.getPayload()) {
			FormRecordEntryBean recEntry = formDao.getRecordEntry(recId);
			if (recEntry != null) {
				recEntry.setActivityStatus(Status.CLOSED);
				formDao.saveOrUpdateRecordEntry(recEntry);
				deletedRecIds.add(recId);
			} 
		}
		
		return  ResponseEvent.response(deletedRecIds);
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<Boolean> removeFormContext(RequestEvent<RemoveFormContextOp> req) {
		try {
			RemoveFormContextOp opDetail = req.getPayload();
			
			switch (opDetail.getFormType()) {
				case DATA_ENTRY_FORMS: 
					return ResponseEvent.userError(FormErrorCode.OP_NOT_ALLOWED);
				
				case QUERY_FORMS:
					Long formId = opDetail.getFormId();
					FormContextBean queryForm = formDao.getQueryFormContext(formId);
				
					if (queryForm == null) { 
						return ResponseEvent.userError(FormErrorCode.NOT_FOUND);
					}
				
					formDao.delete(queryForm);
					return ResponseEvent.response(true);
				
				default:
					return ResponseEvent.userError(FormErrorCode.INVALID_REQ);
			}
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
		recordEntry.setUpdatedBy(req.getSessionDataBean().getUserId());
		recordEntry.setUpdatedTime(Calendar.getInstance().getTime());
		recordEntry.setActivityStatus(Status.ACTIVE);

		formDao.saveOrUpdateRecordEntry(recordEntry);		
		return ResponseEvent.response(recordEntry.getIdentifier());
	}
	
	@Override
	public ResponseEvent<List<Long>> genereateBoTemplate(RequestEvent<GenerateBoTemplateOp> boReq) {
		GenerateBoTemplateOp opDetail = boReq.getPayload();		
		Long formId = opDetail.getFormId();
		BOTemplateGeneratorUtil generator = new BOTemplateGeneratorUtil();
		
		try {
			for (String level : opDetail.getEntityLevels()) {
				generator.generateAndUploadTemplate(formId, level);
			}
		} catch (Exception e) {
			ResponseEvent.serverError(e);
		}
		
		return ResponseEvent.response(Collections.<Long>emptyList());
	}
	
	private FormData saveOrUpdateFormData(SessionDataBean session, Long recordId, FormData formData ) {
		Map<String, Object> appData = formData.getAppData();
		if (appData.get("formCtxtId") == null || appData.get("objectId") == null) {
			throw new IllegalArgumentException("Invalid form context id or object id ");
		}

		Long objectId = ((Double) appData.get("objectId")).longValue();
		List<Long> formCtxtId = new ArrayList<Long>();
		formCtxtId.add(((Double) appData.get("formCtxtId")).longValue());
		
		List<FormContextBean> formContexts = formDao.getFormContextsById(formCtxtId);
		if(formContexts == null) {
			throw new IllegalArgumentException("Invalid form context id");
		}
		
		FormContextBean formContext = formContexts.get(0);
		
		formData.setRecordId(recordId);
		boolean isInsert = (recordId == null);
		
		if(isInsert) {
			if(!formContext.isMultiRecord()) {
				Long noOfRecords = formDao.getRecordsCount(formContext.getIdentifier(), objectId);
				if(noOfRecords >= 1L) {
					throw new RuntimeException("Form is single record ");
				}
			}
		}

		FormDataManager formDataMgr = new FormDataManagerImpl(false);
		recordId = formDataMgr.saveOrUpdateFormData(null, formData);

		FormRecordEntryBean recordEntry = null;
		if (isInsert) {
			recordEntry = new FormRecordEntryBean();
			recordEntry.setActivityStatus(Status.ACTIVE);
		}
		else {
			recordEntry = formDao.getRecordEntry(formContext.getIdentifier(), objectId, recordId);
		}

		if (recordEntry.getActivityStatus() == Status.CLOSED) {
			throw new IllegalArgumentException("Provied record id does not exist");
		}

		recordEntry.setFormCtxtId(formContext.getIdentifier());
		recordEntry.setObjectId(objectId);
		recordEntry.setRecordId(recordId);
		recordEntry.setUpdatedBy(session.getUserId());
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
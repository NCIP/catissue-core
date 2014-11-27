package com.krishagni.catissueplus.core.de.services.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import krishagni.catissueplus.beans.FormContextBean;
import krishagni.catissueplus.beans.FormRecordEntryBean;
import krishagni.catissueplus.beans.FormRecordEntryBean.Status;

import org.springframework.web.multipart.MultipartFile;

import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.de.events.AddFormContextsEvent;
import com.krishagni.catissueplus.core.de.events.AddRecordEntryEvent;
import com.krishagni.catissueplus.core.de.events.AllFormsSummaryEvent;
import com.krishagni.catissueplus.core.de.events.BOTemplateGeneratedEvent;
import com.krishagni.catissueplus.core.de.events.BOTemplateGenerationEvent;
import com.krishagni.catissueplus.core.de.events.BulkFormDataSavedEvent;
import com.krishagni.catissueplus.core.de.events.DeleteFormEvent;
import com.krishagni.catissueplus.core.de.events.DeleteRecordEntriesEvent;
import com.krishagni.catissueplus.core.de.events.EntityFormRecordsEvent;
import com.krishagni.catissueplus.core.de.events.EntityFormsEvent;
import com.krishagni.catissueplus.core.de.events.FileDetail;
import com.krishagni.catissueplus.core.de.events.FileDetailEvent;
import com.krishagni.catissueplus.core.de.events.FileUploadedEvent;
import com.krishagni.catissueplus.core.de.events.FormContextDetail;
import com.krishagni.catissueplus.core.de.events.FormContextRemovedEvent;
import com.krishagni.catissueplus.core.de.events.FormContextsAddedEvent;
import com.krishagni.catissueplus.core.de.events.FormContextsEvent;
import com.krishagni.catissueplus.core.de.events.FormCtxtSummary;
import com.krishagni.catissueplus.core.de.events.FormDataEvent;
import com.krishagni.catissueplus.core.de.events.FormDefinitionEvent;
import com.krishagni.catissueplus.core.de.events.FormDeletedEvent;
import com.krishagni.catissueplus.core.de.events.FormFieldSummary;
import com.krishagni.catissueplus.core.de.events.FormFieldsEvent;
import com.krishagni.catissueplus.core.de.events.FormRecordSummary;
import com.krishagni.catissueplus.core.de.events.ObjectCpDetail;
import com.krishagni.catissueplus.core.de.events.RecordEntriesDeletedEvent;
import com.krishagni.catissueplus.core.de.events.RecordEntryEventAdded;
import com.krishagni.catissueplus.core.de.events.RemoveFormContextEvent;
import com.krishagni.catissueplus.core.de.events.ReqAllFormsSummaryEvent;
import com.krishagni.catissueplus.core.de.events.ReqEntityFormRecordsEvent;
import com.krishagni.catissueplus.core.de.events.ReqEntityFormsEvent;
import com.krishagni.catissueplus.core.de.events.ReqFileDetailEvent;
import com.krishagni.catissueplus.core.de.events.ReqFormContextsEvent;
import com.krishagni.catissueplus.core.de.events.ReqFormDataEvent;
import com.krishagni.catissueplus.core.de.events.ReqFormDefinitionEvent;
import com.krishagni.catissueplus.core.de.events.ReqFormFieldsEvent;
import com.krishagni.catissueplus.core.de.events.SaveBulkFormDataEvent;
import com.krishagni.catissueplus.core.de.events.SaveFormDataEvent;
import com.krishagni.catissueplus.core.de.events.UploadFileEvent;
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
	public AllFormsSummaryEvent getForms(ReqAllFormsSummaryEvent req) {
		switch (req.getFormType()) {
		    case DATA_ENTRY_FORMS:
			    return AllFormsSummaryEvent.ok(formDao.getAllFormsSummary());
			    
		    case SPECIMEN_EVENT_FORMS: 
		    	return AllFormsSummaryEvent.ok(formDao.getSpecimenEventFormsSummary());
					    
		    case QUERY_FORMS:
		    default:
		    	return AllFormsSummaryEvent.ok(formDao.getQueryForms());		
		}		
	}

    @Override
    @PlusTransactional
	public FormDefinitionEvent getFormDefinition(ReqFormDefinitionEvent req) {
		Container container = Container.getContainer(req.getFormId());
		if (container == null) {
			return FormDefinitionEvent.notFound(req.getFormId());
		} else {
			return FormDefinitionEvent.ok(container);
		}
	}
    
	@Override
	@PlusTransactional
	public FormDeletedEvent deleteForm(DeleteFormEvent req) {
		Long formId = req.getFormId();
		boolean isAdmin = req.getSessionDataBean().isAdmin();
		
		if (!isAdmin) {
			return FormDeletedEvent.notAuthorized(req.getFormId());
		}
		
		try {
			if (Container.softDeleteContainer(formId)) {
				return FormDeletedEvent.ok(formId);
			} else {
				return FormDeletedEvent.notFound(formId);
			}
		} catch (Exception e) {
			return FormDeletedEvent.serverError("Error deleting form", e);
		}
	}
    
    @Override
    @PlusTransactional
	public FormFieldsEvent getFormFields(ReqFormFieldsEvent req) {
		Long formId = req.getFormId();
		Container form = Container.getContainer(formId);
		if (form == null) {
			return FormFieldsEvent.notFound(formId);
		}
		
		List<FormFieldSummary> fields = getFormFields(form);
		Long cpId = req.getCpId();
		if (!req.isExtendedFields()) {
			return FormFieldsEvent.ok(formId, fields);
		}
		
		if (cpId == null || cpId < 0) {
			cpId = -1L;
		}
		
		String formName = form.getName();
		if (!staticExtendedForms.contains(formName)) {
			return FormFieldsEvent.ok(formId, fields);
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

		return FormFieldsEvent.ok(formId, fields);
	}
	
	@Override
	@PlusTransactional
	public FormContextsEvent getFormContexts(ReqFormContextsEvent req) {
		return FormContextsEvent.ok(formDao.getFormContexts(req.getFormId()));		
	}
	
	@Override
	@PlusTransactional
	public FormContextsAddedEvent addFormContexts(AddFormContextsEvent req) { // TODO: check form is deleted
		List<FormContextDetail> formCtxts = req.getFormContexts();
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
		return FormContextsAddedEvent.ok(formCtxts);
	}

	@Override
	@PlusTransactional
	public EntityFormsEvent getEntityForms(ReqEntityFormsEvent req) {
		List<FormCtxtSummary> forms = null;
		
		switch (req.getEntityType()) {
		    case COLLECTION_PROTOCOL_REGISTRATION:
		    	forms = formDao.getCprForms(req.getEntityId());
		    	break;
		    	
		    case SPECIMEN:
		    	forms = formDao.getSpecimenForms(req.getEntityId());
		    	break;
		    	
		    case SPECIMEN_COLLECTION_GROUP:
		    	forms = formDao.getScgForms(req.getEntityId());
		    	break;
		    	
		    case SPECIMEN_EVENT :
		    	forms = formDao.getSpecimenEventForms(req.getEntityId());
		    	break;	
		}
		
		return EntityFormsEvent.ok(forms);
	}

	@Override
	@PlusTransactional
	public EntityFormRecordsEvent getEntityFormRecords(ReqEntityFormRecordsEvent req) {
		List<FormRecordSummary> formRecs = formDao.getFormRecords(req.getFormCtxtId(), req.getEntityId());
		return EntityFormRecordsEvent.ok(formRecs);
	}

	@Override
	@PlusTransactional
	public FormDataEvent getFormData(ReqFormDataEvent req) {
		Long formId = req.getFormId(), recordId = req.getRecordId();
		FormDataManager formDataMgr = new FormDataManagerImpl(false);
		
		FormData formData = formDataMgr.getFormData(formId, recordId);		
		if (formData == null) {
			return FormDataEvent.notFound(formId, recordId);
		} else {
			return FormDataEvent.ok(formId, recordId, formData);
		}
	}

	@Override
	@PlusTransactional
	public FormDataEvent saveFormData(SaveFormDataEvent req) {
		try {
			FormData formData = saveOrUpdateFormData(req.getSessionDataBean(), req.getRecordId(), req.getFormData());
			return FormDataEvent.ok(formData.getContainer().getId(), formData.getRecordId(), formData);
		} catch(IllegalArgumentException ex) {
			return FormDataEvent.badRequest();
		} 
	}

	@Override
	@PlusTransactional
	public BulkFormDataSavedEvent saveBulkFormData(SaveBulkFormDataEvent req) {
		try{ 
			List<FormData> formDataList = req.getFormDataList();
			List<FormData> savedFormDataList = new ArrayList<FormData>();
			for (FormData formData : formDataList) {
				FormData savedFormData = saveOrUpdateFormData(req.getSessionDataBean(), formData.getRecordId(), formData);
				savedFormDataList.add(savedFormData);
			}
			return BulkFormDataSavedEvent.ok(req.getFormId(), savedFormDataList);
		} catch(IllegalArgumentException ex) {
			return BulkFormDataSavedEvent.badRequest(ex);
		}	
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
	
	@Override
	@PlusTransactional
	public FileDetailEvent getFileDetail(ReqFileDetailEvent req) {
		FormDataManager formDataMgr = new FormDataManagerImpl(false);
		FileControlValue fcv = formDataMgr.getFileControlValue(req.getFormId(), req.getRecordId(), req.getCtrlName());
		if (fcv == null) {
			return FileDetailEvent.notFound();
		}
		
		return FileDetailEvent.ok(FileDetail.from(fcv));
	}
	
	@Override
	public FileUploadedEvent uploadFile(UploadFileEvent req) {
		MultipartFile input = req.getFile();
		
		FileDetail fileDetail = new FileDetail();
		fileDetail.setFilename(input.getOriginalFilename());
		fileDetail.setSize(input.getSize());
		fileDetail.setContentType(input.getContentType());
		
		try {
			InputStream in = input.getInputStream();
			String fileId = FileUploadMgr.getInstance().saveFile(in);
			fileDetail.setFileId(fileId);
			return FileUploadedEvent.ok(fileDetail);
		} catch (Exception e) {
			return FileUploadedEvent.serverError();
		}		
	}
	
	@Override
	@PlusTransactional
	public RecordEntriesDeletedEvent deleteRecords(DeleteRecordEntriesEvent delRecEntry) {
		List<Long> deletedRecIds = new ArrayList<Long>();
		for(Long recId : delRecEntry.getRecordIds()) {
			FormRecordEntryBean recEntry = formDao.getRecordEntry(recId);
			if (recEntry != null) {
				recEntry.setActivityStatus(Status.CLOSED);
				formDao.saveOrUpdateRecordEntry(recEntry);
				deletedRecIds.add(recId);
			} 
		}
		
		return  RecordEntriesDeletedEvent.ok(deletedRecIds);
	}
	
	@Override
	@PlusTransactional
	public FormContextRemovedEvent removeFormContext(RemoveFormContextEvent req) {
		try {
			switch (req.getFormType()) {
			case DATA_ENTRY_FORMS: 
				return FormContextRemovedEvent.badRequest("Deletion of data entry forms not supported!", null);
				
			case QUERY_FORMS:
				Long formId = req.getFormId();
				FormContextBean queryForm = formDao.getQueryFormContext(formId);
				
				if (queryForm == null) { 
					return FormContextRemovedEvent.notFound(formId);
				}
				
				formDao.delete(queryForm);
				return FormContextRemovedEvent.ok(formId);
				
			default:
				return FormContextRemovedEvent.badRequest("Invalid Entry Parameters!", null);
			}
		} catch (Exception e) {
			return FormContextRemovedEvent.serverError(e.getMessage(), e);
		}
	}
		
	@Override
	@PlusTransactional
	public RecordEntryEventAdded addRecordEntry(AddRecordEntryEvent req) {
		String entityType = (String) req.getRecIntegrationInfo().get("entityType");

		ObjectCpDetail objCp = formDao.getObjectCpDetail(req.getRecIntegrationInfo());
		Long formCtxtId = formDao.getFormCtxtId(req.getContainerId(), entityType, objCp.getCpId());

		FormRecordEntryBean recordEntry = new FormRecordEntryBean();
		recordEntry.setFormCtxtId(formCtxtId);
		recordEntry.setObjectId(objCp.getObjectId());
		recordEntry.setRecordId(req.getRecordId());
		recordEntry.setUpdatedBy(req.getSessionDataBean().getUserId());
		recordEntry.setUpdatedTime(Calendar.getInstance().getTime());
		recordEntry.setActivityStatus(Status.ACTIVE);

		formDao.saveOrUpdateRecordEntry(recordEntry);
		
		return RecordEntryEventAdded.ok(recordEntry.getIdentifier());
	}
	
	@Override
	public BOTemplateGeneratedEvent genereateBoTemplate(BOTemplateGenerationEvent req) {
		Long formId = req.getFormId();
		BOTemplateGeneratorUtil generator = new BOTemplateGeneratorUtil();
		
		try {
			for (String level : req.getEntityLevels()) {
				generator.generateAndUploadTemplate(formId, level);
			}
		} catch (Exception e) {
			BOTemplateGeneratedEvent.not_ok();
		}
		
		return BOTemplateGeneratedEvent.ok(null);
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